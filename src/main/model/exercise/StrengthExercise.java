package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;
import model.muscle.MuscleGroup;

public class StrengthExercise extends Exercise {

    // REQUIRES: 
    /* EFFECTS: Create an instance of a strength/hypertrophy exercise, initializing:
                1. This exercise's name
                2. Number of sets and reps
                3, 4. Time per rep (seconds) and rest time (minutes)
                5. The equipment used for this exercise
                6. Targeted muscle group(s) 
                IF ANY OF sets, reps, numSecondsPerRep < 0, they are set to 0, respectively. */
    public StrengthExercise(String name, int sets, int reps, double numSecondsPerRep, 
                            double restTime, Equipment equipmentUsed, MuscleGroup musclesTargeted) {

    }

    // MODIFIES: this
    // EFFECTS: Change the sets of this strength exercise by sets and return true. However, if sets < 0,
    //          return false and make no changes. 
    public boolean modifySets(int sets) {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Change the reps of this strength exercise by sets and return true. However, if reps < 0,
    //          return false and make no changes. 
    public boolean modifyReps(int reps) {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Change the rest time of this strength exercise by sets and return true. However, if seconds < 0,
    //          return false and make no changes. 
    public boolean modifyRestTime(int seconds) {
        return false;
    }

    // EFFECTS: Calculate and return this exercise's total duration, rounded to the nearest minute.
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0);
    }
    
    // EFFECTS: Return 'Interval' as this exercise's type
    @Override
    public String exerciseType() {
        return "";
    }

    // EFFECTS: Return the equipment this endurance exercise targets
    @Override
    public Equipment getRequiredEquipment() {
        return new Treadmill(); // stub
    }

    // EFFECTS: Return the group of muscles this endurance exercise targets
    @Override
    public MuscleGroup getMusclesTargeted() {
        return new MuscleGroup(); // stub
    }

    /* EFFECTS: Return four key-value pairs for information about this exercise: 
             1, 2. 'Sets' and 'Reps' (number of repetitions) 
             3. 'Rest time' (duration in seconds)
             4. 'Time per rep' (duration in seconds) */
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
