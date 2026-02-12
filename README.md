# Inventory Reconciliation Tool

Internal Java web application for detecting and managing inventory discrepancies.

---

## Stack

- Java (Servlet)
- JDBC (no ORM)
- MySQL
- Apache Tomcat
- Maven (WAR packaging)

---

## What It Does

1. Reads expected inventory
2. Reads actual inventory snapshot
3. Detects mismatches
4. Creates discrepancies
5. Enforces lifecycle:

```

OPEN → IN_REVIEW → RESOLVED → CLOSED

````

6. Logs every state change
7. Maintains full audit trail

---

## Database Tables

- expected_inventory
- actual_inventory_snapshot
- inventory_discrepancy
- discrepancy_state_history
- audit_log

---

## Run Locally

### 1. Configure Database
Create database:

```sql
CREATE DATABASE inventory_db;
````

Run schema to create tables.

Update DB credentials in:

```
DbConnectionFactory.java
```

---

### 2. Build WAR

```bash
mvn clean package
```

---

### 3. Deploy

Copy WAR to Tomcat `webapps/` and start Tomcat.

---

### 4. Insert Test Data

```sql
INSERT INTO expected_inventory
(sku, warehouse_id, quantity, as_of_timestamp, source_reference)
VALUES ('SKU-1', 'WH-1', 120, NOW(), 'test');

INSERT INTO actual_inventory_snapshot
(sku, warehouse_id, quantity, snapshot_timestamp, source_system)
VALUES ('SKU-1', 'WH-1', 100, NOW(), 'warehouse');
```

Run reconciliation using `TestRunner`.

---

### 5. Access UI

```
http://localhost:8080/inventory-reconciliation-1.0-SNAPSHOT/discrepancies
```

---

## Key Properties

* Idempotent reconciliation
* Strict workflow validation
* Explicit transaction control
* Full state history
* Full audit logging

---
