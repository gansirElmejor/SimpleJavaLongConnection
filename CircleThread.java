import java.util.ArrayList;
import java.util.*;
import java.math.*;

public class CircleThread implements Runnable{ 
    ArrayList<Circle> circles;
    Circle circle;
    int threadID;
    HashSet<String> direction;
    final int SLEEPMICRO = Const.SLEEPMICRO;
    final int BOTTOM = Const.BOTTOM;
    final int RIGHT = Const.RIGHT;
    final int LEFT = Const.LEFT;
    final int TOP = Const.TOP;
    
    CircleThread(ArrayList<Circle> circles, int threadID, String letter, HashSet<String> direction){
        this.circles = circles;
        this.threadID = threadID;
        this.circle = new Circle(threadID+"", letter);
        circles.add(circle);  
        this.direction = direction;          
    }
    
    // Leave this for client
    private Boolean checkBorderBounce(int x, int y, int xMove, int yMove) {
        if ((x - circle.getRadius()) <= LEFT || (x + circle.getRadius()) >= RIGHT) {
            this.circle.setXMove(-xMove);
            return true;
        }
        if ((y - circle.getRadius()) <= TOP || (y + circle.getRadius()) >= BOTTOM) {
            this.circle.setYMove(-yMove);
            return true;
        }
        return false;
    }
    /* Migrate to server
    private synchronized void getBigger(int newSelfRad, int currentSelfRad) {
        for (int i = currentSelfRad; i <newSelfRad; i ++){
            try{Thread.sleep(Const.FRAME_DURATION);} catch (Exception exc){}
            this.circle.setRadius(i);
        }        
    }
    private Boolean checkBallTouch(int x, int y) {
        for (int i=0; i<circles.size(); i++) {
            Circle targCircle = circles.get(i);
            if (targCircle != this.circle) {
                int targRad = targCircle.getRadius();
                int selfRad = this.circle.getRadius();
                // check border
                int targX = targCircle.getX();
                int targY = targCircle.getY();
                int distanceSqr = (x-targX)*(x-targX) + (y-targY)*(y-targY);
                int targRadiusSqr = (targRad+selfRad)*(targRad+selfRad);
                if (distanceSqr <= targRadiusSqr) {
                if (selfRad <= targRad) {   
                    circles.remove(this.circle);
                    return true;    
                } else {
                    // larger ball get bigger //ultimate area: pi(r1^2+r2^2)// ultimate radius srt(area/pi)
                    int newSelfRad = (int)Math.sqrt(targRad*targRad + selfRad*selfRad);
                    getBigger(newSelfRad,selfRad);
                    return false;
                }
            }
            }
        }
        return false ;
    }
*/
    private void move(HashSet<String> direction) {
        int x = this.circle.getX();
        int y = this.circle.getY();
        int xMove = this.circle.getXMove();
        int yMove = this.circle.getYMove(); 
        if (direction.isEmpty()) {
            return;
        }
        if (direction.contains("UP")) {
            yMove = -Const.STEP;
        } else if (direction.contains("DOWN")) {
            yMove = Const.STEP;
        } else {
            yMove = 0;
        }
        if (direction.contains("LEFT")) {
            xMove = -Const.STEP;
        } else if (direction.contains("RIGHT")) {
            xMove = Const.STEP;
        } else {
            xMove = 0;
        }  
        if (checkBorderBounce(x, y, xMove, yMove)) {
            this.circle.setX(x + this.circle.getXMove());
            this.circle.setY(y + this.circle.getYMove());
            return;
        };
        /* Migrate to server
        if (checkBallTouch(x,y)) {
            this.circle.setAlive(false);
            return;
        }
        */
        this.circle.setX(x + xMove);
        this.circle.setY(y + yMove);
    }
    @Override
    public void run(){ 
        try{
            while (!Thread.interrupted() && circle.getAlive()) {
                Thread.sleep(Const.FRAME_DURATION);
                move(this.direction);
            }
            throw new InterruptedException();
        } catch(InterruptedException err) {
            System.out.println("Interruped a ball!!!!");
        }
    }
}  
