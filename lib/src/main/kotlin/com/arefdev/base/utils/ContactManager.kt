package com.arefdev.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.arefdev.base.model.Contact

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ContactManager {

    fun extractContact(context: Context, data: Intent): Contact {
        val cursor: Cursor?
        val contact = Contact()
        try {
            val phoneNumber: String
            val name: String
            val uri = data.data!!
            cursor = context.contentResolver.query(
                uri, null, null, null, null
            )
            assert(cursor != null)
            cursor!!.moveToFirst()
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            phoneNumber = cursor.getString(phoneIndex)
            name = cursor.getString(nameIndex)
            cursor.close()
            contact.firstName = name
            contact.phoneNumber = phoneNumber
        } catch (ignored: Exception) {
        }
        return contact
    }

    @SuppressLint("Range")
    fun getContactDisplayName(context: Context, number: String?): String? {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        var name: String? = null
        val contentResolver = context.contentResolver
        contentResolver.query(
            uri, arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME
            ), null, null, null
        ).use { contactLookup ->
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
            }
        }
        return name
    }

    @SuppressLint("Range")
    fun getContactList(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return contacts
        }
        val cur = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur?.count ?: 0) > 0) {
            while (cur!!.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val contact = Contact()
                        contact.firstName = name
                        contact.phoneNumber = phoneNumber
                        contacts.add(contact)
                    }
                    pCur.close()
                }
            }
            cur.close()
        }
        return contacts
    }
}
