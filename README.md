# SuperLi – Inventory Subsystem (Phase 1)

**Branch:** `inventory-phase1`
**Subsystem:** Inventory Management – Phase 1 (no database persistence)
**Developers:** Student IDs 215452079, 324194984

---

## Overview

This branch contains the **Phase 1** implementation of the **Inventory Management** subsystem
for the SuperLi supply chain system. It covers product stock tracking, discount management,
and report generation (expiry, damage, absence, inventory).

> **Note:** This is the Phase 1 version — data is held in memory only (no database/persistence).
> The later `supplyinventory` branch adds a full DataAccessLayer with SQLite persistence.

---

## Architecture

The subsystem follows a 3-layer architecture:

```
dev/
├── DomainLayer/              # Business logic and domain models
│   ├── Product.java          # Core product entity
│   ├── ProductFacade.java    # Facade for all product operations
│   ├── Sale.java             # Sale record (quantity sold + cost)
│   ├── Discount.java         # Discount rules
│   ├── InventoryReport.java / InventoryDesc.java
│   ├── DamageReport.java / DamageDesc.java
│   ├── ExpiryReport.java / ExpiryDesc.java
│   └── AbscenceReport.java / AbscenceDesc.java
├── ServiceLayer/             # Service layer (API for presentation)
│   ├── ProductService.java
│   ├── ReportService.java
│   └── ServiceFactory.java   # Single point of access to services
└── PresetationLayer/         # Console-based interactive UI
```

---

## Key Features

- **Product management** – add products with cost, price, quantities, location, and supply info
- **Supply tracking** – each supply has its own cost per unit and expiry date
- **Discount management** – set discounts on products or categories by percentage
- **Sales recording** – record sold/broken quantities, affecting stock levels
- **Minimum quantity calculation** – auto-calculates reorder threshold based on average daily sales
- **Reports:**
  - *Inventory Report* – full snapshot of current stock levels
  - *Damage Report* – items marked as broken/damaged
  - *Expiry Report* – items past their expiry date
  - *Absence Report* – items that fell below minimum quantity threshold

---

## Running the Application

```bash
cd dev
javac -d out DomainLayer/*.java ServiceLayer/*.java PresetationLayer/*.java
java -cp out PresetationLayer.Main
```

Or run the pre-built JAR from `dev/`.

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `supplyinventory` | Phase 2: Inventory + Supplier with full SQLite DB persistence |
| `supplier-procurement-dev` | Supplier & order management subsystem |
| `transport-dev` | Transport management subsystem |
| `employee-hr-dev` | HR & shift scheduling subsystem |
