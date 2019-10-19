import sbt._

object Dependencies {

  lazy val hedgehogResolver = "bintray-scala-hedgehog" at "https://dl.bintray.com/hedgehogqa/scala-hedgehog"

  lazy val hedgehogVersion = "6dba7c9ba065e423000e9aa2b6981ce3d70b74cb"

  lazy val hedgehogCore: ModuleID = "hedgehog" %% "hedgehog-core" % hedgehogVersion
  lazy val hedgehogRunner: ModuleID = "hedgehog" %% "hedgehog-runner" % hedgehogVersion
  lazy val hedgehogSbt: ModuleID = "hedgehog" %% "hedgehog-sbt" % hedgehogVersion

  lazy val hedgehogAll: Seq[ModuleID] = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt).map(_ % Test)

}
