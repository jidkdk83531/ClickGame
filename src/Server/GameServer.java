package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class GameServer {

	protected ServerSocket server;
	protected Vector output;
	
	protected void setUpNetworkAndGetClient(){
		try {
			output = new Vector();  
			server = new ServerSocket(12345);
			System.out.println("Server is created waiting connection!!");
			while(true){
				Socket cSocket = server.accept();
				System.out.println("Got a connetion!!");
				
				PrintStream writer = new PrintStream(cSocket.getOutputStream());  
				System.out.println(writer); 
				output.add(writer);
				Thread t = new Thread(new Process(cSocket)); 
			    t.start();           
			    
			    System.out.println(cSocket.getLocalSocketAddress()+ 
			         "有"+(t.activeCount()-1)+  
			         "個連接");   
			}			
		}catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public class Process implements Runnable{   
		  //暫存資料的Buffered
		BufferedReader reader;  
		  //建立一個Socket變數  
		Socket sock;            
		  //----------------------------------------------------------//
		  //-3.1-由執行緒呼叫---建立接收
		  //----------------------------------------------------------//
		public Process(Socket cSocket)
		{
			try{
				sock = cSocket;
		      //取得Socket的輸入資料流
				InputStreamReader isReader =        
				new InputStreamReader(sock.getInputStream()); 
		        
				reader = new BufferedReader(isReader);
		    }catch(Exception ex){
		    	System.out.println("連接失敗Process");
		    } 
		}
		
		public void run(){
		   String message;
		   try{
		       //讀取資料
			   	while ((message = reader.readLine())!=null){   
				   	System.out.println("收到"+message);
				   	tellApiece(message);
		    	}
		   }catch(Exception ex){System.out.println("有一個連接離開");}
		}
		
		public void tellApiece(String message){
			   
			Iterator it = output.iterator(); 
			while(it.hasNext()){          
				try{
					PrintStream writer = (PrintStream) it.next(); 
					writer.println(message); 
					writer.flush();           
				}
				catch(Exception ex){
					System.out.println("連接失敗Process");
				}
			}
		}
	}
	
	
	
	
	public static void main(String[] args) {
		GameServer gs = new GameServer();
		gs.setUpNetworkAndGetClient();

	}

}
