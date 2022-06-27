package eu.mahdiahbab.cst3130;

import java.util.Set;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Class handling saving of Games, Comparisons and Platforms
 * @author Mahdi Ahbab
 */
public class GameDAO {

    //Member variable of SessionFactory, with Setter method
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory s) {
        this.sessionFactory = s;
    }
    
    /** 
     * Class that saves platform into database
     * @param game game to be used 
     * @param platformName platform to be saved and set
     * @return Game
     */
    public Game savePlatform(Game game, String platformName) {
        
        GamePlatform platform = new GamePlatform();
        platform.setTitle(platformName);
  
        Session session = sessionFactory.getCurrentSession();

        //Searches for existing platform name in database
        String queryStr = "from GamePlatform where title='" + platform.getTitle() + "'";
        List<GamePlatform> platformList = session.createQuery(queryStr).getResultList();
        
        //If match found, set game's platform to that returned instance. Else set passed in platform as new instance
        if (platformList.size() >= 1) {
            game.setPlatform(platformList.get(0));
        } 
        else if (platformList.size() == 0) {
            game.setPlatform(platform);
        }
        return game;
    }

    /**
     * Class that saves game into database
     * @param name name to be saved in game
     * @param image image to be saved in game
     * @param platformName platform to be saved 
     * @return Game
     */
    public Game saveGame(String name, String image, String platformName) {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        
        Game game = new Game();
        game.setName(name);
        game.setImage(image);
        
        //Using helper method to find match. If false, set platform and save instance of game.
        if (searchGameDuplicate(game) == false) {
            game = savePlatform(game, platformName); // invokes savePlatform to find appropriate platform
            session.save(game);
        }

        session.getTransaction().commit();
        session.close();
        
        return game;
    }

    /**
     * Class that saves comparison into database
     * @param gameName game name to be added to comparison set
     * @param websiteName web site name to be saved in comparison object
     * @param price price to be saved in comparison object
     * @param linkToGame  link to be saved in comparison object
     */
    public void saveComparison(String gameName, String websiteName, Double price, String linkToGame) {
        
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //Creating comparison object to hold passed in variable values.
        GameComparison comparison = new GameComparison();
        comparison.setPrice(price);
        comparison.setWebsiteName(websiteName);
        comparison.setLinkToGame(linkToGame);

        //Searches for existing game with same name in database
        String queryStr = "from Game where name='" + gameName + "'";
        List<Game> gameList = session.createQuery(queryStr).getResultList();

        //Uses helper method to find duplicate. If false, add game to comparison's set and save comparison
        if (searchComparisonDuplicate(comparison, gameList.get(0)) == false) {

            Set<GameComparison> temp = gameList.get(0).getComparisons();
            temp.add(comparison);
            gameList.get(0).setComparisons(temp);
            comparison.setGame(gameList.get(0));
            session.save(comparison);

        }
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Helper method for saveGame. Searches for duplicate in database
     * @param game game to be used for searching for match
     * @return boolean
     */
    public boolean searchGameDuplicate(Game game) {

        Session session = sessionFactory.getCurrentSession();

        //Searches for game with passed in game object's name
        String queryStr = "from Game where name='" + game.getName() + "'";
        List<Game> gameList = session.createQuery(queryStr).getResultList();

        boolean matchFound;

        //If match found, return true, else return false.
        if (gameList.size() >= 1) {
            return matchFound = true;
        } else {
            return matchFound = false;
        }
    }

    /**
     * Helper method that searches for comparison duplicate
     * @param comparison comparison to be used to search for match
     * @param game game to be used in combination with comparison
     * @return boolean
     */
    public boolean searchComparisonDuplicate(GameComparison comparison, Game game) {

        Session session = sessionFactory.getCurrentSession();
   
        //Searches for comparison with the same website name and game id.
        String queryStr = "from GameComparison where website_name='" 
                + comparison.getWebsiteName() + "' and game_id='" + game.getId() + "'";
        List<GameComparison> comparisonList = session.createQuery(queryStr).getResultList();

        boolean matchFound = false;

        //If match found, return true. Else return false
        if (comparisonList.size() == 1) {
            matchFound = true;
        } else if (comparisonList.size() == 0) {
            matchFound = false;
        }

        return matchFound;
    }
}
