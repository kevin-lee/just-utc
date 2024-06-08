import Dependencies._
import ProjectInfo._
import kevinlee.sbt.SbtCommon.crossVersionProps
import just.semver.SemVer
import org.scoverage.coveralls.Imports.CoverallsKeys._

ThisBuild / scalaVersion := props.ProjectScalaVersion
ThisBuild / organization := props.Org
ThisBuild / version := ProjectVersion
ThisBuild / crossScalaVersions := props.CrossScalaVersions
ThisBuild / developers := List(
  Developer("Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee"))
)
ThisBuild / homepage := Some(url("https://github.com/Kevin-Lee/just-utc"))
ThisBuild / scmInfo :=
  Some(
    ScmInfo(
      url("https://github.com/Kevin-Lee/just-utc"),
      "git@github.com:Kevin-Lee/just-utc.git"
    )
  )
ThisBuild / licenses := List(License.MIT)

lazy val justUtc = (project in file("."))
  .settings(
    name := prefixedProjectName(""),
  )
  .aggregate(core)

lazy val core = module("core").settings(
  libraryDependencies := crossVersionProps(List(libs.justFp), SemVer.parseUnsafe(scalaVersion.value)) {
    case (SemVer.Major(2), SemVer.Minor(10), _) =>
      libraryDependencies
        .value
        .filterNot(m => m.organization == "org.wartremover" && m.name == "wartremover")
    case x =>
      libraryDependencies.value
  },
  libraryDependencies := libraryDependencies.value.filterNot(removeDottyIncompatible),
  Compile / unmanagedSourceDirectories ++= {
    val sharedSourceDir = baseDirectory.value / "src/main"
    if (scalaVersion.value.startsWith("2.10") || scalaVersion.value.startsWith("2.11"))
      Seq(sharedSourceDir / "scala-2.10_2.11")
    else if (scalaVersion.value.startsWith("2.12") || scalaVersion.value.startsWith("2.13"))
      Seq(sharedSourceDir / "scala-2.12_2.13")
    else
      Seq.empty
  },
  Compile / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value),
  Test / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value),
  scalacOptions := scalacOptions.value.filterNot(_.startsWith("-P:wartremover")),
  (Compile / compile / scalacOptions) := (Compile / compile / scalacOptions)
    .value
    .filterNot(_.startsWith("-P:wartremover")),
  (Test / compile / scalacOptions) := (Test / compile / scalacOptions).value.filterNot(_.startsWith("-P:wartremover")),
  licenses := List(License.MIT),
)

lazy val props = new {
  final val Org = "io.kevinlee"

  val ProjectName = "just-utc"

  final val CrossScalaVersions  = Seq("2.11.12", "2.12.13", "2.13.10", "3.0.0").distinct
  final val ProjectScalaVersion = CrossScalaVersions.last

  final val JustFpVersion = "1.6.0"
}

lazy val libs = new {
  lazy val justFp: ModuleID = "io.kevinlee" %% "just-fp-core" % props.JustFpVersion
}

val removeDottyIncompatible: ModuleID => Boolean =
  m =>
    m.name == "wartremover" ||
      m.name == "ammonite" ||
      m.name == "kind-projector" ||
      m.name == "better-monadic-for" ||
      m.name == "mdoc"

def prefixedProjectName(projectName: String): String =
  if (projectName.isEmpty) props.ProjectName else s"${props.ProjectName}-$projectName"

def module(projectName: String): Project = {
  val prefixedName = prefixedProjectName(projectName)

  Project(projectName, file(s"modules/$prefixedName"))
    .settings(
      name := prefixedName,
      libraryDependencies ++= hedgehogAll,
      Compile / unmanagedSourceDirectories ++= {
        val sharedSourceDir = baseDirectory.value / "src/main"
        if (scalaVersion.value.startsWith("2.10") || scalaVersion.value.startsWith("2.11"))
          Seq(sharedSourceDir / "scala-2.10_2.11")
        else if (scalaVersion.value.startsWith("2.12") || scalaVersion.value.startsWith("2.13"))
          Seq(sharedSourceDir / "scala-2.12_2.13")
        else
          Seq.empty
      },
      Compile / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value),
      Test / compile / wartremoverErrors ++= commonWarts((update / scalaBinaryVersion).value),
      scalacOptions := scalacOptions.value.filterNot(_.startsWith("-P:wartremover")),
      (Compile / compile / scalacOptions) := (Compile / compile / scalacOptions)
        .value
        .filterNot(_.startsWith("-P:wartremover")),
      (Test / compile / scalacOptions) := (Test / compile / scalacOptions)
        .value
        .filterNot(_.startsWith("-P:wartremover")),
      licenses := List(License.MIT),
      /* Coveralls { */
      coverageHighlighting := (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) =>
          false
        case _ =>
          true
      }),
      coverallsTokenFile := Option(s"""${Path.userHome.absolutePath}/.coveralls-credentials""")
      /* } Coveralls */
    )
}

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

  val parent    = file(".").getCanonicalFile
  val pathFound = search(parent)
    .getOrElse(new File(parent, path))

  if (pathFound.exists()) {
    pathFound
  } else {
    throw new MessageOnlyException(s"Could not find submodule: $pathFound")
  }
}
