package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact;
import android.util.Log;

import java.util.ArrayList;

/*
 * Form http://www.sachinmuralig.me/2013/11/android-simple-multi-contacts-picker.html
 */
public class ContactsList {

    public ArrayList<Contact> contactArrayList;

    public ContactsList() {
        contactArrayList = new ArrayList<Contact>();
    }

    public int getCount() {
        return contactArrayList.size();
    }

    public void addContact(Contact contact) {
        contactArrayList.add(contact);
    }

    public  void removeContact(Contact contact) {
        contactArrayList.remove(contact);
    }

    public Contact getContact(int id) {
        for(int i=0;i<this.getCount();i++){
            if(Integer.parseInt(contactArrayList.get(i).id)==id)
                return contactArrayList.get(i);
        }

        return null;
    }
}
