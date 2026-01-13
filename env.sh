#!/bin/bash
# Environment variables for RMI services

export RMI_PORT=1099
export RMI_HOST=localhost
export PROJECT_ROOT="/home/samuele/Desktop/java/Hotel-Colossus"
export CLASSPATH="/home/samuele/Desktop/java/Hotel-Colossus/target/classes"
export LOGS_DIR="/home/samuele/Desktop/java/Hotel-Colossus/logs"
export PID_DIR="/home/samuele/Desktop/java/Hotel-Colossus/pids"

# MySQL JDBC Driver (aggiungere se necessario)
# export CLASSPATH="$CLASSPATH:/home/samuele/Desktop/java/Hotel-Colossus/lib/mysql-connector-java.jar"

# Bcrypt library
# export CLASSPATH="$CLASSPATH:/home/samuele/Desktop/java/Hotel-Colossus/lib/bcrypt.jar"

# Java RMI options
export JAVA_OPTS="-Djava.rmi.server.hostname=localhost"
export JAVA_OPTS="$JAVA_OPTS -Djava.security.policy=/home/samuele/Desktop/java/Hotel-Colossus/rmi.policy"
export JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.codebase=file:///home/samuele/Desktop/java/Hotel-Colossus/target/classes/"

echo "✓ Environment variables loaded"
