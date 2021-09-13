package com.emiliaengberg.sqliteinlamningsuppgift;

public class Coworker {
    private int mId;
    private String mName;
    private String mShiftNumber;
    private String mPhoneNumber;

    public Coworker(){}

    public Coworker(int id, String name, String shiftNumber, String phoneNumber) {
        mId = id;
        mName = name;
        mShiftNumber = shiftNumber;
        mPhoneNumber = phoneNumber;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getShiftNumber() {
        return mShiftNumber;
    }

    public void setShiftNumber(String shiftNumber) {
        mShiftNumber = shiftNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
}
