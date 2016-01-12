/**
 * Horcrux class creates new horcruxes from a string 
 * each horcrux has a unique location, size, and image
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Horcrux {
	public int hx, hy, sizex, sizey, ptValue;
	//(hx, hy) make horcrux location pts
	//(sizex, sizey) size of horcrux
	//ptValue is how many points awarded after you collect the horcrux
	public Image horcPic; //horcrux picture

    public Horcrux(String inline){
    	String[] lets = inline.split(" ");
		hx = Integer.parseInt(lets[0]); //gets x coord of horcrux location
		hy = Integer.parseInt(lets[1]); //gets y coord of horcrux location
		int m = Integer.parseInt(lets[2]); //horcrux number
		horcPic = new ImageIcon("pics/icon_horcrux"+m+".png").getImage();
		sizex = Integer.parseInt(lets[3]); //gets sizex
		sizey = Integer.parseInt(lets[4]); //gets sizey
		ptValue = Integer.parseInt(lets[5]); //gets pt value
    }
    
    
}