/*DEATHEATER CLASS:
 * - contains information about the enemies and their sprites
 * - manages movement and changing direction
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class DeathEater {

	private double posx, posy;	//position
	private double vx, vy;		//direction vectors
	private int sizex, sizey;	//size
	private int speed, health;		//speed it moves
	private String type;		//draco, narc, or lucius
	private int damage;			//amount of damage bullets do to HP

	private boolean stalk;		//whether DE is stalking
	private int destx, desty;	//if stalking, stores last place seen HP
	
	private double tempvx, tempvy;	//stores previous direction vectors
	private int killPts;			//points HP earns when killed
	
	private static int count=0;
	public int id;
	private static HashMap<String, Image>imageStore= new HashMap<String, Image>();
	public ArrayList<Image> lmvt = new ArrayList<Image>(); 
	public ArrayList<Image> rmvt = new ArrayList<Image>(); 
	public ArrayList<Image> umvt = new ArrayList<Image>();  
	public ArrayList<Image> dmvt = new ArrayList<Image>(); 
	
	String [] moves = {"back","left","right","forward"};
	
	public Image getImage(String img){
	    Image pic;
	    if(!imageStore.keySet().contains(img)){
			imageStore.put(img,new ImageIcon(img).getImage());
	    }
	    pic = imageStore.get(img);
		return pic;
	}
	
	public DeathEater(String inline){	//takes str and creates DE
		String[] lets = inline.split(" ");
		posx = Integer.parseInt(lets[0]);
		posy = Integer.parseInt(lets[1]);
		vx = Integer.parseInt(lets[2]);
		vy = Integer.parseInt(lets[3]);
		type = lets[4];
		
		id = ++count;
		sizex = 26;
		sizey = 32;
		stalk = false;
		destx = 0;
		desty = 0;
		tempvx = 0;
		tempvy = 0;
		killPts = 10;
		speed = 1;
		damage = 10;
		health = 15;
		
		if (type.equals("draco")){
			killPts = 1000;
			speed = 2;
			damage = 20;
			health = 15;
		}
		if (type.equals("narc")){
			killPts = 2500;
			speed = 3;
			damage = 25;
			health = 30;
		}
		if (type.equals("lucius")){
			killPts = 5000;
			speed = 5;
			damage = 40;
			health = 50;
		}
		
		for (String m : moves){
			for (int i = 1; i<5; i++){
	    		/*Image left = new ImageIcon("../sprites_harry/hp_left"+i+".png").getImage();*/
	    		Image im = getImage("pics/"+type+"_"+m+i+".png");
	    		if (m.equals("back")){
	    			umvt.add(im);
	    		}
	    		else if (m.equals("left")){
	    			lmvt.add(im);
	    		}
	    		else if (m.equals("right")){
	    			rmvt.add(im);
	    		}
	    		else if (m.equals("forward")){
	    			dmvt.add(im);
	    		}	
	    	}
		}
		
	}

//returns the coordinates as integers
	public int getX(){
		return (int)posx;
	}
	public int getY(){
		return (int)posy;
	}
//returns the coordinates as doubles
	public double getDX(){
		return posx;
	}
	public double getDY(){
		return posy;
	}
//returns the velocities (x, y components)
	public double getvx(){
		return vx;
	}
	public double getvy(){
		return vy;
	}
//sets the velocities
	public void setvx(double newvx){
		vx = newvx;
	}
	public void setvy(double newvy){
		vy = newvy;
	}
//returns speed at which DE is travelling
	public int getspeed(){
		return speed;
	}
//returns the coordinates of the last place HP was seen
	public int getdestx(){
		return destx;
	}
	public int getdesty(){
		return desty;
	}
//returns physical size of itself
	public int getsx(){
		return sizex;
	}
	public int getsy(){
		return sizey;
	}
//returns amount of damage the type of DE does to HP
	public int getdamage(){
		return damage;
	}
//returns the pts awarded to player when a DE is killed
	public int getkillPts(){
		return killPts;
	}
//returns T/F on whether DE is stalking
	public boolean isstalking(){
		return stalk;
	}

//sets up stalking mode
	public void startstalking(int x, int y){
		stalk = true;
		destx = x;
		desty = y;
		
		tempvx = vx;
		tempvy = vy;
		
		resetdir();	//calculates vx, vy using a target location (HP's coords or last place saw Harry
	}
	//stops stalking mode (resets to previous statel
	public void stopstalking(){
		stalk = false;
		vx = tempvx;
		vy = tempvy;
	}
	//reduces health points when shot
	public void shot(Bullet b){
		health-= b.getdamage();
	}
	//checks if still alive
	public boolean isalive(){
		if (health>0){
			return true;
		}
		return false;
	}
	//calculates new direction for DE to continue headed for taget destination
	public void resetdir(){
		double dist = Math.sqrt(Math.pow(destx - posx, 2)+Math.pow(desty - posy, 2));
		if (dist!=0){	//division by 0
			vx = ((destx - posx)/dist);
			vy = ((desty - posy)/dist);
		}
		else{
			stopstalking();
		}
	}
	public boolean orthdest(){		//checks when either vx or vy <=1;
		if (posx-destx<=0.1 || posy-desty<=0.1){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return String.format("DE: x:%.1f y:%.1f VX:%.1f VY:%.1f Speed:%d", posx,posy,vx,vy,speed);
	}
//regular moving or moving specified velocities
	public void move(){
		posx+=vx;
		posy+=vy;
	}
	public void move(double movex, double movey){
		posx = (posx+movex);
		posy = (posy+movey);
	}
	//flips direction DE is travelling in when bumbed into a wall
	public void changedir(){
		vx*=-1;
		vy*=-1;
	}	
	
}