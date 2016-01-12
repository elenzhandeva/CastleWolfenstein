/*Info.java
 *Lynn Liu
 *Sets up and displays info page
 */
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import java.applet.*;

public class Info extends JFrame implements  MouseListener {
	private int destx,desty; //coordinates of mouse position
	private Rectangle backButton = new Rectangle(855, 560, 145, 51); //the button you must press to close window and go back to the main menu
	Game mainmenu; //main menu page
	
    public Info() {
    	super ("Harry Potter and the | 3 Depths of Azkaban");
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	Image img = Toolkit.getDefaultToolkit().getImage("pics/info_bg.png"); //gets the bg image and displays it in panel
    	ImageIcon background = new ImageIcon(img.getScaledInstance(1000,600,Image.SCALE_SMOOTH));
		setContentPane(new JLabel(background));
		
    	addMouseListener(this);
    	
    	setLayout(null);
    	setSize(1006,628);
    	setResizable(false);
    }
	
	// mouse listener gets mouse actions yey//
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseClicked(MouseEvent e){}  
    	
    public void mousePressed(MouseEvent e){ //if mouse presses 
    	System.out.println("pressed");
		destx = e.getX();
		desty = e.getY();	
		if (backButton.contains(destx,desty)){ //if mouse is pressed on backbutton
			System.out.println("hide");
			setVisible(false); //sets game to invisible so that user can see the main menu again
		}
		if (backButton.contains(destx, desty)==false){
			System.out.println(destx+", "+desty);
		}
	}	
	
	public static void main (String [] args){
		Info inf = new Info();
	}
} 