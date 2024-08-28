# fraud-detection

This is an application intended to persist data about users and transactions in order to detect fraud in the context of e-commerce, providing a REST API to inject the data.

It uses Neo4j to deploy a graph database. This way of connecting entities is more natural and efficient for the purposes of the application.



## Endpoints

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
