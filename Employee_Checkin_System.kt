package org.example

import java.time.*
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
    val checkinDateTime: LocalDateTime,
    var checkoutDateTime: LocalDateTime? = null
)
val employeeDatabase = mutableListOf(
    EmployeeDetails("E001", "John", "Doe", "Developer", "IT", "E010"),
    EmployeeDetails("E002", "Jane", "Smith", "Manager", "HR", "E015"),
    EmployeeDetails("E003", "David", "Johanson", "Analyst", "Finance", "E002")
)

val attendanceList = mutableListOf<AttendanceEntry>()

fun generateEmployeeId(): String {
    val lastId = employeeDatabase.maxOfOrNull { it.employeeId.removePrefix("E").toIntOrNull() ?: 0 } ?: 0
    return "E" + String.format("%03d", lastId + 1)
}

fun findEmployee(employeeId: String) = employeeDatabase.find { it.employeeId == employeeId }

fun isAlreadyCheckedIn(employeeId: String, date: LocalDate) =
    attendanceList.find { it.employeeId == employeeId && it.checkinDateTime.toLocalDate() == date }

fun checkInEmployee(employeeId: String, dateTime: LocalDateTime): String {
    val employee = findEmployee(employeeId)
    if (employee == null) {
        return "❌ Invalid Employee ID!"
    }
    val date = dateTime.toLocalDate()
    if (isAlreadyCheckedIn(employeeId, date) != null) {
        return "⚠️ You've already checked in for $date!"
    }
    attendanceList.add(AttendanceEntry(employeeId, dateTime))
    val formattedTime = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
    return "✅ Check-in successful for ${employee.firstName} at $formattedTime on $date"
}

fun checkoutEmployee(employeeId: String, checkoutTime: LocalDateTime): String {
    if (findEmployee(employeeId) == null) {
        return "❌ Invalid Employee ID!"
    }
    val attendance = isAlreadyCheckedIn(employeeId, checkoutTime.toLocalDate())
        ?: return "❌ No check-in record found for today."
    if (attendance.checkoutDateTime != null) {
        return "⚠️ Already checked out at ${attendance.checkoutDateTime}"
    }
    val checkInDateTime = attendance.checkinDateTime
    val duration = Duration.between(checkInDateTime,checkoutTime)
    attendance.checkoutDateTime = checkoutTime
    val formattedCheckout = checkoutTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    return "✅ Checkout successful at $formattedCheckout | Working hours: ${duration.toHours()}h ${duration.toMinutes() % 60}m"

}

fun parseDateTime(input: String?): Pair<LocalDate, LocalTime>? {
    val today = LocalDate.now()
    if (input.isNullOrBlank()) {
        val now = LocalTime.now().withSecond(0).withNano(0)
        return today to now
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
            println("❌ You can only check in/out for today's date.")
            null
        } else {
            date to time
        }
    } catch (e: DateTimeParseException) {
        println("❌ Invalid format! Use yyyy-MM-dd HH:mm")
        null
    }
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
    println("Employee Checkin System")
    while (true) {
        println("\nPlease choose an option:")
        println("1. Add New Employee")
        println("2. Check-In")
        println("3. Check-Out")
        println("4. Exit")
        print("Enter your choice (1-4): ")
        val choice = readln().trim()
        when (choice) {
            "1" -> addEmployee()
            "2" -> {
                print("Enter Employee ID for check-in: ")
                val empId = readln().trim()
                val employee = findEmployee(empId)
                if (employee == null) {
                    println("❌ Invalid Employee ID!")
                    continue
                }
                print("Enter check-in date and time (yyyy-MM-dd HH:mm) or press Enter for current date and time: ")
                val dtInput = readln()
                val dateTime = parseDateTime(dtInput)
                if (dateTime != null) {
                    val (date, time) = dateTime
                    val dateTimeCombined = LocalDateTime.of(date, time)
                    println(checkInEmployee(empId, dateTimeCombined))
                }
            }
            "3" -> {
                print("Enter Employee ID for checkout: ")
                val empId = readln().trim()
                val employee = findEmployee(empId)
                if (employee == null) {
                    println("❌ Invalid Employee ID!")
                    continue
                }
                print("Enter checkout date and time (yyyy-MM-dd HH:mm) or press Enter for current date and time: ")
                val dtInput = readln()
                val dateTime = parseDateTime(dtInput)
                if (dateTime != null) {
                    val (date, time) = dateTime
                    val checkoutTime = LocalDateTime.of(date, time)
                    println(checkoutEmployee(empId, checkoutTime))
                }
            }
            "4" -> {
                println("👋 Exiting system... Have a great day!")
                break
            }
            else -> {
                println("❌ Invalid choice! Please enter 1, 2, 3, or 4.")
            }
        }
    }
}
