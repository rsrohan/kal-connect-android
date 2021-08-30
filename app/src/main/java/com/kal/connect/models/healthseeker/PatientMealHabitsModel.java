package com.kal.connect.models.healthseeker;

public class PatientMealHabitsModel {

    String MealHabitID="0", MealTime, HungerRatings="-1", MealType;

    public PatientMealHabitsModel(String mealHabitID, String mealTime, String hungerRatings, String mealType) {
        MealHabitID = mealHabitID;
        MealTime = mealTime;
        HungerRatings = hungerRatings;
        MealType = mealType;
    }

    public PatientMealHabitsModel() {
    }

    public String getMealHabitID() {
        return MealHabitID;
    }

    public void setMealHabitID(String mealHabitID) {
        MealHabitID = mealHabitID;
    }

    public String getMealTime() {
        return MealTime;
    }

    public void setMealTime(String mealTime) {
        MealTime = mealTime;
    }

    public String getHungerRatings() {
        return HungerRatings;
    }

    public void setHungerRatings(String hungerRatings) {
        HungerRatings = hungerRatings;
    }

    public String getMealType() {
        return MealType;
    }

    public void setMealType(String mealType) {
        MealType = mealType;
    }
}
