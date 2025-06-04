# 🧪 Transport Management System - Complete Testing Guide

## 📋 Pre-Test Setup

### 1. Start the System
```bash
cd dev
javac -d out src/main/**/*.java
java -cp out src.main.Main
```

### 2. Initialize Demo Data
- When prompted: **Type `Y` (Yes) to create demo data**
- Wait for initialization to complete

## 🔐 Login Testing

### Test 1: Valid Login
1. Username: `admin`
2. Password: `admin123`
3. Expected: Login successful, welcome message

### Test 2: Invalid Login (Optional)
1. Username: `wrong`
2. Password: `wrong`
3. Expected: Error message, try again option

---

## 👥 FLEET MANAGEMENT TESTING

### Test 3: Add New Driver
**Navigation:** Main Menu → 3 (Fleet) → 2 (Driver Management) → 1 (Add New Driver)

1. **ID:** `999888777`
2. **Name:** `Test Driver Smith`
3. **Phone:** `050-9998887`
4. **License:** Choose `1` (C1) or `2` (C) or `3` (CE)
5. **Expected:** Driver added successfully
6. **Verify:** Go to "3 (View All Drivers)" and see the new driver

### Test 4: Add New Site/Location
**Navigation:** Main Menu → 4 (Sites) → 1 (Add New Site)

1. **ID:** `TESTSITE01`
2. **Name:** `Test Distribution Center`
3. **Address:** `Test Street 123, Test City`
4. **Phone:** `03-1234567`
5. **Contact Name:** `Test Manager`
6. **Zone:** Choose any zone (1-4)
7. **Expected:** Site added successfully
8. **Verify:** Go to "3 (View All Sites)" and see the new site

### Test 5: Add New Truck
**Navigation:** Main Menu → 3 (Fleet) → 1 (Truck Management) → 1 (Add New Truck)

1. **Registration:** `99-888-77`
2. **Model:** `Test Truck Model`
3. **Empty Weight:** `5000`
4. **Max Weight:** `15000`
5. **License Type:** Choose `2` (C)
6. **Expected:** Truck added successfully
7. **Verify:** Go to "3 (View All Trucks)" and see the new truck

### Test 6: Truck Capacity Tracking
**Purpose:** Test that truck available capacity updates correctly when orders are assigned

1. **Create heavy order:**
   - Navigation: Main Menu → 2 (Orders) → 1 (Create Order)
   - Date: `2025-06-15`
   - Site: Choose any site
   - Type: `2` (Delivery)
   - Items: Add heavy items (total ~100kg)
   - Create transport: `y`
   - **Note the truck capacity before selection**

2. **Select truck and observe:**
   - **Expected:** Trucks should show "Available: XXkg / Total Capacity" 
   - Choose truck with sufficient capacity
   - Complete transport creation

3. **Create another order immediately:**
   - Navigation: Main Menu → 2 (Orders) → 1 (Create Order)
   - Similar heavy order
   - Create transport: `y`
   - **Expected:** The previously selected truck should now show reduced available capacity

---

## 📦 ORDER TESTING

### Test 7: Create Pickup Order
**Navigation:** Main Menu → 2 (Orders) → 1 (Create Order)

1. **Date:** `2025-06-01`
2. **Site:** Choose `1` (Central Warehouse)
3. **Type:** Choose `1` (Pickup)
4. **Items source:** Choose `1` (Use existing items)
5. **Select items:**
   - Choose `1` (Milk Carton) → Quantity: `10`
   - Choose `2` (Bread Loaf) → Quantity: `20` 
   - Choose `0` to finish
6. **Expected:** Order created with Pickup type, NO transport prompt

### Test 8: Create Delivery Order + Immediate Transport
**Navigation:** Main Menu → 2 (Orders) → 1 (Create Order)

1. **Date:** `2025-06-02`
2. **Site:** Choose `2` (Tiberias Branch - North)
3. **Type:** Choose `2` (Delivery)
4. **Items source:** Choose `2` (Add custom items)
5. **Custom items:**
   - Name: `Heavy Box` → Quantity: `5` → Weight: `15` → done
