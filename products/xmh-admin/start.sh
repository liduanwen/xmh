#!/bin/bash


/usr/local/jdk1.8.0_171/bin/java -Xmx128m -Djava.security.egd=file:/./urandom -jar /project/XMH/admin/xmh-admin-1.0.0-SNAPSHOT.jar --spring.config.location=/project/XMH/admin/application-prod.properties &
