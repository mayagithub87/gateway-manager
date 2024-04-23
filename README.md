# Gateway Manager

### Documentation

This sample project is managing gateways - master devices that control multiple peripheral devices. 
Your task is to create a REST service (JSON/HTTP) for storing information about these gateways and their associated devices. This information must be stored in the database. 
When storing a gateway, any field marked as “to be validated” must be validated and an error returned if it is invalid. Also, no more than 10 peripheral devices are allowed for a gateway.
The service must also offer an operation for displaying information about all stored gateways (and their devices) and an operation for displaying details for a single gateway. Finally, it must be possible to add and remove a device from a gateway.

<p>Each gateway has:</p>

* a unique serial number
* human-readable name
* IPv4 address (to be validated)
* multiple associated peripheral devices

<p>Each device has:</p>

* a UID
* vendor
* date created
* status online/offline

### Requisites
* VSCode or intelliJ Idea IDE
* Maven installed
* MYSQL or MariaDb database

### Database
Create a MYSQL or MariaDb database and specify connection values at the file placed in `resources\application.properties` in both packages `java` and `test`
of the project.
`
spring.datasource.url=jdbc:mysql://localhost:3306/gateways-db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
`
### Build and Run Project

Execute the following command for putting up and running REST api.

`./mvnw spring-boot:run`

### Run Integration Tests

Integration tests (end to end) were created using JUnit 5.  
To execute them type following command `./mvnw test` .

### API Documentation

API was documented using Swagger library. At the following url you may see the information after project is running:

`http://localhost:8080/gateway-manager/swagger-ui/#/gateway-controller`

### Postman Collection

For testing REST api a postman collection has been provided with name `GATEWAYS.postman_collection`, which you may import, it can be found along the project.