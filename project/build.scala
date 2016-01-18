import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.docker.{ExecCmd, Cmd, DockerPlugin}
import sbt._
import Keys._
import sbtassembly.{AssemblyPlugin, AssemblyKeys}
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import spray.revolver.RevolverPlugin.Revolver

object build extends Build {

  lazy val root = Project(
    id = "article-reader",
    base = file("."),
    settings = Defaults.coreDefaultSettings ++ graphSettings ++ Revolver.settings ++ Seq(
      resolvers ++= {
        Seq(
          "bintray cppexpert" at "https://dl.bintray.com/cppexpert/maven/",
          "bintray-bankmonitor-hu.bankmonitor.commons" at "http://dl.bintray.com/bankmonitor/hu.bankmonitor.commons"
        )
      },

      version := "1.20",
      scalaVersion := "2.11.7",
      libraryDependencies ++= {
        val akkaV = "2.4.1"
        val akkaStreamV = "2.0.1"
        val scalaTestV = "2.2.5"
        Seq(
          "org.scala-lang" % "scala-reflect" % "2.11.7",
          "com.typesafe.akka" %% "akka-actor" % akkaV withSources(),
          "com.typesafe.akka" %% "akka-typed-experimental" % akkaV withSources(),
          "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamV withSources(),
          "com.typesafe.akka" %% "akka-slf4j" % akkaV withSources(),
          "org.reflections" % "reflections" % "0.9.10" withSources(),
          "org.jsoup" % "jsoup" % "1.8.3" withSources(),
          "org.jasypt" % "jasypt" % "1.9.2" withSources(),

          "org.mongodb" % "mongo-java-driver" % "3.2.1",

          "org.ahocorasick" % "ahocorasick" % "0.3.0" withSources(),

          "org.scalikejdbc" %% "scalikejdbc" % "2.3.+",
          "com.chuusai" %% "shapeless" % "2.2.5",
          "commons-validator" % "commons-validator" % "1.4.1" withSources(),
          "org.apache.commons" % "commons-csv" % "1.1" withSources(),
          "org.apache.commons" % "commons-lang3" % "3.4" withSources(),
          "org.apache.james" % "apache-mime4j" % "0.7.2",
          "commons-codec" % "commons-codec" % "1.10" withSources(),
          "com.esotericsoftware.kryo" % "kryo" % "2.24.0" withSources(),
          "org.postgresql" % "postgresql" % "9.4.1207.jre7" withSources(),
          "com.zaxxer" % "HikariCP" % "2.4.3" withSources(),

          "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
          "ch.qos.logback" % "logback-classic" % "1.1.+",
          "org.scalatest" %% "scalatest" % scalaTestV % "test"
        )
      },

      scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-language:implicitConversions", "-language:postfixOps", "-language:higherKinds"), //, "-Xlog-implicits"),
      crossPaths := false,
      mainClass in Compile := Some("main")
    )
  ).enablePlugins(AssemblyPlugin).enablePlugins(JavaAppPackaging).enablePlugins(DockerPlugin)
}
