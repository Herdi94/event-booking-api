# event-booking-api
# Event Booking Management API

## Project Description
The Event Booking Management API is a backend application designed to manage events, users, and ticket bookings. Users can register, log in, book tickets for events, and view profiles. Event managers can create, update, delete, and search events. The system exposes RESTful APIs for all operations.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3
- **ORM:** JPA/Hibernate
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Logging:** SLF4J + Logback
- **Libraries:** Lombok, Jakarta Persistence API
- **API Documentation:** Swagger/OpenAPI 

## Setup & Run Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Git

### Clone Repository
- git clone https://github.com/Herdi94/event-booking-api.git
- cd event-booking-api

### Build & Run
- mvn clean install
- mvn spring-boot:run

## API Endpoint Summary

### User Management
- POST	  /api/user/registration  Register a new user
- POST	  /api/user/login	      User login
- GET	      /api/user/profile	      Get logged-in user profile

### Event Management
- GET	  /api/event/{idEvent}	  Get event by ID
- PUT	  /api/event/{idEvent}	  Update event by ID
- DELETE  /api/event/{idEvent}    Delete event by ID
- POST	  /api/event/create	      Create a new event
- GET	  /api/event	          Get all events upcoming
- GET	  /api/event/search	      Search events

### Booking Management
- POST    /api/booking/create     Create a new booking
