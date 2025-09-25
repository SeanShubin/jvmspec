#!/usr/bin/env bash

mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get \
    -DrepoUrl=https://repo1.maven.org/maven2 \
    -Dartifact=com.seanshubin.code.structure:code-structure-console:1.0.3
