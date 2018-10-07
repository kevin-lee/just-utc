import sbt._

object Dependencies {

  lazy val hedgehogResolver =
    Resolver.url("bintray-scala-hedgehog",
      url("https://dl.bintray.com/hedgehogqa/scala-hedgehog")
    )(Resolver.ivyStylePatterns)

  lazy val hedgehogVersion = "eb31ccb5e2549c9424969cc3b3231f49330dd31f"

  lazy val hedgehogCore = "hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  lazy val hedgehogRunner = "hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  lazy val hedgehogSbt = "hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test

  lazy val hedgehogAll = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt)

}
