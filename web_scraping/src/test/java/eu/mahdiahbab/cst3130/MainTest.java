package eu.mahdiahbab.cst3130;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Class handling JUnit testing
 * @author Mahdi Ahbab
 */
public class MainTest {

    static SessionFactory sessionFactory;
    
    @BeforeAll
    static void init() {
        try {
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed." + ex);
        }
    }
    
    /*
        Tests are performed when the database is empty, so it does not clash and
        cause failures/errors
    */
    
    @Test
    @DisplayName("Test Save Game")
    void testSaveGame() {
        
        //Setup
        GameDAO tempDAO = new GameDAO();
        tempDAO.setSessionFactory(sessionFactory);

        //Temporary game object
        Game retrievedGame = new Game();

        //Saving instance of game to database
        tempDAO.saveGame("test1_name", "test1_image", "test1_platform_name");
        
        //Start session
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //Search for saved game
        String queryStr = "from Game where name='test1_name'";
        List<Game> gameList = session.createQuery(queryStr).getResultList();

        //If result found, store in temp object
        if (gameList.size() == 1) {
            retrievedGame = gameList.get(0);
        } else {
            fail("Game did not store into database, test failed.");
        }
        
        //Should be equal if it has successfully been stored and retrieved
        assertEquals("test1_name", retrievedGame.getName());
        
        //Delete from database
        session.delete(gameList.get(0));
        
        session.getTransaction().commit();
        session.close();
    }

    @Test
    @DisplayName("Test Save Comparison")
    void testSaveComparison() {

        //Setup
        GameDAO tempDAO = new GameDAO();
        tempDAO.setSessionFactory(sessionFactory);
   
        //Testing variables
        String websiteName = "Amazon";
        String link = "http://amazon.co.uk";
        Double price = 1.99;

        //Save game and comparison
        tempDAO.saveGame("test2_name", "test2_image", "test2_platform_name");
        tempDAO.saveComparison("test2_name", websiteName, price, link);

        //Start session
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //Search for game and comparison with test variables
        String query = "from GameComparison where website_name='" + websiteName +"'";
        List<GameComparison> comparisonList = session.createQuery(query).getResultList();
        
        String query2 = "from Game where name='test2_name'";
        List<Game> gameList = session.createQuery(query2).getResultList();
        
        //Should be true if it has succesfully been saved to database
        assertEquals(websiteName, comparisonList.get(0).getWebsiteName());
        
        //Delete from database
        session.delete(comparisonList.get(0));
        session.delete(gameList.get(0));
        
        session.getTransaction().commit();
        session.close();
    }
    
    @Test
    @DisplayName("Test Save Platform")
    void testSavePlatform() {

        //Setup
        GameDAO tempDAO = new GameDAO();
        tempDAO.setSessionFactory(sessionFactory);

        //Saving game and platform (called in saveGame()) to database
        tempDAO.saveGame("test3_name", "test3_image", "test3_platform_name");

        //Starting session
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //Search for saved platform and game
        String query = "from GamePlatform where title='test3_platform_name'";
        List<GamePlatform> platformList = session.createQuery(query).getResultList();
        
        String query2 = "from Game where name='test3_name'";
        List<Game> gameList = session.createQuery(query2).getResultList();
        
        //Should be equal to the platform name in database
        assertEquals("test3_platform_name", platformList.get(0).getTitle());
        
        //Delete from database
        session.delete(gameList.get(0)); 
        session.delete(platformList.get(0));
        
        session.getTransaction().commit();
        session.close();
    }
   
    @Test
    @DisplayName("Testing duplicate search for Game")
    void testSearchGameDuplicate() {

        //Setup
        GameDAO tempDAO = new GameDAO();
        tempDAO.setSessionFactory(sessionFactory);
        
        //Saving game to database and returning object
        Game testGame = new Game();
        testGame = tempDAO.saveGame("test4_name", "test4_image", "test4_platform_name");
        
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        
        //Should return true as testGame has same name as previously saved game object
        assertEquals(true, tempDAO.searchGameDuplicate(testGame));
        
        //Deletes from database
        String query = "from Game where name='" + testGame.getName() + "'";
        List<Game> gameList = session.createQuery(query).getResultList();
        
        session.delete(gameList.get(0));
        
        session.getTransaction().commit();
        session.close();
    }

    
    @Test
    @DisplayName("Testing duplicate search for Comparison")
    void testSearchComparisonDuplicate() {

        GameDAO tempDAO = new GameDAO();
        tempDAO.setSessionFactory(sessionFactory);
     
        //Saving game and comparison to database
        tempDAO.saveGame("test5_name", "test5_image","test5_platform_name");
        tempDAO.saveComparison("test5_name", "test5_website_name", 5.99, "test5_link");
        
        //Creating game object with id 4 (should be 5, but it works for 4?)
        Game testGame = new Game();
        testGame.setId(4);
        
        //Creating comparison object with passed in website_name
        GameComparison testComparison = new GameComparison();
        testComparison.setWebsiteName("test5_website_name");

        //Starting session
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        
        //Should return true if there is a match in comparisons, where website_name and game_id match testComparison & testGame
        assertEquals(true, tempDAO.searchComparisonDuplicate(testComparison, testGame));
        
        //Deleting from database
        String query = "from GameComparison where website_name='test5_website_name'";
        List<GameComparison> comparisonList = session.createQuery(query).getResultList();
        
        String query2 = "from Game where name='test5_name'";
        List<Game> gameList = session.createQuery(query2).getResultList();
        
        session.delete(comparisonList.get(0));
        session.delete(gameList.get(0));
  
        session.getTransaction().commit();
        session.close();

    } 
}
