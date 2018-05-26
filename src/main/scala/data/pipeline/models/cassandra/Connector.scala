package data.pipeline.models.cassandra.Connector

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.typesafe.config.ConfigFactory

import com.datastax.driver.core.policies.ConstantReconnectionPolicy
import com.datastax.driver.core.{PoolingOptions, SocketOptions}
import com.datastax.driver.core.HostDistance



object Connector {
  val config = ConfigFactory.load.getConfig("cassandra")
  val hosts : List[String] = config.getString("host_ips").split(",").toList
  val port = config.getInt("port")
  var keyspace : String = config.getString("keyspace")

  val connection = ContactPoints(hosts, port)
    .withClusterBuilder(
      _.withSocketOptions(
        new SocketOptions()
          .setReadTimeoutMillis(10000)
          .setConnectTimeoutMillis(20000))
        .withPoolingOptions(
          new PoolingOptions()
            .setMaxQueueSize(100000)
            .setPoolTimeoutMillis(20000)
            .setMaxConnectionsPerHost(HostDistance.LOCAL, 32768)
            .setMaxConnectionsPerHost(HostDistance.REMOTE, 2000))
        .withReconnectionPolicy(new ConstantReconnectionPolicy(3000L))
    ).keySpace(keyspace)
}