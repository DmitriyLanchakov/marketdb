import sbt._
import sbt.Keys._

object MarketDBBuild extends Build {

  lazy val buildSettings = Seq(
    organization := "com.ergodicity.marketdb",
    version      := "0.1-SNAPSHOT",
    scalaVersion := "2.9.1"
  )

  lazy val marketdb = Project(
    id = "marketdb",
    base = file("."),
    aggregate = Seq(marketdbApi, marketdbCore, marketdbLoader)
  )

  lazy val marketdbApi = Project(
    id = "marketdb-api",
    base = file("marketdb-api"),
    settings = Project.defaultSettings ++ repositoriesSetting ++ Seq(libraryDependencies ++= Dependencies.api)
  )

  lazy val marketdbCore = Project(
    id = "marketdb-core",
    base = file("marketdb-core"),
    dependencies = Seq(marketdbApi),
    settings = Project.defaultSettings ++ repositoriesSetting ++ Seq(libraryDependencies ++= Dependencies.core)
  ).configs( IntegrationTest )
    .settings( Defaults.itSettings : _*)

  lazy val marketdbLoader = Project(
    id = "marketdb-loader",
    base = file("marketdb-loader"),
    dependencies = Seq(marketdbApi),
    settings = Project.defaultSettings ++ repositoriesSetting ++ Seq(libraryDependencies ++= Dependencies.loader)
  )

  lazy val repositoriesSetting = Seq(
    resolvers += "Sonatype Repository" at "http://oss.sonatype.org/content/groups/public/",
    resolvers += "JBoss repository" at "http://repository.jboss.org/nexus/content/repositories/",
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Typesafe Repository ide-2.9" at "http://repo.typesafe.com/typesafe/simple/ide-2.9/",
    resolvers += "Twitter Repository" at "http://maven.twttr.com/",
    resolvers += "Akka Repository" at "http://akka.io/snapshots/"
  )
}

object Dependencies {
  import Dependency._

  val api = Seq(sbinary, jodaTime, jodaConvert)

  val core = Seq(kestrel, scalaTime, sbinary, finagleCore, scalaSTM, slf4jApi, logback, asyncHBase, scalaz, cglib, jodaTime, jodaConvert, springCore, springBeans, springContext) ++
    Seq(Test.springTest, Test.junit, Test.mockito, Test.powermockApi, Test.powermockJUnit, Test.scalatest, Test.scalacheck, Test.junitInterface)

  val loader = Seq(httpClient, kestrel, scalaTime, sbinary, jodaTime, jodaConvert, kestrel, slf4jApi, logback, scalaz)
}


object Dependency {

  // Versions

  object V {
    val Scalatest    = "1.6.1"
    val Slf4j        = "1.6.4"
    val Spring       = "3.1.0.RELEASE"
    val Junit        = "4.5"
    val Mockito      = "1.8.1"
    val Powermock    = "1.4.11"
    val Scalacheck   = "1.9"
    val AsyncHBase   = "1.1.0"
    val Scalaz       = "6.0.4"
    val Cglib        = "2.2.2"
    val Logback      = "1.0.0"
    val ScalaSTM     = "0.4"
    val JodaTime     = "2.0"
    val JodaConvert  = "1.2"
    val Finagle      = "1.11.0"
    val SBinary      = "0.4.0"
    val ScalaTime    = "0.5"
    val Kestrel      = "2.1.5"
    val HttpCLient   = "3.1"
  }

  // Compile

  val springCore    = "org.springframework"         % "spring-core"            % V.Spring     // ApacheV2
  val springBeans   = "org.springframework"         % "spring-beans"           % V.Spring     // ApacheV2
  val springContext = "org.springframework"         % "spring-context"         % V.Spring     // ApacheV2
  val slf4jApi      = "org.slf4j"                   % "slf4j-api"              % V.Slf4j      // MIT
  val logback       = "ch.qos.logback"              % "logback-classic"        % V.Logback
  val asyncHBase    = "org.hbase"                   % "asynchbase"             % V.AsyncHBase
  val scalaz        = "org.scalaz"                 %% "scalaz-core"            % V.Scalaz
  val cglib         = "cglib"                       % "cglib"                  % V.Cglib
  val scalaSTM      = "org.scala-tools"            %% "scala-stm"              % V.ScalaSTM
  val jodaTime      = "joda-time"                   % "joda-time"              % V.JodaTime
  val jodaConvert   = "org.joda"                    % "joda-convert"           % V.JodaConvert
  val finagleCore   = "com.twitter"                %% "finagle-core"           % V.Finagle
  val sbinary       = "org.scala-tools.sbinary"    %% "sbinary"                % V.SBinary
  val scalaTime     = "org.scala-tools.time"       %% "time"                   % V.ScalaTime
  val kestrel       = "net.lag"                     % "kestrel"                % V.Kestrel
  val httpClient    = "commons-httpclient"          % "commons-httpclient"     % V.HttpCLient

  // Provided

  object Provided {

  }

  // Runtime

  object Runtime {

  }

  // Test

  object Test {
    val springTest     = "org.springframework"         % "spring-test"             % V.Spring       % "test"
    val junit          = "junit"                       % "junit"                   % V.Junit        % "test" // Common Public License 1.0
    val mockito        = "org.mockito"                 % "mockito-all"             % V.Mockito      % "test" // MIT
    val powermockApi   = "org.powermock"               % "powermock-api-mockito"   % V.Powermock    % "test"
    val powermockJUnit = "org.powermock"               % "powermock-module-junit4" % V.Powermock    % "test"
    val scalatest      = "org.scalatest"              %% "scalatest"               % V.Scalatest    % "it,test" // ApacheV2
    val scalacheck     = "org.scala-tools.testing"    %% "scalacheck"              % V.Scalacheck   % "test" // New BSD
    val junitInterface = "com.novocode"                % "junit-interface"         % "0.8"          % "test"
  }
}