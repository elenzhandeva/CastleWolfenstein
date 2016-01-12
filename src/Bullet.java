/*BULLET CLASS:
 * - contains info for Bullets
 * - function to move
 * - function checking if bullet collides with an object
 */


import java.awt.Point;


public class Bullet {
	private double dx, dy, vx, vy;		//position (dx, dy) & velocity (vx, vy)
	private int sx, sy;		//size
	private int damage;		//damage it does to wizard/witch it hits
	private double speed;	//speed it travels at
	
	public Bullet(int posx, int posy, double dirx, double diry, int dam){	//creates Bullet
	
		dx = posx;
		dy = posy;
		speed = 15;
		vx = dirx*speed;
		vy = diry*speed;
		
		sx = 5;
		sy = 5;
		
		damage = dam;
	}

	public int getX(){		//gets coordinates as ints
		return (int)dx;
	}
	public int getY(){
		return (int)dy;
	}
	public double getvx(){	//gets velocity
		return vx;
	}
	public double getvy(){
		return vy;
	}
	public int getsx(){		//gets size
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public int getdamage(){	//gets damage that bullet does
		return damage;
	}
	
	public void move(){		//moves bullet
		dx = dx+vx;
		dy = dy+vy;
	}
	
//checks if bullet collides with object at (x,y) with dimensions (w, l)
	public boolean collide(int x, int y, int w, int l){	
		if (dx<=x+w && dx+sx>=x && dy<=y+l && dy+sy>=y){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return "("+dx+", "+dy+"), ("+vx+", "+vy+")";
	}
	
}
