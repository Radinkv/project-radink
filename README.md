# Term Project Phase 0: Proposal

## Gym Workout Tracker

### What will the application do?
This application will act as a centralized hub for tracking and interpreting weightlifting and workout information. It will allow users to schedule their workouts on a calendar, estimate their workout duration, track personal records *(PRs)*, and visualize their progress over time. Furthermore, by categorizing workout types *(i.e. dumbbells, barbells, cardio)*, users will be able to analyze the physical balance of their workout routines. The purpose and aim of this tool is to help anyone looking to structure and optimize their gym sessions through better organization and revealed insight.

### Who will use it? 
The application overall targets a broad range of users, from beginners to advanced-level weightlifters and fitness enthusiasts, who are looking to become stronger and healthier. It would ultimately be especially useful to individuals who thrive on visual aids and detailed, informative statistics to fine-tune their workout routines, monitor their longer-term progress, and maintain a balanced fitness regimen.

### Why is this project of interest to me?
I have been weightlifting for a few months and have been looking for a systematic way to consistently remember, keep track of, and employ progressive overload. With this app allowing me to better monitor gains or trends in both weightlifting reps and weight, I can ensure I appropriately introduce progressive load and balance growth across muscle groups. I'm also a visual learner, so having perhaps visual diagrams, charts, and numerical data helps me see at a glance how I'm progressing from a different, tangible perspective. Most importantly, rather than having to store all workout details in my head, this project will let me organize every aspect of my gym routine and both see and access my areas for improvement more clearly and effectively.

## User Stories (PHASE 0)
### As a user, I want to:
- **Create** exercises by specifying their name, equipment, target muscle groups, along with their default sets, reps, weights *(for strength training)* or duration and intervals *(for cardio)*, **so that** I can build a personalized exercise library.
- **Add** exercises from my library to a workout, **so that** I can swiftly assemble customized workouts that match and represent my current fitness level.
- **Estimate** total workout times based on exercises, sets, reps, and rest periods, **so that** I can plan or adjust my gym plan around other commitments.
- **Assign** workouts to calendar dates and view my upcoming plan, **so that** I can maintain consistency in my training regimen.
- **See** time and volume spent per equipment and muscle type *(dumbbells, cardio, arms, etc.)*, **so that** I can balance my routine between different styles of training and volume amongst different muscle groups.


## User Stories SIMPLIFIED (PHASE 1)
### As a user, I want to:
- **Add** multiple exercises (specifying name, type, volume info, equipment, and targeted muscle groups) to my exercise library, **so that** I can build a personalized list of exercises for future workouts.
- **List** all exercises currently in my exercise library, **so that** I can quickly view which exercises I have or don't have available in my library to add to a workout.
- **Create** multiple workouts, each containing a selection of one or more exercises from my library, **so that** I can organize various training routines and easily choose which one to perform.
- **List** all the workouts currently stored in my workout library, **so that** I can keep track of all my different routines and pick one based on my goals.   

#### Extra choices for buffers:
- **Schedule** a singular workout or rest day on different days of a 7-day weekly schedule, **so that** I can plan out and organize my entire week of training in advance and make quick changes if needed.
- **View** my schedule **so that** I can swiftly review which workout or rest day is assigned to each day and plan my day accordingly.
- **Analyze** the usage of different equipment and muscle groups across all my scheduled workouts, **so that** I can quickly identify undertrained or overtrained areas and balance my fitness routine accordingly.  


#### Data Persistence (PHASE 2)
- **Save** my entire workout profile (including exercises, workouts, and weekly schedule) to a file when I *choose* to do so, **so that** I can preserve my training progress and customizations.
- **Load** my previously saved workout profile from a file when I *choose* to do so, **so that** I can continue working with and building my workout program from exactly where I left off.

# Instructions for End User

- You can generate the first required action related to the user story "adding multiple Exercises to a Workout" by selecting exercises from the "Available Exercises" list in the **Workout Creation panel** OR the **Workout Edit panel** and clicking the **"Add >"** button. You can select a single exercise with a click or multiple exercises by holding CMD/CTRL + Click. When selecting multiple exercises using CMD/CTRL + Click, the exercises will be added to the 'Selected Exercises' list in the order they appear in the Available Exercises column, not in the order you select them. **(1.)**

