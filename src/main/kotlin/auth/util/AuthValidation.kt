package com.hadar.auth.util

import com.hadar.auth.util.errors.ValidateException

class AuthValidation {
    fun validate(
        email: String,
        password: String,
        phone: String,
        fName: String,
        lName: String
    ): Boolean {
        if (fName.length < 3 || !fName.matches(regex = Regex("""^[\p{L}]+(?:\s[\p{L}]+)*$"""))) throw ValidateException(
            "first name less than 3 characters or contain digits"
        )
        if (lName.length < 3 || !lName.matches(regex = Regex("""^[\p{L}]+(?:\s[\p{L}]+)*$"""))) throw ValidateException(
            "last name less than 3 characters or contain digits"
        )
        if (!phone.matches(Regex("""^01[0125]\d{8}$""")) && phone.isNotEmpty()) throw ValidateException("number must begin with 010 or 011 or 012 or 015 with 9 digits after")
        if (!password.matches(regex = Regex("""^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$"""))) throw ValidateException(
            "password does not contain special or digit or capital or small letter and length is less than 8"
        )
        if (!email.matches(regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) throw ValidateException(
            "email not match pattern"
        )
        return true
    }
}