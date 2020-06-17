# Build, test and push to maven repo
mvn clean compile test deploy

docker build -t texasholdemserver .
docker tag texasholdemserver:latest cygni/texasholdem:server-1.2.7
#docker login (user cygni)
docker push cygni/texasholdem:server-1.2.7