- You can generate the second required action related to the user story "adding multiple Exercises to a Workout" by selecting exercises from the "Selected Exercises" list in the **Workout Creation panel** OR the **Workout Edit panel** and clicking the **"< Remove"** button. This action allows you to refine your workout composition by removing exercises you no longer want included. As with adding, you can select multiple exercises to remove simultaneously by holding CMD/CTRL + Click.

- You can generate a third related action (buffer second required action) to "adding multiple Exercises to a Workout" by using the "Filter by type" dropdown at the top of the panel. This filter action lets you quickly narrow down the available exercises by type (Strength, Endurance, or Interval), which makes it easier to find and select specific exercises to add to your workout.
    ***Note**: You will need to first create Exercises using the Exercise Creation panel, or load a previous state (this project repository includes a previous state with multiple Exercise objects) to utilize the described add/remove/filter functionalities.*

    *Also: you can **edit an existing workout** by selecting it in the Workout Management panel and clicking the "Edit Workout" button. This will open the Workout Edit panel where you can add or remove exercises from the workout. When you're done, click "Save Changes" to update the workout or "Cancel" to discard your changes.*

- You can locate my visual component when starting the application. A splash screen displaying the Workout Planning System logo will appear for approximately 2.5 seconds during launch. This splash screen provides a visual introduction to the application and establishes its identity. (If the logo file cannot be found, a blue circle with "W" will display instead, though this should NOT occur).

- You can save the state of my application by either:
  1. Clicking the close (X) button on the application window, which will prompt "Would you like to save your progress before exiting?"—Select "Yes" to save.
  2. Clicking the "Save Program State" button from the Main Menu, which will prompt "Are you sure you want to save the current program state? This will override any previous save."—Select "Yes" to confirm.

  ***Note**: Clicking the "Exit" button on the Main Menu will exit without prompting to save.*

- You can reload the state of my application by clicking the "Load Program State" button from the Main Menu. A confirmation dialog will ask "Are you sure you want to load the previous program state? This will replace all current data." Select "Yes" to load your previously saved exercises and workouts (no schedule for GUI).

**(1.)** When you select exercises from the list, they will automatically be arranged in numerical order in the "Selected Exercises" panel, regardless of the order in which you select them.
For example, if exercises are consecutively numbered 1-5 top-to-bottom, and say you select them in the order 1, 5, 4, 3, 2, they will appear as 1, 2, 3, 4, 5 in the Selected Exercises panel. Any additional exercises you select will be appended at the end of this ordered list. To customize ordering specifically, add exercises one-by-one as specified. This same numerical ordering applies when viewing exercises within a Workout, whether you are: Creating a new workout by selecting a name and exercises from the "Selected Exercises" panel, **OR** Viewing workout details after creation (by selecting a workout in the "Available Workouts" panel and clicking "View Details"). Further note that the GUI prevents duplicate exercises from being added to the same workout without displaying any error messages.

# Source Attribution

Splash Screen Image Source: *https://www.gokenko.com/articles/7-gym-logo-ideas-to-inspire-you-for-your-own*

# Phase 4: Task 2

