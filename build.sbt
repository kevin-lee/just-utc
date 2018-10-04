import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.kevinlee",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "just-utc",
    resolvers += hedgehogResolver,
    libraryDependencies ++= hedgehogAll
  )

testFrameworks := Seq(TestFramework("hedgehog.sbt.Framework"))
