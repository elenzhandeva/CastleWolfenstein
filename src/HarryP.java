/*Harry Class
 * - stores info for player (Harry Potter)
 * - function for when shot, reduces HP by bullet's damage points
 * - functions to move and change direction
 * - reset function to reset variables
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class HarryP {
	
	private int posHPx, posHPy, dirHPx, dirHPy, speed;		//position, direction vectors, speed
	private int sizeHPx, sizeHPy;		//size
	
	public ArrayList<Image> lmvt; 
	public ArrayList<Image> rmvt;
	public ArrayList<Image> umvt; 
	public ArrayList<Image> dmvt;
	
	private int health;		//health points
	private int spAtk;		//number of special attacks left (gets 5 from red potion)
	private boolean fast;	//whether HP has extra speed from blue potion
	private boolean invis;	//whether HP has invisibility from white potion

    public HarryP() {	//creates Harry for player
    	health = 100;
    	lmvt = new ArrayList<Image>();
    	rmvt = new ArrayList<Image>();
    	umvt = new ArrayList<Image>();
    	dmvt = new ArrayList<Image>();
    	
    	posHPx = 0;
    	posHPy = 0;
    	dirHPx = 0;
    	dirHPy = 1;
    	sizeHPx = 26;
    	sizeHPy = 31;
    	speed = 8;
    	
    	spAtk = 0;
    	fast = false;
    	invis = false;
    	
    	
    	for (int i = 1; i<5; i++){
    		/*Image left = new ImageIcon("../sprites_harry/hp_left"+i+".png").getImage();*/
    		Image left = new ImageIcon("pics/hp_left"+i+".png").getImage();
    		lmvt.add(left);
    		Image right = new ImageIcon("pics/hp_right"+i+".png").getImage();
    		rmvt.add(right);
    		Image up = new ImageIcon("pics/hp_back"+i+".png").getImage();
    		umvt.add(up);
    		Image down = new ImageIcon("pics/hp_forward"+i+".png").getImage();
    		dmvt.add(down);
    	}
    	
    }
    
    public int getX(){	//gets position
    	return posHPx;
    }
    public int getY(){
    	return posHPy;
    }
    public int getvx(){	//gets directional vectors
    	return dirHPx;
    }
    public int getvy(){
    	return dirHPy;
    }
    public int getsx(){	//gets size
    	return sizeHPx;
    }
    public int getsy(){
    	return sizeHPy;
    }
    public int gethealth(){	//gets health points
    	return health;
    }
    public void shot(Bullet b){	//reduces HP health points by bullet's damage
    	health -= b.getdamage();
    }
    public boolean isalive(){	//returns if HP is alive
    	if (health>0){
    		return true;
    	}
    	return false;
    }
    
    public boolean ispowerful(){	//returns whether HP has special attacks available
    	if (spAtk>0){
    		return true;
    	}
    	return false;
    }
    public void usepower(){	//when HP shoots with special attack, reduce number of special bullets left
    	spAtk -=1;
    }
    public void gotAtk(){	//when HP picks up special attack potion
    	spAtk = 5;
    }
    public int getspAtk(){	//returns num of special attacks left
    	return spAtk;
    }
    public boolean isfast(){	//returns if extra speed potion was picked up
    	return fast;
    }
    public boolean isinvis(){	//returns if invisibility potion was picked up
    	return invis;
    }
    public void gotSpeed(){		//when HP picks up speed potion
    	if (invis){		//can only have either or
    		invis = false;
    	}
    	fast = true;
    }
    public void gotInvis(){		//when HP picks up invisibility potion
    	if (fast){		//can only have either or
    		fast = false;
    	}
    	invis = true;
    }
    public int getspeed(){		//returns HP's speed
    	if (fast){
    		return speed*2;
    	}
    	return speed;
    }
    
    public void setpos(int x, int y){
    	posHPx = x;
    	posHPy = y;
    }
    
    public void changedir(boolean r, boolean l, boolean u, boolean d){		//changes HP's directions
    	
    	if (r==true && l==false){
    		dirHPx = 1;
    	}
    	else if (l==true && r==false){
    		dirHPx = -1;
    	}
    	else{
    		dirHPx = 0;
    	}
    	
    	if (u==true && d==false){
    		dirHPy = -1;
    	}
    	else if (d==true && u==false){
    		dirHPy = 1;
    	}
    	else{
    		dirHPy = 0;
    	}
    }
    
    public void move(boolean[] keys){	//moves HP according to keys pressed
    	if (keys[KeyEvent.VK_RIGHT]){
    		posHPx += 1;
    	}
    	if (keys[KeyEvent.VK_LEFT]){
    		posHPx -= 1;
    	}
    	if (keys[KeyEvent.VK_UP]){
    		posHPy -= 1;
    	}
    	if (keys[KeyEvent.VK_DOWN]){
    		posHPy += 1;
    	}
    }
     
    public void reset(){	//resets all of Harry's info for new level
    	posHPx = 0;
    	posHPy = 0;
    	dirHPx = 0;
    	dirHPy = 1;
    	sizeHPx = 26;
    	sizeHPy = 31;
    	speed = 8;
    	
    	spAtk = 0;
    	fast = false;
    	invis = false;
    }
}












