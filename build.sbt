import Dependencies.*
import ReleaseTransformations.*
import org.typelevel.scalacoptions.ScalacOptions

Global / cancelable := true
Global / scalacOptions += "-no-indent"

val accountId = sys.env.getOrElse("ACCOUNT_ID", "317292977969")

lazy val commonSettings = Seq(
  tpolecatScalacOptions ~= { existingSettings =>
    existingSettings + ScalacOptions.other("-Xmax-inlines", List("100"), _ => true)
  },
  organization := "com.moneylion",
  scalaVersion := scalaV,
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  libraryDependencies ++= mainLibraries,
  publish / skip := true,
  Compile / run / fork := true
)

lazy val dockerSettings = Seq(
  dockerBaseImage := s"$accountId.dkr.ecr.us-east-1.amazonaws.com/ml-ews/ews-base-images:temurin21-scala",
  dockerRepository := Some(s"$accountId.dkr.ecr.us-east-1.amazonaws.com"),
  dockerUsername := Some("ml-ews"), // Organization
  dockerExposedPorts := Seq(8080)
  // Only use the following for testing. Must not be deployed to prod
  //    Docker / dockerGroupLayers := PartialFunction.empty,
  //    Docker / daemonUserUid := Some("0"),
  //    Docker / daemonUser := "root",
)

lazy val testSettings = Seq(
  libraryDependencies ++= testLibraries,
  libraryDependencies ++= integrationTestLibraries,
  Test / parallelExecution := true,
  Test / fork := true
)

lazy val scalaWarsCore = (project in file("modules/scala-wars-core"))
  .settings(
    name := "scala-wars-core",
    commonSettings,
    testSettings
  )
  .enablePlugins(ScalafixPlugin)

lazy val httpService = (project in file("modules/http-service"))
  .settings(
    name := "http-service",
    commonSettings,
    testSettings,
    dockerSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.moneylion.scalawars.httpservice"
  )
  .dependsOn(scalaWarsCore)
  .enablePlugins(JavaAppPackaging, DockerPlugin, BuildInfoPlugin, ScalafixPlugin)

lazy val kafkaService = (project in file("modules/kafka-service"))
  .settings(
    name := "kafka-service",
    commonSettings,
    testSettings,
    dockerSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.moneylion.scalawars.kafkaservice"
  )
  .dependsOn(scalaWarsCore)
  .enablePlugins(JavaAppPackaging, DockerPlugin, BuildInfoPlugin, ScalafixPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "Scala Wars - HTTP vs Kafka",
    commonSettings,
    releaseTagName := s"v${version.value}",
    releaseNextCommitMessage := releaseNextCommitMessage.value + " [ci skip]",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepTask(httpService / Docker / publish),
      releaseStepTask(kafkaService / Docker / publish),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
  .enablePlugins(ReleasePlugin, ScalafixPlugin)
  .aggregate(scalaWarsCore, httpService, kafkaService)

addCommandAlias("format", ";scalafixAll;scalafmtAll;scalafmtSbt")
