package cpe490game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
	
	private static final String serverAddress = "localhost";
	private static final int portNumber = 9001;
	private Socket player1,player2;
	private static BufferedReader input;
	private static PrintWriter output;
	private static ServerSocket serverSocket;
				
	public static void main(String args[]) throws IOException{
	
		System.out.println("Starting game server on port " + portNumber + "...");
		serverSocket = new ServerSocket(portNumber);
		
		try{
		while(true){
		GameLoop game = new GameLoop();
		GameLoop.Player player1 = game.new Player(serverSocket.accept(), 1);
		GameLoop.Player player2 = game.new Player(serverSocket.accept(), 2);
		player1.setOpponent(player2);
		player2.setOpponent(player1);
		game.currentPlayer = player1;
		}
		}finally{
			serverSocket.close();
		}
	}
	
	class GameLoop{
		
		Player currentPlayer;
		private int checkWinner(String player1Choice, String player2Choice){
			int result = 0;
			//player1 wins: result = 0 , player2 wins: result = 1, draw: result = 2
			
			switch(player1Choice){
			case "rock":
				if(player2Choice.equals("rock")){
					result = 2;
					break;
				}else{
					if(player2Choice.equals("paper")){
						result = 1;
						break;
					}
					else{
						result = 0;
					}
				}
				break;
			case "scissor":
				if(player2Choice.equals("rock")){
					result = 1;
					break;
				}else{
					if(player2Choice.equals("paper")){
						result = 0;
						break;
					}
					else{
						result = 2;
					}
				}
				break;
			case "paper":
				if(player2Choice.equals("rock")){
					result = 0;
					break;
				}else{
					if(player2Choice.equals("paper")){
						result = 2;
						break;
					}
					else{
						result = 1;
					}
				}
				break;
			default:
				break;
			}
			return result;
		}
	
		
	class Player extends Thread{
		
		private Socket player;
		private int playerNumber;
		private Player opponent;
		
		public Player(Socket player, int playerNumber){
			this.player = player;
			this.playerNumber = playerNumber;
			try{
				//Sends a message to the client to choose. Then closes the message. 
				input = new BufferedReader(new InputStreamReader(player.getInputStream()));
				output = new PrintWriter(player.getOutputStream(),true);
				output.println("Welcome "+ playerNumber);
				
			}catch(IOException err){
				System.out.println("player disconnected: ");
				err.printStackTrace();
			}
		}
		
		public void setOpponent(Player Opponent){
			this.opponent = Opponent; 
		}
		
		public void run(){
			//will only run once both players are connected 
			try{
				output.println("Both players connected");
				
				while(true){
					String playerChoice = input.readLine();
					if(playerChoice.toLowerCase().equals("quit")){
						return;
					}else{
						
					}
				}
			}catch(IOException err){
				err.printStackTrace();
			}finally{
				try {
					player.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	
	}
	
}
}


