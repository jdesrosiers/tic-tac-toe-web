[![Build Status](https://travis-ci.org/jdesrosiers/tic-tac-toe-web.svg?branch=master)](https://travis-ci.org/jdesrosiers/tic-tac-toe-web)

Tic-Tac-Toe Web
===============
This project gives a web interface to Tic-Tac-Toe written in Scala [jdesrosiers/tictactoe](https://github.com/jdesrosiers/tictactoe) using the Flint web micro-framework written in Java [jdesrosiers/http-server](https://github.com/jdesrosiers/http-server).

Development
-----------
This project uses the [sbt](http://www.scala-sbt.org/) build tool.

### Dependencies
Because the dependent projects are not in a public package repository, they need to be published to a local repository for this project to build properly.  There is a script to build and publish the dependencies for you.  If you want to hack on the dependencies as well, you can publish them to the local repository manually using the `sbt publishLocal` task.

```shell
> bin/publish_local_dependencies.sh
```

### Build
You can build the project with sbt.  Build artifacts will be in in `./target/scala-2.11`.

```shell
> sbt build
```

Or, build a standalone jar at `./target/scala-2.11/tic-tac-toe-web-assembly-1.0.jar`.

```shell
> sbt assembly
```

### Test
Tests are written with JUnit and can be run using sbt

```shell
> sbt test
```

### Run
You can run the project with sbt.

```shell
> sbt run
```

Or, you can run the assembled jar.

```shell
> java -jar target/scala-2.11/tic-tac-toe-web-assembly-1.0.jar
```

Project Plan
------------

### Current Sprint
**From** ??? **to** ???

| AV  | IP  | CP  | SP  | Description |
|:---:|:---:|:---:|:---:|-------------|

**Legend:** *AV* => Available, *IP* => In Progress, *CP* => Completed, *SP* => Story Points

### Backlog
| SP  | Description |
|:---:|-------------|
