# SuperLi — Supply Chain Management System

> A Java supply chain system for a supermarket chain, built by a team of 8 students at Ben-Gurion University.
> The project was developed in two phases: first as 4 independent subsystems, then integrated into 2 complete running systems.

---

## The Two Integrated Systems

This project delivered two fully working, database-backed applications:

### System 1 — Employee + Transport (`employeeTransport` branch)
**→ [View branch](../../tree/employeeTransport)**

Manages the operational side of goods delivery: who drives, what vehicle, which route, and whether it arrived.

| What it does | How |
|---|---|
| Truck & driver fleet management | License hierarchy (C1/C/CE), live availability tracking |
| Delivery scheduling | 4-step transport wizard with zone-based routing |
| Order & cargo management | Weight validation, pickup vs. delivery order types |
| Incident tracking | Auto-created on weight overload; resolution workflow |
| Employee scheduling | Shift management, role-based availability, weekly requirements |
| Cross-system driver validation | A driver only appears in transport if they're scheduled for that shift in HR |

**Built by:** Estee Cohen & Stav (Transport + Integration), + HR team

---

### System 2 — Inventory + Procurement (`supplyinventory` branch)
**→ [View branch](../../tree/supplyinventory)**

Manages the product side: what's in stock, where it came from, and when to reorder.

| What it does | How |
|---|---|
| Product & stock tracking | Per-supply cost/expiry, minimum quantity thresholds |
| Automated reports | Inventory snapshot, damage, expiry, and shortage reports |
| Supplier management | Two supplier types (delivery-day vs. pickup), contact persons |
| Procurement | Supply agreements with quantity-tier pricing, purchase order lifecycle |

---

## How the Systems Were Built

Each integrated system was developed in two phases:

```
Phase 1 — Individual subsystem development (standalone, testable in isolation)

  transport-dev          employee-hr-dev       inventory-phase1      supplier-procurement-dev
  ─────────────          ───────────────       ────────────────      ────────────────────────
  Trucks, drivers,       Employees, shifts,    Products, stock,      Suppliers, agreements,
  routes, incidents      roles, availability   reports, discounts    orders, contact persons


Phase 2 — Integration (two systems, each pair merged with shared SQLite database)

        employeeTransport                          supplyinventory
        ─────────────────                          ───────────────
        transport-dev + employee-hr-dev            inventory-phase1 + supplier-procurement-dev
        shared DB, interface-based coupling        full DataAccessLayer added
```

---

## Architecture

All subsystems follow the same **4-layer pattern**:

```
Presentation Layer   console menus and input handling
       │
Service Layer        business logic, validation, state management
       │
Domain Layer         entities and business rules
       │
Data Access Layer    DAO interfaces + SQLite via JDBC
```

Design patterns used: **Facade**, **Repository**, **DAO**, **DTO + Mapper**, **State Machine**, **Factory**, **Dependency Injection via Interfaces**.

---

## Integration Design (employeeTransport)

The two systems in `employeeTransport` communicate through **interface-based dependency injection** — no direct class dependencies between teams' code:

```java
// Transport pulls driver and site data from HR through interfaces
DriverInfoInterface driverService = employeeFactory.getDriverInfoService();
SiteInfoInterface   siteService   = employeeFactory.getSiteInfoService();

// HR queries Transport for delivery load when planning shifts
employeeFactory.setTransportScheduleService(transportApp.getTransportScheduleService());
```

A driver only appears as assignable in transport if they are: an active employee, scheduled in a shift at that time/location, holding the required license type, and marked available. Full details in the [`employeeTransport` README](../../tree/employeeTransport).

---

## My Contribution

Maintained by **Estee Cohen** (ID: 209094093). I built the **Transport Management** subsystem from scratch and led the integration with the Employee/HR system.

- **Transport subsystem** ([`transport-dev`](../../tree/transport-dev)) — 7 services, 9 entities, full DAO layer, 7 UI screens, 25+ JUnit tests
- **Integration architecture** ([`employeeTransport`](../../tree/employeeTransport)) — interface contracts, bidirectional service injection, shared SQLite schema

---

## Tech Stack

| | |
|---|---|
| Language | Java 17 |
| Database | SQLite via JDBC |
| Testing | JUnit 5 |
| Build | `javac` + JAR manifest |
| Architecture | Layered (Presentation / Service / Domain / DataAccess) |

---

## Running

**System 1 — Employee + Transport:**
```bash
# From the employeeTransport branch
java -jar adss2025_v02.jar
```

**System 2 — Inventory + Procurement:**
```bash
# From the supplyinventory branch
java -jar out/artifacts/adss2025_jar/adss2025.jar
```

Pre-built JARs are in each branch's root or `release/` folder.

---

## All Branches

| Branch | Type | Description |
|---|---|---|
| [`employeeTransport`](../../tree/employeeTransport) | **Full system** | Employee/HR + Transport, shared SQLite DB |
| [`supplyinventory`](../../tree/supplyinventory) | **Full system** | Inventory + Procurement, full DataAccessLayer |
| [`transport-dev`](../../tree/transport-dev) | Subsystem | Transport logistics standalone |
| [`employee-hr-dev`](../../tree/employee-hr-dev) | Subsystem | HR & shift scheduling standalone |
| [`inventory-phase1`](../../tree/inventory-phase1) | Subsystem | Inventory management (in-memory, no DB) |
| [`supplier-procurement-dev`](../../tree/supplier-procurement-dev) | Subsystem | Supplier & procurement standalone |
