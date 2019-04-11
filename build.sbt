import Dependencies._
import ProjectInfo._
import kevinlee.sbt.SbtCommon.crossVersionProps
import kevinlee.semver.{Major, Minor, SemanticVersion}
import org.scoverage.coveralls.Imports.CoverallsKeys._

lazy val root = (project in file(".")).
    settings(
      inThisBuild(List(
        organization := "kevinlee"
      , scalaVersion := ProjectScalaVersion
      , crossScalaVersions := CrossScalaVersions
      , version      := ProjectVersion
      , developers   := List(Developer(
          "Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee")
        ))
    ))
    , name := "just-utc"
    , scalacOptions :=
      crossVersionProps(Seq.empty, SemanticVersion.parseUnsafe(scalaVersion.value)) {
        case (Major(2), Minor(12)) =>
          scalacOptions.value ++ commonScalacOptions
        case (Major(2), Minor(11)) =>
          (scalacOptions.value ++ commonScalacOptions).filter(_ != "-Ywarn-unused-import")
        case _ =>
          (scalacOptions.value ++ commonScalacOptions)
            .filter(option =>
              option != "-Ywarn-unused-import" && option != "-Ywarn-numeric-widen"
            )
      }.distinct
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
