package com.kal.connect.models.healthseeker;

import java.util.ArrayList;

public class FamilyHistory {
    int HereditaryDiseaseID=0;
    String DiseaseName;
    ArrayList<objRelations> objRelations;

    boolean Father = false, Mother = false, Brother=  false, Sister = false, Pgm = false, Pgf = false, Mgm = false, Mgf = false;


    public FamilyHistory(int hereditaryDiseaseID, String diseaseName, ArrayList<FamilyHistory.objRelations> objRelations, boolean father, boolean mother, boolean brother, boolean sister, boolean Pgm, boolean pgf, boolean mgm, boolean mgf) {
        HereditaryDiseaseID = hereditaryDiseaseID;
        DiseaseName = diseaseName;
        this.objRelations = objRelations;
        Father = father;
        Mother = mother;
        Brother = brother;
        Sister = sister;
        this.Pgm = Pgm;
        Pgf = pgf;
        Mgm = mgm;
        Mgf = mgf;
    }

    public void setFamilyData(boolean father,boolean mother,boolean brother,boolean sister,boolean pgm,boolean pgf,boolean mgm,boolean mgf){
        this.Father = father;
        this.Mother = mother;
        this.Brother = brother;
        this.Sister = sister;
        this.Pgm = pgm;
        this.Pgf = pgf;
        this.Mgm = mgm;
        this.Mgf = mgf;
    }

    public boolean isFather() {
        return Father;
    }

    public void setFather(boolean father) {
        Father = father;
    }

    public boolean isMother() {
        return Mother;
    }

    public void setMother(boolean mother) {
        Mother = mother;
    }

    public boolean isBrother() {
        return Brother;
    }

    public void setBrother(boolean brother) {
        Brother = brother;
    }

    public boolean isSister() {
        return Sister;
    }

    public void setSister(boolean sister) {
        Sister = sister;
    }

    public boolean isPgm() {
        return Pgm;
    }

    public void setPgm(boolean pgm) {
        Pgm = pgm;
    }

    public boolean isPgf() {
        return Pgf;
    }

    public void setPgf(boolean pgf) {
        Pgf = pgf;
    }

    public boolean isMgm() {
        return Mgm;
    }

    public void setMgm(boolean mgm) {
        Mgm = mgm;
    }

    public boolean isMgf() {
        return Mgf;
    }

    public void setMgf(boolean mgf) {
        Mgf = mgf;
    }

    public int getHereditaryDiseaseID() {
        return HereditaryDiseaseID;
    }

    public void setHereditaryDiseaseID(int hereditaryDiseaseID) {
        HereditaryDiseaseID = hereditaryDiseaseID;
    }

    public String getDiseaseName() {
        return DiseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }

    public ArrayList<FamilyHistory.objRelations> getObjRelations() {
        return objRelations;
    }

    public void setObjRelations(ArrayList<FamilyHistory.objRelations> objRelations) {
        this.objRelations = objRelations;
    }

    public FamilyHistory() {
    }

    public FamilyHistory(int hereditaryDiseaseID, String diseaseName, ArrayList<FamilyHistory.objRelations> objRelations) {
        HereditaryDiseaseID = hereditaryDiseaseID;
        DiseaseName = diseaseName;
        this.objRelations = objRelations;
    }

    public class objRelations {
        String RelationshipName;
        boolean selected = false;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public objRelations(String relationshipName, boolean selected) {
            RelationshipName = relationshipName;
            this.selected = selected;
        }

        public objRelations(String relationshipName) {
            RelationshipName = relationshipName;
        }

        public objRelations() {
        }

        public String getRelationshipName() {
            return RelationshipName;
        }

        public void setRelationshipName(String relationshipName) {
            RelationshipName = relationshipName;
        }
    }
}
