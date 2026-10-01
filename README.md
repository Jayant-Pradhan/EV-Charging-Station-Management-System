# EV Charging Station Management System

## Project Overview

The **EV Charging Station Management System** is a backend application designed to manage electric vehicle charging stations, users, charger availability, reservations, payments, and notifications.

The system is built using a **microservices architecture**, where each service handles a specific business responsibility and communicates with other services using **REST APIs and Apache Kafka**.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Apache Kafka
- Redis
- Spring Cloud Eureka
- Spring Cloud Gateway
- Docker

## Microservices

### 1. User Service

Responsible for:

- User registration and management
- User authentication and authorization
- Role-based access control
- User and vehicle information
- Password encryption using Spring Security

### 2. Station Service

Responsible for:

- Charging station management
- Station location information
- Charger management
- Charger availability and status
- Charger type and power information

### 3. Booking Service

Responsible for:

- Charger reservations
- Booking management
- Booking status
- Reservation time management
- Publishing booking events through Apache Kafka

### 4. Payment Service

Responsible for:

- Payment processing
- Payment status management
- Transaction information
- Consuming booking events
- Publishing payment completion events through Apache Kafka

### 5. Notification Service

Responsible for:

- Consuming booking and payment events
- Fetching user information through REST APIs
- Sending booking and payment notifications
- Email notification handling

## Service Discovery

**Eureka Server** is used for service discovery.

Each microservice registers itself with the Eureka Server, allowing services to discover and communicate with each other without depending on fixed service addresses.

    Eureka Server :8761
           │
    ┌──────┼───────────────┐
    │      │               │
    ▼      ▼               ▼
    User   Station         Booking
    Service Service        Service
                              │
                              ▼
                         Payment Service
                              │
                              ▼
                       Notification Service

## API Gateway

**Spring Cloud Gateway** is used as the single entry point for client requests.

The API Gateway routes incoming requests to the appropriate microservice using service discovery.

    Client
      │
      ▼
    API Gateway :8086
      │
      ├──► User Service
      ├──► Station Service
      ├──► Booking Service
      ├──► Payment Service
      └──► Notification Service

## Architecture

The application follows a **microservices architecture**, where each service is independently responsible for a specific business domain.

    Client
      │
      ▼
    API Gateway
      │
      ├──────────────► User Service
      │
      ├──────────────► Station Service
      │
      └──────────────► Booking Service
                              │
                              │ Kafka
                              ▼
                       Payment Service
                              │
                              │ Kafka
                              ▼
                     Notification Service

    ┌─────────────────┐
    │  Eureka Server  │
    │     :8761       │
    └─────────────────┘

    ┌─────────────────┐
    │      Kafka      │
    └─────────────────┘

    ┌─────────────────┐
    │      Redis      │
    │      Cache      │
    └─────────────────┘

    ┌─────────────────┐
    │      MySQL      │
    │    Databases    │
    └─────────────────┘

## Communication

The services communicate using two major mechanisms:

### REST API

REST APIs are used for synchronous communication where an immediate response is required.

Example:

    Notification Service
            │
            │ REST API
            ▼
       User Service
            │
            ▼
       User Information

### Apache Kafka

Apache Kafka is used for asynchronous event-driven communication.

Example:

    Booking Service
          │
          │ booking.details
          ▼
    Payment Service
          │
          │ payment.details
          ▼
    Notification Service

## Kafka Event Flow

The main event-driven flow is:

    Booking Created
          │
          ▼
    booking.details
          │
          ▼
    Payment Service
          │
          ▼
    Payment Completed
          │
          ▼
    payment.details
          │
          ▼
    Notification Service
          │
          ▼
    User Information
          │
          ▼
    Email Notification

Kafka consumers use **consumer groups** for message processing.

Error handling includes retry mechanisms and **Dead Letter Topics (DLT)** where required.

## Redis

Redis is used as a caching layer for frequently accessed data.

The project uses **Spring Cache** mechanisms such as:

- `@Cacheable`
- `@CachePut`
- `@CacheEvict`

Caching helps reduce unnecessary database calls for frequently requested data.

## Database

MySQL is used for persistent data storage.

Each microservice follows a separate database/schema approach according to its business responsibility.

    User Service       → User Database
    Station Service    → Station Database
    Booking Service    → Booking Database
    Payment Service    → Payment Database

## Security

Spring Security is used for:

- Authentication
- Authorization
- Password encryption
- Role-based access control
- Protected APIs

Different APIs are protected according to user roles and permissions.

## Docker

The microservices and supporting infrastructure are containerized using **Docker**.

Docker is used for running the application components in isolated containers.

The project includes containerized components such as:

- User Service
- Station Service
- Booking Service
- Payment Service
- Notification Service
- MySQL
- Kafka
- Redis

Docker configuration allows the services and infrastructure to run in a consistent local environment.

## Project Structure

    EV-Charging-Station-Management-System
    │
    ├── 01-user-service
    ├── 02-station-service
    ├── 03-booking-service
    ├── 04-payment-service
    ├── 05-notification-service
    ├── eureka-server
    └── 06-api-gateway

## Project Status

### Completed

- User Service
- Station Service
- Booking Service
- Payment Service
- Notification Service
- REST API communication
- Apache Kafka event-driven communication
- MySQL integration
- Redis caching
- Spring Security integration
- Eureka Service Discovery
- API Gateway
- Docker containerization
- Docker-based service configuration

## Author

**Jayant Pradhan**
