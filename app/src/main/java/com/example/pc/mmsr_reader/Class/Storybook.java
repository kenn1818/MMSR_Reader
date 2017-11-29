package com.example.pc.mmsr_reader.Class;

import java.util.ArrayList;

/**
 * Created by pc on 10/29/2017.
 */

public class Storybook {
    public String storybookID;
    public String language;
    public String dateOfCreation;
    public String readability;
    public String title;
    public String desc;
    public String type;
    public String status;
    public String email;
    public String ageGroup;

    public byte[]  coverPage;
    public int totalWords;
    public int totalPages;
    public String rate;


    private ArrayList<Page> page = new ArrayList<Page>();
    private int lastPageNo = 0;


    public Storybook() {
    }

    public byte[]  getCoverPage() {
        return coverPage;
    }

    public void setCoverPage(byte[]  coverPage) {
        this.coverPage = coverPage;
    }
    public Storybook(String storybookID, String title, String desc, String language, String ageGroup, String dateOfCreation, String email, byte[] coverPage, String rate) {
        this.storybookID = storybookID;
        this.title = title;
        this.desc = desc;
        this.language = language;
        this.ageGroup = ageGroup;
        this.email = email;
        this.dateOfCreation = dateOfCreation;
        this.coverPage = coverPage;
        this.rate = rate;
    }

    public Storybook(String storybookID, String title, String desc, String language, String status, String ageGroup, String dateOfCreation, String type, String email, byte[]  coverPage, String rate) {
        this.storybookID = storybookID;
        this.title = title;
        this.desc = desc;
        this.language = language;
        this.status = status;
        this.ageGroup = ageGroup;
        this.dateOfCreation = dateOfCreation;
        this.type = type;
        this.email = email;
        this.coverPage = coverPage;
        this.rate = rate;
    }

    public String getStorybookID() {
        return storybookID;
    }

    public void setStorybookID(String storybookID) {
        this.storybookID = storybookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getRate() {
        return rate;
    }

    public void setRating(String rate) {
        this.rate = rate;
    }

    public void addPage() {
        this.page.add(new Page(lastPageNo));
        lastPageNo++;
    }

    public Page getPage(int pageNo) {

        return this.page.get(pageNo);
    }

    public int getLastPageNo() {

        return lastPageNo;
    }

    public void setPage(ArrayList<Page> page) {
        this.page = page;
        this.lastPageNo = page.size();
    }

    public void deletePage(int pageNo) {
        page.remove(pageNo);
        lastPageNo--;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }
}
