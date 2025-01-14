# Car sharing

## Project Overview

You are watching **Online Car Sharing** project. This application is designed to simplify the process of renting cars online.
It addresses the challenges of finding and renting cars by offering a user-friendly interface for both customers and administrators.
Users can browse available cars, manage rentals, pay for rentals.

## Technologies Used

- **Spring Boot** - Backend framework for building the core application (_version: 3.4.0_)
- **Spring Security** - For managing authentication and authorization
- **Spring Data JPA** - For interacting with the database
- **MySQL** - Database to store books, categories and users information (_version: 8.0.33_)
- **Docker** - Containerization for easy deployment
- **Swagger** - API documentation and testing (_version: 2.1.0_)
- **MapStruct** - Object mapping (_version: 1.5.5_)
- **Liquibase** - Database change management (_version: 4.29.2_)
- **JUnit & Mockito** - For testing (_version: 1.20.1_)
- **Maven** - Builds and manages project dependencies 
- **Stripe API** - Manages payment processing for rentals. (_version: 28.2.0_)
- **Telegram API** - Sends notifications via a Telegram bot. (_version: 6.9.7.1_)

## Controllers functionalities

### For users

- **Register and Login**: New users can register, login, and start renting cars.
- **Browse Cars**: Users can view available cars and their details.
- **Rent a Car**: Users can rent cars, initiating rental transactions.
- **View Rentals**: Users can check active and past rentals.
- **Return Cars**: Users return rented cars, updating rental status.
- **Payments**: Secure payment processing via Stripe.

### For admins:
- **Manage Inventory**: Managers can add, update, or delete cars.
- **View Rentals and Payments**: Access to all rentals and payment history.
- **Telegram Notifications**: Managers receive updates about new rentals, overdue rentals, and successful payments.


## Requirements

- **Java** version 17 and higher
- **Maven** for dependency management
- **Docker** to prepare the environment

## How to set up

1. Clone repository
    ```bash
       git clone https://github.com/f3ops3/car-sharing-app.git
       cd book-store
   ```

2. Create .evn file for environment by filling the .env.template
    ```
    STRIPE_SECRET_KEY =

    TELEGRAM_BOT_TOKEN =
    TELEGRAM_BOT_NAME =

    JWT_SECRET=
    JWT_EXPIRATION=

    MYSQLDB_DATABASE=
    MYSQLDB_USER=
    MYSQLDB_PASSWORD=
    MYSQLDB_ROOT_PASSWORD=
     
    SPRING_DATASOURCE_PORT=
    MYSQLDB_PORT=
    DEBUG_PORT=

    MYSQLDB_LOCAL_PORT=
    MYSQLDB_DOCKER_PORT=
    SPRING_LOCAL_PORT=
    SPRING_DOCKER_PORT=
    
    DOMAIN=
   ```
3. Build the application
   ``` 
   mvn clean package
   ```
4. Build and then start the containers by using Docker Compose
    ```
   docker-compose build
   docker-compose up
   ```
5. The application will be accessible at `http://localhost:<YOUR_PORT>/api`.

## Testing

1. You can run tests to check if everything is working as it should be by executing following:
    ```bash
      mvn clean test
    ```
2. If there are any trouble of understanding
   the purpose of specific controller or
   endpoint you can check out swagger by visiting:
   http://localhost:8080/api/swagger-ui/index.html

## Challenges faced

1. For proper integration testing, several SQL scripts were created to emulate a realistic database state. If adding new tests, use these scripts to ensure tests remain independent and unaffected by execution order.
2. I used Liquibase in this project to simplify database changes and migrations. All changesets are reliable and easy to manage.

## Postman collection

I've provided Postman collection with all the API requests that can be used. It's located in `postman` folder.

### How to use it

1. **Locate the collection**: The Postman collection is located in the `postman` folder of the project.
    - File path: `./postman/Car sharing.postman_collection.json`
2. **Import the collection**
    - In the upper left corner of Postman use `File -> Import` or just press `ctrl + o`to import the collection

---
The car sharing project provided me with a wealth of experience and fundamentally changed my understanding of building applications. Through this project, I became familiar with a wide range of libraries and frameworks, gained a clear understanding of their purposes and applied them effectively.
Additionally, I integrated and utilized third-party APIs to enhance the application's functionality.

