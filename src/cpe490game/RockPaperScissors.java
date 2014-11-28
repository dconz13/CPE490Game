package cpe490game;

public class RockPaperScissors {

	private String player1Choice;
	private String player2Choice;
	
	public void setInputVariables(String player1, String player2){
		player1Choice = player1;
		player2Choice = player2;
	}

	private int checkWinner(){
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
	
	public void update(){
		int winner = -1;
		winner = checkWinner();
		
		if(winner == 2){
			System.out.println("Draw");
		}
		else{
			if(winner == 1){
				System.out.println("Player 2 wins");
			}
			else{
				System.out.println("Player 1 wins");
			}
		}
	}
}
