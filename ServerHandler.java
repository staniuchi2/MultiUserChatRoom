import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//thread that continuously receives messages from the servers.
public class ServerHandler extends Thread{
    private Socket ServerSocket;
    private BufferedReader in;
    public ServerHandler(Socket socket) throws IOException {
        this.ServerSocket = socket;
        in = new BufferedReader(new InputStreamReader(ServerSocket.getInputStream()));
    }
    public void run(){
        try{
            System.out.println("Enter user name: ");//prompts user.
            while(true){ // continuously takes in messages sent from other users, broad casted by the server.
                String serverRes = in.readLine();
                System.out.println(serverRes);
            }
        }
        catch(IOException e){//catches error when server shutsdown
            System.out.println("system exiting");
            System.exit(0);
        }
        finally {
            try{
                in.close();
                System.exit(1);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
