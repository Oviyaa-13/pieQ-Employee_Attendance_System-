package org.example

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
    val checkinDate: LocalDate,
    val checkinTime: LocalTime
)
val employeeDatabase = mutableListOf(
    EmployeeDetails("E001", "John", "Doe", "Developer", "IT", "E010"),
    EmployeeDetails("E002", "Jane", "Smith", "Manager", "HR", "E015"),
    EmployeeDetails("E003", "David", "Johnson", "Analyst", "Finance", "E002")
)
val attendanceList = mutableListOf<AttendanceEntry>()

fun generateEmployeeId(): String {
    val lastId = employeeDatabase.maxOfOrNull { it.employeeId.removePrefix("E").toIntOrNull() ?: 0 } ?: 0
    return "E" + String.format("%03d", lastId + 1)
}

fun findEmployee(employeeId: String) = employeeDatabase.find { it.employeeId == employeeId }

fun isAlreadyCheckedIn(employeeId: String, date: LocalDate) =
    attendanceList.any { it.employeeId == employeeId && it.checkinDate == date }

fun recordCheckIn(employeeId: String, date: LocalDate, time: LocalTime) =
    attendanceList.add(AttendanceEntry(employeeId, date, time))

fun parseDateTime(input: String?): Pair<LocalDate, LocalTime>? {
    val today = LocalDate.now()

    if (input.isNullOrBlank()) {
        val nowTime = LocalTime.now().withSecond(0).withNano(0)
        return today to nowTime
    }

    val parts = input.trim().split(" ")
    if (parts.size != 2) {
        println("❌ Invalid format! Use yyyy-MM-dd HH:mm")
        return null
    }

    val (datePart, timePart) = parts

    return try {
        val date = LocalDate.parse(datePart)
        val time = LocalTime.parse(timePart).withSecond(0).withNano(0)

        if (date != today) {
            println("❌ You can only check in for today's date, not $date.")
            null
        } else {
            date to time
        }
    } catch (e: DateTimeParseException) {
        println("❌ Invalid format! Use yyyy-MM-dd HH:mm")
        null
    }
}

fun checkInEmployee(employeeId: String, date: LocalDate, time: LocalTime) {
    val employee = findEmployee(employeeId)
    if (employee == null) {
        println("❌ Invalid Employee ID!")
        return
    }
    if (isAlreadyCheckedIn(employeeId, date)) {
        println("⚠️ You've already logged in for $date!")
        return
    }

    recordCheckIn(employeeId, date, time)
    val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))
    println("✅ Check-in successful for ${employee.firstName} at $formattedTime on $date")
}

fun addEmployee() {
    println("\n📋 Add New Employee:")
    fun isValidName(name: String): Boolean = name.length >= 3 && name.all { it.isLetter() }
    fun isValidRoleOrDept(input: String): Boolean = input.length >= 2 && input.all { it.isLetter() }
    val empIdRegex = Regex("^E\\d{3}$")
    var firstName: String
    while (true) {
        print("Enter First Name: ")
        firstName = readln().trim()
        if (isValidName(firstName)) break
        println("❌ First Name must be at least 3 letters and alphabetic only!")
    }
    var lastName: String
    while (true) {
        print("Enter Last Name: ")
        lastName = readln().trim()
        if (isValidName(lastName)) break
        println("❌ Last Name must be at least 3 letters and alphabetic only!")
    }
    var role: String
    while (true) {
        print("Enter Role: ")
        role = readln().trim()
        if (isValidRoleOrDept(role)) break
        println("❌ Role must be at least 2 letters and alphabetic only!")
    }
    var department: String
    while (true) {
        print("Enter Department: ")
        department = readln().trim()
        if (isValidRoleOrDept(department)) break
        println("❌ Department must be at least 2 letters and alphabetic only!")
    }
    var reportingTo: String
    while (true) {
        print("Enter Reporting To (Manager ID): ")
        reportingTo = readln().trim()
        if (empIdRegex.matches(reportingTo)) break
        println("❌ Invalid format! Employee ID should be like E001, E015, etc.")
    }
    val newId = generateEmployeeId()
    employeeDatabase.add(
        EmployeeDetails(
            employeeId = newId,
            firstName = firstName,
            lastName = lastName,
            role = role,
            department = department,
            reportingTo = reportingTo )
    )
    println("✅ Employee $firstName $lastName added successfully with ID: $newId\n")
}

fun main() {
    println("🕒 Employee Check-In System")
    println("Type 'exit' to quit | 'add' to add employee\n")
    while (true) {
        print("Enter Employee ID to check in or command: ")
        val input = readln().trim()
        when {
            input.equals("exit", ignoreCase = true) -> {
                println("👋 Exiting... Have a great day!")
                break
            }
            input.equals("add", ignoreCase = true) -> addEmployee()
            else -> {
                val employee = findEmployee(input)
                if (employee == null) {
                    println("❌ Invalid Employee ID!")
                    continue
                }
                print("Enter check-in date and time (yyyy-MM-dd HH:mm) or press Enter to use current: ")
                val dtInput = readln()
                val dateTime = parseDateTime(dtInput)
                if (dateTime == null) {
                    continue
                }
                val (date, time) = dateTime
                checkInEmployee(input, date, time)
                println()
            }
        }
    }
}
