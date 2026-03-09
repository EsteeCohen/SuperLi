# SuperLi – Supply Chain Management System

**Branch:** `main`
**Type:** Base / initial skeleton branch

---

## Project Overview

**SuperLi** is an academic supply chain management system built for a supermarket chain.
It was developed as a university project (Ben-Gurion University) by a team of students,
with each pair of students responsible for one subsystem.

The system covers the full supply chain: from supplier agreements and procurement,
through inventory management, to delivery logistics and employee scheduling.

---

## Subsystems

| Subsystem | Branch | Description |
|---|---|---|
| **Transport** | `transport-dev` | Trucks, drivers, transport scheduling, site management, incident tracking |
| **Inventory (Phase 1)** | `inventory-phase1` | Product stock, reports (expiry/damage/absence), discounts — in-memory only |
| **Employee / HR** | `employee-hr-dev` | Employees, roles, shift scheduling, availability management |
| **Supplier / Procurement** | `supplier-procurement-dev` | Suppliers, agreements, purchase orders, delivery terms |

## Integration Branches

| Branch | Description |
|---|---|
| `employeeTransport` | Employee + Transport subsystems integrated with shared SQLite DB |
| `supplyinventory` | Inventory + Supplier subsystems integrated with full DataAccessLayer |

---

## Branch History

```
main (this branch)
 └── initial skeleton (Hello World JAR, team IDs, docs)

Individual subsystem development:
 ├── employee-hr-dev        (HR & shift scheduling)
 ├── transport-dev          (transport logistics)
 ├── inventory-phase1       (inventory without DB)
 └── supplier-procurement-dev (supplier management)

Integration:
 ├── employeeTransport      (Employee + Transport merged)
 └── supplyinventory        (Inventory + Supplier merged, full DB)
```

---

## About This Branch

The `main` branch holds the **initial project skeleton** — a basic Hello World JAR and
team ID documentation required for the first submission milestone. All real development
happened in the subsystem-specific branches listed above.

---

## Tech Stack

- **Language:** Java
- **Database:** SQLite (via JDBC)
- **Testing:** JUnit
- **Build:** Manual javac + JAR manifest
- **Architecture:** Layered (Presentation → Service → Domain → DataAccess)

---

## Running the Full System

For the most complete integrated version, see the `employeeTransport` or `supplyinventory`
branches, each of which contains a runnable JAR in their `release/` or `out/` directory.
