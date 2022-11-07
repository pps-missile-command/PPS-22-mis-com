ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.2.12" % Test
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
    assembly / mainClass := Some("view.startGame"),
    assembly / assemblyJarName := "missile_command.jar",
  )

/*mainClass in assembly := some("package.MainClass")
assemblyJarName := "desired_jar_name_after_assembly.jar"

val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}*/


