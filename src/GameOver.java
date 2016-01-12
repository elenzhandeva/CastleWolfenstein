/**
 * @(#)GameOver.java
 *
 * the page players see after they finish a game
 * displays score
 * goes back to main menu once the button is clicked
 */

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import java.applet.*;

public class GameOver extends JPanel implements MouseListener{
	private int destx,desty; //coordinates of mouse location
	Timer myTimer; //new timer to keep track of how long page should be visible
	Game si; //Spaceinvaders to pass through
	public int finalscore; //player's score
	Image endBG; //bg pic
	GameMenu mainmenu; //main menu page
	private Rectangle backButton; //rectange where you click to go back to main menu
	
	public static int timer;

	public GameOver(Game b, GameMenu g,int s, int t){
		super();
		si=b;
		
		finalscore=s;
		mainmenu = g;
		timer = 0; //set at 0
		endBG = Toolkit.getDefaultToolkit().getImage("pics/end menu_bg.png"); //gets the image with the Infotions on it

    	setLayout(null);
    	setVisible(true);
    	
    	addMouseListener(this);
    	backButton = new Rectangle(770, 500, 72, 50);
	}
   	
   	// mouse listener//
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseClicked(MouseEvent e){}  
    	
    public void mousePressed(MouseEvent e){ //if mouse is pressed
		destx = e.getX(); //gets x and y coords of mouse
		desty = e.getY();	
		if (backButton.contains(destx,desty)){ //if mouse pressed the gameButton button
			si.setVisible(false); //sets game to invisible
			si.setgamenull(); //set game to null (to start over)
			mainmenu.setVisible(true); //sets menu to visible
		}
	}	
		
	public void paintComponent(Graphics g){
		//WRITES THE SCORE
		g.drawImage(endBG, 0, 0, this); //draws bg image
		g.setColor(new Color(215,161,0)); //displays score
		g.setFont(new Font("Pristina", Font.PLAIN, 32));
		g.drawString("Game Over!",656,369);
		g.drawString("Your score is: ",656,399);
		g.drawString(""+finalscore,656,463);
	}
}