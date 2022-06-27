package eu.mahdiahbab.cst3130;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Class that handles scraping for web site, GAME
 * @author Mahdi Ahbab
 */
public class Scraper1 extends WebScraper {

    //Empty Constructor
    Scraper1() {}

    /**
     * Method that scrapes data from the web site
     * @param n n to be used for determining the platform
     */
    @Override
    public void scrapeWebsite(int n) {

        //URLs for different platform games
        String playstationUrl = "https://www.game.co.uk/en/games/playstation-5/?contentOnly=&in"
                + "StockOnly=true&listerOnly=&pageSize=48&sortBy=MOST_POPULAR_DESC&pageNumber=";

        String xboxUrl = "https://www.game.co.uk/en/games/xbox-series/?contentOnly=&inStockOnly"
                + "=true&listerOnly=&pageSize=48&sortBy=MOST_POPULAR_DESC&pageNumber=";
        
        String nintendoUrl = "https://www.game.co.uk/en/games/nintendo-switch/nintendo-switch-g"
                + "ames/?contentOnly=&inStockOnly=true&listerOnly=&pageSize=48&sortBy=MOST_POPU"
                + "LAR_DESC&pageNumber=";

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

        //Loops through 3 pages of the website and extracts data
        try {
            for (int page = 1; page <= 3; page++) {
                
                //Resetting URL string and adding page value
                String originalUrl = url;
                originalUrl += page;
                
                //Using Jsoup to connect to website
                Document doc = Jsoup.connect(originalUrl).get();
                Elements prods = doc.select(".product");

                //Extracting website name
                String websiteName = doc.select(".container").select("span").first().text();

                //Looping through entire list of products to extract required information
                for (int i = 0; i < prods.size(); ++i) {

                    //Grabbing data
                    String name = prods.get(i).select("h2").text();
                    String price = prods.get(i).select(".value").text();
                    String image = prods.get(i).select(".productHeader").select("img").attr("data-src");
                    String linkToGame = prods.get(i).select(".productHeader").select("a").attr("href");
                    String platformTitle = prods.get(i).select(".productHeader").select("span").text();

                    //Using helper methods to modify and check data
                    name = modifyPunctuation(name);
                    name = removeEndOfString(name);
                    Double newPrice = modifyPrice(price);
                    platformTitle = modifyPunctuation(platformTitle);

                    //Using helper method to check if name is ok.
                    //If true, save using gameDAO
                    if (checkName(name) == true) {
                        gameDAO.saveGame(name, image, platformTitle);
                        gameDAO.saveComparison(name, websiteName, newPrice, linkToGame);
                    }
                }
            }
            stopThread(); //stops thread after scraping 2 pages
        } 
        catch (IOException ex) {
            Logger.getLogger(Scraper1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
