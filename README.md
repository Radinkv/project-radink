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

- You can generate the first required action related to the user story "adding multiple Exercises to a Workout" by selecting exercises from the "Available Exercises" list in the **Workout Creation panel** and clicking the **"Add >"** button. You can select a single exercise with a click or multiple exercises by holding CMD/CTRL + Click. When selecting multiple exercises using CMD/CTRL + Click, the exercises will be added to the 'Selected Exercises' list in the order they appear in the Available Exercises column, not in the order you select them. **(1.)**

- You can generate the second required action related to the user story "adding multiple Exercises to a Workout" by selecting exercises from the "Selected Exercises" list in the **Workout Creation panel** and clicking the **"< Remove"** button. This action allows you to refine your workout composition by removing exercises you no longer want included. As with adding, you can select multiple exercises to remove simultaneously by holding CMD/CTRL + Click.

- You can generate a third related action (buffer second required action) to "adding multiple Exercises to a Workout" by using the "Filter by type" dropdown at the top of the panel. This filter action lets you quickly narrow down the available exercises by type (Strength, Endurance, or Interval), which makes it easier to find and select specific exercises to add to your workout.
    ***Note**: You will need to first create Exercises using the Exercise Creation panel, or load a previous state (this project repository includes a previous state with multiple Exercise objects) to utilize the described add/remove/filter functionalities.*


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