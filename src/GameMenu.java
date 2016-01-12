/**
 * @(#)Menu.java
 * Displays menu and allows user to choose to play game or view instructions
 *
 * @author 
 * @version 1.00 2015/1/8
 */

//imports stuff ye
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import java.applet.*;

public class GameMenu extends JFrame implements MouseListener{
	private int destx,desty; //coordinates of mouse location
	private Rectangle gameButton; //rectange where you click to start the game
	private Rectangle infoButton; //rectangle where you click to view instructions

	Game game; //Game you pass through
	Info howto; //info page of Info class
	
    public GameMenu(Game g, Info ht) {
    	super("Harry Potter and the | 3 Depths of Azkaban");
    	game = g;
    	howto = ht;
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	Image img = Toolkit.getDefaultToolkit().getImage("pics/main menu_bg.png"); //loads background and displays it 
    	ImageIcon background = new ImageIcon(img.getScaledInstance(1000,600,Image.SCALE_SMOOTH));
    	setContentPane(new JLabel(background));
    	
    	addMouseListener(this);
    	gameButton = new Rectangle(750, 400, 200, 100);
		infoButton = new Rectangle(750, 500, 200, 100);
    	
    	setLayout(null);
    
    	setResizable(false);
    	setSize(1006,628);
    	setVisible(true);
    	System.out.println(game);
    }
    // mouse listener//
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseClicked(MouseEvent e){}  
    	
    public void mousePressed(MouseEvent e){ //if mouse is pressed
		destx = e.getX(); //gets x and y coords of mouse
		desty = e.getY();	
		if (gameButton.contains(destx,desty)){ //if mouse pressed the gameButton button
			System.out.println("pressed, should init");
			game.initialize(); //creates a new game in Game
			game.setVisible(true); //sets it visible and sets menu invisible so you don't see it
    		setVisible(false);
		}
		
		if (infoButton.contains(destx,desty)){ //if mouse pressed the info button
			howto.setVisible(true); //sets instructions to visible
		} 
	}	
      
}  
    

    