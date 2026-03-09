# Transport Management Subsystem

**Branch:** `transport-dev` | **Project:** [SuperLi](../../tree/main) — Supply Chain Management System
**Developers:** Estee Cohen, Stav (IDs: 209094093, 314937012)

A complete transport logistics system built in Java, managing the full lifecycle of goods delivery between supplier warehouses and store branches — from fleet and driver management through route planning, weight validation, and incident resolution.

---

## Features

### Fleet Management
- **Trucks** — register trucks with model, registration number, empty weight, max weight capacity, and license requirement (C1/C/CE)
- **Drivers** — manage drivers with license type, availability status, and assignment tracking
- **License compatibility** — enforces a license hierarchy: a driver must hold a license ≥ the truck's requirement (`C1 < C < CE`)
- Availability is automatically updated when a driver is assigned to or released from a transport

### Order Management
- Create orders with a destination site, list of items (name, quantity, unit weight), and order date
- Two order types: **DELIVERY** (requires truck dispatch) and **PICKUP** (warehouse collection, no transport needed)
- Total order weight calculated automatically from items
- Full lifecycle: `CREATED → IN_PROGRESS → DONE / CANCELLED`
- Remove individual items from orders; weight recalculated in real time
- Assign orders to transports with weight-capacity enforcement

### Transport Scheduling
Transport creation follows a **4-step wizard**:
1. **Basic details** — date, time, source site
2. **Route planning** — add destination sites (must all be in the same shipping zone as the source)
3. **Vehicle assignment** — select a truck with sufficient remaining capacity
4. **Driver assignment** — select an available driver with a compatible license; confirm and create

Additional operations:
- Change truck or driver (only while status is `PLANNING`)
- Add or remove destinations (zone constraint enforced throughout)
- Track current cargo weight vs. truck capacity in real time
- Status progression: `PLANNING → IN_PROGRESS → COMPLETED / CANCELLED`

### Zone-Based Routing
Sites belong to shipping zones (`NORTH / SOUTH / EAST / WEST`). All stops on a single transport must be within the same zone. This keeps routes geographically coherent and is enforced at every step.

### Weight Validation & Incident Management
- When an order is assigned to a transport, total weight (truck empty weight + all cargo) is validated against the truck's max capacity
- If the limit would be exceeded, a **WEIGHT_OVERLOAD** incident is automatically created — no manual entry needed
- Additional incident types: `TRUCK_BREAKDOWN`, `DRIVER_UNAVAILABLE`, `OTHER`
- Incident workflow: `REPORTED → IN_PROGRESS → RESOLVED / CANCELLED`
- Each incident can have a resolution record attached (description + timestamp)

### User Management & Access Control
- Login/logout with session management
- Four roles: **ADMIN**, **MANAGER**, **DRIVER**, **WAREHOUSE_STAFF**
- Role-based permission checking applied to all operations
- Create, deactivate, and update users; change password

---

## Architecture

```
dev/src/main/
│
├── TransportApp.java                  # Entry point, wires all layers together
│
├── ui/                                # Presentation layer — console menus
│   ├── MainUI.java
│   ├── TransportUI.java
│   ├── OrderUI.java
│   ├── FleetUI.java
│   ├── SiteUI.java
│   ├── UserManagementUI.java
│   └── ReportsUI.java
│
├── controllers/
│   └── FacadeController.java          # Single entry point from UI to services
│
├── services/                          # Business logic layer
│   ├── TransportService.java
│   ├── OrderService.java
│   ├── DriverService.java
│   ├── TruckService.java
│   ├── SiteService.java
│   ├── UserService.java
│   └── IncidentService.java
│
├── entities/                          # Domain entities
│   ├── Transport.java
│   ├── Order.java
│   ├── Driver.java
│   ├── Truck.java
│   ├── Site.java
│   ├── Item.java
│   ├── Incident.java
│   ├── IncidentResolution.java
│   ├── User.java
│   └── DatabaseConnection.java
│
├── repositories/                      # Repository pattern (in-memory collections)
│
├── dao/                               # Data Access Objects (SQLite via JDBC)
│
└── enums/
    ├── LicenseType.java               # C1, C, CE — with comparison logic
    ├── TransportStatus.java
    ├── OrderStatus.java
    ├── OrderType.java
    ├── IncidentType.java
    ├── IncidentStatus.java
    ├── ShippingZone.java
    └── UserRole.java
```

---

## Key Design Decisions

