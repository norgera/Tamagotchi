package logic;

import java.io.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.time.*;

/**
 * The logic.ParentalControls class provides methods to enable or disable various types
 * of parental control restrictions such as daily or weekly playtime limits,
 * time of day restrictions, and limiting playable days.
 */
public class ParentalControls {

    /**
     * Flag indicating whether daily playable time restrictions are enabled.
     */
    private boolean dailyPlayableTimeEnabled = false;

    /**
     * Flag indicating whether a daily time limit for gameplay is enabled.
     */
    public boolean dailyTimeLimitEnabled = false;

    /**
     * Flag indicating whether a weekly time limit for gameplay is enabled.
     */
    public boolean weeklyTimeLimitEnabled = false;

    /**
     * Flag indicating whether gameplay is restricted to specific days of the week.
     */
    private boolean limitPlayableDaysEnabled = false;

    /**
     * Dictionary that maps days of the week (e.g., "monday", "tuesday") to
     * boolean values indicating whether gameplay is allowed on those days.
     */
    public Dictionary<String, Boolean> limitedDays = new Hashtable<>();

    /**
     * The start time of the daily playable period, represented as a {@code LocalTime}.
     * Defaults to midnight.
     */
    private LocalTime limitTimeOfDayStart = LocalTime.MIDNIGHT;

    /**
     * The end time of the daily playable period, represented as a {@code LocalTime}.
     * Defaults to midnight.
     */
    private LocalTime limitTimeOfDayEnd = LocalTime.MIDNIGHT;

    /**
     * The total allowable playtime per day, represented as a {@code Duration}.
     * Defaults to zero seconds.
     */
    public Duration limitDailyPlaytime = Duration.ofSeconds(0);

    /**
     * The total allowable playtime per week, represented as a {@code Duration}.
     * Defaults to zero seconds.
     */
    public Duration limitWeeklyPlaytime = Duration.ZERO;

    /**
     * The total amount of playtime accumulated during the current day, represented as a {@code Duration}.
     * Defaults to zero seconds.
     */
    private Duration currentDailyPlaytime = Duration.ZERO;

    /**
     * The total amount of playtime accumulated during the current week, represented as a {@code Duration}.
     * Defaults to zero seconds.
     */
    private Duration currentWeeklyPlaytime = Duration.ZERO;

    /**
     * The total amount of playtime across all sessions, represented as a {@code Duration}.
     * Defaults to zero seconds.
     */
    private Duration totalPlaytime = Duration.ZERO;

    /**
     * The total number of gameplay sessions that have occurred.
     * Defaults to zero.
     */
    private int sessionCount = 0;

    /**
     * The timestamp representing the start time of the current gameplay session,
     * represented as an {@code Instant}. Defaults to {@code null} until a session begins.
     */
    private Instant sessionStartTime;



    /**
     * Constructor that loads the state of logic.ParentalControls from a CSV file if it exists and is not empty.
     * Otherwise, it uses default values and sets the restriction for each day of the week to false.
     *
     * @param filename The name of the CSV file.
     */
    public ParentalControls(String filename) {
        startTracking();
        File file = new File(filename);
        if (file.exists() && file.length() > 0) {
            loadFromCSV(filename);
        } else {
            limitedDays.put("monday", false);
            limitedDays.put("tuesday", false);
            limitedDays.put("wednesday", false);
            limitedDays.put("thursday", false);
            limitedDays.put("friday", false);
            limitedDays.put("saturday", false);
            limitedDays.put("sunday", false);
        }
    }

    /**
     * Starts tracking the session duration.
     */
    public void startTracking() {
        sessionStartTime = Instant.now();
    }

    /**
     * Stops tracking the session duration and updates current playtime and total playtime.
     */
    public void stopTracking() {
        if (sessionStartTime != null) {
            Duration sessionDuration = Duration.between(sessionStartTime, Instant.now());
            currentDailyPlaytime = currentDailyPlaytime.plus(sessionDuration);
            currentWeeklyPlaytime = currentWeeklyPlaytime.plus(sessionDuration);
            totalPlaytime = totalPlaytime.plus(sessionDuration);
            sessionCount++;
            sessionStartTime = null; // Reset session start time
        }
    }

    /**
     * Gets the total amount of time played.
     *
     * @return The total playtime.
     */
    public Duration calculateTotalPlaytime() {
        return totalPlaytime;
    }

    /**
     * Returns the total amount of time played as a formatted string
     *
     * @return A string that represents the total playtime.
     */
    public String getTotalPlaytime() {
        return String.format("%02d:%02d.%02d", totalPlaytime.toHours(), totalPlaytime.toMinutesPart(), totalPlaytime.toSecondsPart());
    }

