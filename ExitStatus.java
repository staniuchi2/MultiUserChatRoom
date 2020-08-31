import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// a thread that continuously waits for user to enter EXIT and shuts the server down.
public class ExitStatus extends Thread{

    private BufferedReader userin;

    public  ExitStatus(){}

    public void run() {
        userin = new BufferedReader(new InputStreamReader(System.in));
        try{
            while(true) {
                String UserExit = userin.readLine();
                if (UserExit.equals("EXIT")) {
                    System.out.println("shutting down");
                    System.exit(1);
                }
            }
        }
        catch(IOException e){
            System.out.println("error");
        }
    }
}
