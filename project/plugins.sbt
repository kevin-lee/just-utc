logLevel := sbt.Level.Warn

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.15")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.0")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.2.7")

val SbtDevOopsVersion = "3.1.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % SbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % SbtDevOopsVersion)
