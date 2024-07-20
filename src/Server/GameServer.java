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
			         "��"+(t.activeCount()-1)+  
			         "�ӳs��");   
			}			
		}catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public class Process implements Runnable{   
		  //�Ȧs��ƪ�Buffered
		BufferedReader reader;  
		  //�إߤ@��Socket�ܼ�  
		Socket sock;            
		  //----------------------------------------------------------//
		  //-3.1-�Ѱ�����I�s---�إ߱���
		  //----------------------------------------------------------//
		public Process(Socket cSocket)
		{
			try{
				sock = cSocket;
		      //���oSocket����J��Ƭy
				InputStreamReader isReader =        
				new InputStreamReader(sock.getInputStream()); 
		        
				reader = new BufferedReader(isReader);
		    }catch(Exception ex){
		    	System.out.println("�s������Process");
		    } 
		}
		
		public void run(){
		   String message;
		   try{
		       //Ū�����
			   	while ((message = reader.readLine())!=null){   
				   	System.out.println("����"+message);
				   	tellApiece(message);
		    	}
		   }catch(Exception ex){System.out.println("���@�ӳs�����}");}
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
					System.out.println("�s������Process");
				}
			}
		}
	}
	
	
	
	
	public static void main(String[] args) {
		GameServer gs = new GameServer();
		gs.setUpNetworkAndGetClient();

	}

}
