import sbt._

object Dependencies {

  lazy val hedgehogResolver =
    Resolver.url("bintray-scala-hedgehog",
      url("https://dl.bintray.com/hedgehogqa/scala-hedgehog")
    )(Resolver.ivyStylePatterns)

  lazy val hedgehogVersion = "55d9828dc6bcdc85ba3ebb31efd541d0a14423bf"

  lazy val hedgehogCore = "hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  lazy val hedgehogRunner = "hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  lazy val hedgehogSbt = "hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test

  lazy val hedgehogAll = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt)

}
