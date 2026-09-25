# EV Charging Station Management System

## Project Overview

The **EV Charging Station Management System** is a backend application designed to manage electric vehicle charging stations, users, charger availability, reservations, payments, and notifications.

The system is built using a **microservices architecture**, where each service handles a specific business responsibility and communicates with other services using **REST APIs and Apache Kafka**.

## Technologies Used

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* MySQL
* Apache Kafka
* Redis
* Docker
* Kubernetes
* AWS

## Microservices

### 1. User Service

Responsible for:

* User registration and management
* User authentication and authorization
* Role-based access control
* User and vehicle information
* Password encryption using Spring Security

### 2. Station Service

Responsible for:

* Charging station management
* Station location information
* Charger management
* Charger availability and status
* Charger type and power information

### 3. Booking Service

Responsible for:

* Charger reservations
* Booking management
* Booking status
* Reservation time management
* Publishing booking events through Apache Kafka

### 4. Payment Service

Responsible for:

* Payment processing
* Payment status management
* Transaction information
* Consuming booking events
* Publishing payment completion events through Apache Kafka

### 5. Notification Service

Responsible for:

* Consuming booking and payment events
* Fetching user information through REST APIs
* Sending booking and payment notifications
* Email notification handling

## Architecture

The application follows a **microservices architecture** where each service is independently responsible for a specific business domain.

```text
                         ┌──────────────────┐
                         │      Client      │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   User Service  │
                         └──────────────────┘

     ┌──────────────────┐      REST      ┌──────────────────┐
     │ Station Service  │ ◄────────────► │ Booking Service │
     └──────────────────┘                └────────┬─────────┘
                                                  │
                                             Kafka Event
                                                  │
                                                  ▼
                                         ┌──────────────────┐
                                         │ Payment Service  │
                                         └────────┬─────────┘
                                                  │
                                             Kafka Event
                                                  │
                                                  ▼
                                        ┌────────────────────┐
                                        │ Notification Service│
                                        └────────────────────┘

                         ┌──────────────────┐
                         │      Kafka       │
                         └──────────────────┘

                         ┌──────────────────┐
                         │      Redis       │
                         │      Cache       │
                         └──────────────────┘

                         ┌──────────────────┐
                         │      MySQL       │
                         │    Databases     │
                         └──────────────────┘
```

## Communication

The services communicate using two major mechanisms:

### REST API

Used for synchronous communication between services where immediate data is required.

Example:

```text
Notification Service
        │
        ▼
GET User Information
        │
        ▼
User Service
```

### Apache Kafka

Used for asynchronous event-driven communication.

Example:

```text
Booking Service
      │
      │ booking.details
      ▼
Payment Service
      │
      │ payment.details
      ▼
Notification Service
```

## Kafka Event Flow

The main event-driven flow is:

```text
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
```

Kafka consumers use consumer groups for independent message processing.

Error handling is implemented using retry mechanisms and Dead Letter Topics (DLT) where required.

## Redis

Redis is used as a caching layer to provide faster access to frequently requested data.

The project uses **Spring Cache** mechanisms such as:

* `@Cacheable`
* `@CachePut`
* `@CacheEvict`

This reduces unnecessary database calls for frequently accessed data.

## Database

MySQL is used for persistent data storage.

Each microservice follows a separate database/schema approach according to its business responsibility.

Example:

```text
User Service       → User Database
Station Service    → Station Database
Booking Service    → Booking Database
Payment Service    → Payment Database
```

## Security

Spring Security is used for:

* Authentication
* Authorization
* Password encryption
* Role-based access control
* Protected APIs

Different APIs are protected according to user roles and permissions.

## Docker

The application is being containerized using **Docker**.

Planned containerized components include:

* User Service
* Station Service
* Booking Service
* Payment Service
* Notification Service
* MySQL
* Kafka
* Redis

Docker Compose will be used to manage the local multi-container environment.

## Kubernetes

Kubernetes will be used for container orchestration.

Planned Kubernetes components include:

* Deployments
* Services
* ConfigMaps
* Secrets
* Service-to-service communication
* Scaling and container management

## AWS Deployment

The application is planned to be deployed on **Amazon Web Services (AWS)** after containerization and Kubernetes configuration.

The deployment will include:

* Application deployment
* Database configuration
* Kafka/Redis configuration
* Kubernetes-based service deployment
* Environment-specific configuration

## Project Status

🚧 **Currently under development**

### Completed

* User Service
* Station Service
* Booking Service Kafka integration
* Payment Service
* Notification Service
* REST API communication
* Kafka event-driven communication
* MySQL integration
* Redis integration

### In Progress

* Docker containerization
* Docker Compose setup
* Kubernetes deployment
* AWS deployment

## Future Improvements

* Load Balancing
* Rate Limiting
* Resilience and Retry mechanisms
* Distributed tracing
* Monitoring and observability
* Improved scalability

## Author
**Jayant Pradhan**