    /**
     * Gets the average session length.
     *
     * @return The average session length as a Duration object. Returns Duration.ZERO if no sessions have occurred.
     */
    public Duration calculateAverageSessionLength() {
        if (sessionCount == 0) {
            return Duration.ZERO;
        }
        return totalPlaytime.dividedBy(sessionCount);
    }

    /**
     * Prints the average session length as a properly formatted string
     *
     * @return The average session length as a String object. Returns 0:00 if no sessions have occurred.
     */
    public String returnAverageSessionLength() {
        if (sessionCount == 0) {
            return "00:00.00";
        }
        Duration sessionLength = calculateAverageSessionLength();
        return String.format("%02d:%02d.%02d", sessionLength.toHours(), sessionLength.toMinutesPart(), sessionLength.toSecondsPart());
    }

    /**
     * Saves the current state of logic.ParentalControls to a CSV file.
     *
     * @param filename The name of the CSV file.
     */
    public void saveToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Save boolean flags
            writer.println(dailyPlayableTimeEnabled + "," + dailyTimeLimitEnabled + "," + weeklyTimeLimitEnabled + "," + limitPlayableDaysEnabled);

            // Save limited days (monday, tuesday, etc.)
            Enumeration<String> keys = limitedDays.keys();
            while (keys.hasMoreElements()) {
                String day = keys.nextElement();
                writer.println(day + "," + limitedDays.get(day));
            }

            // Save time limits
            writer.println((limitTimeOfDayStart != null ? limitTimeOfDayStart.toString() : "") + "," +
                    (limitTimeOfDayEnd != null ? limitTimeOfDayEnd.toString() : ""));
            writer.println(limitDailyPlaytime.toHours() + ":" + limitDailyPlaytime.toMinutesPart());
            writer.println(limitWeeklyPlaytime.toHours() + ":" + limitWeeklyPlaytime.toMinutesPart());

            // Save current daily and weekly playtime
            writer.println(currentDailyPlaytime.toHours() + ":" + currentDailyPlaytime.toMinutesPart() + "." + currentDailyPlaytime.toSecondsPart());
            writer.println(currentWeeklyPlaytime.toHours() + ":" + currentWeeklyPlaytime.toMinutesPart() + "." + currentWeeklyPlaytime.toSecondsPart());

