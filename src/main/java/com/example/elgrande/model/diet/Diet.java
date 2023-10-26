package com.example.elgrande.model.diet;

import com.example.elgrande.model.enums.enums_diet.DietType;
import com.example.elgrande.model.enums.enums_diet.FoodType;
import com.example.elgrande.model.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dietName;
    @ManyToMany
    @JoinTable(name = "diets", joinColumns = @JoinColumn(name="diet_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private List<Meal> mealsArray;
    private FoodType foodType;
    private DietType dietType;
    private int dietCalories;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Diet(String dietName, List<Meal> mealsArray, FoodType foodType, DietType dietType) {
        this.dietName = dietName;
        this.mealsArray = mealsArray;
        this.foodType = foodType;
        this.dietType = dietType;
        this.dietCalories = calculateDietCalories();
    }
    public Diet() {
    }

    private int calculateDietCalories() {
        int sum = 0;
        for(Meal meal : mealsArray) {
            sum += meal.getMealCalories();
        }
        return sum;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Diet{" +
                "dietName='" + dietName + '\'' +
                ", mealsArray=" + mealsArray +
                ", foodType=" + foodType +
                ", dietType=" + dietType +
                ", dietCalories=" + dietCalories +
                '}';
    }
}
