package data.pipeline.models.cassandra.Operation

import com.betaout.cassandra.Model._
import com.betaout.normalizer.Entity._
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

class RecordsDatabase(override val connector: CassandraConnection, tablename: String) extends Database[RecordsDatabase](connector) {

  object RecordsModel extends RecordsModel(tablename) with connector.Connector

  def saveOrUpdate(record: Product , project_id : String): Future[ResultSet] = {
    val final_record : Product = Product(
      record.id,
      record.price,
      record.name,
      record.currency,
      record.tags,
      record.categories,
      record.specs,
      record.old_price,
      record.discount,
      record.margin,
      record.sku,
      record.quantity,
      record.image_url,
      record.product_url,
      record.stock_availability,
      record.product_group_id,
      record.product_group_name,
      record.brandName,
      Option(project_id)
    )

    for {
      rse <- RecordsModel.store(final_record).future()
    } yield rse
  }
}


class UserRecordsDatabaseWithDID(override val connector: CassandraConnection, tablename: String) extends Database[UserRecordsDatabaseWithDID](connector) {

  object UserRecordsModelWithDID extends UserRecordsModelWithDID(tablename) with connector.Connector

  def saveOrUpdate(record: UserData): Future[ResultSet] = {
    for {
      rse <- UserRecordsModelWithDID.store(record).future()
    } yield rse
  }
}

class UserRecordsDatabaseWithCID(override val connector: CassandraConnection, tablename: String) extends Database[UserRecordsDatabaseWithCID](connector) {

  object UserRecordsModelWithCID extends UserRecordsModelWithCID(tablename) with connector.Connector

  def saveOrUpdate(record: UserData): Future[ResultSet] = {
    for {
      rse <- UserRecordsModelWithCID.store(record).future()
    } yield rse
  }
}



class UserConditionalVariablesDatabase(override val connector: CassandraConnection, tablename: String) extends Database[UserConditionalVariablesDatabase](connector) {

  object UserComparisonVariablesModel extends UserConditionalVariablesModel(tablename) with connector.Connector

  def saveOrUpdate(record: UserConditionalVariables ): Future[ResultSet] = {
    for {
      rse <- UserComparisonVariablesModel.store(record).future()
    } yield rse
  }


}


class UserAppendVariablesDatabase(override val connector: CassandraConnection, tablename: String) extends Database[UserAppendVariablesDatabase](connector) {

  object UserAppendVariablesModel extends UserAppendVariablesModel(tablename) with connector.Connector

  def saveOrUpdate(record: UserAppendVariables ): Future[ResultSet] = {
    for {
      rse <- UserAppendVariablesModel.store(record).future()
    } yield rse
  }


}


class UserTotalVariablesDatabase(override val connector: CassandraConnection, tablename: String) extends Database[UserTotalVariablesDatabase](connector) {

  object UserTotalVariablesModel extends UserTotalVariablesModel(tablename) with connector.Connector

  def saveOrUpdate(record: UserTotalVariables ): Future[ResultSet] = {
    for {
      rse <- UserTotalVariablesModel.store(record).future()
    } yield rse
  }


}


class UserCountVariablesDatabase(override val connector: CassandraConnection, tablename: String) extends Database[UserCountVariablesDatabase](connector) {

  object UserCountVariablesModel extends UserCountVariablesModel(tablename) with connector.Connector

  def saveOrUpdate(record: UserCountVariables ): Future[ResultSet] = {
    for {
      rse <- UserCountVariablesModel.store(record).future()
    } yield rse
  }

}

class OrderUpdate(override val connector: CassandraConnection, tablename: String) extends Database[OrderUpdate](connector) {

  object OrderUpdateModel extends OrderUpdateModel(tablename) with connector.Connector
  def saveOrUpdate(record: OrderCacheStub): Future[ResultSet] = {
    for {
      rse <- OrderUpdateModel.store(record).future()
    } yield rse
  }
}


class UserAverageVariablesDatabase(override val connector: CassandraConnection, tablename: String) extends Database[UserAverageVariablesDatabase](connector) {

  object UserAverageVariablesModel extends UserAverageVariablesModel(tablename) with connector.Connector

  def saveOrUpdate(record: UserAverageVariables ): Future[ResultSet] = {
    for {
      rse <- UserAverageVariablesModel.store(record).future()
    } yield rse
  }
}

class FormulaBuilderConfigDataBase(override val connector: CassandraConnection, tablename: String) extends Database[FormulaBuilderConfigDataBase](connector) {
  object FormulaBuilderConfigModel extends FormulaBuilderConfigModel(tablename) with connector.Connector

  def saveOrUpdate(record: FormulaBuilderConfig ): Future[ResultSet] = {
    for {
      rse <- FormulaBuilderConfigModel.store(record).future()
    } yield rse
  }
}

class AttributionDatabase(override val connector: CassandraConnection, tablename: String) extends Database[AttributionDatabase](connector) {
  object AttributionModel extends AttributionModel(tablename) with connector.Connector

  def saveOrUpdate(record: UtmAttributionStub, ttl_value : Int): Future[ResultSet] = {
    for {
      rse <- AttributionModel.store(record).ttl(ttl_value).future()
    } yield rse
  }
}
