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
		int globalPlayerNumber = 1;

		try{
			/*This while loop creates a new game object after 2 players have joined.
			 *The two players are Thread objects within that game object. 
			 *If two more players join it will create a new game object for them. 
			 */
			while(true){			
				GameLoop game = new GameLoop();
				System.out.println("Waiting on player " + globalPlayerNumber + "...");
				GameLoop.Player player1 = game.new Player(serverSocket.accept(), 1);
				System.out.println("Player " + globalPlayerNumber + " connected!");
				globalPlayerNumber++;
				System.out.println("Waiting on player " + globalPlayerNumber + "...");
				GameLoop.Player player2 = game.new Player(serverSocket.accept(), 2);
				System.out.println("Player " + globalPlayerNumber + " connected!");
				globalPlayerNumber++;

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
		//Logic for the switch variables responses: 
		//0 = draw, 1 = player 1 wins, 2 = player 2 wins.
		switch(player1Choice){
		case "rock":
			if(player2Choice.equals("rock")){
				winner = "0";
				break;
			}else{
				if(player2Choice.equals("paper")){
					winner = "2";
					break;
				}
				else{
					winner = "1";
				}
			}
			break;
		case "scissor":
			if(player2Choice.equals("rock")){
				winner = "2";
				break;
			}else{
				if(player2Choice.equals("paper")){
					winner = "1";
					break;
				}
				else{
					winner = "0";
				}
			}
			break;
		case "paper":
			if(player2Choice.equals("rock")){
				winner = "1";
				break;
			}else{
				if(player2Choice.equals("paper")){
					winner = "0";
					break;
				}
				else{
					winner = "2";
				}
			}
			break;
		default:
			break;
		}
	}

	private void setPlayerChoices(String Choice,Player player){
		System.out.println("Setting: "+ Choice + ", count: "+count);
		//if player 1, set flags
		if(player == currentPlayer){
			player1Choice = Choice;
			player.playerChoiceFlag = 1;
			player.opponent.opponentChoiceFlag = 1;
			count++;
			//if player 2, set flags
		}else{
			player2Choice = Choice;
			player.playerChoiceFlag = 1;
			player.opponent.opponentChoiceFlag = 1;
			count++;
		}
		//after both players have chosen. 
		if(count == 2){
			checkWinner(player1Choice,player2Choice);
			player1Choice = player2Choice = null;
			System.out.println("winner after check: "+ winner);
			count = 0;
		}
	}

	class Player extends Thread{

		private Socket player;
		private int playerNumber;
		private Player opponent;
		private BufferedReader input;
		private PrintWriter output;
		public int playerChoiceFlag = 0;
		public int opponentChoiceFlag = 0;

		public Player(Socket player, int playerNumber){
			this.player = player;
			this.playerNumber = playerNumber;
			try{
				//Sends a message to the client to choose. Then closes the message. 
				input = new BufferedReader(new InputStreamReader(player.getInputStream()));
				output = new PrintWriter(player.getOutputStream(),true);

				//Let the client know what player number they are.
				output.println(playerNumber);

			}catch(IOException err){
				System.out.println("player disconnected: ");
				err.printStackTrace();
			}
		}

		public void setOpponent(Player Opponent){
			this.opponent = Opponent; 
		}

		@Override
		public void run(){
			String socketInput;
			//will only run once both players are connected
			output.println("Opponent connected.");
			try{
				while(true){
					socketInput = input.readLine();
					System.out.println(playerNumber+ ": " + socketInput);
					if(socketInput.toLowerCase().equals("quit")){
						return;
					}
					else{
						if(playerChoiceFlag != 1){
							setPlayerChoices(socketInput, this);
							System.out.println("thread " + playerNumber);
						}
						if((playerChoiceFlag == 1 )&&(opponentChoiceFlag == 1)){
							System.out.println("Sending winner to: " + playerNumber);
							output.println(winner);
							System.out.println("Sending winner to: " + opponent.playerNumber);
							opponent.output.println(winner);
							playerChoiceFlag = 0;
							opponentChoiceFlag = 0;
							opponent.playerChoiceFlag = 0;
							opponent.opponentChoiceFlag = 0;
						}
					}
				}

			}catch(IOException err){
				System.out.println("Player " + playerNumber +" has disconnected.");
				//err.printStackTrace();
			}finally{
				try {
					System.out.println("Player " + playerNumber +" has quit. Socket Closed.");
					player.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
}
