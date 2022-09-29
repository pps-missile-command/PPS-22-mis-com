ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.12" % Test
val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.17.0" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "missile command",
    libraryDependencies ++= Seq(scalatest, scalaCheck)
  )
