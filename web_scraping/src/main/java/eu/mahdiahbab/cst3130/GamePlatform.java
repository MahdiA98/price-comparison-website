package eu.mahdiahbab.cst3130;

import java.util.Set;

/**
 * Class that creates Platform object
 * @author Mahdi Ahbab
 */
public class GamePlatform {
    
    //Member variables
    private int id;
    private String title;
    private Set<Game> games;
    
    //Empty constructor
    GamePlatform() {}
    
    //Setters & Getters 
    public void setId(int n) {
        this.id = n;
    }
    
    public void setTitle(String s) {
        this.title = s;
    }
    
    public void setGames(Set<Game> g) {
        this.games = g;
    }
    
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Set<Game> getGames() {
        return games;
    }
}
