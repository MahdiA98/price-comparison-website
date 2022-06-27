package eu.mahdiahbab.cst3130;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Class that handles scraping from web site, JDWilliams
 * @author Mahdi Ahbab
 */
public class Scraper3 extends WebScraper {

    //Empty constructor
    Scraper3() {}

    /**
     * Method that scrapes data from the web site
     * @param n n to be used to determine the platform
     */
    @Override
    public void scrapeWebsite(int n) {
        
        //URLs for different platform games
        String playstationUrl = "?text=playstation%20games&nofollowind=true";
        String xboxUrl = "?text=xbox%20games&nofollowind=true";
        String nintendoUrl = "?text=nintendo%20games&nofollowind=true";
        
        //Start of URL obtained as page value is in the middle of URL
        String startOfUrl = "https://www.jdwilliams.co.uk/shop/s/page-";
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
                System.out.println("Index out of range.");
                break;
        }

        //Loops through 2 pages of the website and extracts data
        try {
            for (int page = 1; page <= 3; page++) {
                
                //Resseting URL string with start of URL, then appending page with rest of it
                String originalUrl = startOfUrl;
                originalUrl += page + url;
                
                //Using Jsoup to connect to website
                Document doc = Jsoup.connect(originalUrl).get();
                Elements prod = doc.getElementsByClass("product__item js-product-item");
                
                //Extracting website name
                String websiteName = doc.select("img").attr("alt");
                
                //Looping through product list to extract required information
                for (int i = 0; i < prod.size(); i++) {

                    //Grabbing data
                    String name = prod.get(i).select("a.js-product-title-anchor").text();
                    String price = prod.get(i).getElementsByClass("product-price__now js-product-price").text();
                    String linkToGame = prod.get(i).select("a.product__link").attr("href");
                    String image = prod.get(i).select("img.js-plp-image").attr("src");
                    String platformTitle = "";
                    
                    if (price.equals("")) {
                        price = String.valueOf(prod.get(i).getElementsByClass("product-price__discounted js-product-price").text());
                    }
   
                    //Scraper broke last minute. So added platform title using scraped data
                    //Only thing in website that indicates platform name, so used end of name to retrieve this
                    //E.g Fifa 22 - PS4 or Halo Reach - Xbox One
                    if (name.endsWith("PS4")) {
                        platformTitle = "PS4";
                    }
                    else if (name.contains("Xbox")) {
                        platformTitle = "Xbox";
                    }
                    else if (name.contains("Switch")) {
                        platformTitle = "Switch";
                    }
                    else {
                        platformTitle = "Unavailable";
                    }

                    //Appending http and domain name to ensure it links correctly
                    linkToGame = "http://jdwilliams.co.uk" + linkToGame; 

                    //Using helper methods to modify and check data
                    name = modifyPunctuation(name);
                    name = removeEndOfString(name);
                    Double newPrice = modifyPrice(price);

                    //Using helper method to check if name is ok
                    //If true, save it with gameDAO.
                    if (checkName(name) == true) {
                        gameDAO.saveGame(name, image, platformTitle);
                        gameDAO.saveComparison(name, websiteName, newPrice, linkToGame);
                    }
                }
            }
            stopThread(); //Stops thread after executing code
        }
        catch (IOException ex) {
            Logger.getLogger(Scraper3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}