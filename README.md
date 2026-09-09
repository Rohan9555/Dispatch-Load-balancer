# Dispatch Load Balancer

## Overview
This is a Spring Boot application that assigns delivery orders to vehicles based on priority, vehicle capacity, and travel distance. It uses the Haversine formula to calculate distance between vehicle locations and delivery locations.

## Approach
* Accept delivery orders with location, weight, and priority
* Accept vehicle details with capacity and current location
* Process high-priority orders first
* Assign orders only if vehicle capacity is available
* Select the nearest suitable vehicle using Haversine distance
* Generate a dispatch plan with assigned orders, total load, and total distance
* Return unassigned orders when they cannot be allocated

## APIs
* `POST /api/dispatch/orders` – Add delivery orders
* `POST /api/dispatch/vehicles` – Add vehicle details
* `GET /api/dispatch/plan` – Get the optimized dispatch plan

## Testing
Unit testing is implemented using JUnit and Mockito.

## Tech Stack
* **Language:** Java
* **Framework:** Spring Boot
* **Persistence:** Spring Data JPA, MySQL
* **Build Tool:** Maven
* **Testing:** JUnit, Mockito
