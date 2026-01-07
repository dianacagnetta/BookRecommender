@echo off
title Client BookRecommender
cd /d "%~dp0"

echo Avvio Client in corso...
:: Puntiamo al JAR creato (controlla il nome esatto in target)
java -jar clientBR\target\clientBR.jar

pause