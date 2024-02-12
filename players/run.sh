#!/bin/zsh

### 1. Build using maven:
mvn clean install
### 2. To test the single-threaded implementation:
java -cp target/players-1.0-SNAPSHOT.jar com.roughterr.players.example.SingleThreadExample
### 3. To test the multi-threaded implementation:
java -cp target/players-1.0-SNAPSHOT.jar com.roughterr.players.example.MultiThreadExample
