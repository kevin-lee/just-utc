logLevel := sbt.Level.Warn

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.4.1")
addSbtPlugin("org.scoverage"   % "sbt-scoverage"   % "2.3.1")
addSbtPlugin("org.scoverage"   % "sbt-coveralls"   % "1.3.11")

val SbtDevOopsVersion = "3.1.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % SbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % SbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % SbtDevOopsVersion)
