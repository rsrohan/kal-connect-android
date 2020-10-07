package com.kal.connect.customLibs.autoCompletor;

/**
 * Created by MysteryMachine on 12/29/2016.
 */
public class AutoCompleteItem {
    public String itemID;
    public String itemName;
    public AutoCompleteItem(){

    }
    public AutoCompleteItem(String id,String value){
        this.itemID = id;
        this.itemName = value;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
