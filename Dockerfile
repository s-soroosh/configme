FROM ubuntu
WORKDIR /opt 
COPY target/configme-*-runner runner
CMD ["/opt/runner"]
