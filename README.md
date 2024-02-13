**GitHub Service API**

**Description**

The GitHub Service API is a Spring Boot application that allows users to retrieve information about non-forked repositories from GitHub for a given user. The application uses the GitHub REST API to fetch data.

**Tech Stack**

- Language: Java 21

- Frameworks: Spring Boot 3, Hibernate, Junit

- Libraries: Lombok

- Build Tool: Maven

- UI: REST API

**Features**

- Retrieving a list of non-forked repositories for a specified GitHub user.

- Returning detailed information about repositories, including the name, owner (login), and a list of branches along with the latest commit SHA.

**Requirements**

- Java 11 or newer

- Maven 3.6 or newer

- Internet access for connections to the GitHub API

**Error Handling**

In case a user is not found, the application will return a 404 error with an appropriate message.

**Launching**

After successful installation, run the application using:

`java -jar target/github-service-api-0.0.1-SNAPSHOT.jar`

**Usage**

_Retrieving Data from Repositories and their Components_

To retrieve data for a GitHub user, make a GET request to the endpoint:

`GET /api/users/{username}/repos`

Replace {username} with the GitHub username.