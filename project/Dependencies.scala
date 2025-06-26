import sbt.*

// TODO: prune dependencies once project is understood
object Dependencies {

  val scalaV = "3.3.5" // LTS version of Scala 3

  val circeVersion               = "0.14.12"
  val cirisVersion               = "3.8.0"
  val http4sVersion              = "0.23.30"
  val fs2KafkaVersion            = "3.7.0"
  val log4catsVersion            = "2.7.0"
  val logbackVersion             = "1.5.18"
  val logstashEncoderVersion     = "8.1"
  val newtypesVersion            = "0.3.0"
  val munitCatsEffectVersion     = "2.1.0"
  val munitVersion               = "1.1.0"
  val testContainersScalaVersion = "0.43.0"

  lazy val mainLibraries: Seq[ModuleID] = Seq(
    "ch.qos.logback"       % "logback-classic"          % logbackVersion         % Runtime,
    "io.circe"            %% "circe-generic"            % circeVersion,
    "is.cir"              %% "ciris"                    % cirisVersion,
    "io.monix"            %% "newtypes-circe-v0-14"     % newtypesVersion,
    "io.monix"            %% "newtypes-core"            % newtypesVersion,
    "net.logstash.logback" % "logstash-logback-encoder" % logstashEncoderVersion % Runtime,
    "org.http4s"          %% "http4s-circe"             % http4sVersion,
    "org.http4s"          %% "http4s-dsl"               % http4sVersion,
    "org.http4s"          %% "http4s-ember-client"      % http4sVersion,
    "org.http4s"          %% "http4s-ember-server"      % http4sVersion,
    "com.github.fd4s"     %% "fs2-kafka"                % fs2KafkaVersion,
    "org.typelevel"       %% "log4cats-slf4j"           % log4catsVersion
  )

  lazy val testLibraries: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit"             % munitVersion,
    "org.scalameta" %% "munit-scalacheck"  % munitVersion,
    "org.typelevel" %% "munit-cats-effect" % munitCatsEffectVersion,
    "org.typelevel" %% "log4cats-noop"     % log4catsVersion
  ).map(_ % Test)

  lazy val integrationTestLibraries: Seq[ModuleID] = Seq(
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testContainersScalaVersion,
    "com.dimafeng" %% "testcontainers-scala-munit"      % testContainersScalaVersion,
    "com.dimafeng" %% "testcontainers-scala-kafka"      % testContainersScalaVersion
  ).map(_ % Test)
}
