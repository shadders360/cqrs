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
* Dockerize the application with multi layer docker build layers
* sort out the path of all the endpoints into a web portal
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

### Commands
### Read model
### Docker
don't know about docker as a java dev!!!! [start here](https://www.docker.com/what-docker)

### Spring boot

If the build step has completed type the following to start the spring boot application

```
cd vehicle
cd target
java -jar vehicle-1.0.0-SNAPSHOT.jar
```

if no errors occur when sprint boot starts up the service is running 
[at](http://localhost:8080/api/vehicles).
The default mode will use in memory database for the query side and axon in memory Event store. 
To make use of the swagger ui interface use this [endpoint](http://localhost:8080/swagger-ui.html).

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
Once nice way to check the swagger api is correct is run the swager-ui in docker and load the generated swagger
spec output from (http://localhost:8080/v2/api-docs).

```
docker pull swaggerapi/swagger-editor
docker run -d -p 8090:8080 swaggerapi/swagger-editor
```

###Spring HAL browser of the read models
further stuff here .........

[hal](http://localhost:8080/)
