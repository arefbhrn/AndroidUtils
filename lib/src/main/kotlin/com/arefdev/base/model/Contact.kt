package com.arefdev.base.model

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class Contact(
    var phoneNumber: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
) : BaseModel()
