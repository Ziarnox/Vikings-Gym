package com.example.elgrande.service;

import com.example.elgrande.forms.UserForm;
import com.example.elgrande.model.diet.Diet;
import com.example.elgrande.model.diet.Meal;
import com.example.elgrande.model.enums.Level;
import com.example.elgrande.model.enums.enums_diet.Allergy;
import com.example.elgrande.model.enums.enums_diet.DietType;
import com.example.elgrande.model.enums.enums_training.Body;
import com.example.elgrande.model.training.Exercise;
import com.example.elgrande.model.training.Training;
import com.example.elgrande.model.user.UserEntity;
import com.example.elgrande.service.diet_service.DietService;
import com.example.elgrande.service.training_service.ExerciseService;
import com.example.elgrande.service.training_service.TrainingService;
import com.example.elgrande.service.user_service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MainService {
    private ExerciseService exerciseService;
    private TrainingService trainingService;
    private UserService userService;
    private DietService dietService;
    Random rand = new Random();


    @Autowired
    public MainService(ExerciseService exerciseService, TrainingService trainingService, UserService userService, DietService dietService) {
        this.exerciseService = exerciseService;
        this.trainingService = trainingService;
        this.userService = userService;
        this.dietService = dietService;
    }


    public void setUserTrainingInfo(UserForm userForm, int id) {
        UserEntity user = userService.getUserById(id);
        user.setWeight(userForm.weight());
        user.setAge(userForm.age());
        user.setHeight(userForm.height());
        user.setGender(userForm.gender());
        user.setAllergies(userForm.allergies());

        double bmi = user.getBMI();
        int weeklyTrainingSessions = userForm.amountOfTrainingsPerWeek();


        if (bmi < 18.5) {
            user.setLevel(Level.BEGINNER);
        } else if (bmi >= 18.5 && bmi < 30) {
            if (user.getAge() < 16) {
                user.setLevel(Level.BEGINNER);
            } else if (user.getAge() >= 16 && user.getAge() < 50) {
                if (user.getGender().equals("Male")) {
                    if (weeklyTrainingSessions < 2) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 2 && weeklyTrainingSessions < 3) {
                        user.setLevel(Level.INTERMEDIATE);
                    } else if (weeklyTrainingSessions >= 3) {
                        user.setLevel(Level.PROFESSIONAL);
                    }
                } else if (user.getGender().equals("Female")) {
                    if (weeklyTrainingSessions < 3) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 3 && weeklyTrainingSessions < 5) {
                        user.setLevel(Level.INTERMEDIATE);
                    } else if (weeklyTrainingSessions >= 5) {
                        user.setLevel(Level.PROFESSIONAL);
                    }
                }
            } else if (user.getAge() >= 50) {
                if (user.getGender().equals("Male")) {
                    if (weeklyTrainingSessions < 3) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 3) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                } else if (user.getGender().equals("Female")) {
                    if (weeklyTrainingSessions < 4) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 4) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                }
            }
        } else if (bmi >= 30) {
            if (user.getAge() < 16) {
                user.setLevel(Level.BEGINNER);
            } else if (user.getAge() >= 16 && user.getAge() < 50) {
                if (user.getGender().equals("Male")) {
                    if (weeklyTrainingSessions < 2) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 2) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                } else if (user.getGender().equals("Female")) {
                    if (weeklyTrainingSessions < 3) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 3) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                }
            } else if (user.getAge() >= 50) {
                if (user.getGender().equals("Male")) {
                    if (weeklyTrainingSessions < 3) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 3) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                } else if (user.getGender().equals("Female")) {
                    if (weeklyTrainingSessions < 4) {
                        user.setLevel(Level.BEGINNER);
                    } else if (weeklyTrainingSessions >= 4) {
                        user.setLevel(Level.INTERMEDIATE);
                    }
                }
            }
        }
        user.setTrainings(trainingService.prepareTrainings(trainingService.getTrainingsByLevel(user.getLevel()), 3));
        userService.saveUser(user);
    }




    public Level getNextLevel(Level currentLevel) {
        switch (currentLevel) {
            case BEGINNER:
                return Level.INTERMEDIATE;
            case INTERMEDIATE:
                return Level.PROFESSIONAL;
            case PROFESSIONAL:
                return Level.ELITE;
            case ELITE:
                return Level.EXPERT;
            case EXPERT:
                return null;
            default:
                return null;
        }
    }


    public double getIncreaseRate(Level currentLevel) {
        switch (currentLevel) {
            case BEGINNER:
                return 1;
            case INTERMEDIATE:
                return 2;
            case PROFESSIONAL:
                return 2.5;
            case ELITE:
                return 3.5;
            case EXPERT:
                return 1;
            default:
                return 1;
        }
    }


    public Training getTrainingFormUser(int trainingIndex, int userId){
        UserEntity user = userService.getUserById(userId);
        Training training = user.getTrainings().get(trainingIndex - 1);
        return training;
    }


    public Training getNextTrainingFromUser(UserEntity user){
        List<Training> userTrainings = user.getTrainings();
        for(int i =0;i<userTrainings.size();i++){
            int index = user.getAmountOfTrainingsDone() % user.getTrainingsPerWeek();
            if(index == i){
                return userTrainings.get(index);
            }else {
                continue;
            }
        }
        return null;
    }


    public UserEntity getPropperUser(int id, int amountOfTrainingsToChange, double addedWeight){
        UserEntity user = userService.getUserById(id);

        List<Training> UserTrainings = user.getTrainings();

        if((user.getAmountOfTrainingsDone() % (amountOfTrainingsToChange * getIncreaseRate(user.getLevel()))) == 0 && user.getAmountOfTrainingsDone() != 0){
            int amountOfTimesToMultiply = user.getTimesToMultiply();
            user.setTimesToMultiply(amountOfTimesToMultiply + 1);
            userService.saveUser(user);
        }
        user.setTrainings(trainingService.increaseExercises(addedWeight * user.getTimesToMultiply(),UserTrainings));
        return user;
    }

    public void updateTrainingPlan(int id , int amountOfTrainingsToChangeLevel){
        UserEntity user = userService.getUserById(id);

        if(user.getAmountOfTrainingsDone() % (amountOfTrainingsToChangeLevel * getIncreaseRate(user.getLevel())) == 0){

            Level previousLevel = user.getLevel();
            List<Training> updatedtrainings = trainingService.getTrainingsByLevel(getNextLevel(previousLevel));

            List<Training> trainingsToSet = trainingService.prepareTrainings(updatedtrainings,user.getTrainingsPerWeek());

            user.setTrainings(trainingsToSet);
            user.setLevel(getNextLevel(previousLevel));
        }
        userService.saveUser(user);
    }


    public void updateFirstPlan(int userId){
        UserEntity user = userService.getUserById(userId);

        List<Training> updatedtrainings = trainingService.getTrainingsByLevel(user.getLevel());

        List<Training> trainingsToSet = trainingService.prepareTrainings(updatedtrainings,user.getTrainingsPerWeek());

        user.setTrainings(trainingsToSet);
        userService.saveUser(user);
    }


    public void deleteTrainingFromUser(int trainingid, int userid){
        UserEntity user = userService.getUserById(userid);
        user.getTrainings().remove(trainingid -1);
        userService.saveUser(user);
    }



