package cpe490game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Scanner;

public class MainClass extends Applet implements Runnable, MouseListener{
	
	private RockPaperScissors gameStart;
	private Scanner userInput;
	private URL base;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() {
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addMouseListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("CPE490 Alpha");
		try{
			base = getDocumentBase();
		}catch(Exception e){
			//TODO: handle exception
		}
	}

	@Override
	public void start() {
		gameStart = new RockPaperScissors();
		userInput = new Scanner(System.in);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void update(Graphics arg0) {
		// TODO Auto-generated method stub
		super.update(arg0);
	}

	public void repaint(){
		
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//registers after release
		/*TODO: this will set the player choice variable based on the image selected.*/
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//registers before release
		/*TODO: this will detect which image is being pressed and to update it with the image pressed icon. 
	     */
		
		System.out.println("Mouse pressed");
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		/*TODO: this will set the image back to normal*/
		System.out.println("Mouse released");
		repaint();
		
	}

	@Override
	public void run() {
		String player1Choice;
		String player2Choice;
		
		while(true){
			System.out.println("Player 1 pick: ");
			player1Choice = userInput.nextLine();
			System.out.println("Player 2 pick: ");
			player2Choice = userInput.nextLine();
			gameStart.setInputVariables(player1Choice.toLowerCase(), player2Choice.toLowerCase());
			gameStart.update();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}

/*THE PLAN:
 * 
 * clear play again flag
 * on mouse press change update graphic
 * on release update graphic
 * on click set flag that its been decided and set player variable (depending on if you are player 1 or 2)
 * wait till opposite player data has been sent
 * clear flag, check to see who won locally on devices
 * winner increments win count
 * display winner or loser and play again
 * if play again is selected set flag and send confirmation, wait for response from opponent
 * 
 * */
