#!/bin/bash

# java --module-path ~/.m2/repository/org/openjfx \
#      --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
#      -jar chinesecheckers/client/target/client-1.0-SNAPSHOT.jar

cd chinesecheckers/client
mvn javafx:run
