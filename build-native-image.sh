VERSION=$1
echo "Building the docker image tagged: javawormops/configme:$VERSION"
mvn package   -Dquarkus.native.container-build=true
docker build -t javawormops/configme:$VERSION .
