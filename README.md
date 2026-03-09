# Employee + Transport Integration

**Branch:** `employeeTransport` | **Project:** [SuperLi](../../tree/main) — Supply Chain Management System
**Lead:** Estee Cohen (Transport + Integration)

This branch integrates the **Transport Management** and **Employee/HR** subsystems into a single runnable system with a shared SQLite database. It demonstrates cross-subsystem communication through a clean interface contract — each system remains independently testable while sharing data at runtime.

---

## What Was Integrated

| Subsystem | Standalone branch | Role in this branch |
|---|---|---|
| Transport | [`transport-dev`](../../tree/transport-dev) | Manages trucks, routes, orders, incidents |
| Employee / HR | [`employee-hr-dev`](../../tree/employee-hr-dev) | Manages staff, shifts, driver records |

**Core integration problem:** The transport system needs to assign a driver to a truck — but a driver is only valid if they are (1) an employee in the HR system, (2) scheduled in a shift at the correct time and location, and (3) hold a license compatible with the truck. These three conditions span two independent subsystems.

---

## Repository Layout

```
dev/
├── employeeDev/
│   └── src/
│       ├── domainLayer/        # Employee, Shift, Role, Availability, Driver (extends Employee)
│       ├── serviceLayer/
│       │   ├── Factory.java                      # Service container + integration wiring
│       │   ├── ShiftService.java                 # Implements DriverInfoInterface
│       │   ├── Interfaces/
│       │   │   ├── DriverInfoInterface.java       # ← Transport consumes this
│       │   │   ├── SiteInfoInterface.java         # ← Transport consumes this
│       │   │   └── ITransportScheduleService.java # ← Transport implements this for HR
│       │   ├── DriverSL.java                     # DTO exposed to transport system
│       │   └── ...
│       ├── dataAccessLayer/    # SQLite DAOs for employees, shifts, roles
│       └── presentationLayer/  # HR console UI
│
├── transportDev/
│   └── src/main/
│       ├── TransportApp.java   # Accepts Factory from HR, wires interfaces
│       ├── services/           # All transport services — depend on interfaces, not HR classes
│       ├── entities/           # Transport, Truck, Order, Site, Driver, Incident...
│       ├── dao/                # SQLite DAOs for transport tables
│       └── ui/                 # Transport console UI
│
├── main.java                   # Combined startup — boots HR factory, passes to Transport
├── db.db                       # Single shared SQLite database
└── lib/                        # Shared dependencies (sqlite-jdbc.jar)
```

---

## Integration Architecture

The systems communicate through **three interfaces**. The HR system defines them; the transport system depends on them. Neither system imports the other's implementation classes.

### 1. `DriverInfoInterface` — Transport queries HR for driver data

```java
public interface DriverInfoInterface {
    // Get drivers available at a specific time, location, and with a minimum license
    List<DriverSL> getAvailableDriversWithLicense(
        LocalDateTime time,
        Site site,
        LicenseType licenseType
    );

    DriverSL getDriverById(String id);
    List<DriverSL> getAllDrivers();
    List<DriverSL> getDriversByLicenseType(LicenseType licenseType);
    void setAvailableToDrive(String driverID, boolean isAvailable);
    void addDriver(String fullName, String password, String id, int wage,
                   String wageType, int yearlySickDays, int yearlyDaysOff,
                   String siteName, String phoneNumber, LicenseType licenseType);
}
```

**Implemented by:** `ShiftService` in the HR system

When `getAvailableDriversWithLicense(time, site, licenseType)` is called:
1. HR looks up the shift scheduled at that `(time, site)` pair
2. Retrieves all employees assigned to that shift with the `DRIVER` role
3. Filters to those with `driver.licenseType >= licenseType` and `driver.isAvailableToDrive == true`
4. Returns them as `DriverSL` DTOs — plain data objects with no HR internals exposed

A driver only appears in transport's assignment list if they are scheduled to work that shift. No more, no less.

---

### 2. `SiteInfoInterface` — Shared site registry

