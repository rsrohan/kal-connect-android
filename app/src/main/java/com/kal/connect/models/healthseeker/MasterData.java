package com.kal.connect.models.healthseeker;

import java.util.ArrayList;

public class MasterData {

    String MasterElementID="0", MasterElement;
    ArrayList<objSubElements> objSubElements;

    public MasterData(String masterElementID, String masterElement, ArrayList<MasterData.objSubElements> objSubElements) {
        MasterElementID = masterElementID;
        MasterElement = masterElement;
        this.objSubElements = objSubElements;
    }

    public MasterData() {
    }

    public String getMasterElementID() {
        return MasterElementID;
    }

    public void setMasterElementID(String masterElementID) {
        MasterElementID = masterElementID;
    }

    public String getMasterElement() {
        return MasterElement;
    }

    public void setMasterElement(String masterElement) {
        MasterElement = masterElement;
    }

    public ArrayList<MasterData.objSubElements> getObjSubElements() {
        return objSubElements;
    }

    public void setObjSubElements(ArrayList<MasterData.objSubElements> objSubElements) {
        this.objSubElements = objSubElements;
    }

    public class objSubElements{
        String SubElementID="0", SubElement, MasterElementID, IsTextbox, CustomSubElement="";
        boolean selected = false;

        public objSubElements(String subElementID, String subElement, String masterElementID, String isTextbox, String customSubElement, boolean selected) {
            SubElementID = subElementID;
            SubElement = subElement;
            MasterElementID = masterElementID;
            IsTextbox = isTextbox;
            CustomSubElement = customSubElement;
            this.selected = selected;
        }

        public objSubElements() {
        }

        public String getCustomSubElement() {
            return CustomSubElement;
        }

        public void setCustomSubElement(String customSubElement) {
            CustomSubElement = customSubElement;
        }

        public String getIsTextbox() {
            return IsTextbox;
        }

        public void setIsTextbox(String isTextbox) {
            IsTextbox = isTextbox;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getSubElementID() {
            return SubElementID;
        }

        public void setSubElementID(String subElementID) {
            SubElementID = subElementID;
        }

        public String getSubElement() {
            return SubElement;
        }

        public void setSubElement(String subElement) {
            SubElement = subElement;
        }

        public String getMasterElementID() {
            return MasterElementID;
        }

        public void setMasterElementID(String masterElementID) {
            MasterElementID = masterElementID;
        }
    }
}
