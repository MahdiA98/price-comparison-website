package eu.mahdiahbab.cst3130;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class that handles Spring dependencies and injections
 * @author Mahdi Ahbab
 */
@Configuration
public class SpringConfig {
    
    //Member variable of SessionFactory
    SessionFactory sessionFactory;

    /**
     * Creating GameDAO bean and injecting dependencies
     * @return GameDAO
     */
    @Bean
    public GameDAO gameDAO(){
        GameDAO gameDAO = new GameDAO();
        gameDAO.setSessionFactory(sessionFactory());
        return gameDAO;
    }
    /**
     * Creating ScraperManger bean and injecting dependencies
     * @return ScraperManager
     */
    @Bean
    public ScraperManager scraperManager(){
        ScraperManager scraperManager = new ScraperManager();
        
        //Adding scraper beans to list
        List<WebScraper> scraperList = new ArrayList();
        scraperList.add(scraper1());
        scraperList.add(scraper2());
        scraperList.add(scraper3());
        scraperList.add(scraper4());
        scraperList.add(scraper5());
        
        //setting list to scraperManager
        scraperManager.setScraperList(scraperList);

        return scraperManager;
    }
    
    /**
     * Scraper1 bean and dependencies
     * @return Scraper1
     */
    @Bean
    public Scraper1 scraper1(){
        Scraper1 scraper1 = new Scraper1();
        scraper1.setGameDAO(gameDAO());
        return scraper1;
    }
    
    /**
     * Scraper2 bean and dependencies
     * @return Scraper3
     */
    @Bean 
    public Scraper2 scraper2(){
        Scraper2 scraper2 = new Scraper2();
        scraper2.setGameDAO(gameDAO());
        return scraper2;
    }
    
    /**
     * Scraper3 bean and dependencies
     * @return Scraper3
     */
    @Bean
    public Scraper3 scraper3(){
        Scraper3 scraper3 = new Scraper3();
        scraper3.setGameDAO(gameDAO());
        return scraper3;
    }
    
    /**
     * Scraper4 bean and dependencies
     * @return Scraper4
     */
    @Bean
    public Scraper4 scraper4(){
        Scraper4 scraper4 = new Scraper4();
        scraper4.setGameDAO(gameDAO());
        return scraper4;
    }
    
    /**
     * Scraper5 bean and dependencies
     * @return Scraper5
     */
    @Bean
    public Scraper5 scraper5(){
        Scraper5 scraper5 = new Scraper5();
        scraper5.setGameDAO(gameDAO());
        return scraper5;
    }
    
    /**
     * Creating SessionFactory bean and dependencies
     * @return SessionFactory
     */
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null){
            try {
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                standardServiceRegistryBuilder.configure("hibernate.cfg.xml"); 

                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                }
                catch (Exception e) {
                        System.err.println("Session Factory build failed.");
                        e.printStackTrace();
                        StandardServiceRegistryBuilder.destroy( registry );
                }
                System.out.println("Session factory built.");
            }
            catch (Throwable ex) {
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
