package server;

import java.util.Scanner;
import java.util.StringTokenizer;


public class Request {

    private StringTokenizer req;
    private String method;
    private String head;
    private String path;
    String sourceType;
    
    
    public Request(Scanner req) 
    {
        synchronized(this){
        String temp="";
        Boolean b;
        while(b=req.hasNextLine())
        {
           
                String temp2=req.nextLine();
                temp+=temp2;
                try{
                if(temp2.contains("Accept:"))
                    sourceType=temp2.substring(temp2.indexOf(":")+1,temp2.indexOf(",",temp2.indexOf(":")));
                }catch(IndexOutOfBoundsException ex)
                {sourceType="text/javascript";}
                    
                if(temp2.equals(""))
                {
                    break;
                }
        }   
        System.out.println(sourceType);
        System.out.println("finish");
        this.req=new StringTokenizer(temp);
        method=this.req.nextToken();
        path=this.req.nextToken();
        }
    }   
    final public String getMethod()
    {
    return method;
    }
    
    final public String getPath()
    {
    return path;
    }
    
    public String  getSrcType()
    {   
        if(sourceType.isEmpty())
            return "text/javascript";
        if(sourceType.equals(" image/webp"))
            return sourceType=" image/png";
        return sourceType;
    }
    
}