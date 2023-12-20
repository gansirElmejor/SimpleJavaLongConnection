//imports for network communication
import java.io.*;
import java.net.*;
import java.util.ArrayList;

class MultiThreadedServer {
    final int PORT = 5000;       
    
    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter output;
    BufferedReader input;
    int clientCounter = 0;
    static public ArrayList<PlayerBall> plrBalls = new ArrayList<PlayerBall>();
    
    public static void main(String[] args) throws Exception{ 
        MultiThreadedServer server = new MultiThreadedServer();
        server.go();
    }
    
    public void go() throws Exception{ 
        //create a socket with the local IP address and wait for connection request       
        System.out.println("server up, awaiting connection ...");
        serverSocket = new ServerSocket(PORT);                //create and bind a socket
        while(true) {
            clientSocket = serverSocket.accept();             //wait for connection request
            clientCounter = clientCounter +1;
            System.out.println("Client "+clientCounter+" connected");
            Thread connectionThread = new Thread(new ConnectionHandler(clientSocket));
            connectionThread.start();                         //start a new thread to handle the connection
        }
    }
    
//------------------------------------------------------------------------------
    class ConnectionHandler extends Thread { 
        Socket socket;
        PrintWriter output;
        BufferedReader input;
        
        public ConnectionHandler(Socket socket) { 
            this.socket = socket;
        }
        
        private int searchBall(ArrayList<PlayerBall> tempArr, String clientName) {
            for (int i=0; i<tempArr.size(); i++) {
                PlayerBall currBall = tempArr.get(i);
                if (currBall.getName() == clientName) {
                    return i;
                }
            }
            return -1;
        }

        synchronized private String arrDataEncode(String clientName,String clientData) {
            ArrayList<PlayerBall> tempArr = (ArrayList<PlayerBall>)plrBalls.clone();
            int indexOfBall = searchBall(tempArr, clientName);
           
            String[] clientDataSpt = clientData.split(" ");
            if (!clientDataSpt[0].equals("200")) {return "400";}
            if (indexOfBall != -1){
                tempArr.remove(indexOfBall);
                PlayerBall playerBall = plrBalls.get(indexOfBall);            
                playerBall.setXY(Integer.parseInt(clientDataSpt[1]), Integer.parseInt(clientDataSpt[2]));
                playerBall.setRad(Integer.parseInt(clientDataSpt[3]));
            } else {
                PlayerBall newBall = new PlayerBall(clientName,Integer.parseInt(clientDataSpt[1]), Integer.parseInt(clientDataSpt[2]), Integer.parseInt(clientDataSpt[3])); // change this to random;
                plrBalls.add(newBall);
            };
            String result = "200";
            for (int i = 0; i<tempArr.size(); i++) {
                PlayerBall currBall = tempArr.get(i);
                result = result + "&" +currBall.getName() + " " + currBall.getAlive() + " " + currBall.getX() + " " + currBall.getY() + " " + currBall.getRad();
            }
            return result;
        }
        
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
                String clientIpAddress = socketAddress.getAddress().getHostAddress();
                //receive a message from the client
                while (true) {
                    try{
                        String msg = input.readLine();
                        System.out.println("Message from: "+clientIpAddress+" : " + msg);
                        output.println(arrDataEncode(clientIpAddress,msg));
                        output.flush(); 
                    } catch(SocketException err) {
                        break;
                    }
                    
                }
                        
                //after completing the communication close the streams but do not close the socket!
                input.close();
                output.close();
            }catch (IOException e) {e.printStackTrace();}
        }
    }    
}

class PlayerBall{
    private String name;
    private int x;
    private int y;
    private int rad;
    private Boolean alive = true;
    //Could have color, rtc
    PlayerBall(String name, int x, int y, int rad) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.rad = rad;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setRad(int rad) {this.rad = rad;};
    public int getRad() {return this.rad;};
    public int getX() {return this.x;};
    public int getY() {return this.y;};
    public Boolean getAlive() {return this.alive;};
    public String getName() {return this.name;};
    public void setAlive(Boolean a) {this.alive = a;};
}
