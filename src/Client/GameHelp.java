package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameHelp {
	
	
	protected Charater Player = null;
	protected JFrame mFrame;
	protected JButton mButton;
	private final static String GAME_TITLE = "Click it!!";
	private final static String BUTTON_TXT = "Click me!";
	private final static String HOST_NAME = "127.0.0.1";
	private final static int HOST_PORT = 12345;
	private Socket socket;
	private final static String SAVE_FILENAME = "player.ser";
	private BufferedReader  reader;           
	private PrintWriter  writer;
	
	public GameHelp(){
		this.EstablishConnection();
	}
	
	protected void setUpGUI(){
		mFrame = new JFrame(GAME_TITLE);
		mButton = new JButton(BUTTON_TXT);
		mFrame.setSize(400,400);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.getContentPane().add(BorderLayout.CENTER ,mButton);
		mFrame.setVisible(true);
		mButton.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) 
			{ 
				Player.CheckLeveUp(Player.Level,Player.Experience);
				Player.Experience = Player.Experience + Player.attack(Player.Damage);
				System.out.println(Player.toString());
			} 
		}); 
		
	}
	
	protected  void SelectJob() {	
		Scanner sin = new Scanner(System.in);
		String selectjob = null;
		System.out.println("select job Warrior = 1 or Mage = 2");
		
		
		switch(sin.nextInt()){
			case 1:
				System.out.println("select warrior!");	
				this.Player = new Warrior();
				break;
			case 2:
				System.out.println("select Mage! But does implement Mage so select Warrior");	
				this.Player = new Warrior();
				break;				
			default: 
				System.out.println("Default select warrior!");	
				this.Player = new Warrior();
				break;
		}
				
	}
	
	protected void MainMenu(){
		Scanner sin = new Scanner(System.in);
		System.out.println("Input 1or2   1:Starts new game 2:Load game");
		
		switch(sin.nextInt()){
		case 1:
			System.out.println("Select start new game!");
			SelectJob();
			break;
		case 2:
			System.out.println("Select load game!");	
			Player = LoadCharater();
			break;				
		default: 
			System.out.println("Good bye");	
			System.exit(1);
			break;
		}
	}
	
	Runnable mRunnable = new Runnable() {	
		private static final String QUITKEY = "quit";
		private static final String SAVEGAMEKEY = "save";	
		@Override
		public void run() {
			Scanner sin = new Scanner(System.in);
			
			while(sin.hasNext()){
				String tmp = sin.nextLine();
				switch(tmp){
					case QUITKEY:
						System.out.println("Good bye");
						System.exit(1);
						break;
					case SAVEGAMEKEY:
						System.out.println("Saving wait a second");
						SaveCharater(Player);
						break;
					default:
						SendMessagToServer(tmp);
						break;
				}
			}
		}
	};
	
	private void EstablishConnection(){
		try{
			socket = new Socket(HOST_NAME, HOST_PORT);
			OutputStreamWriter oupput_stream = new OutputStreamWriter(socket.getOutputStream());
			InputStreamReader input_stream = new InputStreamReader(socket.getInputStream()); 
			writer = new PrintWriter(oupput_stream);
			reader = new BufferedReader(input_stream);
			System.out.println("Establish Connection Success!");
		}catch (IOException e) {
			System.out.println("Establish Connection Failed!");
		}
	}
	
	protected Runnable IncomingReader = () -> {String message;
		try{
			while ((message = reader.readLine()) != null){
				System.out.println(message);
			}
		}catch(Exception ex ){ex.printStackTrace();} 
	};
	
	protected void SendMessagToServer(String message){
		try{
			writer.println(message);
			writer.flush();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Failed to send mssage");
			try{
				socket = new Socket(HOST_NAME, HOST_PORT);
				OutputStreamWriter oupput_stream = new OutputStreamWriter(socket.getOutputStream());
				InputStreamReader input_stream = new InputStreamReader(socket.getInputStream()); 
				writer = new PrintWriter(oupput_stream);
				reader = new BufferedReader(input_stream);
				System.out.println("Reconneted success ");
				writer.println(message);
				writer.flush();
				System.out.println("Resend success ");
				
			}catch (Exception ex) {
				// TODO: handle exception
				System.out.println("Reconneted failed");
			}
						
		}
	}
	
	
	protected void SaveCharater (Charater player){
		 try {
	            FileOutputStream fos = new FileOutputStream(SAVE_FILENAME);
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(player);
	            oos.flush();
	            oos.close();
	            System.out.println("Saving  : "  + player.toString());
	        } catch (Exception e) {
	        	System.out.println("Save faild");
	        }
	}
	
	protected Charater LoadCharater (){
		try {
            Charater player;
            FileInputStream fis = new FileInputStream(SAVE_FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            player = (Charater) ois.readObject();
            ois.close();
            System.out.println("Load success : " + player.toString());
            return player;
        } catch (Exception e) {
        	System.out.println("Load faild");
        	return null;
        }
		
	}
	
	
	
	public static void main(String[] args){
		GameHelp gh = new GameHelp();
		gh.MainMenu();
		Thread maint = new Thread(gh.mRunnable);
		maint.start();
		gh.setUpGUI();
		Thread chatt = new Thread(gh.IncomingReader);
		chatt.start();
	}
}
