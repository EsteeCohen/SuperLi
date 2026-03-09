# SuperLi – Employee & HR Subsystem

**Branch:** `employee-hr-dev`
**Subsystem:** Human Resources – Employee, Shift & Role Management
**Developers:** Student IDs 322869645, 323084970

---

## Overview

This branch contains the **Employee & HR Management** subsystem of the SuperLi supply chain system.
It manages employees, their roles, shift scheduling, and availability — ensuring the right
workers (including drivers) are assigned to shifts across store branches.

---

## Architecture

The subsystem follows a 4-layer architecture:

```
dev/src/
├── domainLayer/              # Core business logic
│   ├── EmployeeDL.java       # Employee domain entity
│   ├── EmployeeFacade.java   # Facade for employee operations
│   ├── ShiftDL.java          # Shift domain entity
│   ├── ShiftFacade.java      # Facade for shift operations
│   ├── RoleDL.java           # Role domain entity
│   ├── RoleFacade.java       # Facade for role operations
│   ├── AvailabilityDL.java   # Employee availability record
│   ├── AvailabilityFacade.java
│   ├── WeeklyShiftRequirements.java  # Required staffing per shift
│   └── Enums/                # Role types, shift types, etc.
├── serviceLayer/             # Business logic orchestration
│   ├── EmployeeSL.java / EmployeeService.java
│   ├── ShiftSL.java / ShiftService.java
│   ├── RoleSL.java / RoleService.java
│   ├── WageTypeSL.java
│   ├── AssigningService.java # Core: assigns workers to shifts
│   ├── TransportScheduleSL.java  # Integration interface with Transport
│   ├── Factory.java          # Service factory / DI container
│   └── Interfaces/           # Cross-module service contracts
├── dataAcssesLayer/          # Data persistence (SQLite)
├── presentationLayer/        # Console-based UI
└── unitTests/                # JUnit tests
```

---

## Key Features

- **Employee management** – add/remove employees, track their roles and wage types
- **Role management** – define required roles (cashier, shift manager, driver, etc.)
- **Shift scheduling** – create shifts for morning/evening, assign available employees
- **Availability tracking** – employees submit availability; system checks before assigning
- **Weekly shift requirements** – configure how many of each role are needed per shift
- **Driver interface** – exposes `TransportScheduleSL` so the Transport subsystem can check driver availability and license validity before scheduling a transport

---

## Integration with Transport

The `TransportScheduleSL` service interface is the bridge to the Transport subsystem.
Transport queries HR to validate that a driver:
1. Is an active employee
2. Has the correct license type for the assigned truck
3. Is marked available for the shift date/time

---

## Running

```bash
cd dev
javac -d out src/**/*.java
java -cp out presentationLayer.Main
```

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `transport-dev` | Transport management subsystem |
| `employeeTransport` | Employee + Transport integrated branch |
| `supplier-procurement-dev` | Supplier & order management subsystem |
| `supplyinventory` | Inventory + Supplier with full DB persistence |
