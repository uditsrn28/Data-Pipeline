package data.pipeline.bolts


import java.util.UUID

import data.pipeline.entity.logData.LogData
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.json4s.native.Json
import org.slf4j.{Logger, LoggerFactory}

import scala.util.parsing.json.JSON


class Splitter extends BaseRichBolt {
  val tag: String = "Splitter"
  val log: Logger = LoggerFactory.getLogger(getClass)
  var collector: OutputCollector = _

  override def prepare(config: java.util.Map[_, _], context: TopologyContext, collector: OutputCollector): Unit = {
    this.collector = collector
  }

  override def execute(tuple: Tuple) {
    try {
	log.info(s"[${tag}][execute] request_id : 11111")
      //log.info(s"[${tag}][execute] request_id : N.A  data : ${tuple.getString(0).toString}")
      //val message : String = tuple.getString(0) //.toString.split("=", 2)(1).dropRight(1)
      val records: Array[Byte] = tuple.getBinary(0)
      val message = (records.map(_.toChar)).mkString.toString()
      val data = Json.apply(org.json4s.DefaultFormats).read[LogData](message)
      collector.emit("default", new Values(data.name , message))
    }
    catch {
      case exception: Exception => {
        log.error(s"[${tag}][execute] request_id : N.A exception : ${exception.toString.filter(_ >= ' ' )}  path : ${exception.getStackTraceString.filter(_ >= ' ' )} data : ${tuple.toString}")
      }
    }
    finally {
      log.info(s"[$tag][execute][ack] request_id : N.A. tuple : ${tuple.toString}")
      this.collector.ack(tuple)
    }
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declareStream("default", new Fields("user_name", "data"))
  }
}
