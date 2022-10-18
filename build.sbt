ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.12" % Test
val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.17.0" % "test"
val scalactic = "org.scalactic" %% "scalactic" % "3.2.14"
val scalaFX = "org.scalafx" %% "scalafx" % "18.0.1-R28"

lazy val root = (project in file("."))
  .settings(
    name := "missile command",
    libraryDependencies ++= Seq(scalatest, scalaCheck, scalactic, scalaFX)
      ++
      Seq("linux", "mac", "win").flatMap {
        os =>
          Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
            .map(m => "org.openjfx" % s"javafx-$m" % "18" classifier os)
      }
      ++
      Seq(
        "io.monix" %% "monix" % "3.4.1",
        "dev.optics" %% "monocle-core" % "3.1.0",
        "dev.optics" %% "monocle-macro" % "3.1.0"
      )
  )