```java
public interface SiteInfoInterface {
    Site addSite(String name, String address, String contactPhone,
                 String contactName, String zone);
    boolean deleteSite(String siteName);
    Site getSiteByName(String siteName);
    List<Site> getAllSites();
    List<Site> getSitesByZone(String zone);
}
```

Sites are defined once and used by both systems. When HR assigns an employee to a site, and when Transport plans a route — both reference the same `Site` objects. The `Site` class is owned by the transport package and shared via this interface.

---

### 3. `ITransportScheduleService` — HR queries Transport for delivery load

```java
public interface ITransportScheduleService {
    boolean areThereArrivalsAtTheShift(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Site site
    );

    int getNumberOfDeliveriesToSiteInDate(LocalDate date, Site site);
}
```

**Implemented by:** `TransportScheduleService` in the transport system

HR uses this when planning shifts: it can check how many deliveries are scheduled to arrive at a site during a given time window, and use that to decide staffing levels. This makes the integration **bidirectional** — Transport depends on HR for drivers, HR depends on Transport for workload.

---

## Wiring It All Together

The `Factory` class in the HR system is the integration point. Transport receives a `Factory` instance and pulls its interfaces from it:

```java
// main.java — combined startup
Factory employeeFactory = new Factory();          // Boot HR system, load all data from DB

TransportApp transportApp = new TransportApp(employeeFactory);
// TransportApp extracts:
//   driverService = employeeFactory.getDriverInfoService()   → ShiftService
//   siteService   = employeeFactory.getSiteInfoService()     → SiteService

// Complete the bidirectional loop
employeeFactory.setTransportScheduleService(
    transportApp.getTransportScheduleService()    // TransportScheduleService
);

// Run both UIs (or switch between them from a top-level menu)
```

---

## Shared Database Schema

Both systems write to `dev/db.db`. Table ownership is split cleanly:

| Owner | Tables |
|---|---|
| HR system | `employees`, `roles`, `shifts`, `shift_assignments`, `availability`, `weekly_requirements` |
| Transport system | `transports`, `orders`, `trucks`, `drivers`, `sites`, `items`, `incidents`, `incident_resolutions`, `users` |

No cross-schema foreign keys. Each system reads only its own tables directly; shared data (sites, driver availability) flows through the interfaces.

---

## Design Decisions

**Why interfaces and not direct class references?**
The two subsystems were built by different teams. Using interfaces as the boundary means each team could develop and test independently. It also makes the coupling explicit — there's a documented API contract, not an ad-hoc dependency on internal classes.

**Why DTOs (`DriverSL`, `EmployeeSL`) at the boundary?**
The HR domain has complex entity graphs (Employee → Role → Shift → Site → ...). Exposing a lightweight `DriverSL` DTO at the interface boundary prevents transport from accidentally depending on HR's internal object model. The DTO contains exactly what transport needs: ID, name, license type, availability flag.

**`LicenseType` enum owned by Transport, imported by HR**
Both systems need to talk about license types. Rather than duplicating the enum, HR imports it from the transport package. This gives a single source of truth for the `C1 < C < CE` hierarchy used in license validation.

**Bidirectional but asymmetric**
Transport → HR is **required**: transport cannot function without driver data.
HR → Transport is **optional**: HR works without knowing about transports. The `setTransportScheduleService()` call is done after both systems are initialized, and HR degrades gracefully if it isn't called.

---

## Running

```bash
# From the root of this branch
cd dev
javac -cp lib/sqlite-jdbc.jar -d out \
  employeeDev/src/**/*.java \
  transportDev/src/**/*.java \
  main.java

java -cp out:lib/sqlite-jdbc.jar Main
```

Or use the pre-built JAR:
```bash
java -jar adss2025_v02.jar
```

The JAR includes the SQLite driver. A fresh `db.db` is created on first run.

---

## Related Branches

| Branch | Description |
|---|---|
| [`main`](../../tree/main) | Project overview |
| [`transport-dev`](../../tree/transport-dev) | Transport system standalone (with in-memory storage) |
| [`employee-hr-dev`](../../tree/employee-hr-dev) | Employee/HR system standalone |
| [`supplyinventory`](../../tree/supplyinventory) | Inventory + Supplier integration (other team) |
