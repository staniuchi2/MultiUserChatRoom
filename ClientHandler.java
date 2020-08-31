import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
//thread that continuously receives meessages from client.
public class ClientHandler extends Thread{
    private Socket ClientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String UserName;
    private ArrayList<ClientHandler> ClientList;
    private Boolean StartChat = true;//true if first time program is run

    public ClientHandler(Socket socket, ArrayList<ClientHandler> ClientList) throws IOException{
        this.ClientSocket = socket;
        in  =  new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        out = new PrintWriter(ClientSocket.getOutputStream(),true);
        this.ClientList = ClientList;
    }
    public void run() {
        try{
            String userInput = "";
            while(true){
                userInput = in.readLine();
                if(StartChat){// prompts user to enter name, first thing they type is set to name.
                    UserName = userInput;
                    StartChat = false;
                }
                else {
                    userInput = getUserName() + " : " + userInput;
                    BroadcastMessage(userInput);
                }
            }
        } catch (IOException e) {//catches exception when server shutsdown
            System.out.println("error system exiting");
            System.exit(0);
        }
    }
    public String getUserName(){
        return UserName;
    }//returns username
    private void BroadcastMessage(String Msg){
        for (ClientHandler client : ClientList){
            client.out.println(Msg); //iterates through client list, sends the message to all users connected. Broadcasts.
        }
    }

}
