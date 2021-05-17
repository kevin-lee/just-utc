import sbt._

object Dependencies {
  
  lazy val hedgehogVersion = "0.7.0"

  lazy val hedgehogCore: ModuleID = "qa.hedgehog" %% "hedgehog-core" % hedgehogVersion
  lazy val hedgehogRunner: ModuleID = "qa.hedgehog" %% "hedgehog-runner" % hedgehogVersion
  lazy val hedgehogSbt: ModuleID = "qa.hedgehog" %% "hedgehog-sbt" % hedgehogVersion

  lazy val hedgehogAll: Seq[ModuleID] = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt).map(_ % Test)

}
