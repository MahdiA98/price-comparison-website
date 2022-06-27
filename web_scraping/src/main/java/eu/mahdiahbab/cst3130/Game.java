package eu.mahdiahbab.cst3130;

import java.util.Set;

/**
 * Class that creates Game object
 * @author Mahdi Ahbab
 */
public class Game {

    //Member variables
    private int id;
    private String name;
    private String image;
    private GamePlatform platform;
    private Set<GameComparison> comparisons;

    //Empty constructor
    public Game() {}

    //Setters & Getters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String s) {
        this.name = s;
    }
    
    public void setImage(String s) {
        this.image = s;
    }
    
    public void setComparisons(Set<GameComparison> gc) {
        this.comparisons = gc;
    }
    
    public void setPlatform(GamePlatform gp) {
        this.platform = gp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getImage() {
        return image;
    }
    
    public Set<GameComparison> getComparisons() {
        return comparisons;
    }
    
    public GamePlatform getPlatform() {
        return platform;
    }
}
