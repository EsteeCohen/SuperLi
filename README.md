# SuperLi – Employee + Transport Integration Branch

**Branch:** `employeeTransport`
**Type:** Integration branch
**Subsystems:** Employee/HR + Transport Management

---

## Overview

This branch integrates the **Employee/HR** and **Transport Management** subsystems into a
unified runnable system. It combines both subsystems' codebases, wires their service layers
together, and provides a single entry point for running the combined application.

This was the first integration milestone before all four subsystems were merged into `main`.

---

## Structure

```
dev/
├── employeeDev/         # Employee/HR subsystem source
│   └── src/             # domainLayer, serviceLayer, dataAccessLayer, presentationLayer
├── transportDev/        # Transport subsystem source
│   └── src/             # main/ (controllers, dao, entities, services, ui)
├── main.java            # Combined entry point
├── db.db                # Shared SQLite database
└── lib/                 # Shared dependencies (JDBC driver, etc.)
```

Both subsystems coexist and communicate:
- Transport checks **driver availability** via the HR service layer before assigning a driver
- The HR system exposes `TransportScheduleSL` interface to validate driver license types and shift availability

---

## Key Integration Points

| Integration | Description |
|---|---|
| Driver validation | Transport queries HR: "Is driver X available and licensed for truck type Y?" |
| Shift assignment | Drivers are treated as employees with `DRIVER` role in the HR system |
| Shared DB | Both subsystems read/write to the same SQLite database file |

---

## Running

```bash
cd dev
javac -cp lib/* -d out employeeDev/src/**/*.java transportDev/src/**/*.java main.java
java -cp out:lib/* Main
```

Or use the pre-built JAR: `adss2025_v02.jar`

---

## Related Branches

| Branch | Description |
|---|---|
| `main` | Fully integrated system (all 4 subsystems) |
| `employee-hr-dev` | Employee/HR subsystem standalone development |
| `transport-dev` | Transport subsystem standalone development |
| `supplyinventory` | Inventory + Supplier integrated branch |
