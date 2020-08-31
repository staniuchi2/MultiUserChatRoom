import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatBot {
    private Socket BotSocket;
    private PrintWriter out;
    private BufferedReader in;//array that holds facts, just cycles through it
    private String[] FactArray = {"the entire world's population could fit inside LA","more people visit France than any other country",
            "the Canary Islands are named after dogs, not birds","there are only three countries in the world that don't use the metric system",
            "the hottest chili pepper in the world is so hot it could kill you","more people visit France than any other country",
            "four babies are born every second","Japan is the world's most earthquake prone country","only two countries use purple in their national flags",
            "Africa and Asia are home to nearly 90 percent of the world's rural population","People 60 years and older make up 12.3 percent of the global population"};
    private int counter = 0;

    public ChatBot(String address, int port){
        try {
            BotSocket = new Socket(address, port);
        }
        catch(IOException e){
            System.out.println("Unavailable port ");
            System.exit(1);
        }
    }
    public void go(){
        try {
            out = new PrintWriter(BotSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(BotSocket.getInputStream()));
            out.println("ChatBot");
            while (true) {
                String userRes = in.readLine();
                String[] userMsg = userRes.split(" : ", 2);// splits message into the user name and the actual contents of the message.
                userMsg[1].toLowerCase();
                if (userMsg[1].contains("fun fact") || userMsg[1].contains("fact")) {// if user inputs anything with fact in it, it just spits out fact.
                    out.println("hello " + userMsg[0] + " did you know " + FactArray[counter]);
                    counter++;
                    if (counter == 11) {//counter loops back around.
                        counter = 0;
                    }
                }
            }
        }
        catch(IOException e){//catches error when server shutsdown
            System.out.println("system exiting");
        }
    }

    private static void ConsoleInput(String[] ConsoleArg) throws IOException {
        if (ConsoleArg.length%2 == 1 || ConsoleArg.length > 4) { // same as client checks.
            System.out.println("invalid arguments ");
            System.exit(1);
        }
        else if (ConsoleArg.length == 0){
            new ChatBot("localhost", 14001).go();
        }
        int Port = 0;
        boolean PortFlag = false;
        String IP = "";
        boolean IPFlag = false;
        for (int counter = 0; counter < ConsoleArg.length; counter++) {
            if (ConsoleArg[counter].equals("-ccp")) { //detects for the code, the next value should be the port
                try {
                    Port = Integer.parseInt(ConsoleArg[counter + 1]);
                    PortFlag = true;
                } catch (Exception e) {
                    System.out.println("invalid arguments");
                    System.exit(1);
                }
            }
            else if (ConsoleArg[counter].equals("-cca")) { //the next value should be the ip
                IP = ConsoleArg[counter + 1];
                boolean valid = true;
                char[] IPcheck = IP.toCharArray();
                for (char c : IPcheck) {
                    valid = ((c >= '0') && (c <= '9')) ||
                            (c == '.');
                    if (!valid) {
                        System.out.println("invalid arguments");
                        System.exit(1);
                    }
                }
                IPFlag = true;
            }
        }

        if (PortFlag == true && IPFlag == true){ // if both flags are activated that means parameters for both were passed
            new ChatBot(IP,Port).go();
        }
        else if (PortFlag == true && IPFlag == false){ //flag for port only activated so default IP used
            new ChatBot("localhost",Port).go();
        }
        else if(PortFlag == false && IPFlag == true){// flag for UP only activated so default port used
            new ChatBot(IP, 14001).go();
        }
    }

    public static void main(String[] args) throws IOException{
        ConsoleInput(args);
    }

}
