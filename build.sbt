name := "Data Pipeline"
version := "0.1"
scalaVersion := "2.12.4"
resolvers ++= Seq(
  "typesafe-repository" at "http://repo.typesafe.com/typesafe/releases/",
  "clojars-repository" at "https://clojars.org/repo",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "rediscala" at "http://dl.bintray.com/etaty/maven"

)

val stormVersion = "1.1.0"
val stormFluxVersion = "1.1.1"
libraryDependencies ++= Seq(
  "com.github.blemale" %% "scaffeine" % "2.1.0" % "compile",
  "org.apache.kafka" % "kafka_2.12" % "0.10.2.1"
    exclude("javax.jms", "jms")
    exclude("com.sun.jdmk", "jmxtools")
    exclude("com.sun.jmx", "jmxri")
    exclude("org.slf4j", "slf4j-simple")
    exclude("log4j", "log4j")
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("com.101tec", "zkclient")
    exclude("org.apache.zookeeper", "zookeeper"),
  "org.apache.storm" % "storm-core" % stormVersion % "provided"
    exclude("org.apache.zookeeper", "zookeeper")
    exclude("log4j", "log4j")
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.apache.storm" % "storm-metrics" % stormVersion
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.apache.storm" % "flux-core" % stormFluxVersion
    exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.storm" % "flux-wrappers" % stormFluxVersion
    exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.storm" % "storm-kafka" % stormVersion // % "provided"
    exclude("log4j", "log4j")
    exclude("org.apache.zookeeper", "zookeeper")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.apache.storm" % "storm-redis" % stormVersion
    exclude("log4j", "log4j")
    exclude("org.apache.zookeeper", "zookeeper")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.apache.curator" % "curator-framework" % "2.12.0"
    exclude("org.jboss.netty", "netty")
    exclude("org.slf4j", "log4j-over-slf4j")
    exclude("org.slf4j", "slf4j-log4j12"),
  "com.google.guava" % "guava" % "12.0",
  "org.apache.commons" % "commons-pool2" % "2.3",
  "org.scala-lang.modules" % "scala-parser-combinators_2.12" % "1.0.5",
  "com.outworkers" % "phantom-dsl_2.12" % "2.12.1",
  "com.outworkers" % "phantom-streams_2.12" % "2.12.1",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "net.liftweb" % "lift-json_2.12" % "3.0.1"
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0"
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "ch.qos.logback" % "logback-classic" % "1.2.3"
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.1"
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("org.slf4j", "log4j-over-slf4j"),
  "org.elasticsearch" % "elasticsearch" % "5.4.3",
  "org.elasticsearch.plugin" % "transport-netty4-client" % "5.4.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.1" withSources() withJavadoc(),
  "com.sksamuel.elastic4s" % "elastic4s-tcp_2.12" % "5.4.13"
    exclude("org.typelevel","macro-compat_2.11")
    exclude("org.yaml","snakeyaml")
    exclude("org.jboss.netty","netty-transport-native-epoll")
    exclude( "org.elasticsearch","elasticsearch")
    exclude("org.elasticsearch.plugin","transport-netty4-client")
)

libraryDependencies <+= (scalaVersion) ("org.scala-lang" % "scala-reflect" % _)
libraryDependencies += "org.clojure" % "clojure" % "1.7.0"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.16.0"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.3"
libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6" // % "provided"
libraryDependencies += "io.netty" % "netty-transport-native-epoll" % "4.1.10.Final"
libraryDependencies += "com.github.wnameless" % "json-flattener" % "0.4.0"

dependencyOverrides += "io.netty" % "netty-codec-http" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-handler" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-codec" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-transport" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-buffer" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-common" % "4.0.44.Final"
dependencyOverrides += "io.netty" % "netty-transport-native-epoll" % "4.0.44.Final"



assemblyMergeStrategy in assembly := {
  case PathList("io", "netty", xs@_*) => MergeStrategy.last
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case PathList("org", "apache", "log4j", xs@_*) => MergeStrategy.last
  case PathList("META-INF", "maven", "jline", "jline", xs@_*) => MergeStrategy.last
  case PathList("META-INF",xs @ _*) => MergeStrategy.discard
  case x =>
    MergeStrategy.first
  //    val oldStrategy = (assemblyMergeStrategy in assembly).value
  //    oldStrategy(x)
}

fullClasspath in(Test, assembly) := {
  val cp = (fullClasspath in Test).value
  cp.filter { file => (file.data.name contains "classes") || (file.data.name contains "test-classes") } ++ (fullClasspath in Runtime).value
}