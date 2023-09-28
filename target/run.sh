#!/bin/bash
export LIBGL_ALWAYS_INDIRECT=1
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml,javafx.web -Dprism.order=sw -jar ModelingLegalReasoning-1.0-SNAPSHOT.jar

