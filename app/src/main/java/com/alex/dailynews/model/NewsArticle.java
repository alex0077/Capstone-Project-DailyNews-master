package com.alex.dailynews.model;

import com.google.gson.annotations.SerializedName;


public class NewsArticle {
    @SerializedName("author")
    private
    String author;

    @SerializedName("title")
    private
    String title;

    @SerializedName("description")
    private
    String description;

    @SerializedName("url")
    private
    String url;

    @SerializedName("urlToImage")
    private
    String urlToImage;

    @SerializedName("publishedAt")
    private
    String publishedAt;

    @SerializedName("source")
    private
    SourceInfo sourceInfo;

    public NewsArticle() {
    }

    public NewsArticle(String author, String title, String description, String url, String urlToImage, String publishedAt, SourceInfo sourceInfo) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.sourceInfo = sourceInfo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

}
