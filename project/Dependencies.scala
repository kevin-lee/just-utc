import sbt._

object Dependencies {

  lazy val hedgehogResolver =
    Resolver.url("bintray-scala-hedgehog",
      url("https://dl.bintray.com/hedgehogqa/scala-hedgehog")
    )(Resolver.ivyStylePatterns)

  lazy val hedgehogVersion = "06b22e95ca1a32a2569914824ffe6fc4cfd62c62"

  lazy val hedgehogCore = "hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  lazy val hedgehogRunner = "hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  lazy val hedgehogSbt = "hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test

  lazy val hedgehogAll = Seq(hedgehogCore, hedgehogRunner, hedgehogSbt)

}
