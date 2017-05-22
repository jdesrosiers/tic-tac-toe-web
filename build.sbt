name := """tic-tac-toe-web"""

organization := "jdesrosiers"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "jdesrosiers" %% "http-server" % "1.0",
  "jdesrosiers" %% "tic-tac-toe" % "1.0",
  "junit" % "junit" % "4.12"  % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)
