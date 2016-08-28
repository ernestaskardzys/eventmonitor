# eventmonitor
A sample project with ActiveMQ, Spring, WebSockets and Spring Web Services.

# Installation
- Download [Tomcat 8.5.4](http://tomcat.apache.org/)
- In Tomcat - add **-Dspring.profiles.active=prod** to environment variables.
Simplest way to do this is to create **<tomcat_directory>/bin/setenv.sh** file with the following content:
```bash
CATALINA_OPTS="-Dspring.profiles.active=prod"
export
```
- Build the project with:
```bash
./gradlew clean build
```
- Copy **eventmonitor.war** from **build/libs** directory to **<tomcat_directory>/webapps**
- Start Tomcat - execute
```bash
./startup.sh
```
in **<tomcat_directory>/bin**
- Go to: [working application](http://localhost:8080/eventmonitor/event-monitor) in your browser.

# A short overview of the architecture
All requests from clients come to **/rest** endpoint and are written to the ActiveMQ queue **incomingEventQueue** where it's processed and saved into other queue - **processedEventQueue**. Records from this table are written to WebSocket endpoint **/server/list**.
 
All the errors also are written to **/server/list**.

# Configuration options
There are 3 configuration files: **dev.application.properties** (for development environment), **test.application.properties** (for tests) and **prod.application.properties** (for production environment).
 
Database configuration options:
```bash
# URL to database server
db.url=jdbc:h2:mem:prod
# DB user name
db.user=sa
# DB password
db.password=
```
 
There are several processing configuration options available:
```bash
# Should system simulate processing delay. If "false" - event.processingTime.initial and event.processingTime.maxRandom will be ignored
event.processingTime.enableDelay=true
# Constant delay (in ms)
event.processingTime.initial=500
# Max random delay (in ms)
event.processingTime.maxRandom=500
```

There are several ActiveMQ configuration options available:
```bash
# Name of ActiveMQ broker
activemq.broker.name=broker
# URL of ActiveMQ server
activemq.broker.url=tcp://localhost:61616
# Incoming queue name
activemq.queue.name.incomingEventQueue=incomingEventQueue
# Processed (outgoing) queue name
activemq.queue.name.processedEventQueue=processedEventQueue
# Minumum number of ActiveMQ listeners/consumers available
activemq.queue.consumers.count=5
# Maximum number of ActiveMQ listeners/consumers available
activemq.queue.consumers.maxCount=10
```

# Endpoints
There are several endpoints available on the application.
HTTP endpoint:
- **/** or **/event-monitor** - main entry point for application

REST endpoints:
-- **/rest/** (PUT method) - send a new event request to the application
-- **/rest/info** (GET method) - version information about the application
-- **/rest/health-check** (GET method) - health check information about the application

# Tests
- There is a sample JMeter test available at **/etc/jmeter/LoadTest.jmx**
- All unit tests are in **src/test/**.
- Integration test is in **src/integration-test** directory. This integration test loads Spring application context into embedded Tomcat application server and runs an integration test on it.