            // Save total playtime and session count
            writer.println(totalPlaytime.toHours() + ":" + totalPlaytime.toMinutesPart() + "." + totalPlaytime.toSecondsPart());
            writer.println(sessionCount);
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }


    /**
     * Loads the logic.ParentalControls state from a CSV file.
     *
     * @param filename The name of the CSV file.
     */
    public void loadFromCSV(String filename) {
        System.out.println("Reading csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Load boolean flags
            String line = reader.readLine();
            String[] flags = line.split(",");
            dailyPlayableTimeEnabled = Boolean.parseBoolean(flags[0]);
            dailyTimeLimitEnabled = Boolean.parseBoolean(flags[1]);
            weeklyTimeLimitEnabled = Boolean.parseBoolean(flags[2]);
            limitPlayableDaysEnabled = Boolean.parseBoolean(flags[3]);

            // Load limited days (monday, tuesday, etc.)
            for (int i = 0; i < 7; i++) {
                line = reader.readLine();
                String[] dayData = line.split(",");
                limitedDays.put(dayData[0], Boolean.parseBoolean(dayData[1]));
            }

            // Load time limits
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] timeLimits = line.split(",");
                if (timeLimits.length == 2) {
                    limitTimeOfDayStart = timeLimits[0].isEmpty() ? null : LocalTime.parse(timeLimits[0]);
                    limitTimeOfDayEnd = timeLimits[1].isEmpty() ? null : LocalTime.parse(timeLimits[1]);
                }
            }

            // Load daily and weekly playtime limits
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] dailyLimit = line.split(":");
                limitDailyPlaytime = Duration.ofHours(Long.parseLong(dailyLimit[0]))
                        .plusMinutes(Long.parseLong(dailyLimit[1]));
            }

            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] weeklyLimit = line.split(":");
                limitWeeklyPlaytime = Duration.ofHours(Long.parseLong(weeklyLimit[0]))
                        .plusMinutes(Long.parseLong(weeklyLimit[1]));
            }

            // Load current daily playtime
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] currentDaily = line.split(":|\\.");
                currentDailyPlaytime = Duration.ofHours(Long.parseLong(currentDaily[0]))
                        .plusMinutes(Long.parseLong(currentDaily[1]))
                        .plusSeconds(Long.parseLong(currentDaily[2]));
            }

            // Load current weekly playtime
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] currentWeekly = line.split("[:|\\.]");
                currentWeeklyPlaytime = Duration.ofHours(Long.parseLong(currentWeekly[0]))
                        .plusMinutes(Long.parseLong(currentWeekly[1]))
                        .plusSeconds(Long.parseLong(currentWeekly[2]));
            }


            // Load total playtime
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] totalTime = line.split(":|\\.");
                totalPlaytime = Duration.ofHours(Long.parseLong(totalTime[0]))
                        .plusMinutes(Long.parseLong(totalTime[1]))
                        .plusSeconds(Long.parseLong(totalTime[2]));
            }

            // Load session count
            line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                sessionCount = Integer.parseInt(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading from CSV: " + e.getMessage());
        }
    }


    // Existing methods for enabling/disabling restrictions, checking playability, etc.

    /**
     * Enables daily playable time restriction.
     */
    public void enableDailyPlayableTime() {
        dailyPlayableTimeEnabled = true;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Disables daily playable time restriction.
     */
    public void disableDailyPlayableTime() {
        dailyPlayableTimeEnabled = false;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Enables daily playtime limit.
     */
    public void enableDailyTimeLimit() {
        dailyTimeLimitEnabled = true;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Disables daily playtime limit.
     */
    public void disableDailyTimeLimit() {
        dailyTimeLimitEnabled = false;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Enables weekly playtime limit.
     */
    public void enableWeeklyTimeLimit() {
        weeklyTimeLimitEnabled = true;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Disables weekly playtime limit.
     */
    public void disableWeeklyTimeLimit() {
        weeklyTimeLimitEnabled = false;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Enables limits on playable days.
     */
    public void enableLimitPlayableDays() {
        limitPlayableDaysEnabled = true;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    public boolean isDailyTimeLimitEnabled() {
        System.out.println("Daily Limit Enabled: " + dailyTimeLimitEnabled);
        return dailyTimeLimitEnabled;
    }

    public boolean isWeeklyTimeLimitEnabled() {
        System.out.println("Weekly Limit Enabled: " + weeklyTimeLimitEnabled);
        return weeklyTimeLimitEnabled;
    }

    /**
     * Disables limits on playable days.
     */
    public void disableLimitPlayableDays() {
        limitPlayableDaysEnabled = false;
        saveToCSV("files/parental_controls.csv"); // Save the state immediately
    }

    /**
     * Sets the time of day limit for gameplay.
     *
     * @param startTime The start time in HH:mm format.
     * @param endTime The end time in HH:mm format.
     */
    public void setTimeOfDayLimit(String startTime, String endTime) {
        limitTimeOfDayStart = LocalTime.parse(startTime);
        limitTimeOfDayEnd = LocalTime.parse(endTime);
    }


    /**
     *
     * @return the LocalTime object when the limited period starts
     */
    public LocalTime getLimitTimeOfDayStart() {
        return limitTimeOfDayStart;
    }


    /**
     *
     * @return the LocalTime object when the limited period ends
     */
    public LocalTime getLimitTimeOfDayEnd() {
        return limitTimeOfDayEnd;
    }


    /**
     * Checks if the current time is within the limited playable time of day.
     *
     * @return true if the current time is between the start and end time of the playable period, otherwise false.
     */
    public boolean checkTimeOfDayLimit() {
        LocalTime currentTime = LocalTime.now();
        return (currentTime.isAfter(limitTimeOfDayStart) && currentTime.isBefore(limitTimeOfDayEnd));
    }

    /**
     * Sets the total daily playtime limit.
     *
     * @param time The daily playtime limit in the format HH:mm.
     */
    public void setTotalDailyPlaytimeLimit(String time) {
        String[] parts = time.split(":"); // Split the string into hours and minutes
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        limitDailyPlaytime = Duration.ofHours(hours).plusMinutes(minutes); // Create the Duration object
    }

    /**
     * Sets the total weekly playtime limit.
     *
     * @param time The weekly playtime limit in the format HH:mm.
     */
    public void setTotalWeeklyPlaytimeLimit(String time) {
        String[] parts = time.split(":"); // Split the string into hours and minutes
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        limitWeeklyPlaytime = Duration.ofHours(hours).plusMinutes(minutes); // Create the Duration object
    }

    /**
     * Enables the playtime limit for a specific day.
     *
     * @param day The day to limit, such as "monday", "tuesday", etc.
     */
    public void enableLimitOnDay(String day) {
        day = day.toLowerCase().strip();
        limitedDays.put(day, true);
    }

    /**
     * Disables the playtime limit for a specific day.
     *
     * @param day The day to remove the limit, such as "monday", "tuesday", etc.
     */
    public void disableLimitOnDay(String day) {
        day = day.toLowerCase().strip();
        limitedDays.put(day, false);
    }

    /**
     * Checks if the current daily playtime has exceeded the daily limit.
     *
     * @return true if the current daily playtime exceeds the limit, otherwise false.
     */
    private boolean isPastDailyLimit() {
        return (limitDailyPlaytime.compareTo(currentDailyPlaytime) < 0);
    }

    /**
     * Checks if the current weekly playtime has exceeded the weekly limit.
     *
     * @return true if the current weekly playtime exceeds the limit, otherwise false.
     */
    private boolean isPastWeeklyLimit() {
        return (limitWeeklyPlaytime.compareTo(currentWeeklyPlaytime) < 0);
    }

    /**
     * Checks if today is a restricted day.
     *
     * @return true if today is restricted, otherwise false.
     */
    private boolean isDayLimited() {
        LocalDate currentDate = LocalDate.now();
        // Get the day of the week
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        String currentDay = dayOfWeek.toString().charAt(0) + dayOfWeek.toString().substring(1);
        currentDay = currentDay.toLowerCase();
        return limitedDays.get(currentDay);
    }

    /**
     * Determines if the game is currently playable based on the enabled limits.
     *
     * @return true if the game is playable, otherwise false.
     */
    public boolean isGameBlocked() {
//        printInfo();
        if (dailyPlayableTimeEnabled && checkTimeOfDayLimit()) return true;// If current time is within a limited period, and limits are enabled, return true
        if (dailyTimeLimitEnabled && isPastDailyLimit()) return true; // If daily limits are reached and enabled, return true
        if (weeklyTimeLimitEnabled && isPastWeeklyLimit()) return true; // If weekly limits are reached and enabled, return true
        return isDayLimited() && limitPlayableDaysEnabled; // If day is limited and limit is enabled, return true, else false
    }


    void printInfo() {
        System.out.println("dailyPlayableTimeEnabled: " + dailyPlayableTimeEnabled);
        System.out.println("dailyTimeLimitEnabled: " + dailyTimeLimitEnabled);
        System.out.println("weeklyTimeLimitEnabled: " + weeklyTimeLimitEnabled);
        System.out.println("limitPlayableDaysEnabled: " + limitPlayableDaysEnabled);
        System.out.println("limitedDays: " + limitedDays.toString());
        System.out.println("limitTimeOfDayStart: " + limitTimeOfDayStart.toString());
        System.out.println("limitTimeOfDayEnd: " + limitTimeOfDayEnd.toString());
        System.out.println("limitDailyPlaytime: " + limitDailyPlaytime.toHours() + ":" + limitDailyPlaytime.toMinutesPart()+ "." + limitDailyPlaytime.toSecondsPart());
        System.out.println("limitWeeklyPlaytime: " + limitWeeklyPlaytime.toHours() + ":" + limitWeeklyPlaytime.toMinutesPart()+ "." + limitWeeklyPlaytime.toSecondsPart());
        System.out.println("currentDailyPlaytime: " + currentDailyPlaytime.toHours() + ":" + currentDailyPlaytime.toMinutesPart()+ "." + currentDailyPlaytime.toSecondsPart());
        System.out.println("currentWeeklyPlaytime: " + currentWeeklyPlaytime.toHours() + ":" + currentWeeklyPlaytime.toMinutesPart()+ "." + currentWeeklyPlaytime.toSecondsPart());
        System.out.println("totalPlaytime: " + totalPlaytime.toHours() + ":" + totalPlaytime.toMinutesPart() + "." + totalPlaytime.toSecondsPart());
        System.out.println("sessionCount: " + sessionCount);
        System.out.println("-------------------------------------------------------------------------------------");
    }

    /**
     * Reset the session count to 1 when the statistics have been reset
     */
    public void resetTracking() {
        sessionCount = 1;
        totalPlaytime = Duration.ofSeconds(1);
    }


    public String resetPet(int slot) throws IOException {
        String filePath = "files/slot" + slot + ".csv";
        try {
            Pet loadedPet = SaveManager.loadPet(filePath);
            if (loadedPet.isDead()) {
                PetType type = loadedPet.getType();
                VitalStatistics stats = new VitalStatistics(type.getMaxHealth(), type.getMaxSleep(), type.getMaxFullness(), type.getMaxHappiness());
                loadedPet.setVitalStats(stats);
                loadedPet.checkStates();
                SaveManager.savePet(filePath, loadedPet);
                return "Pet in slot " + slot + " has been revived!";
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return "This pet is not dead and cannot be revived!";
    }


    /**
     * This method checks if the daily playtime limits are enabled
     * @return true if the time limits are enabled, else false
     */
    public boolean isDailyPlayableTimeEnabled() {
        return dailyPlayableTimeEnabled;
    }


    /**
     * This method checks if the day-limiter is enabled.
     * @return true if limitPlayableDaysEnabled is true, else false
     */
    public boolean isLimitPlayableDaysEnabled() {
        return limitPlayableDaysEnabled;
    }

}