```text
Event Log:
----------
Thu Mar 27 17:35:58 PDT 2025
WeeklySchedule initialized with default rest days
----------
Thu Mar 27 17:36:10 PDT 2025
Exercise created: Exercise1 (Type: Strength)
----------
Thu Mar 27 17:36:10 PDT 2025
Exercise 'Exercise1' added to ExerciseLibrary
----------
Thu Mar 27 17:36:17 PDT 2025
Exercise created: Exercise2 (Type: Endurance)
----------
Thu Mar 27 17:36:17 PDT 2025
Exercise 'Exercise2' added to ExerciseLibrary
----------
Thu Mar 27 17:36:27 PDT 2025
Exercise created: Exercise3 (Type: Interval)
----------
Thu Mar 27 17:36:27 PDT 2025
Exercise 'Exercise3' added to ExerciseLibrary
----------
Thu Mar 27 17:36:31 PDT 2025
Exercise created: Exercise4 (Type: Strength)
----------
Thu Mar 27 17:36:31 PDT 2025
Exercise 'Exercise4' added to ExerciseLibrary
----------
Thu Mar 27 17:36:41 PDT 2025
Exercise 'Exercise2' removed from ExerciseLibrary
----------
Thu Mar 27 17:36:52 PDT 2025
Workout created: Workout1 with 2 exercises
----------
Thu Mar 27 17:36:52 PDT 2025
Workout 'Workout1' added to WorkoutLibrary
----------
Thu Mar 27 17:37:01 PDT 2025
Workout 'Workout1' exercises updated to 3 exercises: Exercise1, Exercise4, Exercise3
----------
Thu Mar 27 17:37:15 PDT 2025
Workout created: Workout2 with 1 exercises
----------
Thu Mar 27 17:37:15 PDT 2025
Workout 'Workout2' added to WorkoutLibrary
----------
Thu Mar 27 17:37:23 PDT 2025
Workout 'Workout2' removed from WorkoutLibrary
----------
Thu Mar 27 17:37:27 PDT 2025
ExerciseLibrary serialized to JSON
----------
Thu Mar 27 17:37:27 PDT 2025
WorkoutLibrary serialized to JSON
----------
Thu Mar 27 17:37:27 PDT 2025
WeeklySchedule serialized to JSON
----------
Thu Mar 27 17:37:30 PDT 2025
Exercise created: Exercise1 (Type: Strength)
----------
Thu Mar 27 17:37:30 PDT 2025
Exercise created: Exercise3 (Type: Interval)
----------
Thu Mar 27 17:37:30 PDT 2025
Exercise created: Exercise4 (Type: Strength)
----------
Thu Mar 27 17:37:30 PDT 2025
ExerciseLibrary deserialized from JSON with 3 exercises
----------
Thu Mar 27 17:37:30 PDT 2025
Workout created: Workout1 with 3 exercises
----------
Thu Mar 27 17:37:30 PDT 2025
WorkoutLibrary deserialized from JSON with 1 workouts
----------
Thu Mar 27 17:37:30 PDT 2025
WeeklySchedule deserialized from JSON
----------
```

Another Example (Abundant Deleting)

```text
Event Log:
----------
Thu Mar 27 22:01:58 PDT 2025
WeeklySchedule initialized with default rest days
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Ab Rollers (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Inward Calf Raises (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Weighted Crunches (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Cable Lat PullDown (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Cable Chest Flys (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Barbell Shoulder Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Basketball (Type: Endurance)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Outward Calf Raises (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Reverse Grip Curls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Deadlift (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Overhead Dumbbell Tricep Push (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Wide Grip Curls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Wide Grip Tricep Pushdown (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Narrow Grip Tricep Pushdown (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Hamstring Falls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Close Grip Curls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Reverse Grip Face Pulls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Incline Chest Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Weighted Leg Raises (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Wide-Grip Pull Ups (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Cable Hammer Curls (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Flat Bench Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Wide Stance Leg Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Closed Stance Leg Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Single-leg Leg Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Russian Twists (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Dumbbell Shoulder Press (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Cable Close-Grip Rows (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
Exercise created: Cable Row (Type: Strength)
----------
Thu Mar 27 22:02:08 PDT 2025
ExerciseLibrary deserialized from JSON with 29 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: TestWorkoutForPhaseIII with 7 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: TestWorkout with 29 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Legs with 4 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Abs with 4 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Chest and Biceps with 6 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Shoulders and Triceps with 7 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Hamstrings and Calves with 3 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Back and Forearms with 5 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
Workout created: Third Test Workout with PHASE III GUI with 1 exercises
----------
Thu Mar 27 22:02:08 PDT 2025
WorkoutLibrary deserialized from JSON with 10 workouts
----------
Thu Mar 27 22:02:08 PDT 2025
WeeklySchedule deserialized from JSON
----------
Thu Mar 27 22:02:18 PDT 2025
Schedule cleared for Wednesday (set to Rest Day)
----------
Thu Mar 27 22:02:18 PDT 2025
Workout 'Rest Day' removed from WorkoutLibrary
----------
Thu Mar 27 22:02:46 PDT 2025
Workout 'Third Test Workout with PHASE III GUI' removed from WorkoutLibrary
----------
Thu Mar 27 22:03:03 PDT 2025
Workout 'TestWorkout' removed from WorkoutLibrary
----------
Thu Mar 27 22:03:13 PDT 2025
Workout 'TestWorkoutForPhaseIII' exercises updated to 8 exercises: Inward Calf Raises, Cable Lat PullDown, Barbell Shoulder Press, Outward Calf Raises, Deadlift, Wide Grip Tricep Pushdown, Hamstring Falls, Basketball
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'TestWorkoutForPhaseIII'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Legs'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Abs'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Chest and Biceps'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Shoulders and Triceps'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Hamstrings and Calves'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from workout 'Back and Forearms'
----------
Thu Mar 27 22:03:21 PDT 2025
Exercise 'Cable Row' removed from ExerciseLibrary
----------
```

