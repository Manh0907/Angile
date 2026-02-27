package com.nhom1.kttstoreapp.model;

public class Notification {
    private String title;
    private String content;
    private String timestamp;
    private boolean isRead;

    public Notification(String title, String content, String timestamp, boolean isRead) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
