## Overview

This is the workflow or the approach for an **Employee Check-In System**, built using **Kotlin** as a **Command Line Interface (CLI)**.

We aim to check in the employee using their `emp_id` and to validate their check-in.

---

## Data Classes

### `EmployeeDetails`
Represents an employee's information:
- `employeeId: String`
- `firstName: String`
- `lastName: String`
- `role: String`
- `department: String`
- `reportingTo: String`

### `AttendanceEntry`
Stores each check-in record:
- `employeeId: String`
- `checkinDate: LocalDate`
- `checkinTime: LocalTime`

---

## ⚙️ Functionalities

### `generateEmployeeId()`
- Automatically generates a new employee ID in the format `E###` based on the highest existing ID.

### `findEmployee(employeeId)`
- Searches for the employee in `employeeDatabase` by ID.
- Returns the matching `EmployeeDetails` object if found, else returns `null`.

### `isAlreadyCheckedIn(employeeId, date)`
- Checks if the employee has already checked in on the given date.

### `recordCheckIn(employeeId, date, time)`
- Adds a new `AttendanceEntry` to the `attendanceList`.

### `parseDateTime(input)`
- Accepts a date-time string in `yyyy-MM-dd HH:mm` format.
- Returns a pair of `LocalDate` and `LocalTime` if the format is valid and the date is today.
- Defaults to current system date and time if input is blank.
- Returns `null` if validation fails.

### `checkInEmployee(employeeId, date, time)`
- Validates:
  - Whether the `employeeId` exists in `employeeDatabase`.
  - Whether the employee has already checked in **today**.
- If valid:
  - Calls `recordCheckIn()` to store the check-in.
  - Prints a success message with the check-in time.

### `addEmployee()`
- Interactive CLI to add a new employee.
- Validates input for names, role, department, and reporting manager ID.
- Generates a new `employeeId` and adds the employee to the `employeeDatabase`.

### `main()`
- CLI loop to:
  - Prompt for employee ID or command.
  - Accept `"exit"` (case-insensitive) to quit the program.
  - Accept `"add"` to enter employee registration flow.
  - Otherwise, initiates the check-in flow for the given `employeeId`.

---
