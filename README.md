# pieQ-Employee_Attendance_System-
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
- `checkinDate: String`
- `checkinTime: String`

---

## ⚙️ Functionalities

### `findEmployee(employeeId)`
- Searches for the employee in `employeeDatabase` by ID.
- Returns the matching `EmployeeDetails` object if found, else returns `null`.

### `isAlreadyCheckedIn(employeeId,date)`
- Checks if the employee has already checked in on the given date.

### `recordCheckIn(employeeId,date,time)`
- Adds a new attendance entry to `attendanceList`.

### `checkInEmployee(employeeId)`
- Validates:
  - Whether the `employeeId` exists in `employeeDatabase`.
  - Whether the employee has already checked in **today**.
- If both validations pass:
  - Fetches current system date and time.
  - Calls `recordCheckIn()` to store the check-in.
  - Prints success message with check-in time.

### `main()`
- CLI loop to:
  - Prompt for employee ID input.
  - Accept `"exit"` (case-insensitive) to quit the program.
  - Otherwise, attempts to check in the entered employee ID.

```
package org.example

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class EmployeeDetails(
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val department: String,
    val reportingTo: String
)
data class AttendanceEntry(
    val employeeId: String,
    val checkinDate: String,
    val checkinTime: String
)
val employeeDatabase = listOf(
    EmployeeDetails("E001", "John", "Doe", "Developer", "IT", "E010"),
    EmployeeDetails("E002", "Jane", "Smith", "Manager", "HR", "E015"),
    EmployeeDetails("E003", "David", "Johnson", "Analyst", "Finance", "E002")
)
val attendanceList = mutableListOf<AttendanceEntry>()
fun findEmployee(employeeId: String) = employeeDatabase.find { it.employeeId == employeeId }

fun isAlreadyCheckedIn(employeeId: String, date: String) =
    attendanceList.any { it.employeeId == employeeId && it.checkinDate == date }

fun recordCheckIn(employeeId: String, date: String, time: String) =
    attendanceList.add(AttendanceEntry(employeeId, date, time))

fun checkInEmployee(employeeId: String) {
    val employee = findEmployee(employeeId)
    if (employee == null) {
        println("❌ Invalid Employee ID!")
        return
    }
    val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    if (isAlreadyCheckedIn(employeeId, todayDate)) {
        println("⚠️ You've already logged in today!")
        return
    }
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    recordCheckIn(employeeId, todayDate, currentTime)
    println("✅ Check-in successful for ${employee.firstName} at $currentTime on $todayDate")
}

fun main() {
    println("Employee Check-In System")
    println("Type 'exit' to quit\n")
    while (true) {
        print("Enter Employee ID to check in: ")
        val input = readln().trim()
        if (input.equals("exit", ignoreCase = true)) {
            println("👋 Exiting... Have a great day!")
            break
        }
        checkInEmployee(input)
        println()
    }
}

