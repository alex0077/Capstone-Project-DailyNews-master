package com.alex.dailynews.model;


public class SourcesList {
    private String source;
    private boolean isSelected;

    public SourcesList(String source, boolean isSelected) {
        this.source = source;
        this.isSelected = isSelected;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public SourcesList() {
    }
}
