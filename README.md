# CashMe Technical Interview Project
This project was developed for a technical interview at CashMe.

### Prerequisites
* Java 17
* Maven
* Docker
* PostgreSQL 15.7

### Setting Up Environment Variables
Before running the application, set the following environment variable:

```bash
API_INVERTEXTO_TOKEN=<your_api_token_here>
```
This token is required for authentication with an external API (api.invertexto.com).

### Running the Database
To start the PostgreSQL database, run the following command:

```bash
docker-compose up
```
This command initializes a Docker container with PostgreSQL version 15.7.

### Building and Running the Application
Ensure you have Java 17 and Maven installed. To build and run the application, execute:

```bash
mvn clean install
```

### Importing Postman Collection
A collection of API endpoints is available in *CashMe.postman_collection.json*. You can import this file into Postman to explore and test the endpoints.

### Additional Notes
This project uses Spring Boot 2.7.14.
Make sure all dependencies are resolved correctly before building the project.
Verify that the database is running and accessible before starting the application.

### Contribuitors
#### Augusto Kruger Ortolan
