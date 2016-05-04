package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Form http://www.sachinmuralig.me/2013/11/android-simple-multi-contacts-picker.html
 */
public class ContactsLoader extends AsyncTask<String,Void,Void> {

    ContactsListAdapter contactsListAdapter;
    Context context;
    private ArrayList<Contact> tempContactHolder;
    TextView txtProgress;
    int totalContactsCount,loadedContactsCount;


    ContactsLoader(Context context,ContactsListAdapter contactsListAdapter){
        this.context = context;
        this.contactsListAdapter = contactsListAdapter;
        this.tempContactHolder= new ArrayList<>();
        loadedContactsCount=0;
        totalContactsCount=0;
        txtProgress=null;
    }

    @Override
    protected Void doInBackground(String[] filters) {
        String filter = filters[0];
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA
        };

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor;

        if(filter.length() > 0) {
            cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                    new String[]{filter},
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );
        } else {
            cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );

        }

        if (cursor != null) {
            try {
                final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                long contactId;
                String displayName, mail;

                while (cursor.moveToNext()) {
                    contactId = cursor.getLong(contactIdIndex);
                    displayName = cursor.getString(displayNameIndex);
                    mail = cursor.getString(emailIndex);
                    tempContactHolder.add(new Contact(Long.toString(contactId), displayName, mail));

                    loadedContactsCount++;
                    publishProgress();
                }
            } finally {
                cursor.close();
            }
        }

        /*totalContactsCount = cursor.getCount();
        if(cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id},
                            null
                    );

                    if (phoneCursor != null && phoneCursor.getCount() > 0) {

                        while (phoneCursor.moveToNext()) {
                            String phId = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                            String phNo = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            tempContactHolder.add(new Contact(phId, name, phNo));
                        }
                        phoneCursor.close();

                    }

                }
                loadedContactsCount++;

                publishProgress();


            }
            cursor.close();
        }*/

        return null;
    }

    @Override
    protected void onProgressUpdate(Void[] v) {
        if(this.tempContactHolder.size() >= 100) {
            contactsListAdapter.addContacts(tempContactHolder);
            this.tempContactHolder.clear();

            if(txtProgress!=null) {
                txtProgress.setVisibility(View.VISIBLE);
                String progressMessage = "Loading... ("+loadedContactsCount+"/"+totalContactsCount+")";
                txtProgress.setText(progressMessage);
            }
        }
    }

    @Override
    protected void onPostExecute(Void v){
        contactsListAdapter.addContacts(tempContactHolder);
        tempContactHolder.clear();

        if(txtProgress!=null) {
            txtProgress.setText("");
            txtProgress.setVisibility(View.GONE);
        }
    }
}
