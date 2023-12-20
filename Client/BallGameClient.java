//imports for network communication
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class BallGameClient{
    static public ArrayList<Circle> circles = new ArrayList<Circle>();
    static public ArrayList<Circle> nextCircles = new ArrayList<Circle>();
    public static void main(String[] args) throws Exception {
        Thread game = new Thread(new BouncingBalls(circles),"ClientMovement");
        BasicClient client = new BasicClient();
        
        client.start(); //Start Socket
        game.start(); //Start Game
        while (true) { //Long connection to the server
            if (!client.listen()) {
                break;
            }
            try{Thread.sleep(Const.FRAME_DURATION);} catch (Exception exc){}
        }
        client.stop(); //Close Socket
    }
}

class BasicClient {
    final String SERVER_ADRESS = "127.0.0.1";
    final int PORT = 5000;
    
    Socket clientSocket;
    PrintWriter output;    
    BufferedReader input;
    
    public void start() throws Exception{ 
        //create a socket with the local IP address and attempt a connection
        System.out.println("Attempting to establish a connection ...");
        clientSocket = new Socket(SERVER_ADRESS, PORT);          //create and bind a socket, and request connection
        output = new PrintWriter(clientSocket.getOutputStream());
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("200 from "+SERVER_ADRESS);
        output.println("Ball_Spawn");                       //spawn the ball
        output.flush();                                       //ensure the message was sent but not kept in the buffer
        String msg = input.readLine();                        //get a response from the server
        System.out.println("Message from server: '" + msg+"'"); //AllowSpawn X Y
    }

    private String encodeOwnData() {
        if (BallGameClient.circles.isEmpty()) {
            return "948"; // empty body
        }
        Circle myBall = BallGameClient.circles.get(0);
        String data = "200 "+myBall.getX()+" "+myBall.getY()+" "+myBall.getRadius();
        return data;
    }

    private ArrayList<Circle> decodeOtherData(String encoded) { //ensure server does not respond own data
        //200&PlayerName Alive X Y Rad&PlayerName Alive X Y Rad&PlayerName Alive X Y Rad
        if (encoded.equals("400") || encoded.equals("200")) {
            return null;
        }
        System.err.println(encoded);
        ArrayList<Circle> result = new ArrayList<Circle>();
        String[] eachPlayer = encoded.split("&");
        for (int i=0; i<eachPlayer.length-1;i++) {
            String[] plrData = eachPlayer[i+1].split(" ");
            String plrName = plrData[0];
            Boolean alive = Boolean.parseBoolean(plrData[1]);
            int x = Integer.parseInt(plrData[2]);
            int y = Integer.parseInt(plrData[3]);
            int rad = Integer.parseInt(plrData[4]);
            Circle refCircle = new Circle("-1", plrName,x,y,rad,alive);
            result.add(refCircle);
        }
        return result;
    }

    public Boolean listen() throws Exception{
        if (clientSocket.isClosed()) {
            System.out.println("Connection Closed! Porgram Stopped");
            return false;
        }
        if (!clientSocket.isConnected()) {
            System.out.println("Connection Lost! Trying to reconnect!!!");
            this.start();
            if (this.listen()) {
                System.out.println("Connection Lost! reconnecting...");
                return true;
            }
            return false;
        }
        output = new PrintWriter(clientSocket.getOutputStream());
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output.println(encodeOwnData());                 //send own position of the ball
        output.flush();                                       //ensure the message was sent but not kept in the buffer
        String msg = input.readLine();                    //get a response of other player's position from the server
        ArrayList<Circle> decoded = decodeOtherData(msg);
        if (decoded == null) {
            return true;
        }
        BallGameClient.nextCircles = decoded;
        return true;
    }

    public void stop() throws Exception{ 
        System.out.println("Socket disconnected");
        input.close();
        output.close();
        clientSocket.close();
    }
}
