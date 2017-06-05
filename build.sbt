name := """tic-tac-toe-web"""

organization := "jdesrosiers"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "io.javaslang" % "javaslang" % "2.0.6",
  "io.javaslang" % "javaslang-jackson" % "2.0.5",
  "jdesrosiers" %% "http-server" % "1.0",
  "jdesrosiers" %% "tic-tac-toe" % "1.0",

  "junit" % "junit" % "4.12"  % "test",
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "com.tngtech.java" % "junit-dataprovider" % "1.12.0" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)
