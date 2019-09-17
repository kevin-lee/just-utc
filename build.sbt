import Dependencies._
import ProjectInfo._
import kevinlee.sbt.SbtCommon.crossVersionProps
import kevinlee.semver.{Major, Minor, SemanticVersion}
import org.scoverage.coveralls.Imports.CoverallsKeys._

ThisBuild / scalaVersion := ProjectScalaVersion
ThisBuild / organization := "kevinlee"
ThisBuild / version      := ProjectVersion
ThisBuild / crossScalaVersions := CrossScalaVersions
ThisBuild / developers   := List(Developer(
    "Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee"))
  )

lazy val justUtc = (project in file(".")).
    settings(
      name := "just-utc"
    , wartremoverErrors in (Compile, compile) ++= commonWarts
    , wartremoverErrors in (Test, compile) ++= commonWarts
    , resolvers += hedgehogResolver
    , libraryDependencies ++= hedgehogAll
    , dependencyOverrides ++= crossVersionProps(Seq.empty[ModuleID], SemanticVersion.parseUnsafe(scalaVersion.value)) {
      case (Major(2), Minor(10)) =>
        Seq("org.wartremover" %% "wartremover" % "2.3.7")
      case x =>
        Seq.empty
    }
    , testFrameworks := Seq(TestFramework("hedgehog.sbt.Framework"))
    /* Bintray { */
    , bintrayPackageLabels := Seq("Scala", "UTC", "DateTime")
    , bintrayVcsUrl := Some("""git@github.com:Kevin-Lee/just-utc.git""")
    , licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
    /* } Bintray */
    /* Coveralls { */
    , coverageHighlighting := (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) =>
        false
      case _ =>
        true
    })
    , coverallsTokenFile := Option(s"""${Path.userHome.absolutePath}/.coveralls-credentials""")
    /* } Coveralls */
  )


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
