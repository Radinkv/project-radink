package model.info;

import java.util.Date;

/*
 * This class is an immutable object, representing a physical profile entry for a user who uses upcoming  
 * implementations and features of this program.
 * 
 * While this object cannot be updated, UI will make it easy to add or 'update' new entries; merely tweaking limited
 * physical profile fields, in the future UI, will automatively instantiate a new PhysicalProfileEntry object with that
 * given change added. The plan is that these objects are added and cumulated onto a calendar, displayed to the user
 * via a chart to show the user their phsyical progress across these categories over time. 
 */
public class PhysicalProfileEntry {

    // EFFECTS: Create an instance this person's physical profile.
    public PhysicalProfileEntry(Date entryDate, int age, double height, double weight, double bodyFatPercentage) {
        // stub
    }

    public Date getEntryDate() {
        return new Date(); // stub
    }

    public int getAge() {
        return 0; // stub
    }

    public double getHeight() {
        return 0.0; // stub
    }

    public double getWeight() {
        return 0.0; // stub
    }

    public double getBodyFatPercentage() {
        return 0.0; // stub
    }
}
