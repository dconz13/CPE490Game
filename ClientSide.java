package cpe490game;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ClientSide extends Applet implements Runnable, ActionListener{

	Button r,p,s,q;
	
	private static final int PNUM = 9001;
	private static String sAddr = "54.149.127.254";
	private static Socket socket;
	private static BufferedReader input;
	private static PrintWriter output;
	private static InputStreamReader strread;
	private int FLAG = 0;
	private String playerNum;
	private Image result_img;
	public ClientSide() throws IOException {
		connection();
		checkPlayerNumber();
		opponentConnected();
	}
	
	public void init(){		
		setSize(350,350);
		setBackground(Color.GRAY);
		setFocusable(true);
		
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("CPE490 Game");
		
		r = new Button("Rock");
		p = new Button("Paper");
		s = new Button("Scissor");
		q = new Button("Quit");
		add(r);
		add(p);
		add(s);
		add(q);
		r.addActionListener(this);
		p.addActionListener(this);
		s.addActionListener(this);
		q.addActionListener(this);
	}
	
	public void connection() throws IOException{
		try {
			socket = new Socket(sAddr, PNUM);
			strread = new InputStreamReader(socket.getInputStream());
			input = new BufferedReader(strread);
			output = new PrintWriter(socket.getOutputStream(),true);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkPlayerNumber() throws IOException{
		playerNum = input.readLine();
		System.out.println("You are player: " + playerNum);
	}
	
	public void opponentConnected() throws IOException{
		while(true){
			if(input.readLine().equals("Opponent connected.")){
				System.out.println("Your opponent has connected\n");
				break;
			}			
		}
	}
	
	public void paint (Graphics g){
		g.drawImage(result_img, 40, 40, this);
	}
	
	public void resultImg(String I){
		if(I.equals("win")){
			result_img = getImage(getCodeBase(), "pics/youwin.gif");
		}
		else if(I.equals("lost")){
			result_img = getImage(getCodeBase(), "pics/youlose.gif");
		}
		else if(I.equals("tie")){
			result_img = getImage(getCodeBase(), "pics/youtied.gif");
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == q){
			System.out.print("Pressed Quit. You will now be disconnected\n");
			output.println("quit");
			System.exit(0);
		}
		else if(FLAG == 0){
			if(arg0.getSource() == r){
				System.out.print("Pressed Rock\n");
				output.println("rock");
			}
			else if(arg0.getSource() == p){
				System.out.print("Pressed Paper\n");
				output.println("paper");
			}
			else if(arg0.getSource() == s){
				System.out.print("Pressed Scissors\n");
				output.println("scissor");
			}	
			FLAG = 1;
			try {
				checkWin();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void checkWin() throws IOException{
		String hold = input.readLine();
			if(playerNum.equals(hold)){
				System.out.print("You won!\n");
				resultImg("win");
			}
			else if(hold.equals("0")){
				System.out.print("Draw!\n");
				resultImg("tie");
			}
			else{
				System.out.print("You lost!\n");
				resultImg("lost");
			}
			FLAG = 0;
			repaint();
	}
	
	public static void main(String args[]){
		try {
			ClientSide client = new ClientSide();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
