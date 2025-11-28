# scolarity-application

## Overview
This is a full-stack application designed to manage student and class information (scholastic/scolarity tracking). It consists of a Java-based backend serving a RESTful API and a modern web frontend built with React.

## Technology Stack

The project utilizes a two-part architecture:

### 1. Backend
The server-side component is a Java application built with Maven.

* **Language & Platform:** Java (JDK required, configuration files present for Maven Wrapper).
* **API Framework:** Appears to use a Java REST framework like Spring Boot/Jersey.
* **Security:** Implements JWT (JSON Web Token) and Spring Security configuration for authentication and authorization.
* **Data Models (Entities):** Includes models for `AppUser`, `Classe`, and `Etudiant` (Student).

### 2. Frontend
The client-side application is a Single Page Application (SPA).

* **Framework:** React.
* **State Management:** (Inferred, standard React).
* **Routing:** React Router DOM.
* **Styling:** Bootstrap CSS framework.
* **HTTP Client:** Axios for API communication.

## Setup and Installation

Follow these steps to get a copy of the project running on your local machine.

### Prerequisites

* **Java Development Kit (JDK):** Version 17+ recommended.
* **Apache Maven**
* **Node.js & npm (or yarn)**

### 1. Backend Setup

1.  Navigate to the backend directory:
    ```bash
    cd backend
    ```
2.  Build the project using Maven:
    ```bash
    ./mvnw clean install
    ```
3.  Run the application:
    ```bash
    java -jar target/backend-0.0.1-SNAPSHOT.jar
    ```
    *(Note: Startup configuration and active profiles may need adjustment in `application.properties` before running)*.

### 2. Frontend Setup

1.  Navigate to the project root directory.
2.  Install dependencies:
    ```bash
    npm install
    # or
    yarn install
    ```
3.  Start the development server:
    ```bash
    # Command inferred from typical React project structure
    npm start
    ```

## API Documentation
The API endpoints are documented using Swagger/OpenAPI. Please refer to the dedicated guide for details:

* [SWAGGER_GUIDE](SWAGGER_GUIDE.md)
