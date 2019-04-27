package arsatech.co.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import arsatech.co.utils.Models.Contact;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class ContactManager {

	public static Contact extractContact(Context context, Intent data) {
		Cursor cursor;
		Contact contact = new Contact();
		try {
			String phoneNumber;
			String name;
			Uri uri = data.getData();
			//Query the content uri
			assert uri != null;
			cursor = context.getContentResolver().query(uri, null, null, null
					, null);
			assert cursor != null;
			cursor.moveToFirst();
			int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
			phoneNumber = cursor.getString(phoneIndex);
			name = cursor.getString(nameIndex);
			cursor.close();
			contact.setName(name);
			contact.setPhoneNumber(phoneNumber);
		} catch (Exception ignored) {
		}
		return contact;
	}

	public static String getContactDisplayName(Context context, String number) {
		int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			return null;
		}

		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		String name = null;

		ContentResolver contentResolver = context.getContentResolver();

		try (Cursor contactLookup = contentResolver.query(uri, new String[]{ContactsContract.Contacts._ID,
				ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null)) {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			}
		}

		return name;
	}

	public ArrayList<Contact> getContactList(Context context) {
		ArrayList<Contact> contacts = new ArrayList<>();

		int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			return contacts;
		}

		Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		if ((cur != null ? cur.getCount() : 0) > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
					Cursor pCur = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
							null);
					while (pCur.moveToNext()) {
						String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Contact contact = new Contact();
						contact.setName(name);
						contact.setPhoneNumber(phoneNumber);
						contacts.add(contact);
					}
					pCur.close();
				}
			}
			cur.close();
		}
		return contacts;
	}

}
