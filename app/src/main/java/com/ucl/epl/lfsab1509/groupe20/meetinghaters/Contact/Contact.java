package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Form http://www.sachinmuralig.me/2013/11/android-simple-multi-contacts-picker.html
 */
public class Contact implements Parcelable {

    public String id,name,mail;

    Contact(String id, String name, String mail) {
        this.id = id;
        this.name = name;
        this.mail = mail;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
        mail = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public String toString() {
        return name + ": " + mail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mail);;
    }
}
