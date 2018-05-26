package data.pipeline.models.mongodb

import Betaout.Entity.Request.{ProjectDetailsSetRequestEntity}
import Betaout.Logs.logs
import Betaout.Utility.PlayOnBson
import Betaout.Utility.RemoteServiceCall

import com.typesafe.config.ConfigFactory
import reactivemongo.api.QueryOpts
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.{BSONArray, BSONDocument, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class MongoModels extends logs {
  val config = ConfigFactory.load.getConfig("mongodb")
  val mongoConnection = new MongoConnection()
  val tag = "MongoDb.MongoModels"
  val config_urlNames = ConfigFactory.load.getConfig("urlNames")

  def insertRawRecords(logData: LogData): Future[Boolean] = {
    insertDocument = BSONDocument(
      "auth" -> logData.auth,
      "degree" -> logData.degree,
      "events" -> logData.events,
      "flair" -> logData.flair,
      "match" -> logData.`match`,
      "name" -> logData.name,
      "points" -> logData.points,
      "score" -> logData.score,
      "team" -> logData.team,
      "0_teams_name" -> logData.`0_teams_name`,
      "0_teams_score" -> logData.`0_teams_score`,
      "0_teams_splats" -> logData.`0_teams_splats`,
      "1_teams_name" -> logData.`1_teams_name`,
      "1_teams_score" -> logData.`1_teams_score`,
      "1_teams_splats" -> logData.`1_teams_splats`,
      "date" -> logData.date,
      "duration" -> logData.duration,
      "group" -> logData.group.getOrElse(""),
      "mapId" -> logData.mapId,
      "official" -> logData.official,
      "port" -> logData.port,
      "server" -> logData.server,
      "timeLimit" -> logData.timeLimit,
    )
    collectionObj.insert(document = insertDocument).map {
      insertResult =>
        insertResult match {
          case UpdateWriteResult(true, _, _, _, _, _, _, errmsg) =>
            if (errmsg == None) {
              true
            } else {
              false
            }
          case UpdateWriteResult(false, _, _, _, _, _, _, errmsg) =>
            false
        }
    }
  }



  def insertSummaryRecords(summaryData: SummaryData): Future[Boolean] = {
    insertDocument = BSONDocument(
      "start_date" -> summaryData.start_date,
      "active_duration" -> summaryData.active_duration,
      "scores" -> summaryData.scores,
      "durations" -> summaryData.durations,
      "dates" -> summaryData.dates,
      "best_score" -> summaryData.best_score,
      "worst_score" -> summaryData.worst_score,
      "mean_score" -> summaryData.mean_score,
      "mean_duration" -> summaryData.mean_duration,
      "std_score" -> summaryData.std_score,
      "play_count" -> summaryData.play_count,
      "best_sub_mean_count" -> summaryData.best_sub_mean_count,
      "best_sub_mean_ratio" -> summaryData.best_sub_mean_ratio,
      "total_wins" -> summaryData.total_wins,
      "last_game_play" -> summaryData.last_game_play,
      "churned" -> summaryData.churned.getOrElse(1)
    )
    collectionObj.insert(document = insertDocument).map {
      insertResult =>
        insertResult match {
          case UpdateWriteResult(true, _, _, _, _, _, _, errmsg) =>
            if (errmsg == None) {
              true
            } else {
              false
            }
          case UpdateWriteResult(false, _, _, _, _, _, _, errmsg) =>
            false
        }
    }
  }

  def getDocumentsByName(name: String): Future[Option[BSONDocument]] = {
    val collectionObj = mongoConnection.connect(config.getString("database"), config.getString("security_collection"))
    val selectorDoc = BSONDocument(
      "_id" -> name
    )
    log.info(s"File : ${tag}   Method: securityIpExists    selectorDoc: ${BSONDocument.pretty(selectorDoc)}  ")
    collectionObj.find(selectorDoc).one[BSONDocument].map(x => x != None)
  }


}