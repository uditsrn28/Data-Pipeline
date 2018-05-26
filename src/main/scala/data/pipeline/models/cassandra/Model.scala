package data.pipeline.models.cassandra.Model


import com.betaout.normalizer.Entity._
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

abstract class UserRecordsModelWithCID(tablename:String) extends Table[UserRecordsModelWithCID, UserData] {

  override def tableName = tablename
  object customer_id extends OptionalStringColumn with PartitionKey {
    override lazy val name = "customer_id"
  }
  object project_id extends StringColumn with PartitionKey {
    override lazy val name = "project_id"
  }
  object device_id extends StringColumn with PartitionKey {
    override lazy val name = "device_id"
  }
  object properties extends MapColumn[String,String]
  object device_ids extends ListColumn[String]
  def getByRecordCustomerId(customer_id: String, project_id : String): Future[Option[UserData]] = {
    select
      .where(_.customer_id eqs customer_id)
      .and(_.project_id eqs project_id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .allowFiltering()
      .one()
  }
  def deleteByCustomerId(customer_id: String, project_id : String): Future[ResultSet] = {
    delete
      .where(_.customer_id eqs customer_id)
      .and(_.project_id eqs project_id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }
}


}