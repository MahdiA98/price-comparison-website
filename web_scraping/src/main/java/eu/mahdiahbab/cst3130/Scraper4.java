package eu.mahdiahbab.cst3130;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class that handles scraping for web site, Very
 * @author Mahdi Ahbab
 */
public class Scraper4 extends WebScraper {

    //Empty constructor
    Scraper4() {}

    /**
     * Method that scrapes data from web site
     * @param n n to be used to determine the platform
     */
    @Override
    public void scrapeWebsite(int n) {

        //URLs for different platform games
        String playstationUrl = "https://www.very.co.uk/gaming-dvd/playstation-4-games/e/b/"
                              + "104131.end?pageNumber=";

        String xboxUrl = "https://www.very.co.uk/gaming-dvd/xbox-one-games/e/b/104127.end?p"
                       + "ageNumber=";

        String nintendoUrl = "https://www.very.co.uk/e/q/nintendo-games.end?pageNumber=";

        String url = "";

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

        //Loops through 2 pages of website and extracts data
        try {
            for (int page = 1; page <= 2; page++) {

                //Resetting URL to avoid errors inside loop, and adding page number at end
                String originalUrl = url;
                originalUrl += page;

                //Using Jsoup to connect to web site
                Document doc = Jsoup.connect(originalUrl).get();
                Elements prod = doc.select(".product");

                //Extracting website name 
                String websiteName = doc.select("img.header-logoImage").attr("title");
                
                //Looping through entire list of products to extract required information
                for (int i = 0; i < prod.size(); ++i) {

                    //Grabbing data
                    String name = prod.get(i).select(".productBrandDesc").text();
                    String price = prod.get(i).select(".productPrice").text();
                    String image = prod.get(i).select("a.productMainImage").select("source").attr("data-srcset");
                    String linkToGame = prod.get(i).select("a.productMainImage").attr("href");
                    String platformTitle = prod.get(i).select("em.productBrand").text();

                    //Some images were returning empty, so required a different query selector
                    if (image.equals("")) {
                        image = prod.get(i).select("a.productMainImage").select("source").attr("srcset");
                    }
                    
                    //Some platform names returned empty, so simply just set as unavailable
                    if (platformTitle.equals("")) {
                        platformTitle = "Unavailable";
                    }

                    //Using helper methods to modify and check data
                    name = modifyPunctuation(name);
                    name = removeEndOfString(name);
                    Double newPrice = modifyPrice(price);
              
                    //Using helper method to check if name is ok
                    //If true, save using gameDAO.
                    if (checkName(name) == true) {
                        gameDAO.saveGame(name, image, platformTitle);
                        gameDAO.saveComparison(name, websiteName, newPrice, linkToGame);
                    }
                }
            }
            stopThread(); //Stops thread after executing
        } 
        catch (IOException ex) {
            Logger.getLogger(Scraper4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
