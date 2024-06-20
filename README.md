# THWS Wuerzburg Exam Project: Study Trip Management Backend Application

The Partner University Management Application, developed as part of an exam at THWS Wuerzburg for the course
"Foundation of Distributed Systems",
provides a backend solution for
organizing and managing partner universities for study trips.
It offers functionalities to store and retrieve data about
universities and their modules.

## Table of Contents
- [Overview](#overview)
- [Project Structure](#project-structure)
- [How to Start](#how-to-start)
- [Usage](#usage)
- [Features](#features)
- [Tests](#tests)
***
## Overview
This application is built using Java and utilizes the Spring Framework. It stores data in-memory with the H2 database and uses Jackson for handling JSON serialization and deserialization.

***

## Project Structure

The application is organized as follows:

* **Source Code**: Located in **src/main/java/de/thws/fds**, with two main directories:
  - _**client**_: Contains the client-side code.

  - _**server**_: Contains the server-side code.


* **Tests**: All test cases to verify the functionality of the backend are in the test directory.

* **README.md**: This documentation file.

* **Dockerfile**: Configuration for building the Docker image of the application.


### &#x25B6; The Client

The Client consists of:

* 2 Client Classes for handling separately the requests of the two resources : 
 ModuleClient and PartnerUniversityClient


* 2 wrapper Classes in order to handle the getAllRequest which works with Collection-Model and Collectors: ModuleCollectionModel and PartnerUniversityCollectionModel


### &#x25B6; The Server

The server application is structured into distinct components for the two resources Partner University and Module:

#### Business Logic

The Business Logic layer is implemented in the following components:
- Controllers: Handle incoming HTTP Requests and route to appropriate service methods.


- Services: Contain the core business logic of the application.

#### Database Layer
- Model: Defines the structure and attributes of partner universities and modules.


- Repository: Interface with the underlying in-memory database.


- In-Memory Database: Stores data temporarily in memory during the application runtime. Database is initialized with few universities and modules.

### &#x25B6; Test 

The Test folder contains three classes

* **FdsApplicationTest**: Is where every Endpoint is testes


* PartnerUniversityControllerTest: Tests few Controller Methods and kept fpr further Debugging 


* PartnerUniversityTest: Tests Serialization and Deserialization and is kept for further Debugging

### &#x25B6; Docker
Go to Maven and follow this steps:
If you're using Inteliji you'll find Maven on the right sidebar marked with an "m"

1) ```start the FdsApplication.class``` (start the server)
2) **make sure the docker desktop app is open else it won't work**
3) ```clean```
4) ```install```
5) go to terminal and verify if images were created: ```docker images```
6) type in terminal ```docker build -t springapi . ``` in order to build an image
7) type in terminal: ```docker run -p 8000:8080 springapi``` (8000 is just an example)
8) The Application can now be accessed on port 8000

***

## How To Start

To start the server, follow these steps:

&rarr; The Server runs on ```localhost:8080```

1. Navigate to the ```FdsApplication.class``` located in ```src/main/java/de/thws/fds```
2. Run the application to start the server.

Once the application is running, you can manually test the endpoints using tools like Postman or by executing tests
located in the test folder.

To view the stored data in tables, after starting the server you can go in your browser and visit: 
http://localhost:8080/h2-console/login.jsp?jsessionid=678f0f42943faf1174c34808074fd816

username and password can be found in the ```application.properties``` file in ```resources``` folder


 

***

## Usage

Explore and interact with the following endpoints:
- **GET** ```/api/v1/universities```  &rarr;  Retrieve all partner universities.



- **GET** ```/api/v1/universities/{universityId}``` &rarr;
  Displays a single University


- **GET** ``` /api/v1/universities/{universityId}/modules```  &rarr;
  Retrieve modules associated with a specific university.


- **GET** ```/api/v1/universities/{universityId}/modules/{moduleId}```  &rarr;
  Display a single Module of a certain University


- **GET** ``` /api/v1/universities/{universityId}/modules/filter?{&name,semester,creditPoints}```  &rarr; Filters university according given querries


- **GET** ```/api/v1/universities/{universityId}/modules/filter?{&country,name,spring,autumn,contactPerson}```  &rarr; Filters Modules according given querries


- **POST** ```/api/v1/universities/create``` &rarr; Creates a new University


- **POST**  ```/api/v1/universities/{universitiyId}/modules/create``` &rarr; Creates a new Module for an existing University



- **PUT** ```/api/v1/universities/{universityId}/update```&rarr; Updates Information on an existing University


- **PUT** ```/api/v1/universities/{universityId}/modules/{moduleId}/update``` &rarr; Updates Information on an existing moduel of a University



- **DELETE** ```/api/v1/universities/{universityId}/delete``` &rarr; Deletes an existing University


- **DELETE** ```/api/v1/universities/{universityId}/modules/{moduleId}/delete``` &rarr; Deletes an existing Module of a University

***


## Features

- **Pagination**
The two ressources can be both paginated by using the querries parameters "page" and "pageSize"
For example: **GET** ```api/v1/universities?page=0&pageSize=2```


- **Filtering**: Both PartnerUniversity and Module can be filtered by various querries
For Example: **GET** ``` api/v1/universities/1/modules/filter?semester=1```


- **Sorting**:
Only the primary Ressource PartnerUniversity allows sorting after name in ascending or descending order
For Example: **GET** ```api/v1/universities?page=0&pageSize=2&sortDirection=desc```


***

## Tests

In order to execute **mvn verify** first you have to make sure that you **run the server firsthand**.

* **FdsApplicationTest**:
Here you'll find test cases that are testing every endpoint for PartnerUniversity and Modules packed altogether in one class.
In order to run the FdsApplicationTest make sure to run firstly the FdsApplication.


* **PartnerUniversityControllerTest**:
Here you'll find two written tests toGetUniById() and toAddUni().


* **PartnerUniversityTest**:
Here you'll find two written tests testSerialization() and testDeserialization().