//Diets diets diets diets diets
    public List<Diet> getDietsFormUser(int userId) {
        UserEntity user = userService.getUserById(userId);
        List<Diet> diets = user.getDiets();
        if(diets.isEmpty()) {
            List<Diet> empty = new ArrayList<>();
            return empty;
        }
        return diets;
    }
    public void randomizeMeals(int userId) {
        UserEntity user = userService.getUserById(userId);
        Diet diet = user.getDiet();
        List<Meal> currentMealsArray = diet.getMealsArray();
        List<Meal> randomizedMealsArray = new ArrayList<>();
        int indexToMove = 0;

        while(!currentMealsArray.isEmpty()) {
            indexToMove = rand.nextInt(currentMealsArray.size());
            randomizedMealsArray.add(currentMealsArray.get(indexToMove));
            currentMealsArray.remove(indexToMove);
        }

        diet.setMeals(randomizedMealsArray);
    }
    public Meal getNextMealFromUserDiet(int userId) {
        UserEntity user = userService.getUserById(userId);
//        if(user.getLastUpdatedDate() + 7days > new Date()) {
//            randomizeMeals(userId);
//            user = userService.getUserById(userId);
//        }
        Diet diet = user.getDiet();

        List<Meal> mealsArray = diet.getMealsArray();

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int mealIndex = dayOfWeek % mealsArray.size();

//        user.setDietUpdateDate(new Date());
        return mealsArray.get(mealIndex);
    }

    public List<Diet> suggestDiet(int userId) {
        List<Diet> diets = new ArrayList<>();
        UserEntity user = userService.getUserById(userId);
        System.out.println(user);

        //Getting all required user info;
        String gender = user.getGender();
        int weight = user.getWeight();
        int height = user.getHeight();
        int age = user.getAge();
        int amountOfTrainingsPerWeek = user.getTrainingsPerWeek();
        DietType dietType = user.getDietType();
        List<Allergy> userAllergies = user.getAllergies();

        //Calculating Daily Calorie Intake
        int dailyKcal = 0;
        dailyKcal = (int) dietService.calculateCalorieIntake(gender, weight, height, age, amountOfTrainingsPerWeek, dietType);

        //Searching for suiting diets;
        List<Diet> foundDiets = dietService.filterDiets("", dailyKcal, user.getFoodType());

        //Adding 3* of the foundDiets to diets list;
        if(foundDiets.isEmpty()) {
            throw new NoSuchElementException("FoundDiets list should not be empty");
        } else if(foundDiets.size() <= 3){
            for(Diet diet : foundDiets) {
                diets.add(diet);
            }
        } else {
            for(int i = 0; i < 3; i++) {
                diets.add(foundDiets.get(i));
            }
        }

        return diets;
    }

    public void setDiet(int userId, int dietId) {
        UserEntity user = userService.getUserById(userId);
        Diet diet = dietService.getDietById(dietId);

        user.setDiet(diet);
    }


}