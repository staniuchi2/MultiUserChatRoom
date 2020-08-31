import javax.sound.sampled.Port;
import java.io.*;
import java.net.Socket;


public class Client {

    private Socket socket;
    private BufferedReader userin;
    private PrintWriter out;

    public Client(String address, int port){
        try {
            socket = new Socket(address, port);
        }
        catch(IOException e){
            System.out.println("invalid port or IP address");
            System.exit(1);
        }
    }

    public void go() {
        try {
            ServerHandler  ServerWorker = new ServerHandler(socket);
            userin = new BufferedReader(new InputStreamReader(System.in));//takes input from user
            out = new PrintWriter(socket.getOutputStream(), true);
            String userInput = "";
            new Thread(ServerWorker).start();
            while(true) {
                userInput = userin.readLine();
                out.println(userInput);
            }

        } catch (Exception e) {//catches exception when server shutsdown
            System.out.println("error system exiting");
            System.exit(0);
        }
    }
    private static void ConsoleInput(String[] ConsoleArg) throws IOException {
        if (ConsoleArg.length%2 == 1 || ConsoleArg.length > 4) { //argument is invalid if there is only one parameter, also invalid if there is more than 4
            System.out.println("invalid arguments");
            System.exit(1);
        }
        else if (ConsoleArg.length == 0){ // if there are no parameters then use default IP and port
            new Client("localhost", 14001).go();
        }
        int Port = 0;
        boolean PortFlag = false; // flag to see if user passed parameters,
        String IP = "";
        boolean IPFlag = false;
        for (int counter = 0; counter < ConsoleArg.length; counter++) {
            if (ConsoleArg[counter].equals("-ccp")) { // detect code, value that follows should be the port
                try {
                    Port = Integer.parseInt(ConsoleArg[counter + 1]); //port needs to be a integer value, if not its invalid
                    PortFlag = true;
                } catch (Exception e) {
                    System.out.println("invalid arguments ");
                    System.exit(1);
                }
            }
            else if (ConsoleArg[counter].equals("-cca")) {//value that follows should be the IP
                IP = ConsoleArg[counter + 1];
                boolean valid = true;
                char[] IPcheck = IP.toCharArray();
                for (char c: IPcheck){
                    valid = ((c >= '0') && (c <= '9')) ||
                            (c == '.');
                    if (!valid){
                        System.out.println("invalid arguments");
                        System.exit(1);
                    }
                }
                IPFlag = true;
            }
        }

        if (PortFlag == true && IPFlag == true){ //if both flags are true means that user input port and port
            new Client(IP,Port).go();
        }
        else if (PortFlag == true && IPFlag == false){ // only port parameter taken, default iP used
            new Client("localhost",Port).go();
        }
        else if(PortFlag == false && IPFlag == true){ // only  IP parameter taken default port used.
            new Client(IP, 14001).go();
        }
    }


    public static void main(String[] args) throws IOException {
        ConsoleInput(args);

    }
}

