package data.pipeline.models.mongodb

import data.pipeline.Logs.logs
import com.typesafe.config.ConfigFactory
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global

class MongoConnection() extends logs{
  val config = ConfigFactory.load.getConfig("mongodb")
  val tag = "MongoDb.MongoConnection"
  val host_ip = config.getString("conf.host")
  val portNum = config.getString("conf.port")
  val driver = new MongoDriver()
  val connection = driver.connection(List((host_ip + ":" + portNum)))
  log.info(s"File : ${tag}   connection: ${connection.toString}")
  def connect(databaseName: String, collectionName: String): BSONCollection = {
    val db = connection(databaseName)
    val bSONCollection:BSONCollection = db.collection(collectionName)
    log.info(s"File : ${tag}   connection: ${bSONCollection.toString}")
    bSONCollection
  }
}
