Introduction
------------

The purpose of this project was to get a better understanding of the CQRS [Command Query Responsibility Segregation] and Event Sourcing patterns 
using Java8 and a REST API. In this journey I am hoping it will allow me to consolidate my knowledge of REST , use new Java 8 features and 
have fun learning.

My first iteration was trying to write commands, events and queries to model the CQRS pattern as outlined by
[martin fowler](https://www.martinfowler.com/bliki/CQRS.html). All I wanted was to create a event log , play back my events 
and build my Aggregates! I got into a muddle the pattern is clear but implementing it was no an easy feat.
The key part of CQRS is the focus on Domain-Driven Design and building Aggregates which can be created form a playback of events.

The classic model to think about is when your visit your local GP Doctors. They have a medical file with a entry of every visit ,
drugs issued and test etc. At any point in time they can build up a picture of your medical history which in a nutshell is 
what CQRS and Event sourcing is about. [Greg Young] name comes up a lot and he has a number of interesting youtube talks on 
this subject [cqrs](https://youtu.be/8JKjvY4etTY) which are worth a watch. 

After looking at a number of sites on my CQRS trail I concluded that building from scratch was not going to be easy in my time frame to do this work. The same conclusion comes out from (https://docs.microsoft.com/en-us/azure/architecture/patterns/cqrs)[microsoft] in this area.
Also many people state the following which should be kept in mind.

**For some situations, this separation can be valuable, but beware that for most systems CQRS adds risky complexity.**

Finally I went to my favorite java framework (https://spring.io/) which always has something for java Engineers to 
build software with.

I used [spring-boot](https://projects.spring.io/spring-boot/) and [Axon framework](http://www.axonframework.org/).
So with some late nights ,looking at example reading and digging around in code I built this CQRS and
Event sourcing example project in spring boot as the first step.

##Things to do
* Dockerize the application with multi layer docker build layers final image jre not jdk
* docker start up fix boot app fails when no db connection. can be fixed with container auto start or other
* sort out the path of all the endpoints into a web portal
* add integration test when docker container is built using docker plugin
* use REACT to build simple front end
* expose the events via endpoint
* change register command to return created url with UUID. use sync command (makes sense on create)
* add command log
* stop jpa rest service posting to read model
* build distributed micro service using swarm 
* axon distributed message (rabbit mq?)
* swap from in memory to mysql or postgres using profiles
* add more commands and events to build more complex vehicle aggregate
* sort out checkstyle

##Building 

To run the service standalone using In memory database and Event store perform the following from the root
source directory.
```
mvn clean install
```
This will download all the jars and build the fat jar to be run in the target directory.

to run the standalone version 
```
cd vehicle
cd target
java -jar vehicle-1.0.0-SNAPSHOT.jar
```

### Commands
**more stuff here
### Read model

```
 "id": "vehicleQueryObject-representation",
        "href": "http://localhost:8080/profile/vehicleQueryObjects",
        "descriptors": [
          {
            "name": "vrm",
            "type": "SEMANTIC"
          },
          {
            "name": "vin",
            "type": "SEMANTIC"
          },
          {
            "name": "status",
            "doc": {
              "value": "UNTAXED, TAXED, SORNED",
              "format": "TEXT"
            },
            "type": "SEMANTIC"
          },
          {
            "name": "vedPaid",
            "type": "SEMANTIC"
          },
          {
            "name": "vedDuration",
            "type": "SEMANTIC"
          },
          {
            "name": "listPrice",
            "type": "SEMANTIC"
          },
          {
            "name": "make",
            "type": "SEMANTIC"
          },
          {
            "name": "model",
            "type": "SEMANTIC"
          }
        ]
      },
```



### Docker
don't know about docker as a java dev!!!! [start here](https://www.docker.com/what-docker)

This project makes use of the io.fabric8 docker plugin. Docker must be installed onto your 
laptop (windows/mac).

Once installed the normal build process can be followed. At the point where the docker image is required
use the maven profile switch to trigger docker build. 
```
mvn clean package -Pdocker

< END of maven build should see something like the following>
[INFO] Copying files to C:\home\java\shadders360\vehicle\target\docker\cqrs\vehicle\1.0.0-SNAPSHOT\build\maven
[INFO] Building tar: C:\home\java\shadders360\vehicle\target\docker\cqrs\vehicle\1.0.0-SNAPSHOT\tmp\docker-build.tar
[INFO] DOCKER> [cqrs/vehicle:1.0.0-SNAPSHOT] "vehicle": Created docker-build.tar in 1 second
[INFO] DOCKER> [cqrs/vehicle:1.0.0-SNAPSHOT] "vehicle": Built image sha256:10083
[INFO] DOCKER> [cqrs/vehicle:1.0.0-SNAPSHOT] "vehicle": Removed old image sha256:9f488
```
The image will be built and can be run using the normal docker cli commands.
In the target directory will be the generated docker file. To see the images use (docker images command).

The plugin has the following goals making use of the configuration elements defined for the image.

* mvn docker:start -Pdocker
makes use of the run section of the plugin same as 
docker run -d -p 8088:8080 --name vehicle cqrs/vehicle:1.0.0-SNAPSHOT

* mvn docker:logs -Ddocker.follow -Pdocker
same as docker logs <container> -f

* mvn clean install -Pdocker
runs the new docker image (memory version of app)(currently run attached to the install phase)

more plugin details can be found [fabric8.io](https://dmp.fabric8.io/#introduction) docs.


To run using mysql in the target/docker directory use the docker-compose.yaml file to run the 
 dockerized services.
```
 docker-compose up -d
 docker-compose ps
 
 ##### to stop conatiners
 docker-compose stop
 
 ##### to start conatiners
 docker-compose start
 
 ##### to stop and remove conatiners
 docker-compose down 
 
```
You may find the spring boot service fails as the mysql container is not up before the 
sprong boot try's to connect in which case re run docker-compose up -d command. (todo need to improve jpa
if no db connection )

### Spring boot

If the build step has completed type the following to start the spring boot application

```
cd vehicle
cd target
java -jar vehicle-1.0.0-SNAPSHOT.jar
```

if no errors occur when spring boot starts up the service will be running [here](http://localhost:8080/api/vehicles).

The default mode will use in memory database for the query side and axon in memory Event store. 
To make use of the swagger ui interface use [swagger ui](http://localhost:8080/swagger-ui.html).

note: if the docker container is running you will get a bind on port error if you start application and will 
      need to stop the docker container (docker ps , docker stop <conatiner or name>).
      In both cases a running application is exposed [here](http://localhost:8080/api/vehicles)

End points

* [swagger root](http://localhost:8080/swagger-resources)
* [swagger default api](http://localhost:8080/v2/api-docs) 
* [spring HAL endpoint](http://localhost:8080/)


### Swagger
This application makes use of springfox for integration of swagger and spring boot app. 
The SwaggerConfig.class is taken from the web tutorial
[springfox](http://springfox.github.io/springfox/docs/current/#configuring-the-objectmapper)
core swagger annotations found [here](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X).

The swagger annotations can be found in VehicleResource and SwaggerConfig class files.
Once nice way to check the swagger api is correct is run the swager-ui in docker 
and load the generated swagger spec output from (http://localhost:8080/v2/api-docs).

```
docker pull swaggerapi/swagger-editor
docker run -d -p 8090:8080 swaggerapi/swagger-editor
```

###Spring HAL browser of the read models
**further stuff here .........

[hal](http://localhost:8080/)

