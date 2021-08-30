package com.kal.connect.models.healthseeker;

import java.util.ArrayList;

public class DemographyData {

    String DemographyType;
    ArrayList<objSubElements> objSubElements;

    public DemographyData(String masterElement, ArrayList<DemographyData.objSubElements> objSubElements) {
        DemographyType = masterElement;
        this.objSubElements = objSubElements;
    }

    public DemographyData() {
    }


    public String getDemographyType() {
        return DemographyType;
    }

    public void setDemographyType(String DemographyType) {
        this.DemographyType = DemographyType;
    }

    public ArrayList<DemographyData.objSubElements> getObjSubElements() {
        return objSubElements;
    }

    public void setObjSubElements(ArrayList<DemographyData.objSubElements> objSubElements) {
        this.objSubElements = objSubElements;
    }

    public static class objSubElements {
        String Type;
        boolean selected = false;

        public objSubElements(String type, boolean selected) {
            Type = type;
            this.selected = selected;
        }

        public objSubElements() {
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}