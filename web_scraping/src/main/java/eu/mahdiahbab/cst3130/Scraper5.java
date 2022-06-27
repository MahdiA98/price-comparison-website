package eu.mahdiahbab.cst3130;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class that handles scraping for web site, GameStop
 * @author Mahdi Ahbab
 */
public class Scraper5 extends WebScraper {

    //Empty constructor
    Scraper5() {}

    /**
     * Method that scrapes data from web site
     * @param n n to be used to determine the platform
     */
    @Override
    public void scrapeWebsite(int n) {

        //URLs for different platform games
        String playstationUrl = "https://www.gamestop.com/video-games/playstation-4?start=";
        String xboxUrl = "https://www.gamestop.com/video-games/xbox-one?start=";
        String nintendoUrl = "https://www.gamestop.com/search/?q=nintendo+games&lang=default&start=";

        //End of URL as seperate string, as page value is in the middle
        String url = "";
        String endOfUrl = "&sz=24";

        //Based on integer passed in, determines which game platform is being scraped
        switch (n) {
            case 1:
                url += playstationUrl;
                break;
            case 2:
                url += xboxUrl;
                break;
            case 3:
                url += nintendoUrl;
                break;
            default:
                System.out.println("Index out of range. Please select 1, 2 or 3.");
                break;
        }

        //Loops through 2 pages of the website and extracts data
        try {
            for (int page = 0; page <= 24; page += 24) {

                //Resetting URL string and adding page value, and end of URL
                String originalUrl = url;
                originalUrl += page + endOfUrl;

                //Using Jsoup to connect to website
                Document doc = Jsoup.connect(originalUrl).get();
                Elements prod = doc.getElementsByClass("product-tile product-detail");

                //Extracting website name. Converting first letter to uppercase
                String websiteName = doc.select("img.nav-logo").attr("alt");
                
                for (int i = 0; i < prod.size(); i++) {
                    
                    //Grabbing data
                    String name = prod.get(i).select("a.link-name").text();
                    String price = prod.get(i).select(".default-pricing").text();
                    String image = prod.get(i).select("img").attr("data-src");
                    String linkToGame = prod.get(i).select("a.product-tile-link").attr("href");
                    String platformTitle = prod.get(i).select(".product-tile-availability").select("span").text();
   
                    //Appending http and domain name to ensure link is working
                    linkToGame = "http://gamestop.com" + linkToGame;

                    //Using helper methods to modify and check data
                    name = removeEndOfString(name);
                    name = modifyPunctuation(name);
                   
                    //This website has price start with "See" so have to check for this beforehand
                    //If false, modifies price
                    //Then uses helper method to check if name is ok
                    //If true,saves using gameDAO
                    if (!price.startsWith("See")) {
                        Double newPrice = modifyPrice(price);
                        if (checkName(name) == true) {
                            gameDAO.saveGame(name, image, platformTitle);
                            gameDAO.saveComparison(name, websiteName, newPrice, linkToGame);
                        }
                    }
                }
            }
            stopThread();
        } 
        catch (IOException ex) {
            Logger.getLogger(Scraper5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
