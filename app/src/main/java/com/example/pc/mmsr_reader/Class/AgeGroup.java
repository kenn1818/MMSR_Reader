package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 10/29/2017.
 */

public class AgeGroup {
    public String ageGroupCode;
    public String ageGroupDesc;

    public AgeGroup() {
    }

    public AgeGroup(String ageGroupCode, String ageGroupDesc) {
        this.ageGroupCode = ageGroupCode;
        this.ageGroupDesc = ageGroupDesc;
    }

    public String getAgeGroupCode() {
        return ageGroupCode;
    }

    public void setAgeGroupCode(String ageGroupCode) {
        this.ageGroupCode = ageGroupCode;
    }

    public String getAgeGroupDesc() {
        return ageGroupDesc;
    }

    public void setAgeGroupDesc(String ageGroupDesc) {
        this.ageGroupDesc = ageGroupDesc;
    }
}