# Phase 4: Task 3

After I reviewed my UML class diagram, I noticed a handful of design choices I made for this program that aren't visibly represented in the diagram, which reveals hidden coupling to improve upon in my implementation. Most noticeably, RestDay implements WorkoutPlan but violates the Liskov Substitution Principle by which it returns empty ArrayLists for getExercises() and zero for getDuration(). This forced me to write UI code that checks if a WorkoutPlan is a Workout or RestDay, which defeats polymorphism. While the UML shows the inheritance structure, it fails to capture this behavioral incompatibility. I would refactor this issue by creating separate interfaces: an ExerciseContainer for Workout, and a ScheduleItem interface for both classes. Furthermore, my ExerciseAssociator class currently implements an Observer pattern informally, where Equipment and Muscle track metrics from associated exercises through manual registerExercise() method calls. Similarly, this important relationship is completely absent from the UML diagram. To improve upon this design choice, I would make Exercise a proper Subject with notify methods and have Equipment and Muscle implement an Observer interface, which would allow them to automatically update when metrics change, which also would make this pattern explicit in both code and my UML diagram. Finally, the dependency between WorkoutLibrary and WeeklySchedule, and Exercise to ExerciseLibrary and Workout is invisible in the UML diagram. When I remove a Workout from WorkoutLibrary in my UI, I must manually check if it exists in WeeklySchedule. Similarly, when I remove an Exercise from ExerciseLibrary, I must manually check if it exists in any Workout in WorkoutLibrary. If I were to apply the Observer pattern here, it would trigger automatic updates between these components without manual dependency checks and would clarify this relationship in the UML diagram design.

The relationship between MuscleGroup and Muscle also presents problems that the UML fails to illustrate—"Quadriceps" appears both as a Muscle and MuscleGroup, and muscles like "Lower Back" appear in multiple MuscleGroups. My current implementation uses a simple container model where MuscleGroup acts like a folder that can only contain Muscle objects, but not other MuscleGroups. This doesn't accurately represent the natural hierarchical structure of the muscular system, where a muscle group can contain both individual muscles and smaller muscle groups (like how the "Legs" might contain both a groin muscle and the "Quadriceps" group). This trivial detail results in huge differences in aggregated metrics across muscle groups and muscles. If I implemented the Composite pattern, both Muscle and MuscleGroup would implement a common Component interface with shared operations. This enhancement would allow me to create a more accurate representation of these complex relationships, where muscle components could be managed and aggregated for metrics automatically (with the ExerciseAssociator improvement explained) at arbitrary depths, while still maintaining consistent behavior regardless of whether an operation is performed on a single muscle or an entire group of muscles. Furthermore, while I believe I have done a relatively good job on minimizing code duplication through interfaces and abstractions, the UI components ExerciseManagementPanel and WorkoutManagementPanel have nearly identical methods like createExerciseList/createWorkoutList and createDetailsPanel. Both also follow the same layout with a list on the left, details on the right, and action buttons at the bottom. However, the UML class diagram does not show or indicate this behavioral duplication coupling. So, if I had time to create an abstract EntityManagementPanel superclass, I would extract this common structure into an abstract class (like WorkoutEditPanel). Such a change will positively benefit with creating management panels for more complexity introduced in the model package, and will enhance the detail in the UML diagram-depicted class hierarchy regarding how the program works.
