package com.example.kaithaangu;

import android.os.Parcel;
import android.os.Parcelable;

public class DocumentClass implements Parcelable {

    private String UserName;
    private String mail;
    private String Phone;
    private String Id;
    private String isUser;
    private int TotalAMount = 0;

    public int getTotalAMount() {
        return TotalAMount;
    }

    public void setTotalAMount(int totalAMount) {
        TotalAMount = totalAMount;
    }

    public DocumentClass(){
    }

    protected DocumentClass(Parcel in) {
        UserName = in.readString();
        mail = in.readString();
        Phone = in.readString();
        Id = in.readString();
        isUser = in.readString();
    }

    public static final Creator<DocumentClass> CREATOR = new Creator<DocumentClass>() {
        @Override
        public DocumentClass createFromParcel(Parcel in) {
            return new DocumentClass(in);
        }

        @Override
        public DocumentClass[] newArray(int size) {
            return new DocumentClass[size];
        }
    };

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(UserName);
        parcel.writeString(mail);
        parcel.writeString(Phone);
        parcel.writeString(Id);
        parcel.writeString(isUser);
    }
}
