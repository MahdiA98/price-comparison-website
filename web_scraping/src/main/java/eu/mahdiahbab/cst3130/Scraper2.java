package eu.mahdiahbab.cst3130;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class that handles scraping for web site, Curry
 * @author Mahdi Ahbab
 */
public class Scraper2 extends WebScraper {

    //Empty Constructor
    Scraper2() {}

    /**
     * Method that scrapes data from the web site
     * @param n n to be used to determine the platform
     */
    @Override
    public void scrapeWebsite(int n) {
        
        //URLs for different platform games
        String playstationUrl = "https://www.currys.co.uk/gbuk/playstation-gaming/console-gamin"
                + "g/games/634_4803_32583_9646_xx/";
        
        String xboxUrl = "https://www.currys.co.uk/gbuk/xbox-gaming/console-gaming/games/634_48"
                + "03_32583_9645_xx/";
        
        String nintendoUrl = "https://www.currys.co.uk/gbuk/nintendo-switch-gaming/console-gami"
                + "ng/games/634_4803_32583_7127_xx/";
        
        //End of URL as seperate string, as page value is in the middle
        String endOfUrl = "_20/relevance-desc/xx-criteria.html";
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
        
        //Loops through 2 pages of the website and extracts data
        try {
            for (int page = 1; page <= 2; page++) {
                
                //Resetting URL string and adding page value, and end of URL
                String originalUrl = url;
                originalUrl += page + endOfUrl;
                
                //Using Jsoup to connect to website
                Document doc = Jsoup.connect(originalUrl).get();
                Elements prods = doc.getElementsByClass("Product-sc-1f9ti8e-0 ProductListItem__PProductList"
                                                        + "Item-sc-pb4x98-0 hMmZRT fMtRir product");
                
                //Extracting website name. Converting first letter to uppercase
                String websiteName = doc.select("span").attr("aria-label");
                websiteName = websiteName.substring(0, 1).toUpperCase() + websiteName.substring(1);
                
                //Looping through entire list of products to extract required information
                for (int i = 0; i < prods.size(); i++) {

                    //Grabbing data
                    String name = prods.get(i).select("[data-product=name]").text();
                    String price = prods.get(i).getElementsByClass("ProductPriceBlock__Price-eXioPm eTWvaA").text();
                    String image = prods.get(i).getElementsByClass("ListItemImageAndBadges__DivProductImages-sc-10yfl17-1 egnPwy product-images").select("img").attr("src");
                    String linkToGame = prods.get(i).select("a").attr("href");
                    String platformTitle = prods.get(i).select("[data-product=brand]").text();
                    
                    //Some returned empty and required a different extraction query
                    if (image.equals("")) {
                        image = prods.get(i).select("img").attr("src");
                    }
                    
                    //Using helper methods to modify and check data
                    name = modifyPunctuation(name);
                    name = removeEndOfString(name);
                    Double newPrice = modifyPrice(price);
                    //Platform name was capitialised and converted it to lower case
                    platformTitle = platformTitle.substring(0, 1) + platformTitle.substring(1, platformTitle.length()).toLowerCase();
              

                    //Using helper method to check if name is ok
                    //If true, save using  gameDAO
                    if (checkName(name) == true) {
                        gameDAO.saveGame(name, image, platformTitle);
                        gameDAO.saveComparison(name, websiteName, newPrice, linkToGame);
                    }
                }  
            }
            stopThread(); //stops thread after executing
        }
        catch (IOException ex) {
            Logger.getLogger(Scraper2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
