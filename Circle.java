import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Circle {
    private String id;    
    private String letter;
    private int radius;    
    private int x;
    private int y;
    private int xStep;
    private int yStep;
    private Color clr;
    private Font letterFont;
    private boolean alive;
    
    public Circle(String id, String letter) {
        this.id = id;        
        this.letter = letter;
        this.radius = Const.MIN_RADIUS;
        this.x = (int)(Math.random()*(Const.WINDOW_SIZE-2*radius)+radius);
        this.y = (int)(Math.random()*(Const.WINDOW_SIZE-2*radius)+radius);
        int xStep = (int)(Const.MAX_RADIUS-radius)/Const.STEP + 1;
        int yStep = (int)(Const.MAX_RADIUS-radius)/Const.STEP + 1;
        this.xStep = (new Random()).nextBoolean()? +xStep : -xStep;
        this.yStep = (new Random()).nextBoolean()? +yStep : -yStep;
        int clrIndex = 1; // make it random
        this.clr = Const.COLORS[clrIndex];        
        this.letterFont = new Font("Arial",Font.PLAIN,radius);
        this.alive = true;
    }
    public Circle(String id, String letter, int x, int y, int radius, Boolean alive) {
        this.id = id;        
        this.letter = letter;
        this.radius = radius;
        this.x = x;
        this.y = y;
        int clrIndex = 1; // make it certain
        this.clr = Const.COLORS[clrIndex];        
        this.letterFont = new Font("Arial",Font.PLAIN,radius);
        this.alive = alive;
    }
//------------------------------------------------------------------------------    
    //getters and setters
    public int getX(){return this.x;}
    public void setX(int x){this.x = x;}
    public int getY(){return this.y;}
    public void setY(int y){this.y = y;}
    public int getYMove(){return this.yStep;}
    public void setYMove(int yStep){this.yStep = yStep;}
    public int getXMove(){return this.xStep;}
    public void setXMove(int xStep){this.xStep = xStep;}    
    public int getRadius(){return this.radius;}
    public void setRadius(int r){radius = r;}
    public Color getColor(){return this.clr;}
    public void setColor(Color clr){this.clr = clr;}
    public String getName(){return this.letter;}
    public Font getFont(){return this.letterFont;}
    public String getId(){return this.id;}
    public boolean getAlive(){return this.alive;}
    public void setAlive(boolean alive){this.alive = alive;}    
//------------------------------------------------------------------------------     
// somecode here
}