# SuperLi – Supply & Inventory Integration Branch

**Branch:** `supplyinventory`
**Type:** Integration branch
**Subsystems:** Inventory Management + Supplier/Procurement

---

## Overview

This branch integrates the **Inventory Management** and **Supplier/Procurement** subsystems
into a unified system with full database persistence. It is the Phase 2 / production-ready
version of these two subsystems, adding a complete `DataAccessLayer` backed by SQLite.

This was the second integration milestone before all four subsystems were merged into `main`.

---

## Structure

```
dev/src/
├── DomainLayer/
│   ├── Inventory/           # Inventory domain
│   │   ├── Product.java          # In-store product with stock levels
│   │   ├── ProductFacade.java    # All inventory operations
│   │   ├── Sale.java             # Records of sold quantities
│   │   ├── Discount.java         # Product/category discounts
│   │   ├── InventoryReport.java / InventoryDesc.java
│   │   ├── DamageReport.java / DamageDesc.java
│   │   ├── ExpiryReport.java / ExpiryDesc.java
│   │   └── AbscenceReport.java / AbscenceDesc.java
│   └── Supplier/            # Procurement domain
│       ├── Supplier.java
│       ├── SupplierWithDeliveryDays.java
│       ├── SupplierNeedsPickup.java
│       ├── Agreement.java
│       ├── ContactPerson.java
│       ├── Order.java
│       ├── Product.java     # Product as seen by procurement (catalog)
│       ├── SystemController.java
│       └── Repositories/
├── DataAccessLayer/         # SQLite persistence (added in this branch)
│   ├── DatabaseConnection.java
│   ├── InventoryProductDAOImpl.java
│   ├── ProductDAOImpl.java
│   ├── SupplyDAOImpl.java
│   ├── SupplierDAOImpl.java
│   ├── AgreementDAOImpl.java
│   ├── OrderDAOImpl.java
│   ├── ContactPersonDAOImpl.java
│   ├── DTO/                 # Data Transfer Objects for DB ↔ Domain mapping
│   └── interfacesDAO/       # DAO interfaces
├── ServiceLayer/            # Business logic services
├── PresetationLayer/        # Console-based interactive UI
└── Main.java                # Entry point
```

---

## Key Features Added in This Branch (vs `inventory-phase1`)

- **Full SQLite persistence** – all products, supplies, suppliers, agreements, and orders survive restarts
- **DAO layer** – clean separation between domain and database via interfaces + implementations
- **Integrated procurement flow** – Inventory system can receive supplies triggered by Supplier orders
- **Startup data loading** – system initializes from database on launch
- **Tests** – `JUnit` unit tests for `ProductFacade` and domain logic

---

## Database

SQLite database file: `dev/src/supplyinventory.db` (and `release/supplyinventory.db`)

---

## Running

```bash
cd dev
javac -cp lib/* -d out src/**/*.java
java -cp out:lib/* Main
```

Or use the pre-built JAR: `out/artifacts/adss2025_jar/adss2025.jar`

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `inventory-phase1` | Inventory subsystem Phase 1 (in-memory, no DB) |
| `supplier-procurement-dev` | Supplier subsystem standalone development |
| `employeeTransport` | Employee + Transport integrated branch |
