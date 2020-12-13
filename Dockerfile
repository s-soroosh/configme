FROM ubuntu
WORKDIR /opt 
COPY target/configme-core-1.0-SNAPSHOT-runner runner
CMD ["/opt/runner"]
