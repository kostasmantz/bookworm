# BookWorm REST API

This is a Spring Boot application developed to expose a REST API
allowing creating, viewing, searching books.
After building and running the application with the instructions below,
a server will be deployed on port 8080 which will connect to the provided
database and have some test data.

## Requirements
The following are required to build and run the application.

- Java

## Tech
Technologies used to develop the application:

- Java 11
- Spring Boot
- PostgreSQL
- Project Lombok
- Maven

## Installation
Provide in `bookworm/src/main/resources/application.properties` 
the required properties to connect to your PostgreSQL database.

To build and run the server execute:
```shell
cd bookworm
./mvnw clean install
./mvnw spring-boot:run
```
After running the above commands, a server will be up-and-running
on port 8080 exposing the REST API.

## Sample API Documentation

### Create book
**URL** : `http://<base_url>:8080/api/books`

**Method** : `POST`

Request example:
```json
{
  "title": "Harry Potter and the Goblet of Fire",
  "status": "LIVE",
  "authorId": 1,
  "isbn": "9780439139595"
}
```

Response example:
```json
{
  "id": 8,
  "title": "Harry Potter and the Goblet of Fire",
  "description": null,
  "status": "LIVE",
  "createdAt": null,
  "isbn": "9780439139595",
  "authorId": 1,
  "publisherId": null
}
```

### Update book
**URL** : `http://<base_url>:8080/api/books/8`

**Method** : `PUT`

Request example:
```json
{
  "title": "Harry Potter and the Goblet of Fire",
  "description": "Another Harry Potter novelty.",
  "status": "LIVE",
  "createdAt": null,
  "isbn": "9780439139595",
  "authorId": 2,
  "publisherId": null
}
```

Response example:
```json
{
  "id": 8,
  "title": "Harry Potter and the Goblet of Fire",
  "description": "Another Harry Potter novelty.",
  "status": "LIVE",
  "createdAt": null,
  "isbn": "9780439139595",
  "authorId": 2,
  "publisherId": null
}
```
### Delete book
**URL** : `http://<base_url>:8080/api/books/8`

**Method** : `DELETE`

Response example:
200
```json
{ }
```

### Get Visible books
**URL** : `http://<base_url>:8080/api/books/visible`

**Method** : `GET`

Response example:
```json
[
  {
    "title": "Power of Habit",
    "description": "Learning the power of your own habits...",
    "isbn": "9780414520621",
    "authorName": "Maria Green"
  },
  {
    "title": "Agile Principles, Patterns, and Practices in C#",
    "description": "Software engineering patterns and best practices",
    "isbn": "9780457150021",
    "authorName": "Maria Green"
  }
]
```

### Get Book's details
**URL** : `http://<base_url>:8080/api/books/5/details`

**Method** : `GET`

Response example:
```json
{
  "title": "On Writing",
  "description": "The best story ever written...",
  "isbn": "9784737150021",
  "authorName": "Maria Green",
  "createdAt": "2018-05-11",
  "authorEmail": "mariag@yahoo.com",
  "authorDateOfBirth": "1963-11-28",
  "publisherName": null,
  "publisherAddress": null
}
```

### Create publisher
**URL** : `http://<base_url>:8080/api/publishers`

**Method** : `POST`

Request example:
```json
{
  "name": "Sherlock",
  "telephone": "655 543-6541",
  "address": "700 Oak Street, Brockton MA 2301"
}
```

Response example:
```json
{
  "id": 6,
  "name": "Sherlock",
  "telephone": "655 543-6541",
  "address": "700 Oak Street, Brockton MA 2301"
}
```

### Create author
**URL** : `http://<base_url>:8080/api/authors`

**Method** : `POST`

Request example:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@gmail.com"
}
```

Response example:
```json
{
  "id": 6,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@gmail.com",
  "dateOfBirth": null
}
```

### Get author's books
**URL** : `http://<base_url>:8080/api/authors/3/books`

**Method** : `GET`

Response example:
```json
[
    {
        "id": 1,
        "title": "Effective Java",
        "description": "Java best practices etc...",
        "status": "LIVE",
        "createdAt": "2001-06-16",
        "isbn": "9780137150021",
        "authorId": 3,
        "publisherId": null
    },
    {
        "id": 3,
        "title": "Power of Habit",
        "description": "Learning the power of your own habits...",
        "status": "LIVE",
        "createdAt": "2017-12-10",
        "isbn": "9780414520621",
        "authorId": 3,
        "publisherId": 1
    }
]
```