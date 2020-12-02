package com.kal.connect.models;

import java.io.Serializable;

public class IssuesModel implements Serializable {
    String id,title;
    int selected;

    public IssuesModel(String id, String title, int selected) {
        this.id = id;
        this.title = title;
        this.selected = selected;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
