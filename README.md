# rest-with-spring-boot

* Run the spring boot project through the terminal, ignoring the tests
* mvn clean package spring-boot:run -DsKipTests


* Run project migrations
* mvn flyway:migrate

#  DOCKER IMAGES
* build application: mvn clean package -DskipTests
* start container: docker compose up -d
* stop container: docker compose down
* https://hub.docker.com/repositories/josenatal