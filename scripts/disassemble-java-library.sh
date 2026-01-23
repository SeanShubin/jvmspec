#!/usr/bin/env bash

# This script disassembles the entire Java library
# Prerequisite: scripts/load-java-libraries.sh must be run first

set -e

# Build the project
echo "Building project..."
mvn clean package -DskipTests -q

# Copy dependencies to a temporary directory
echo "Gathering dependencies..."
mvn dependency:copy-dependencies -pl console -DoutputDirectory=target/deps -q

# Build classpath: console jar + domain jar + contract jar + all external dependencies
CLASSPATH="console/target/jvmspec-console-0.1.0.jar"
CLASSPATH="$CLASSPATH:domain/target/jvmspec-domain-0.1.0.jar"
CLASSPATH="$CLASSPATH:contract/target/jvmspec-contract-0.1.0.jar"
for jar in console/target/deps/*.jar; do
  CLASSPATH="$CLASSPATH:$jar"
done

# Run the disassembly
echo "Disassembling Java libraries..."
java -cp "$CLASSPATH" com.seanshubin.jvmspec.console.Main scripts/java-library.json

echo "Disassembly complete. Output in generated/analysis/java-library/"
