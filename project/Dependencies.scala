import sbt._

object Dependencies {

  lazy val hedgehogResolver =
    Resolver.url("bintray-scala-hedgehog",
      url("https://dl.bintray.com/hedgehogqa/scala-hedgehog")
    )(Resolver.ivyStylePatterns)

  lazy val hedgehogVersion = "d53add6001242fe005db51bdb3b1045cb358a54e"
  lazy val hedgehogCore = "hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  lazy val hedgehogRunner = "hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  lazy val hedgehogSbt = "hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test

  lazy val hedgehogAll = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt)

}
