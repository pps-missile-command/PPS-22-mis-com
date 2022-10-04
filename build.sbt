ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.12" % Test
val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.17.0" % "test"
val scalactic = "org.scalactic" %% "scalactic" % "3.2.14"
val scalaFX = "org.scalafx" %% "scalafx" % "18.0.1-R28"

lazy val root = (project in file("."))
  .settings(
    name := "missile command",
    libraryDependencies ++= Seq(scalatest, scalaCheck, scalactic, scalaFX),
    /*libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      lazy val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux") => "linux"
        case n if n.startsWith("Mac") => "mac"
        case n if n.startsWith("Windows") => "win"
        case _ => throw new Exception("Unknown platform!")
      }

      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "18" classifier osName)
    }*/
    libraryDependencies ++= Seq("linux", "mac", "win").flatMap { os =>
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "18" classifier os)
    }
  )

