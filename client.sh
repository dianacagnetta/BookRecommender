#!/bin/bash

echo "Avvio Client in corso..."
cd "$(dirname "$0")"

# Puntiamo al JAR creato (controlla il nome esatto in target)
java -jar clientBR/target/clientBR.jar
