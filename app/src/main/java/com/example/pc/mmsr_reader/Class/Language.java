package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 10/29/2017.
 */

public class Language {
    public String languageCode;
    public String languageDesc;
    public String preference;

    public Language() {
    }

    public Language(String languageCode, String languageDesc, String preference) {
        this.languageCode = languageCode;
        this.languageDesc = languageDesc;
        this.preference = preference;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageDesc() {
        return languageDesc;
    }

    public void setLanguageDesc(String languageDesc) {
        this.languageDesc = languageDesc;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }
}
