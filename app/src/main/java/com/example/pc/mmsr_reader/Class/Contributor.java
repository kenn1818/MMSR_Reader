package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 10/29/2017.
 */

public class Contributor {
    public String email;
    public String password;
    public String name;
    public String status;
    public String dob;
    public String type;
    public String reg_date;
    public String reg_time;

    public Contributor() {
    }

    public Contributor(String email, String password, String name, String status, String dob, String type, String reg_date, String reg_time) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.dob = dob;
        this.type = type;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }
}
