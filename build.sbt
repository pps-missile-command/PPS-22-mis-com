ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.12" % Test

lazy val root = (project in file("."))
  .settings(
    name := "missile command",
    libraryDependencies ++= Seq(scalatest)
  )
