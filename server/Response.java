package server;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Scanner;


public class Response {
private String Response;
private Scanner s;



    
    Response(FileInputStream fin,String code,String sourseType)
    {
        String tp = "";
        s = new Scanner(fin);
        
        Response=code+"\n";
        Response+="Accept-range: bytes\n";
        Response+="date:"+new Date().toString()+"\n";
        Response+="content-type: "+sourseType+"; charset=UTF-8"+"\n";
        Response+="\n";
        
        while(s.hasNext())
        Response+=(tp=s.nextLine());
        
    }
    
@Override
    public String toString()
    {
    return Response;
    }
}
