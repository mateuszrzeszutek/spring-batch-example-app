#!/bin/bash

set -e

docker run -d --name sfx-agent -p 9080:9080 \
  -v "${PWD}/agent.yaml":/etc/signalfx/agent.yaml:ro \
  -v /:/hostfs:ro -v /var/run/docker.sock:/var/run/docker.sock:ro -v /etc/passwd:/etc/passwd:ro \
  --pid host \
  quay.io/signalfx/signalfx-agent:5

java -javaagent:/opt/opentelemetry-javaagent-all.jar \
  -Dotel.resource.attributes=environment=mrzeszutek \
  -Dotel.exporter=zipkin \
  -Dotel.exporter.zipkin.endpoint="http://localhost:9080/v1/trace" \
  -Dotel.exporter.zipkin.service.name=spring-batch-example \
  -Dotel.propagators=b3multi \
  -Dotel.instrumentation.spring-batch.chunk.root-span=true \
  -jar target/spring-batch-example-app-1.0-SNAPSHOT.jar
