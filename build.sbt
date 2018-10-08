import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.kevinlee",
      scalaVersion := "2.12.7",
      crossScalaVersions := Seq("2.11.12", "2.12.7"),
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "just-utc",
    resolvers += hedgehogResolver,
    libraryDependencies ++= hedgehogAll
  )
  .dependsOn(ProjectRef(gitSubmodule("submodule/scala-hedgehog"), "core") % Test)
  .dependsOn(ProjectRef(gitSubmodule("submodule/scala-hedgehog"), "runner") % Test)
  .dependsOn(ProjectRef(gitSubmodule("submodule/scala-hedgehog"), "sbt-test") % Test)

testFrameworks := Seq(TestFramework("hedgehog.sbt.Framework"))

def gitSubmodule(path: String): File = {
  @annotation.tailrec
  def search(parentPath: File): Option[File] = Option(parentPath) match {
    case Some(parentDir) =>
      if (new File(parentDir, ".git").isDirectory) {
        Some(new File(parentDir, path))
      } else {
        search(parentDir.getParentFile)
      }
    case None =>
      None
  }
  val parent = file(".").getCanonicalFile
  val pathFound = search(parent)
    .getOrElse(new File(parent, path))

  if (pathFound.exists()) {
    pathFound
  } else {
    throw new MessageOnlyException(s"Could not find submodule: $pathFound")
  }
}
