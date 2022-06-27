package eu.mahdiahbab.cst3130;

import java.util.List;
import java.util.Scanner;

/**
 * Class that handles WebScrapers
 * @author Mahdi Ahbab
 */
public class ScraperManager {

    //Member variable that stores a list of WebScrapers
    List<WebScraper> scraperList;
    
    /**
     * Method that invokes the run method for each scraper in the list
     */
    public void startScraping() {

        //Loops through list and uses start() to run threads
        for (int i = 0; i < scraperList.size(); i++) {
            scraperList.get(i).start();
        }

        //Listens for input from console
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        while (!input.equals("stop")) {
            input = scan.nextLine();
        }

        
        //If stop entered, wait for threads to finish and return confirmation text
        for (int i = 0; i < scraperList.size(); i++) {
            try {
                scraperList.get(i).stopThread();
                scraperList.get(i).join();
            } 
            catch (InterruptedException ie) {
                System.out.println("Error caught: " + ie);
            }
        }
        System.out.println("\nWeb scraping complete.");
    }

    //Setter & Getters
    public void setScraperList(List<WebScraper> s) {
        this.scraperList = s;
    }

    public List getScraperList() {
        return scraperList;
    }

}
