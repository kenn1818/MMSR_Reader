package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 10/29/2017.
 */

public class Page {
    public String languageCode;
    public int pageNo;
    public byte[] Media;
    public String content;
    public int WordCount;

    public Page() {
    }

    public Page(int pageNo) {
        this.pageNo = pageNo;
    }

    public Page(String languageCode, int pageNo, byte[] media, String content, int wordCount) {
        this.languageCode = languageCode;
        this.pageNo = pageNo;
        Media = media;
        this.content = content;
        WordCount = wordCount;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public byte[] getMedia() {
        return Media;
    }

    public void setMedia(byte[] media) {
        Media = media;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWordCount() {
        return WordCount;
    }

    public void setWordCount(int wordCount) {
        WordCount = wordCount;
    }
}
