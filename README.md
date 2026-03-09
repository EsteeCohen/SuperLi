# SuperLi – Transport Subsystem

**Branch:** `transport-dev`
**Subsystem:** Transport Management
**Developers:** Student IDs 209094093, 314937012

---

## Overview

This branch contains the **Transport Management** subsystem of the SuperLi supply chain system.
It handles the full lifecycle of transporting goods between supplier sites and store branches,
including truck assignment, driver management, delivery scheduling, and incident tracking.

---

## Architecture

The subsystem follows a layered architecture:

```
dev/src/main/
├── TransportApp.java         # Entry point
├── controllers/
│   └── FacadeController.java # Single facade for all operations
├── dao/                      # Data Access Objects (SQLite via JDBC)
├── entities/                 # Domain entities
│   ├── Driver.java
│   ├── Truck.java
│   ├── Transport.java
│   ├── Site.java
│   ├── Item.java
│   ├── Order.java
│   ├── Incident.java
│   ├── IncidentResolution.java
│   ├── User.java
│   └── DatabaseConnection.java
├── repositories/             # Repository pattern wrappers
├── services/                 # Business logic layer
├── enums/                    # Shared enumerations
└── ui/                       # Console-based user interface
```

---

## Key Entities

| Entity | Description |
|---|---|
| `Truck` | Vehicle with license plate, weight capacity, cooling type |
| `Driver` | Driver with license type and current assignment |
| `Transport` | A delivery run: truck + driver + origin site + destination sites + items |
| `Site` | A physical location (supplier warehouse or store branch) |
| `Item` | A product item being transported, with weight |
| `Order` | A delivery order linking sites to items |
| `Incident` | A problem during transport (e.g. overweight) |
| `IncidentResolution` | Resolution applied to an incident |

---

## Running the Application

```bash
cd dev
javac -d bin src/main/**/*.java
java -cp bin main.TransportApp
```

Or run the pre-built JAR from `release/`.

---

## Database

Uses SQLite. The database file is located at `dev/src/main/entities/db.db` (created on first run).

---

## Integration Points

This subsystem integrates with:
- **Employee/HR** – queries the HR system to verify driver availability and license types before assigning them to transports
- The integration is handled through `TransportScheduleSL` in the HR branch

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `employee-hr-dev` | HR & shift scheduling subsystem |
| `supplier-procurement-dev` | Supplier & order management subsystem |
| `inventory-phase1` | Early inventory subsystem (no DB) |
| `supplyinventory` | Inventory + Supplier with full DB persistence |
| `employeeTransport` | Employee + Transport integrated branch |
