package eu.mahdiahbab.cst3130;

/** Class used to create Comparison object */
public class GameComparison {
    
    //Member variables
    private int id;
    private double price;
    private String websiteName;
    private String linkToGame;
    private Game game;
    
    //Empty constructor
    GameComparison() {};
    
    //Setters & Getters
    public void setId(int id) {
        this.id = id;
    }
   
    public void setPrice(Double p) {
        this.price = p;
    }
    
    public void setWebsiteName(String s) {
        this.websiteName = s;
    }
    
    public void setLinkToGame(String s) {
        this.linkToGame = s;
    }
    
    public void setGame(Game g) {
        this.game = g;
    }
    
    public int getId() {
        return id;
    }
      
    public Double getPrice() {
        return price;
    }
    
    public String getWebsiteName() {
        return websiteName;
    }
    
    public String getLinkToGame() {
        return linkToGame;
    }
  
    public Game getGame() {
        return game;
    }
    
}
