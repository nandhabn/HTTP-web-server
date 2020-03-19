package server;


import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Server{
        
    ServerSocket ser;
    Socket sock;
    final private int dPort=80;
    final private String defaultServerPath="G:\\obliging";
    private int port=8080;
    private String serverPath=null;
   
    public Server(int port) throws IOException
        {
            serverPath=defaultServerPath;
            this.port=port;
            ser=new ServerSocket(port);
        }
    public Server(int port,String serverpath) throws IOException
        {
            serverPath=serverpath;
            this.port=port;
            ser=new ServerSocket(port);
        }
       
    public Server() throws IOException
    {
        serverPath=defaultServerPath;
        ser=new ServerSocket(dPort);
    }
    
        public void connect()
        {            
            try{
            Runnable rn;
            sock=ser.accept();
            if(sock!=null)
            {
            rn=new RunnableImpl(sock,getServerPath());
            new Thread(rn).start();
            }
            }
            catch(IOException a)
            {
                System.err.println(a.getStackTrace()[0]);
                
            }
            
        }
        public String getServerPath()
            {
                if (serverPath==null)
                {return defaultServerPath;}
                return serverPath;
            }
        
        //Server starts here
        public void startServer(){
            while(true)
            connect();    
        }
    
}
 


//runnable thread
class RunnableImpl implements Runnable {

    Socket sock;
    Scanner in;
    DataOutputStream out;
    FileOutputStream fout;
    FileInputStream fin;
    String serverPath;
    String PAGENOTFOUND;
    Request request;
    String head;
    String body;
    String WELCOME;
    

                public RunnableImpl(Socket sock,String serverPath) {
                    this.sock=sock;
                    this.serverPath=serverPath;
                    PAGENOTFOUND=serverPath+"\\notFound.html";
                    WELCOME=serverPath+"\\index.html";
                }   

    
    
    
    
    
            @Override
            public void run(){
                try {
                    in=new Scanner(sock.getInputStream());
                    out=new DataOutputStream(sock.getOutputStream());
                } catch (IOException ex) {}
            
                System.out.println("Connected");
                request=new Request(in);                
                if(request!=null)
                {
                    if(request.getMethod().equals("GET"))
                    {
                        System.out.println(request.getMethod()+" "+request.getPath());
                        doGet(request.getPath());
                    }
                }
                else
                {
                    System.err.println("reqest is empty");
                }
            }
            
            
        

//GET method 
        public void doGet(String path)
        {
            if(!request.getSrcType().equals(" image/png"))
            {
            server.Response res=null;
            try{
                if(path.equals("/"))
                {   
                    fin=new FileInputStream(WELCOME);
                    if(fin!=null)
                    {
                        res=new server.Response(fin,"HTTP/1.1 200 ok","text/html");  
                       
                    }
                }
                else
                {
                    fin=new FileInputStream(serverPath+path);
                    res=new server.Response(fin,"HTTP/1.1 200 ok",request.getSrcType());
                }
                out.write(res.toString().getBytes("UTF-8"));
                out.close();
            }catch(FileNotFoundException fnf)
            {  
                    try {
                        System.out.println("page not found");
                        fin=new FileInputStream(PAGENOTFOUND);
                        res=new server.Response(fin,"HTTP/1.1 404 pageNotFound","text/html");
                        out.write(res.toString().getBytes("UTF-8"));
                        } 
                    catch (FileNotFoundException ex){ex.getMessage();}
                    catch (UnsupportedEncodingException ex) {ex.getMessage();}
                    catch(IOException ex){ex.getMessage();}
            }
            catch(IOException ex)
            {
            System.out.println(ex.getMessage());
            }              
            }
            else
            {
               synchronized(this) { BufferedImage bi;
                try {
                    String Response;
                    Response="HTTP/1.1 200 ok\n";
                    Response+="Accept-range: bytes\n";
                    Response+="date:"+new Date().toString()+"\n";
                    Response+="content-type: "+request.getSrcType()+"; charset=UTF-8"+"\n";
                    Response+="\n";
                    out.write(Response.getBytes());
                    bi=ImageIO.read(new File(serverPath+path));
                    String ex=path.substring(path.indexOf(".")+1, path.length());
                    ImageIO.write(bi,ex, out);
                    out.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                }
            }
        }

}    