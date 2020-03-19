package server;

import java.io.IOException;



public class Start {

    public static void main(String[] args) {
        
        Runnable server;
        server = new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    Server Obse = new Server();
                            Obse.startServer();
                } 
                catch (IOException ex){System.err.println(ex);}   
            }
        };
        
        new Thread(server).start();
        System.out.println("OBSE is running...");
    }
    
}
