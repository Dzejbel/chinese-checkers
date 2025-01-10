#!/bin/bash

cd chinesecheckers

# Czyści to co pod /target w module client, pobiera zależności, kompiluje, pakuje w jara i testuje
mvn clean -pl client
mvn package -pl client
