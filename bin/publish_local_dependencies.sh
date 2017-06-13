#!/bin/bash

mkdir temp
cd temp

git clone https://github.com/jdesrosiers/tictactoe.git
cd tictactoe
sbt publishLocal
cd ..

git clone https://github.com/jdesrosiers/http-server.git
cd http-server
sbt publishLocal
cd ..

cd ..
rm -rf temp
