# pieQ-Employee_Attendance_System-
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
fun checkInEmployee(employeeId: String) {
    val employee = employeeDatabase.find { it.employeeId == employeeId }
    if (employee == null) {
        println("❌ Invalid Employee ID!")
        return
    }
    val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    val alreadyCheckedIn = attendanceList.any {
        it.employeeId == employeeId && it.checkinDate == todayDate
    }
    if (alreadyCheckedIn) {
        println("⚠️ You've already logged in today!")
        return
    }
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    attendanceList.add(AttendanceEntry(employeeId, todayDate, currentTime))
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
}```
