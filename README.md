# fraud-detection

This is an application intended to persist data about users and transactions in order to detect fraud in the context of e-commerce. With this purpose, it provides a REST API to inject the data and check if a transaction is a fraud attempt.

It also provides a Kafka consumer and a RabbitMQ listener to listen to user data injection commands.

It uses Spring Data Neo4j to model a graph database. This way of connecting entities is more natural and efficient for the purposes of the application.

Plus, it offers the possibility to integrate [CAMARA Project](https://camaraproject.org/) fraud detection APIs. For now, the Device Location Verification API is integrated through [MultiApi Maven Plugin](https://github.com/sngular/scs-multiapi-plugin). The real connection with real telco companies depend on these considerations. Most telco companies require registration or charge for the use of their APIs. It has been intended that the final considerations (like consent management and the relation with the API providers) about the use of these APIs is delegated to the potential users of this application. A revision on the preparation of these matters for final users is pending.

## Technologies

- Java 17
- Spring Boot
- Neo4j
- Junit5
- Maven
- Kafka
- RabbitMQ

## How to run on your host

1. Ensure Docker and Docker Compose are installed on your system.
2. For the tests to pass, you will need an instance of Neo4j running. You can start it with Docker by running:

    ```bash
    docker run --publish=27474:7474 --publish=27687:7687 --volume=$HOME/test/neo4j/data:/data --env NEO4J_AUTH=neo4j/secretgraph neo4j
    ```

3. Create the .jar application by running the following command in this repository root:

    ```bash
    mvn clean package -P kafka,rabbitmq
    ```   
   The activation of "kafka" and "rabbitmq" profiles ensures the creation of the .jar with the necessary dependencies to run the Kafka consumer and the RabbitMQ listener, repectively.


4. Build the `fraud-detection-rabbitmq-kafka` image by running the following command in this repository root:

    ```bash
    docker build -t fraud-detection-rabbitmq-kafka .
    ```

5. Start the services with:

    ```bash
    docker-compose up
    ```

This will start all required services, and the `fraud-detection` REST endpoint of the application will be available at port `http://localhost:8080`.

You can access Neo4j at `http://localhost:7474`, RabbitMQ Management UI at `http://localhost:15672`, and use the application's endpoints to test the fraud detection system.


## REST Endpoints

### /users

<details>
 <summary><code>POST</code> <code><b>/inject</b></code> <code>(registers a User in the database)</code></summary>

##### Parameters

> | name               | type           | data type         | description                                                                                         |
> |--------------------|----------------|-------------------|-----------------------------------------------------------------------------------------------------|
> | injectUserCommand  | required, body | object (JSON)     | Object containing all the necessary information about a user to be stored (InjectUserCommand.class) |


##### Responses

> | http code | content-type                      | response                                                          |
> |-----------|-----------------------------------|-------------------------------------------------------------------|
> | `200`     | `text/plain;charset=UTF-8`        | `User injected successfully`                                      |
> | `400`     | `application/json`                | `a field must not be null, empty, or invalid (e.g. email format)` |


</details>

### /devices

<details>
 <summary><code>POST</code> <code><b>/inject</b></code> <code>(registers a Device in the database, related to a User)</code></summary>

##### Parameters

> | name                | type           | data type         | description                                                                                             |
> |---------------------|----------------|-------------------|---------------------------------------------------------------------------------------------------------|
> | injectDeviceCommand | required, body | object (JSON)     | Object containing all the necessary information about a Device to be stored (InjectDeviceCommand.class) |


##### Responses

> | http code | content-type                      | response                                                          |
> |-----------|-----------------------------------|-------------------------------------------------------------------|
> | `200`     | `text/plain;charset=UTF-8`        | `Device injected successfully`                                    |
> | `400`     | `application/json`                | `a field must not be null, empty, or invalid (e.g. email format)` |
> | `404`     | `application/json`                | `the User of the Device does not exist in database `              |


</details>

### /locations

<details>
 <summary><code>POST</code> <code><b>/inject</b></code> <code>(registers a Location in the database, related to a Device)</code></summary>

##### Parameters

> | name                  | type           | data type         | description                                                                                                 |
> |-----------------------|----------------|-------------------|-------------------------------------------------------------------------------------------------------------|
> | injectLocationCommand | required, body | object (JSON)     | Object containing all the necessary information about a Location to be stored (InjectLocationCommand.class) |


##### Responses

> | http code | content-type                      | response                                           |
> |-----------|-----------------------------------|----------------------------------------------------|
> | `200`     | `text/plain;charset=UTF-8`        | `Location injected successfully`                   |
> | `400`     | `application/json`                | `a field must not be null nor empty`               |
> | `404`     | `application/json`                | `the Device indicated does not exist in database ` |


</details>

### /transactions

<details>
 <summary><code>POST</code> <code><b>/inject</b></code> <code>(registers a Transaction in the database, related to a User, a Device and a Location)</code></summary>

##### Parameters

> | name                       | type           | data type         | description                                                                                                         |
> |----------------------------|----------------|-------------------|---------------------------------------------------------------------------------------------------------------------|
> | registerTransactionCommand | required, body | object (JSON)     | Object containing all the necessary information about a Transaction to be stored (RegisterTransactionCommand.class) |


##### Responses

> | http code | content-type       | response                                                                                                           |
> |-----------|--------------------|--------------------------------------------------------------------------------------------------------------------|
> | `200`     | `application/json` | `the Transaction successfully registered, including a field indicating if it has been declared as a fraud attempt` |
> | `400`     | `application/json` | `a field must not be null nor empty`                                                                               |
> | `404`     | `application/json` | `the Device, User or Location indicated does not exist in database `                                               |


</details>