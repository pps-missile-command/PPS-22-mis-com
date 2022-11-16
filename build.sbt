
ThisBuild / version := "1.0.1"

ThisBuild / scalaVersion := "3.2.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.14" % Test

val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.17.0" % "test"
val scalactic = "org.scalactic" %% "scalactic" % "3.2.14"


lazy val root = (project in file("."))
  .settings(
    name := "missile command",
    libraryDependencies ++= Seq(scalatest, scalaCheck, scalactic)
      ++
      Seq(
        "io.monix" %% "monix" % "3.4.1",
        "dev.optics" %% "monocle-core" % "3.1.0",
        "dev.optics" %% "monocle-macro" % "3.1.0"
      ),

    assembly / mainClass := Some("view.Main"),
    assembly / assemblyJarName := "missile_command.jar",
  )


