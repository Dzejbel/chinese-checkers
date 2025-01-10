#!/bin/bash

cd chinesecheckers

# Czyści to co pod /target w obu projektach podrzędnych, pobiera zależności, kompiluje, pakuje w jara i testuje
mvn clean package
