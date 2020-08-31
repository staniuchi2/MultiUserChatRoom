import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
//continuously runs and accepts users
public class Server {

    private BufferedReader userin;
    private ServerSocket Server;
    private ArrayList<ClientHandler> ClientList = new ArrayList<>(); //stores list of client handler objects

    public Server(int port) {
        try {
            Server = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("port unavailble, try new port");
            System.exit(1);
        }
    }

    public void go() {
        try {
            ExitStatus ExitCheck = new ExitStatus();
            ExitCheck.start();
            while(true) { // continuously accepts clients.
                System.out.println("Server listening");
                Socket ClientSocket = Server.accept();
                System.out.println("Server accepted connection on " + Server.getLocalPort() + " ; " + ClientSocket.getPort());
                ClientHandler ClientThread = new ClientHandler(ClientSocket, ClientList); //new instance of object created everytime user connects
                ClientList.add(ClientThread);                                   //passes client list through for braodcasting
                ClientThread.start();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ConsoleInput(String[] ConsoleArg) throws IOException {
        if (ConsoleArg.length == 1 || ConsoleArg.length > 2) { // has to have atleast 2 arguments but no more to fit valid format.
            System.out.println("invalid arguments ");
            System.exit(1);
        }
        else if (ConsoleArg.length == 0){
            new Server( 14001).go(); // if no arguments passed default port used.
        }
        int Port = 0;
        for (int counter = 0; counter < ConsoleArg.length; counter++) {
            if (ConsoleArg[counter].equals("-csp")) { // once code is detected following value should be the port.
                try {
                    Port = Integer.parseInt(ConsoleArg[counter + 1]); // if it is not a integer it is invalid.
                    new Server(Port).go();
                } catch (Exception e) {
                    System.out.println("invalid arguments");
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ConsoleInput(args);
    }
}
