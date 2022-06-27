package eu.mahdiahbab.cst3130;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {

        //Import spring dependencies
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        //Import ScraperManager bean from SpringConfig
        ScraperManager scraperController = (ScraperManager) context.getBean("scraperManager");
        
        //Invoke method to start scraping
        scraperController.startScraping();
       
    }
}
