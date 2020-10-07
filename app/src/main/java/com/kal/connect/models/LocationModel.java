package com.kal.connect.models;

public class LocationModel {
    String id, name;

    boolean selected;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationModel(String id, String name,boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;

    }
}
