logLevel := sbt.Level.Warn

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.15")
addSbtPlugin("org.scoverage"   % "sbt-scoverage"   % "2.0.7")
addSbtPlugin("org.scoverage"   % "sbt-coveralls"   % "1.3.11")

val SbtDevOopsVersion = "3.1.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % SbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % SbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % SbtDevOopsVersion)
