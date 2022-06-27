package eu.mahdiahbab.cst3130;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Superclass that handles threads and passes methods to child classes
 * @author Mahdi Ahbab
 */
public class WebScraper extends Thread {

    //Member variable of GameDAO
    GameDAO gameDAO;

    //Sleep delay specified with boolean for threads
    int delay = 1000;
    volatile boolean runThread = false;

    /**
     * Method that is inherited from Thread.
     */
    @Override
    public void run() {

        runThread = true;

        //While thread is running, invoke scraping for each platform type
        while (runThread) {
            scrapeWebsite(1);
            scrapeWebsite(2);
            scrapeWebsite(3);
            try {
                sleep(delay);
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(WebScraper.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error caught");
            }
        }
    }

    /**
     * Stops thread.
     */
    public void stopThread() {
        runThread = false;
    }

    /**
     * Method that scrapes web sites.
     * @param n n to be used for determining the platform
     */
    public void scrapeWebsite(int n) {
    }

    //Setters & Getters
    public void setGameDAO(GameDAO g) {
        gameDAO = g;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

    /**
     * Class that modifies punctuation of passed in String
     * @param s s to be passed in and modified
     * @return String
     */
    public String modifyPunctuation(String s) {

        //Replaces character or string with desired output
        String newString = s.replace("'", "").replace("’", "")
                .replace(":", "").replace("-", "").replace("!", "")
                .replace(".", "").replace("  ", " ").replace(" + ", " & ")
                .replace(" and ", " & ").replace(",", "")
                .replace("N.Sane", "N Sane").replace("NSane", "N Sane")
                .replace("III", "3").replace("II", "2")
                .replace("Colors", "Colours").replace("Horison", "Horizon")
                .replace("Horizons", "Horizon")
                .replace("Hawk", "Hawks").replace("™", "")
                .replace("®", "").replace("Tour", "")
                .replace("NitroFueled", "Nitro Fueled")
                .replace("(", "").replace("SuperHeroes", "Super Heroes")
                .replace(")", "").replace("MegaDrive", "Mega Drive");
        
        //returns modified value
        return newString;
    }

    /**
     * Class that modifies Double value 
     * @param s s to be passed in and modified, and returned as Double
     * @return Double
     */
    public Double modifyPrice(String s) {

        Double modifiedPrice = 0.0;

        //Multiple checks to ensure String is appropriately modified
        //E.g some were originally "See $9.99";
        if (s.length() != 0) {
            if (s.startsWith("S"))  {
                modifiedPrice = Double.parseDouble(s.substring(6, 11)); 
            }
            else if (s.length() == 5) {
                modifiedPrice = Double.parseDouble(s.substring(1, 5));
            }
            else {
                modifiedPrice = Double.parseDouble(s.substring(1, 6));
            }
        }
        return modifiedPrice;
    }

    /**
     * Method that checks if passed in String is suitable.
     * @param s s to be passed in and checked
     * @return boolean
     */
    public boolean checkName(String s) {

        boolean isValid = true;

        //Array containing words that I don't want in database
        String[] unwantedWords = {"collector's", "bundle", "edition", 
            "exclusive", "exc", "month", "directors", "premium", "docking", 
            "joycon", "download", "points", "battery", "including", "console",
            "controller", "standard", "OLED", "expansion", "charger", 
            "steelbook", "pack", "crystals", "headset", "disc", "season pass",
            "cable", "(PS3", "(PS2 Classic)", "Pass", "2TB", "VR", "Subscription",
            "Stress", "GOTY", "PSVR", "circuit", "4TB", "charging", "adapter",
            "logitech", "socks", "wheel", "mug", "chair", "code in a box",
            "collection", "playstation hits", "currency", "deluxe", "game", 
            "12switch", "pro skater", "mario & sonic", "combo", "lite",
            "briliant", "neighborvil", "3d world", "nitrofuelled", "12",
            "rocker", "dock", "starter", "stereo"};

        //Loops through array and checks if sentence contains the array value.
        //If true, returns boolean as false
        for (int i = 0; i < unwantedWords.length; i++) {
            if (s.toLowerCase().contains(unwantedWords[i].toLowerCase())) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Method that removes the end of the passed in String.
     * @param s s to be modified and returned
     * @return String
     */
    public String removeEndOfString(String s) {
        
        //Array containing common words found at end of strings
        String[] unwantedWords = {"ps5", "playstation 4", "playstation 5",
            "for nintendo switch", "nintendo switch", "nintendo switch",
            "switch", "nintendo switch (code in a box)", "xbox one & series x",
            "xbox one and series x", "xbox one seriesx", "xbox one disc", 
            "playstation hits range ps4", "hits", "hits range ps4", "xbox one",
            "xboxone", "xbox", "disc", "ps4", "deluxe", "xbox series x",
            "nsw", "set", "xbox one/series x", "ultimate", "remastered",
            " ", "xbox one & seriesx"};
        
        //Loops through array and checks if sentence ends with the array value.
        //If true, returns String with end of value removed using substring
        for (int i = 0; i < unwantedWords.length; i++) {
            if (s.toLowerCase().endsWith(unwantedWords[i])) {
                s = s.substring(0, s.length() - unwantedWords[i].length());
            }
        }
        s = s.trim(); //Removes trailing whitespace from some of entries
        
        return s;    
    }
    
}
