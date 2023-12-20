import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BouncingBalls implements Runnable {
    JFrame frame;
    GraphicsPanel panel;
    public static HashSet<String> myDirection = new HashSet<String>();
    
    private ArrayList<Circle> circles;
    private int newId = 1;
    
    BouncingBalls(ArrayList<Circle> circles){
        this.circles = circles;
    }
    @Override
    public void run() {
        frame = new JFrame("Bouncing Balls");
        panel = new GraphicsPanel();
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(Const.WINDOW_SIZE, Const.WINDOW_SIZE);
        frame.addKeyListener(new MyKeyListener());
        
        //Add own ball first
        new MyBall("YOU");

        while(true){
            //pause thread execution for the duration of one video frame
            try{Thread.sleep(Const.FRAME_DURATION);} catch (Exception exc){}
            new RefresehOtherBall();
            frame.repaint();
        }
    }
//------------------------------------------------------------------------------  
    class GraphicsPanel extends JPanel {
        public void paintComponent(Graphics g) {
            for(int i=0; i<circles.size(); i++) {
                Circle c = circles.get(i);
                g.setColor(c.getColor());
                g.fillOval((c.getX()-(int)c.getRadius()),(c.getY()-(int)c.getRadius()), (int)c.getRadius()*2, (int)c.getRadius()*2);
                g.setColor(Color.WHITE);
                g.setFont(c.getFont());
                g.drawString(c.getName()+"",c.getX(),c.getY());
            }
        }
    }

    public class RefresehOtherBall {
        RefresehOtherBall() {
            ArrayList<Circle> nextCircles_d = BallGameClient.nextCircles;
            for (int i=0; i<nextCircles_d.size(); i++) {
                Circle nextCircle = nextCircles_d.get(i);
                //update circle position and size;
                //Instance new circle if new circle
                String nextCircleName = nextCircle.getName();
                int seachIndex = search(circles,nextCircleName);
                if (seachIndex != -1) {
                    Circle rqCircle = circles.get(seachIndex);
                    //update circle
                    rqCircle.setAlive(nextCircle.getAlive());
                    rqCircle.setRadius(nextCircle.getRadius());
                    rqCircle.setX(nextCircle.getX());
                    rqCircle.setY(nextCircle.getY());
                } else {
                    //add new circle here
                    newId++;
                    Circle plrCircle = new Circle(newId+"", nextCircleName, nextCircle.getX(), nextCircle.getY(), nextCircle.getRadius(), nextCircle.getAlive());
                    circles.add(plrCircle);
                }
            }
        }

        private int search(ArrayList<Circle> source, String name) {
            for (int i=0; i<source.size(); i++) {
                if (source.get(i).getName().equals(name)) {
                    return i;
                }
            }
            return -1;
        }
    }



    public class MyBall {
        MyBall(String name) {
            newId = 1;
            Thread circleThread = new Thread(new CircleThread(circles, newId, name, myDirection),newId+"");
            circleThread.start(); 
        }
    }
//------------------------------------------------------------------------------       
    private class MyKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            //Start Move
            char key = keyEvent.getKeyChar();
            if (key == 'w') {
                BouncingBalls.myDirection.add("UP");
            } else
            if (key == 's') {
                BouncingBalls.myDirection.add("DOWN");
            } else
            if (key == 'a') {
                BouncingBalls.myDirection.add("LEFT");
            } else
            if (key == 'd') {
                BouncingBalls.myDirection.add("RIGHT");
            }
        }
        @Override
        public void keyReleased(KeyEvent keyEvent) { 
            char key = keyEvent.getKeyChar();
            if (BouncingBalls.myDirection.isEmpty()) {
                return;
            }
            if (key == 'w') {
                if (BouncingBalls.myDirection.contains("UP")) {
                    BouncingBalls.myDirection.remove("UP");
                }
            } else
            if (key == 's') {
                if (BouncingBalls.myDirection.contains("DOWN")) {
                   BouncingBalls.myDirection.remove("DOWN");
                }
            } else
            if (key == 'a') {
                if (BouncingBalls.myDirection.contains("LEFT")) {
                    BouncingBalls.myDirection.remove("LEFT");
                }
            } else
            if (key == 'd') {
                if (BouncingBalls.myDirection.contains("RIGHT")) {
                    BouncingBalls.myDirection.remove("RIGHT");
                }
            }
        }        
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            // char key = keyEvent.getKeyChar();
            // if (key >= 'a' && key <= 'z'){
                 
            // }
        }
    }
}

