# SuperLi – Supply Chain Management System

> A full-stack Java supply chain management system built for a supermarket chain,
> covering transport logistics, inventory, HR scheduling, and supplier procurement.

---

## Project Overview

SuperLi is an enterprise-grade supply chain system developed as a university group project at Ben-Gurion University.
Four independent subsystems were built by separate teams and then integrated into a unified platform.

The system manages the complete flow from supplier → warehouse → store:
- Suppliers are managed with agreements and scheduled delivery orders
- Inventory tracks stock levels, raises alerts, and generates reports
- Transports are dispatched with trucks and licensed drivers
- Employees are scheduled into shifts with role-based availability

---

## Architecture

The project uses a **layered architecture** across all subsystems:

```
Presentation Layer  ──  Console UI menus
       │
Service Layer       ──  Business logic, validation, state management
       │
Domain Layer        ──  Core entities and business rules
       │
Data Access Layer   ──  DAO interfaces + SQLite implementations
       │
     SQLite DB
```

Each subsystem is independently runnable and follows consistent design patterns:
**Facade**, **Repository**, **DAO**, **DTO + Mapper**, **State Machine**, **Factory**.

---

## Subsystems

### Transport Management (`transport-dev` branch)
Manages the full lifecycle of goods delivery between supplier sites and store branches.

**Key entities:** `Truck`, `Driver`, `Transport`, `Site`, `Order`, `Item`, `Incident`

**Notable design decisions:**
- **License tier validation** – driver license type must be ≥ truck requirement (A ≥ B ≥ C)
- **Zone enforcement** – all destinations in a transport must be in the same shipping zone
- **State machine** – transports and orders go through strict `PLANNING → ACTIVE → COMPLETED` lifecycle
- **RBAC** – role-based permissions: ADMIN / TRANSPORT_MANAGER / DRIVER / VIEWER
- **Weight management** – live weight tracking with automatic `WEIGHT_OVERLOAD` incident creation

**Architecture:** Service → Repository → DAO (in-memory) + FacadeController

---

### Employee & HR Management (`employee-hr-dev` branch)
Manages employee rosters, shift scheduling, roles, and availability.

**Key entities:** `Employee`, `Shift`, `Role`, `Availability`, `WeeklyShiftRequirements`

**Notable design decisions:**
- Weekly shift requirements configurable per role (e.g. "2 cashiers per morning shift")
- Employees submit availability; system validates before assigning to shifts
- Exposes `TransportScheduleSL` interface so the transport system can validate driver availability

---

### Inventory Management (`inventory-phase1` → `supplyinventory` branches)
Tracks product stock in-store: quantities, discounts, and automated report generation.

**Key entities:** `Product`, `Supply`, `Sale`, `Discount`, `Reports (Inventory / Damage / Expiry / Absence)`

**Notable design decisions:**
- Each supply has its own cost-per-unit and expiry date (supports FIFO/batch tracking)
- Minimum quantity threshold auto-calculated from daily average sales
- Four report types: inventory snapshot, damage, expiry alerts, and stock absence

---

### Supplier & Procurement (`supplier-procurement-dev` branch)
Manages supplier relationships, supply agreements, and purchase orders.

**Key entities:** `Supplier`, `Agreement`, `Order`, `ContactPerson`

**Notable design decisions:**
- Two supplier types: `SupplierWithDeliveryDays` (periodic auto-orders) and `SupplierNeedsPickup` (manual)
- Agreements define per-product pricing with quantity-tier discounts
- DAO layer with SQLite persistence

---

## Integration Milestones

```
Phase 1 – Individual subsystem development
          employee-hr-dev  │  transport-dev  │  inventory-phase1  │  supplier-procurement-dev

Phase 2 – Pairwise integration
          employeeTransport (HR + Transport with shared DB)
          supplyinventory   (Inventory + Supplier with full DataAccessLayer)
```

### employeeTransport Branch – How integration works

The transport system imports Employee system's `Factory` and depends on two interfaces:

```java
DriverInfoInterface  driverService = employeeFactory.getDriverInfoService();
SiteInfoInterface    siteService   = employeeFactory.getSiteInfoService();
```

- **No data duplication** – Transport queries the Employee system live for driver data
- **Bidirectional** – Transport also *implements* `ITransportScheduleService` so HR can check delivery schedules when planning shifts
- **DTO + Mapper** – Database persistence added via DTOs and mapper classes
- **SQLite** – Full 10-table schema with foreign keys and transaction support

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Database | SQLite (via JDBC) |
| Testing | JUnit 5 |
| Build | Manual `javac` + JAR manifest |
| Version Control | Git |
| Architecture | Layered (Presentation / Service / Domain / DataAccess) |

---

## Repository Structure

| Branch | What it contains |
|---|---|
| `main` | Project overview (this README) + initial skeleton |
| `transport-dev` | Transport subsystem – full standalone implementation |
| `employee-hr-dev` | HR & shift scheduling subsystem |
| `inventory-phase1` | Inventory subsystem Phase 1 (in-memory) |
| `supplier-procurement-dev` | Supplier & procurement subsystem |
| `employeeTransport` | **Transport + HR integrated** with SQLite persistence |
| `supplyinventory` | **Inventory + Supplier integrated** with full DataAccessLayer |

---

## My Contribution

This repository is maintained by **Estee Cohen** (ID: 209094093). I built the **Transport Management** subsystem from scratch and led the integration with the Employee/HR system.

**What I designed and implemented:**

- The complete Transport subsystem (`transport-dev` branch) — 7 service classes, 9 entities, full DAO layer, 7 UI screens, 25+ JUnit tests
- The cross-system integration architecture (`employeeTransport` branch) — interface contracts between Transport and HR, bidirectional service injection, shared SQLite schema
- Key design decisions: zone-based routing, license tier hierarchy, automatic incident creation on weight overload, state-machine lifecycle for transports and orders

See the [`transport-dev`](../../tree/transport-dev) and [`employeeTransport`](../../tree/employeeTransport) branches for full details.

---

## Running the System

The most complete runnable versions are in the integration branches.

**employeeTransport branch:**
```bash
cd dev
java -jar adss2025_v02.jar
```

**supplyinventory branch:**
```bash
cd dev
java -jar out/artifacts/adss2025_jar/adss2025.jar
```

Pre-built JARs are included in each branch's `release/` or `out/` directory.
