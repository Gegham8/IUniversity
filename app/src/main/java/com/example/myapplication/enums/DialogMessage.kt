package com.example.myapplication.enums

enum class DialogMessage (val message: String)  {
    ENTER_NAME_AND_PRICE("Please enter profession name and price"),
    UPDATED_SUCCESSFULLY ("Profession updated successfully"),
    ERROR_IN_UPDATE ("Error updating profession"),
    FACULTY_ADDED_SUCCESSFULLY ("Faculty added successfully"),
    ERROR_TO_ADD_FACULTY ("Failed to add faculty. Please try again."),
    DELETED_SUCCESSFULLY ("Faculty deleted successfully"),
    FACULTY_NOT_FOUND ("Faculty not found"),
    UNIVERSITY_NOT_FOUND ("University not found"),
    ENTER_FACULTY_NAME ("Faculty name cannot be empty"),
    INVALID_USER ("Invalid User"),
    ENTER_PRICE ("Price is required")
}