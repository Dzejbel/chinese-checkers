#!/bin/bash

cd chinesecheckers

# Czyści to co pod /target w module server, pobiera zależności, kompiluje, pakuje w jara i testuje
mvn clean -pl server
mvn package -pl server
