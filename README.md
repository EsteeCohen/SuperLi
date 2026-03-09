# SuperLi – Supplier & Procurement Subsystem

**Branch:** `supplier-procurement-dev`
**Subsystem:** Supplier Management & Order Procurement
**Developers:** Student IDs 322931999, 322655507

---

## Overview

This branch contains the **Supplier & Procurement Management** subsystem of SuperLi.
It manages all supplier relationships, supply agreements, and purchase orders —
enabling the supermarket to track who supplies which products, under what terms,
and to place and track orders accordingly.

---

## Architecture

The subsystem follows a 4-layer architecture:

```
dev/src/
├── DomainLayer/                    # Core business logic
│   ├── Supplier.java               # Base supplier entity
│   ├── SupplierWithDeliveryDays.java  # Supplier that delivers on fixed days
│   ├── SupplierNeedsPickup.java    # Supplier that requires store pickup
│   ├── Agreement.java              # Supply agreement (supplier ↔ product)
│   ├── ContactPerson.java          # Supplier contact info
│   ├── Order.java                  # Purchase order
│   ├── Product.java                # Product as seen by procurement
│   ├── SystemController.java       # Top-level domain controller
│   ├── Repositories/               # In-memory repository interfaces
│   └── Enums/                      # Category, order status, etc.
├── ServiceLayer/
│   └── SupplierSystemService.java  # Service facade for all operations
├── PresentationLayer/
│   ├── SupplierSystemUI.java       # Console-based interactive UI
│   ├── SystemInitializer.java      # Seeds initial data on startup
│   └── UserRole.java               # Role-based UI access control
└── dataAcssesLayer/                # SQLite DAO implementations
    ├── SupplierDAOImpl.java
    ├── AgreementDAOImpl.java
    ├── OrderDAOImpl.java
    ├── ContactPersonDAOImpl.java
    └── interfacesDAO/
```

---

## Key Features

- **Supplier management** – add/update/remove suppliers with contact persons and delivery terms
- **Two supplier types:**
  - `SupplierWithDeliveryDays` – delivers on fixed weekly days; system auto-creates periodic orders
  - `SupplierNeedsPickup` – store must collect; system generates pickup requests
- **Agreement management** – define per-product pricing, discounts by quantity tiers
- **Order management** – create, update, approve and track purchase orders
- **Procurement reports** – view open orders, order history, supplier catalog

---

## Running

```bash
cd dev
javac -d out src/**/*.java
java -cp out PresentationLayer.Main
```

Or run the pre-built JAR from `release/`.

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `supplyinventory` | Supplier + Inventory integrated with full DB |
| `inventory-phase1` | Inventory subsystem Phase 1 (no DB) |
| `transport-dev` | Transport management subsystem |
| `employee-hr-dev` | HR & shift scheduling subsystem |
