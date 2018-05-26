package data.pipeline.bolts.Normalization

import java.util.UUID

import com.typesafe.config.ConfigFactory
import data.pipeline.entity.SummaryData.SummaryData
import data.pipeline.entity.converter.SummaryConverter.SummaryConverterReader
import data.pipeline.entity.logData.LogData
import data.pipeline.models.mongodb.MongoModels
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.json4s.native.Json
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Normalization extends BaseRichBolt {

  val TAG: String = "Normalization"
  var collector: OutputCollector = _
  var mongoModels: MongoModels = _
  val log = LoggerFactory.getLogger(getClass)
  val productIDarrayKeyName = "products"
  val observation_period = 3 * 24 * 60 * 60
  val churned_period = 3 * 24 * 60 * 60

  override def prepare(config: java.util.Map[_, _], context: TopologyContext, collector: OutputCollector): Unit = {
    this.collector = collector
    this.mongoModels = new MongoModels()
  }

  override def execute(tuple: Tuple) {
    val request_id: String = UUID.randomUUID().toString
    try {
      log.info(s"[$TAG][execute]   request_id : ${request_id}   tuple :  ${tuple.toString}")
      val message: String = tuple.getValueByField("str").toString.split("=", 2)(1).dropRight(1)
      val data = Json.apply(org.json4s.DefaultFormats).read[LogData](message)

      // get from mongodb
      this.mongoModels.getDocumentsByName(data.name).map {
        bsonDocumentOption =>
          if (bsonDocumentOption != None) {
            val summaryData = SummaryConverterReader.read(bsonDocumentOption.get)
            if(data.date < summaryData.start_date + observation_period){
              val newScores = data.score :: summaryData.scores
              val newDurations = data.duration :: summaryData.durations
              val newDates = data.date :: summaryData.dates
              val mean_score = newScores.sum / newScores.length
              val total_wins = if(data.team == 1 & data.`1_teams_score` > data.`0_teams_score`){
                summaryData.total_wins + 1
              }else if(data.team == 0 & data.`0_teams_score` > data.`1_teams_score`){
                summaryData.total_wins + 1
              }else{
                summaryData.total_wins
              }
              val newSummaryData = SummaryData(
                summaryData.start_date,
                data.date - summaryData.start_date,
                newScores,
                newDurations,
                newDates,
                newScores.max,
                newScores.min,
                mean_score,
                newDurations.sum / newDurations.length,
                Math.sqrt((newScores.map(_ - mean_score).map(t => t * t).sum) / newScores.length),
                summaryData.play_count + 1,
                newScores.max - mean_score / (summaryData.play_count + 1),
                if (mean_score == 0) {
                  summaryData.best_sub_mean_ratio
                } else {
                  (newScores.max - mean_score) / mean_score
                },
                total_wins,
                data.date,
                summaryData.churned
              )
              this.mongoModels.insertSummaryRecords(newSummaryData)
            }else if(data.date < summaryData.start_date + observation_period + churned_period){
              val newSummaryData = SummaryData(
                summaryData.start_date,
                summaryData.active_duration,
                summaryData.scores,
                summaryData.durations,
                summaryData.dates,
                summaryData.best_score,
                summaryData.worst_score,
                summaryData.mean_score,
                summaryData.mean_duration,
                summaryData.std_score,
                summaryData.play_count,
                summaryData.best_sub_mean_count,
                summaryData.best_sub_mean_ratio,
                summaryData.total_wins,
                summaryData.last_game_play,
                Option(0)
              )
              this.mongoModels.insertSummaryRecords(newSummaryData)
            }
          } else {
            val total_wins = if(data.team == 1 & data.`1_teams_score` > data.`0_teams_score`){
              1
            }else if(data.team == 0 & data.`0_teams_score` > data.`1_teams_score`){
              1
            }else{
              0
            }
            val newSummaryData = SummaryData(
              data.date,
              0,
              List(data.score),
              List(data.duration),
              List(data.date),
              data.score,
              data.score,
              data.score,
              data.duration,
              0,
              1,
              0,
              0,
              total_wins,
              data.date,
              None
            )
            this.mongoModels.insertSummaryRecords(newSummaryData)
          }
      }
      this.mongoModels.insertRawRecords(data)
    }
    catch {
      case exception: Exception => {
        log.error(s"[${TAG}][execute] request_id : ${request_id} exception : ${exception.toString.filter(_ >= ' ').filter(_ >= ' ')}  path : ${exception.getStackTraceString.filter(_ >= ' ')} data : ${tuple.toString}")
      }
    }
    finally {
      log.info(s"[$TAG][execute][ack]  request_id : ${request_id}     tuple :  ${tuple.toString}")
      this.collector.ack(tuple)
    }
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declareStream("default", new Fields("event_data", "user_id", "entity_name"))
  }

}
