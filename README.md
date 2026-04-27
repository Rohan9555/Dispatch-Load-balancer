# Dispatch Load Balancer

## Overview

This is a Spring Boot application that assigns delivery orders to vehicles based on priority, capacity, and minimum distance.

## Approach

A greedy algorithm is used:

* Orders are sorted by priority (HIGH → LOW)
* Each order is assigned to the nearest vehicle with available capacity

## APIs

* POST `/api/dispatch/orders` – Add delivery orders
* POST `/api/dispatch/vehicles` – Register vehicles
* GET `/api/dispatch/plan` – Generate dispatch plan

## Edge Cases

* No orders available
* No vehicles available
* Orders exceeding vehicle capacity

## Testing

Unit testing is implemented using JUnit and Mockito.
