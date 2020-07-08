package com.idam.idam_tech.models;

import android.content.Context;
import android.content.SharedPreferences;

public class PackageHistory {
    private int id;
    private String member_id;
    private String package_id;
    private String status_pembayaran;
    private String status_history;

    public PackageHistory() {
    }

    public PackageHistory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public String getStatus_history() {
        return status_history;
    }

    public void setStatus_history(String status_history) {
        this.status_history = status_history;
    }


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_history";

    public PackageHistory(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSessionHistory(PackageHistory packageHistory){
        int id = packageHistory.getId();
        editor.putInt(SESSION_KEY, id).commit();
    }

    public int getSessionHistory(){
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public void removeSessionHistory(){
        editor.putInt(SESSION_KEY, -1).commit();
    }

}
