import Dependencies._
import ProjectInfo._
import kevinlee.sbt.SbtCommon.crossVersionProps
import just.semver.SemVer
import org.scoverage.coveralls.Imports.CoverallsKeys._

val ProjectScalaVersion: String = "2.13.1"
val CrossScalaVersions: Seq[String] = Seq("2.11.12", "2.12.11", ProjectScalaVersion)

lazy val justFp: ModuleID = "io.kevinlee" %% "just-fp" % "1.3.5"

ThisBuild / scalaVersion := ProjectScalaVersion
ThisBuild / organization := "io.kevinlee"
ThisBuild / version      := ProjectVersion
ThisBuild / crossScalaVersions := CrossScalaVersions
ThisBuild / developers   := List(Developer(
    "Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee"))
  )
ThisBuild / homepage := Some(url("https://github.com/Kevin-Lee/just-utc"))
ThisBuild / scmInfo :=
    Some(ScmInfo(
        url("https://github.com/Kevin-Lee/just-utc")
      , "git@github.com:Kevin-Lee/just-utc.git"
    ))


lazy val justUtc = (project in file(".")).
    settings(
      name := "just-utc"
    , libraryDependencies := hedgehogAll ++ crossVersionProps(
          Seq(justFp)
        , SemVer.parseUnsafe(scalaVersion.value)) {
        case (SemVer.Major(2), SemVer.Minor(10)) =>
          libraryDependencies.value
            .filterNot(m => m.organization == "org.wartremover" && m.name == "wartremover")
        case x =>
          libraryDependencies.value
      }
    , Compile / unmanagedSourceDirectories ++= {
      val sharedSourceDir = (ThisBuild / baseDirectory).value / "src/main"
      if (scalaVersion.value.startsWith("2.10") || scalaVersion.value.startsWith("2.11"))
        Seq(sharedSourceDir / "scala-2.10_2.11")
      else if (scalaVersion.value.startsWith("2.12") || scalaVersion.value.startsWith("2.13"))
        Seq(sharedSourceDir / "scala-2.12_2.13")
      else
        Seq()
    }
    , Compile / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value)
    , Test / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value)
    , testFrameworks ++= Seq(TestFramework("hedgehog.sbt.Framework"))
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
