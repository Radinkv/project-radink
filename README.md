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

- You can generate the first required action related to the user story "adding multiple Exercises to a Workout" by clicking the "Add >" button in the Create Workout screen. This allows you to add selected exercises from your exercise library to your current workout.

- You can generate the second required action related to the user story "adding multiple Exercises to a Workout" by either clicking the "< Remove" button to remove selected exercises from your workout, clicking the "Filter by Type" button to filter available exercises by their type (Strength, Endurance, or Interval), or clicking the "Sort by Name" button to alphabetically sort the selected exercises in your workout.

- You can locate my visual component by launching the application and viewing the application splash screen. There should be an image that is displayed for a few seconds as the program initially becomes launched. 

**ADDITIONAL OPTION** Creating a workout or viewing an existing workout's details. After creating a workout, a summary dialog will appear displaying a pie chart visualization of the exercise type distribution in your workout. In workout management, you can also click the "View with Chart" button to see this visualization for any workout. Additionally, pie charts available in the metrics section to view equipment and muscle usage analytics.

- You can save the state of my application by clicking the "Save Program State" button in the main menu, or by clicking "Yes" when prompted to save upon exiting the application.

- You can reload the state of my application from a previously saved state by clicking the "Load Program State" button in the main menu. This will restore all the previously saved exercises and workouts.


Splash Screen Image Source: https://www.gokenko.com/articles/7-gym-logo-ideas-to-inspire-you-for-your-own 