package cpe490game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
	
	private static final int portNumber = 9001;
	private static ServerSocket serverSocket;
				
	public static void main(String args[]) throws IOException{
	
		System.out.println("Starting game server on port " + portNumber + "...");
		serverSocket = new ServerSocket(portNumber);
		
		try{
		while(true){
		GameLoop game = new GameLoop();
		System.out.println("Waiting on player 1...");
		GameLoop.Player player1;
		player1 = game.new Player(serverSocket.accept(), 1);
		System.out.println("Player 1 connected!");
		System.out.println("Waiting on player 2...");
		GameLoop.Player player2;
		player2 = game.new Player(serverSocket.accept(), 2);
		System.out.println("Player 2 connected!");
		
		player1.setOpponent(player2);
		player2.setOpponent(player1);
		game.currentPlayer = player1;
		player1.start();
		player2.start();
		
		}
		}finally{
			serverSocket.close();
		}
	}
}
	
class GameLoop{
		
		Player currentPlayer;
		String player1Choice,player2Choice;
		String winner;
		int count = 0;
		
		private void checkWinner(String player1Choice, String player2Choice){			
			switch(player1Choice){
			case "rock":
				if(player2Choice.equals("rock")){
					winner = "Draw";
					break;
				}else{
					if(player2Choice.equals("paper")){
						winner = "Player 2 wins!";
						break;
					}
					else{
						winner = "Player 1 wins!";
					}
				}
				break;
			case "scissor":
				if(player2Choice.equals("rock")){
					winner = "Player 2 wins!";
					break;
				}else{
					if(player2Choice.equals("paper")){
						winner = "Player 1 wins!";
						break;
					}
					else{
						winner = "Draw";
					}
				}
				break;
			case "paper":
				if(player2Choice.equals("rock")){
					winner = "Player 1 wins!";
					break;
				}else{
					if(player2Choice.equals("paper")){
						winner = "Draw";
						break;
					}
					else{
						winner = "Player 2 wins!";
					}
				}
				break;
			default:
				break;
			}
		}
		
		private void setPlayerChoices(String Choice,Player player){
			if(player == currentPlayer){
				player1Choice = Choice;
				count++;
			}else{
				player2Choice = Choice;
				count++;
				checkWinner(player1Choice,player2Choice);
			}
		}
		
	class Player extends Thread{
		
		private Socket player;
		private int playerNumber;
		private Player opponent;
		private BufferedReader input;
		private PrintWriter output;
		
		public Player(Socket player, int playerNumber){
			this.player = player;
			this.playerNumber = playerNumber;
			try{
				//Sends a message to the client to choose. Then closes the message. 
				input = new BufferedReader(new InputStreamReader(player.getInputStream()));
				output = new PrintWriter(player.getOutputStream(),true);
				output.println("player " + playerNumber + " has connected");
				
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
						setPlayerChoices(playerChoice, this);
						if(count==2){
							output.println(winner);
							count = 0;
						}
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
