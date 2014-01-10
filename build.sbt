import xerial.sbt.Pack._

name := "shipp3r"

scalaVersion := "2.10.3"

libraryDependencies += "net.java.dev.jets3t" % "jets3t" % "0.9.0"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0"

seq(packSettings :_*)

packMain := Map("shipp3r" -> "io.github.fanatoly.Shipp3r")

packJvmOpts := Map("shipp3r" -> Seq("-Xmx8M", "-XX:-UseSerialGC"))