6. **Create transport now?** Choose `y`
7. **Date:** `2025-06-02`
8. **Time:** `14:30`
9. **Source:** Auto-selected (Tiberias Branch)
10. **Destinations:** Choose any destination in same zone (North) → `0` to finish
11. **Truck:** Choose any available truck with sufficient capacity
12. **Driver:** Choose any available driver
13. **Create transport?** Choose `y`
14. **Expected:** Order + Transport created and linked

### Test 9: Create Delivery Order - Transport Later
**Navigation:** Main Menu → 2 (Orders) → 1 (Create Order)

1. **Date:** `2025-06-03`
2. **Site:** Choose `9` (Department Store - Jerusalem)
3. **Type:** Choose `2` (Delivery)
4. **Items source:** Choose `3` (Mix both)
5. **Existing items:**
   - Choose `11` (Refrigerator) → Quantity: `1`
   - Choose `0` to finish existing
6. **Custom items:**
   - Name: `Installation Kit` → Quantity: `1` → Weight: `5` → done
7. **Create transport now?** Choose `n`
8. **Expected:** Order created without transport

### Test 10: View Orders - All Options
**Navigation:** Main Menu → 2 (Orders) → 3 (View All Orders)

1. **Test All Orders:** Choose `1`
   - **Expected:** See all orders created
2. **Back:** Choose `0`
3. **Test By Status:** Choose `2` → Choose `1` (CREATED)
   - **Expected:** See orders not yet in transport
4. **Back:** Choose `0`
5. **Test By Date:** Choose `3` → Enter `2025-06-02`
   - **Expected:** See order from that date
6. **Back:** Choose `0`
7. **Test By Type:** Choose `4` → Choose `1` (Pickup Orders)
   - **Expected:** See only pickup orders
8. **Back:** Choose `0`
9. **Test By Type:** Choose `4` → Choose `2` (Delivery Orders)
   - **Expected:** See delivery orders with transport info

---

## 🚛 TRANSPORT TESTING

### Test 11: Create Empty Transport
**Navigation:** Main Menu → 1 (Transports) → 1 (Create Transport)

1. **Date:** `2025-06-04`
2. **Time:** `09:00`
3. **Type:** Choose `1` (Empty transport)
4. **Source:** Choose `1` (Central Warehouse - North)
5. **Destinations:** Choose `2` (Tiberias Branch) → `0` to finish
6. **Truck:** Choose any available truck (**Note the available capacity**)
7. **Driver:** Choose any compatible driver
8. **Create transport?** Choose `y`
9. **Expected:** Empty transport created

### Test 12: Create Transport with Existing Order
**Navigation:** Main Menu → 1 (Transports) → 1 (Create Transport)

1. **Date:** `2025-06-05`
2. **Time:** `10:00`
3. **Type:** Choose `2` (Transport with existing order)
4. **Select order:** Choose the delivery order without transport (from Test 9)
5. **Source:** Auto-selected from order
6. **Destinations:** Choose any destination in same zone → `0` to finish
7. **Truck:** Choose truck with sufficient capacity (≥85kg for refrigerator)
   - **Expected:** Available capacity should be displayed correctly
8. **Driver:** Choose compatible driver
9. **Create transport?** Choose `y`
10. **Expected:** Transport created with order assigned

### Test 13: View Transports
**Navigation:** Main Menu → 1 (Transports) → 2 (View Transports)

1. **View All:** Choose `1`
   - **Expected:** See all transports with basic info

---

## ⚠️ ERROR & EDGE CASE TESTING

### Test 14: Weight Overload Scenario
**Navigation:** Create a heavy order then try small truck

1. **Create heavy order:**
   - Main Menu → 2 → 1
   - Date: `2025-06-20`
   - Site: Choose any
   - Type: Delivery
   - Items: Choose heavy items (total >100kg)

2. **Try to assign to small truck:**
   - Create transport now: `y`
   - **Expected:** Only trucks with sufficient available capacity should appear

### Test 15: Multiple Orders Same Truck
**Test truck capacity tracking with multiple orders**

1. **Create first light order** (~20kg)
   - Assign to specific truck
   - Note available capacity shown

