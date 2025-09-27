# Urban Drone Simulation System

## Overview

This Java project simulates urban drone operations with sophisticated allocation algorithms and delivery rules. The system manages a fleet of delivery drones, optimizes their assignments based on various criteria, and enforces operational rules to ensure safe and efficient deliveries.

**Developed for DTI Digital selection process.**

## Features

### Core Components

1. **Domain Models**
   - **Location**: Geographical coordinates with distance calculations
   - **Drone**: Delivery drone with capabilities, status, and battery management
   - **Delivery**: Delivery requests with priority levels and deadlines

2. **Allocation Algorithms**
   - **Nearest Neighbor Algorithm**: Assigns deliveries to the closest capable drone
   - **Priority-Based Algorithm**: Optimizes assignments based on urgency and drone efficiency

3. **Delivery Rules Service**
   - Operating hours validation (6:00 AM - 10:00 PM)
   - Weather safety checks
   - Battery level constraints
   - Flight time limitations
   - Maintenance scheduling

4. **Simulation Engine**
   - Orchestrates drone operations
   - Tracks statistics and metrics
   - Manages drone states and delivery lifecycle

## Project Structure

```
src/
├── main/java/com/dti/drone/
│   ├── algorithm/           # Allocation algorithms
│   ├── model/              # Domain models
│   ├── service/            # Business logic services
│   ├── simulation/         # Simulation engine
│   └── DroneSimulationApp.java  # Main application
└── test/java/com/dti/drone/
    ├── model/              # Model unit tests
    └── service/            # Service unit tests
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Building the Project

```bash
mvn clean compile
```

### Running Tests

```bash
mvn test
```

### Running the Simulation

```bash
mvn exec:java
```

## Simulation Output

The application demonstrates both allocation algorithms with sample data:

- **Fleet**: 4 drones with different capabilities
- **Deliveries**: 6 sample deliveries with various priorities and characteristics
- **Location**: São Paulo metropolitan area coordinates
- **Statistics**: Success rates, completion times, and operational metrics

### Sample Output

```
=== Urban Drone Simulation System ===
--- Nearest Neighbor Algorithm ---
Initial state:
Drones status:
  Drone{id='DRONE-001', status=AVAILABLE, battery=100.0%, location=...}
  ...
Pending deliveries: 6
Statistics: Statistics{requests=6, assigned=0, completed=0, ...}

--- Simulation Step 1 ---
...
Final Statistics: Statistics{requests=6, assigned=4, completed=4, pending=2, success_rate=66.7%}
```

## Algorithm Comparison

### Nearest Neighbor Algorithm
- **Strategy**: Assigns to closest available drone
- **Advantage**: Minimizes travel distance
- **Best for**: Dense urban areas with many nearby drones

### Priority-Based Algorithm
- **Strategy**: Considers urgency, efficiency, and drone capabilities
- **Advantage**: Optimizes for delivery deadlines and resource utilization
- **Best for**: Mixed priority deliveries with time constraints

## Architecture Highlights

### Design Patterns Used
- **Strategy Pattern**: Pluggable allocation algorithms
- **Observer Pattern**: Statistics tracking and updates
- **Factory Pattern**: Drone and delivery creation
- **Service Layer**: Business logic separation

### Key Features
- **Geographic Calculations**: Haversine formula for accurate distances
- **Battery Management**: Realistic power consumption modeling
- **Priority System**: Four-level priority classification
- **Operational Constraints**: Real-world delivery rules and limitations

## Testing

The project includes comprehensive unit tests covering:

- **Location calculations**: Distance computation and equality checks
- **Drone capabilities**: Weight, range, and battery constraints
- **Delivery rules**: Safety and operational validations
- **Service logic**: Business rule enforcement

Run with: `mvn test`

## Configuration

### Delivery Operating Hours
- **Start**: 6:00 AM
- **End**: 10:00 PM

### Safety Constraints
- **Minimum Battery**: 20% reserve
- **Maximum Flight Time**: 4 hours
- **Maintenance Interval**: 30 days
- **Weather Restrictions**: No delivery during storms, heavy rain, severe wind, or fog

### Priority Levels
- **URGENT**: Must be delivered within 2 hours
- **HIGH**: Must be delivered within 6 hours  
- **NORMAL**: Standard delivery timing
- **LOW**: Extended delivery window

## Future Enhancements

- Real-time weather integration
- Route optimization algorithms
- Multi-depot support
- Dynamic pricing models
- Machine learning for demand prediction
- REST API for external integrations

## Technologies Used

- **Java 11**: Core programming language
- **Maven**: Build and dependency management
- **JUnit 5**: Unit testing framework

## License

This project is developed as part of the DTI Digital selection process.
