#!/bin/bash


/usr/local/jdk1.8.0_171/bin/java -Xmx128m -Djava.security.egd=file:/./urandom -jar /project/XMH/api/xmh-api-1.0.0-SNAPSHOT.jar --spring.config.location=/project/XMH/api/application-prod.properties &