2. **Create second light order** (~30kg)  
   - Try to assign to same truck
   - **Expected:** Available capacity should show reduced amount

3. **Create heavy order** (remaining capacity + 10kg)
   - Try to assign to same truck
   - **Expected:** Truck should not appear in suitable trucks list

### Test 16: License Mismatch Testing
**Try to observe driver selection based on truck license requirements**

1. **Create transport with heavy truck:**
   - Choose truck requiring CE license
   - **Expected:** Only drivers with CE license should appear

### Test 17: Zone Restriction Testing
1. **Create transport with source in NORTH zone**
2. **Try destinations:** Should only show NORTH zone sites
3. **Expected:** No sites from other zones available

### Test 18: Order Status Updates
**Navigation:** Main Menu → 2 (Orders) → 5 (Update Status)

1. **Find order ID** from previous tests
2. **Try updating status:** Choose different statuses
3. **Expected:** Status changes successfully

### Test 19: Cancel Order Testing
**Navigation:** Main Menu → 2 (Orders) → 4 (Cancel Order)

1. **Try canceling CREATED order:**
   - **Expected:** Success
2. **Try canceling IN_PROGRESS order:**
   - **Expected:** Should fail (cannot cancel)

---

## 🏢 SITE & FLEET TESTING

### Test 20: View Sites
**Navigation:** Main Menu → 4 (Sites)
- **Expected:** See all sites organized by zones (including new test site)

### Test 21: View Fleet
**Navigation:** Main Menu → 3 (Fleet)
- **Expected:** See all trucks with available/total capacity (including new test truck)

---

## 📊 REPORTS TESTING

### Test 22: View Reports
**Navigation:** Main Menu → 5 (Reports)
- Test any available report functions

---

## 🔧 USER MANAGEMENT TESTING (Admin Only)

### Test 23: User Settings
**Navigation:** Main Menu → 6 (Settings) → 1 (User Management)
- **Expected:** Access if admin, denied if not

### Test 24: Change Password
**Navigation:** Main Menu → 6 (Settings) → 2 (Change Password)

1. **Current password:** `admin123`
2. **New password:** `newpass123`
3. **Confirm:** `newpass123`
4. **Expected:** Password changed
5. **Test login with new password**

---

## ❌ EXPECTED ERROR SCENARIOS

### Test 25: Invalid Date Inputs
- Try past dates: **Expected:** Error message
- Try invalid formats: **Expected:** Error message

### Test 26: Empty Selections
- Try creating order without items: **Expected:** Error/prevention
- Try creating transport without destinations: **Expected:** Error/prevention

### Test 27: Back Navigation
- Use "back" option during order/transport creation
- **Expected:** Smooth navigation without errors

---

## 🎯 SUCCESS CRITERIA

✅ **Fleet Management:**
- New drivers, trucks, and sites can be added
- Truck available capacity updates correctly when orders are assigned
- System prevents overloading trucks

✅ **Orders:**
- Pickup orders created without transport prompts
- Delivery orders can create immediate or delayed transports
- All view filters work correctly
- Status updates work

✅ **Transports:**
- Empty transports created successfully
- Order-based transports respect weight limits and show available capacity
- Driver selection respects license requirements
- Zone restrictions enforced

✅ **System:**
- No crashes or exceptions
- Smooth navigation between menus
- Data persistence throughout session
- Proper error messages for invalid inputs
- Real-time capacity tracking

---

## 🐛 WHAT TO COPY FOR DEBUGGING

If any test fails, copy the **entire terminal output** including:
1. The menu choices you made
2. The exact error message
3. The stacktrace (if any)
4. What you expected vs what happened

**Example:**
```
[FAILED TEST 6]
Expected: Truck capacity to show reduced available weight after order assignment
Actual: Still shows full capacity
Steps taken: Main Menu → 2 → 1 → Created order → Assigned to truck → ...
```

---

## 🚀 Quick Test Run (20 minutes)

For a quick system check, run tests: **3, 4, 5, 6, 7, 8, 11, 12, 14, 15**

This covers fleet management, core functionality, and capacity tracking without diving into every edge case. 