**State machines for lifecycle objects**
Both `Transport` and `Order` implement a strict status state machine. A transport in `IN_PROGRESS` cannot have its driver changed; a cancelled order cannot be reopened. This is enforced in the service layer, not the UI.

**Automatic incident creation**
When `OrderService.assignOrderToTransport()` detects a weight overload, it calls `IncidentService.createIncident(WEIGHT_OVERLOAD, ...)` automatically. The system never silently ignores constraint violations — every failure creates an auditable record.

**Zone constraint on routes**
`TransportService` validates the shipping zone of every site before adding it to a transport. The zone is locked to the source site's zone when the transport is created. Attempting to add a site from another zone throws a validation error.

**Facade controller**
All UI classes interact exclusively through `FacadeController`, which delegates to the appropriate service. The UI has no direct dependency on any service or entity — it only sees method signatures through the facade.

**License hierarchy**
`LicenseType` is an enum with an ordinal-based comparison: `C1(0) < C(1) < CE(2)`. Driver assignment validates `driver.licenseType.ordinal() >= truck.requiredLicense.ordinal()`.

---

## Console Menu Map

```
LOGIN
└── MAIN MENU
    ├── 1. Transport Management
    │   ├── Create new transport (4-step wizard)
    │   ├── View transports (all / by date / by status / by zone)
    │   ├── Update transport status
    │   ├── Manage cargo (add/remove orders)
    │   └── Change driver or truck
    ├── 2. Items & Shipments (Orders)
    │   ├── Create new order
    │   ├── View order by ID
    │   ├── View all orders (filter by date / status / type)
    │   ├── Cancel order
    │   ├── Update order status
    │   └── Assign order to transport
    ├── 3. Fleet Management
    │   ├── Trucks — add, view all, query by available capacity
    │   └── Drivers — add, view all, query by license type
    ├── 4. Site Management
    │   ├── Add / delete sites
    │   └── View by ID / zone / all
    ├── 5. Reports & Statistics
    └── 6. User Settings
        ├── Manage users
        ├── Change password
        └── View profile
```

---

## Tests

Located in `dev/src/test/` — 25+ JUnit 5 tests covering:

| Test File | What It Covers |
|---|---|
| `TransportTests.java` | Transport creation, status transitions, zone enforcement, destination management |
| `OrderTests.java` | Order creation, item management, weight calculation, overload detection |
| `TruckTests.java` | Capacity validation, weight arithmetic, registration uniqueness |
| `DriverTests.java` | License filtering, availability toggling, CRUD operations |
| `SiteTests.java` | Site CRUD, zone queries, deletion constraints |

Tests use in-memory service instances (no database dependency).

---

## Documentation

The `docs/` folder contains:

| File | Contents |
|---|---|
| `transport-manual-pdf.pdf` | Full user manual — all screens, workflows, and field descriptions |
| `transit.drawio.pdf` | System architecture and data-flow diagram |
| `דרישות ושאלות לקוח.pdf` | Original client requirements and Q&A (Hebrew) |

A step-by-step testing guide with exact menu navigation and test data is in [`dev/TESTING_GUIDE.md`](dev/TESTING_GUIDE.md).

---

## Running

```bash
# Build
cd dev
javac -cp lib/sqlite-jdbc.jar -d out src/main/**/*.java src/test/**/*.java

# Run
java -cp out:lib/sqlite-jdbc.jar main.TransportApp

# Or use the pre-built JAR
java -jar release/transport.jar
```

When prompted at startup, enter `Y` to load demo data (pre-seeded trucks, drivers, sites, and sample transports).

---

## Integration

This standalone branch uses in-memory storage for drivers and sites. The integrated version — where the transport system queries the Employee/HR system for live driver data — is in the [`employeeTransport`](../../tree/employeeTransport) branch.

**Integration interfaces (defined in HR system, consumed here):**
- `DriverInfoInterface` — query available drivers by license type, time, and location
- `SiteInfoInterface` — shared site data across both subsystems
- `ITransportScheduleService` — transport implements this so HR can check delivery load when scheduling shifts

---

## Related Branches

| Branch | Description |
|---|---|
| [`main`](../../tree/main) | Project overview |
| [`employeeTransport`](../../tree/employeeTransport) | This system integrated with Employee/HR |
| [`employee-hr-dev`](../../tree/employee-hr-dev) | Employee/HR subsystem |
| [`supplyinventory`](../../tree/supplyinventory) | Inventory + Supplier integration |
