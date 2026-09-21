package core;

import java.io.Serializable;

/**
 * Ceci est un chrono utilisant principalement la methode
 * System.currentTimeMillis() prenant la difference de temps entre maintenant et
 * le 1er janvier 1970 à minuit,
 *
 * Cette classe permet l'implementation des commandes
 * start,stop,resume,gettime,stop
 *
 */
public class Chrono implements Serializable {

    /** The time where the timer is set to start */
    private long tempsDepart = 0;
    /** The time for the timer given by the user plus the time of the day */
    private long tempsFin = 0;
    /** The time where the timer is set to pause */
    private long pauseDepart = 0;

    /**
     * Constructor for the Chrono class
     */
    public Chrono() {
        super();
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
     */
    /**
     * Returns the starting time of the chrono.
     * @return returns the starting time
     */
    public long getTempsDepart() {
        return tempsDepart;
    }

    /**
     * Returns the end time of the chrono.
     * @return returns the end time
     */
    public long getTempsFin() {
        return tempsFin;
    }

    /**
     * Returns the time where the timer was paused.
     * @return the time where the timer stopped
     */
    public long getPauseDepart() {
        return pauseDepart;
    }

    /*
    =============================
    ---------- Setters ----------
    =============================
     */
    /**
     * sets the starting time to a new time
     * @param temps time to set the start time to
     */
    public void setTempsDepart(long temps) {
        tempsDepart = temps;
    }

    /**
     * changes when the timer ends
     * @param temps time to set when the timer will end
     */
    public void setTempsFin(long temps) {
        tempsFin = temps;
    }

    /**
     * sets the time where the timer is paused to a new time
     * @param temps time to set the paused time 
     */
    public void setPauseDepart(long temps) {
        pauseDepart = temps;
    }

    /** 
     * Start the chrono with a given duration in minutes.
     * The chrono will count down from the specified time to zero. The chrono
     * will start at the given time in the parameter ,
     *
     * the timer will be kept in the tempsfin variable, the tempsDepart will get
     * the current time of the day the timer will be put in pause to start only
     * when someone click on a start button or resume the method.
     *
     * @param timer the timer in minutes
     *
     * the chrono with a set timer given by the user
     *
     */
    public void start(int timer) {
        this.tempsFin = System.currentTimeMillis() + (timer * 60000);
        this.tempsDepart = System.currentTimeMillis();
        pauseDepart = 0;
        pause();
    }

    /**
     * This function is used to put in pause the chrono, it
     * will verify if the chrono hasn't ended and if the chrono isn't already in
     * pause it will save the current time of the day in pauseDepart for when
     * the user use the resume function
     *
     */
    public void pause() {
        if (tempsDepart != 0 && pauseDepart == 0) {
            pauseDepart = System.currentTimeMillis();
        }
    }

    /**
     * This function resume the chrono restarting it, the
     * chrono will be set by the difference from the current time and the chrono
     * given by the user minus the chrono when it was set to pause. The variable
     * pauseDepart will be put back to 0 so that the chrono can be put in pause
     * again
     *
     */
    public void resume() {
        if (pauseDepart != 0) {
            tempsFin = System.currentTimeMillis() + (tempsFin - pauseDepart); // on recrée une nouvelle fin
            pauseDepart = 0;
        }
    }

    /**
     * This function is used if you want the chrono to
     * stop,it will set every variable to 0 it can be used if you want to close
     * the chrono or reset it
     *
     */
    public void stop() {
        tempsFin = 0;
        pauseDepart = 0;
        tempsDepart = 0;
    }

    /**
     * This fuction will check if the chrono is in pause,if
     * it is in pause it'll return the chrono minus the time where it was put in
     * pause else it'll give the chrono's time minus the current time which will
     * give the time left before the chrono run out.
     * @return the time left in miliseconds
     */
    public long getDureeMs() {
        if (pauseDepart != 0) {
            return (tempsFin - pauseDepart);
        }
        return tempsFin - System.currentTimeMillis();
    }

    /**
     * This function will get the current time from the
     * chrono and convert it into a string with an exemple being xx h xx min xx
     * sec
     * @return returns a string with the time left in the timer formatted correctly
     */
    public String getDureeTxt() {

        if (this.tempsFin != 0) { // if the chrono isn't at 0
            long totalMs = getDureeMs(); //get the timer in Milisecond
            long totalSec = totalMs / 1000; // get the timer in Second

            // Calculation of units :  heurs minute second tenth of a second
            long h = totalSec / 3600; // heurs
            long m = totalSec % 3600 / 60; // a minute
            long s = totalSec % 60; // a second
            //long ms = (totalMs%1000);// tenth of a second

            String resultat = ""; // this is the empty string that will be returned

            if (h > 0) { //This will display the hour if the hour is superior at 0
                resultat = resultat + h + " h ";
            }
            if (m > 0 || h > 0) { // The minutes are displayed while m and h > 0, even if m = 0
                resultat = resultat + m + " min ";
            }    
            resultat = resultat + s + " sec ";
            //resultat += ms+" ms";//this will display the time in a Milisecond if everything up there is > 0 even s = 0
                
            
            return resultat; //return the string of the current time
        } else {
            return ""; // Fallback to satisfy compiler and prevent error from appearing the error list
        }
    }

}
