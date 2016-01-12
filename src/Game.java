//April 28, 2015 - June 12, 2015
//ELENA ZHAN AND LYNN LIU
//the player navigates harry using the arrow keys through the 3 levels 
//the player has to collect horcruxes and is also able to get "power up" potions
//the enemies shoot harry, decreasing the health points
//harry can shoot back at them with the space bar


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.math.*;
import javax.swing.*;


public class Game extends JFrame implements ActionListener{

	GamePanel game = null; //Gamepanel for the main game, which has all the graphics and game functions
	GameOver endGame=null; //page that shows up at the end of the game
	Info howto=new Info(); //information page
	Timer myTimer; //timer keeps track of milliseconds in game
	int timer = 0;
	GameMenu gm = null; //main menu page
	
	public static void main (String[] args){
		Game game = new Game();
	}
	
	public Game(){
		super ("Escape Azkaban!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println(this);
		gm = new GameMenu(this, howto); //calls menu at the very beginning when you open the file that allows player to play/view info
	}	
	public void initialize(){
		
		System.out.println("initializing");
		setSize(1006,653);

		myTimer = new Timer(80,this);
		
		setResizable(false);
		setVisible(true);
		
		if (game == null){ //creates new game in the frame if one isn't currently running
    		game = new GamePanel(this);
    		System.out.println("ADDS GAME");
    		add(game);
    	}
    	myTimer.start();
	}
	
	public void setgamenull(){
		game = null;
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		timer ++;
		game.changeHPdir();
		game.moveHP();
		//if(game!=null){
		
		game.shootHP();
		game.stalk();
		game.moveDE();
		game.shootDE();
		game.moveSpells();
		game.checkshots();
		game.repaint();
		//}
	}
	public void gotoGameOver(int score){ //method is called after the player wins or loses, to activate the end page
		//takes in an int score from Gamepanel (because the variable score does not exist in the SpaceInvaders class)
		game.setVisible(false);
		remove(game); //removes game (game is reloaded if replayed)
		//game=null;
		endGame = new GameOver(this, gm, score, 0); // calls EndGame panel
		endGame.setSize(1006,628);
		endGame.setLocation(0,0);
		add(endGame);
		endGame.setVisible(true);
	}
}


class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
	private Game mainFrame;
	private boolean[] keys, mouse;		//list of keys/buttons clicked
	private boolean keypressed, canclick;			//used for entering name,HH keeps track of when key is pressed so chrs don't repeat from holding down
	private char inchar;		//keeps track of last char entered on keyboard
	//private int mx, my;			//mouse x,y coords relative to panel
	private Point mpos;
	
	private String infile = "";
	private int[][] floorplan;		//stores floorplan (info on where walls are)
	private boolean canshoot;		//whether player can shoot
	private ArrayList<Bullet> posHPspells;		//stores player's bullets
	private ArrayList<DeathEater> posDE;		//stores Death Eaters
	private ArrayList<Bullet> posDEspells;		//stores DE's bullets
	
	
 	private int dimx, dimy;		//number of tiles (x, y)
	private int gamew, gameh;	//dimensions of game floor (x, y in pixels)
	private int gridw, gridh;	//pixel dimensions of each tile
	private int shiftx, shifty;	//top left corner of actual game floor (shifted over)
	public ArrayList <Point> unfoundHorcs = new ArrayList<Point>(); // unfound horcs in Points representing (x,y) where to show the box in the sidebar

	private int levnum;	//level number
	
	private int displayedFrame; //the number of the frame that is currently displayed for the sprites
	//private int movetimer; //
	private HarryP harry;		//the player
	private boolean moving; //if you are moving harry or not
	Horcrux horc; //the horcrux on the level
	private Image bgpic; //the game background

	private boolean gotHorc; //if you have already collected the horcrux on the level
	private int score; //your score
	private Point portal; //the (x,y) coordinates of the portal that allows you to move on to the next level
	
	private Point specPotion, speedPotion, stunPotion; //powerup potions
	private int potsx, potsy;		//size of potions
	private Image potionPic1, potionPic2, potionPic3;	//images of the potions (red, blue, white)
	
	private Image hpSquare, horcBox, atkSquare, speedSquare; //images at the sidebar that indicate your stats
	private Image nextlevel, stairs; //images of the stairs and the next level banner
	
	public GamePanel(Game m){
		
		posHPspells = new ArrayList<Bullet>(); 
		posDE = new ArrayList<DeathEater>();
		posDEspells = new ArrayList<Bullet>();
		
		levnum = 1;
		
		keys = new boolean[KeyEvent.KEY_LAST+1];
		mouse= new boolean[MouseEvent.MOUSE_LAST+1];
		keypressed = false;
		canclick = true;
		canshoot = true;
		mpos = new Point(0,0);
		
		
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);		//focus on mac is not on panel. (keys won't work w/out focus)
        requestFocus();
        
        harry = new HarryP(); //creates harry potter character
        displayedFrame = 1; //frame shown
        moving = false; //harry starts off as not moving
        score = 0; //the score you have in the game
        gotHorc = false; //you haven't found the horcrux yet
        
		readfile();		//reads level file in
        
		gamew = 700;
		gameh = 525;
		shiftx = 21;
		shifty = 83;
		gridw = gamew/dimx;
		gridh = gameh/dimy;
		
		unfoundHorcs.add(new Point(848,149)); //points for the sidebar showing which horcruxes you have found or not
		unfoundHorcs.add(new Point(848,203));
		unfoundHorcs.add(new Point(848,257));
		mainFrame = m;
		//uploads all images -----------------------------------------------------------------------------------------------
		nextlevel = new ImageIcon("pics/nextlevel.png").getImage(); //banner that shows once u beat lvl
		stairs = new ImageIcon("pics/stairs.png").getImage(); //stairs to pass lvl
		hpSquare = new ImageIcon("pics/hp square2.png").getImage(); //square showing HP
		atkSquare = new ImageIcon("pics/sp atk square.png").getImage(); //rectangles showing how many special attacks are left
		speedSquare = new ImageIcon("pics/speed square.png").getImage(); //circle telling you if you have fast speed
		horcBox = new ImageIcon("pics/unfound item.png").getImage(); //a box that is shown over horcs you havent found
		bgpic = new ImageIcon("pics/game interface.png").getImage(); //bg pic
		
		potionPic1 = new ImageIcon("pics/redPotion.png").getImage(); //special attack
		potionPic2 = new ImageIcon("pics/bluePotion.png").getImage(); //speed
		potionPic3 = new ImageIcon("pics/whitePotion.png").getImage(); //invisibility
		potsx = 13; 
		potsy = 28;
		
	}

	public void readfile(){		//reads level file and sets up game and variables
		Scanner infile = null;
		try{
			infile = new Scanner(new File("Level"+levnum+".txt"));
		}
		catch(IOException ex){
			System.out.println("Oops, where did I put that file?");
		}
	
		String tmp = infile.nextLine();		//header of file
		
		//HARRY's position
		String line = infile.nextLine();
		String[] lets = line.split(" ");
		harry.setpos(Integer.parseInt(lets[0]), Integer.parseInt(lets[1]));

		//DEATH EATERS
		int n;		//number of death eaters
		n = Integer.parseInt(infile.nextLine());
		for(int i=0; i<n; i++){
			line = infile.nextLine();
			posDE.add(new DeathEater(line));
		}
		
		/***horcrux stuff***/
		//reads the file and makes the horcrux
		line = infile.nextLine();
		horc = new Horcrux(line);
		
		/***end of horcrux stuff***/
		/**** potion stuff***/
		
		//reads the lines in the file to get the (x,y) coordinates of each file
		line = infile.nextLine();
		lets = line.split(" ");
		specPotion = new Point(Integer.parseInt(lets[0]),Integer.parseInt(lets[1])); //potion for special attack
		
		line = infile.nextLine();
		lets = line.split(" ");
		speedPotion = new Point(Integer.parseInt(lets[0]),Integer.parseInt(lets[1])); //potion for speed 
		
		line = infile.nextLine();
		lets = line.split(" ");
		stunPotion = new Point(Integer.parseInt(lets[0]),Integer.parseInt(lets[1])); //potion for invisibility
		
		/**end of potion stuff**/
			
		//FLOORPLAN
		line = infile.nextLine();	//dimensions of floorplan
		lets = line.split(" ");
		dimx = Integer.parseInt(lets[0]);
		dimy = Integer.parseInt(lets[1]);
		
		floorplan = new int[dimy][dimx];
		
		for (int i=0; i<dimy; i++){
			line = infile.nextLine();
			lets = line.split(",");
			for(int j=0; j<dimx; j++){
				floorplan[i][j] = Integer.parseInt(lets[j]);
			}
		}
		
		//PORTAL
		line = infile.nextLine();
		lets = line.split(" ");
		portal = new Point(Integer.parseInt(lets[0]),Integer.parseInt(lets[1])); //sets the (x,y) coordinates of the portal
		
 	}

//changes HP direction when keys are pressed
	public void changeHPdir(){
		harry.changedir(keys[KeyEvent.VK_RIGHT], keys[KeyEvent.VK_LEFT], keys[KeyEvent.VK_UP], keys[KeyEvent.VK_DOWN]);
	}
	/**check harry collide***/
	public boolean charCollide(){		//checks if player collides with any DE
		for (DeathEater d: posDE){
			if (collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), d.getX(), d.getY(), d.getsx(), d.getsy())==true){ //harry IS colliding
				return true;
			}
		}
		return false;
	}
	
	public void moveHP(){		//move function for player
		displayedFrame +=1;
		moving = false;
		
		if(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_DOWN]){
			harry.changedir(keys[KeyEvent.VK_RIGHT], keys[KeyEvent.VK_LEFT], keys[KeyEvent.VK_UP], keys[KeyEvent.VK_DOWN]);
			for (int c=0; c<harry.getspeed(); c++){
				if (wallcollision(harry.getX()+harry.getvx(), harry.getY()+harry.getvy(), harry.getsx(), harry.getsy())==false
					&& charCollide()==false){
					harry.move(keys);
				}
				if(charCollide()){
					mainFrame.gotoGameOver(score); //ends the game if harry collides into death eater
				}
			}
			moving = true; //yes, harry is moving
		}
		if (gotHorc!=true){	//if horcrux has not been picked up, check if player is getting it
			getHorc();
		}
		getpotions();	//check if harry is picking up any of the potions
		
		score+=1;
		//cases to restart new levels//
		if (gotHorc==true && collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), portal.x, portal.y, 90, 90)==true){
			restart(); //if harry has gotten the potion and collides with the portal, call restart
		}
		if (harry.gethealth()<=0){ 
			restart(); //if harry has no Health left, call restart
		}
		//end of restart
	}
	public void getpotions(){
		//if you haven't gotten the potion yet and collides with it, get it
		
		if (specPotion!=null && 	
			collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), specPotion.x, specPotion.y, potsx, potsy)){
			harry.gotAtk(); 
			specPotion = null; //the potion is removed from the screen
		}
		if (speedPotion!=null &&
			collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), speedPotion.x, speedPotion.y, potsx, potsy)){
			harry.gotSpeed();
			speedPotion = null; //the potion is removed from the screen
		}
		if (stunPotion!=null &&
			collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), stunPotion.x, stunPotion.y, potsx, potsy)){
			harry.gotInvis();
			stunPotion = null; //the potion is removed from the screen
		}
	}
	
	 public void nextLevel(){ //just displays next level banner
	    	Graphics g = getGraphics();
	    	g.drawImage(nextlevel,0,0, this);
	    	delay(2000); //waits 2000 millisecs
	    }
	    
	    public static void delay (long len){ //delays game for set amount of milliseconds as inputted in parameter
	    	try{
	    		Thread.sleep (len);
	    	}
	    	catch (InterruptedException ex){
	    		System.out.println("lmao");
	    	}
	    }
		
		public void restart(){ //new level
			if (harry.gethealth() >0 && levnum<3){ //if you are not dead and level is less than 3, you advance to next level
	    		//all these settings are reset========================================================================================
	    		levnum+=1;
		    	 //resetting all variables for new level----
		    	displayedFrame = 1; 
	        	harry.reset();
	        	moving = false;
	        	gotHorc = false;
	        	canshoot = true;
	        	horc = null;
	        	posDE.clear();
	        	posHPspells.clear();
	        	posDEspells.clear();
	        	readfile();
		    	nextLevel();
	    	}
	    	
	    	else{ //the game is over either you died or you finished all 3 levels //does game over stuff
	    		mainFrame.gotoGameOver(score); //go to game over screen. passes through your score so that the game over class knows it
	    	}
		}


	public void shootHP(){		//checks if player presses space bar and Harry shoots
		if(keys[KeyEvent.VK_SPACE] && canshoot){
			if (harry.ispowerful()){	//if harry has the special attacks, the damage of bullet is twice it's usual
				Bullet spell = new Bullet (harry.getX(), harry.getY(), harry.getvx(), harry.getvy(), 30);
				posHPspells.add(spell);
				harry.usepower();	//reduces number of special attacks left
				canshoot=false;
			}
			else{		//regular bullets
				Bullet spell = new Bullet (harry.getX(), harry.getY(), harry.getvx(), harry.getvy(), 15);
				posHPspells.add(spell);
				canshoot=false;
			}
		}
		if(keys[KeyEvent.VK_SPACE]==false){		//so that holding down space button will not continuously create bullets
			canshoot=true;
		}
	}
	
	public void moveDE(){		//moves DeathEaters
		for (DeathEater d: posDE){
			for (int c=0; c<d.getspeed(); c++){		//loops through for speed (moves 1 pixel at a time)
				if (charCollide()){
					mainFrame.gotoGameOver(score);	//if DE catches Harry, game is over
				}
				if (d.isstalking()==false){		//if not stalking, regular movement (flips direction if collides with wall)
					if (wallcollision(d.getDX()+d.getvx(), d.getDY()+d.getvy(), d.getsx(), d.getsy())){
						d.changedir();
					}
					if (collide(harry.getX(), harry.getY(), harry.getsx(), harry.getsy(),
							d.getX()+d.getvx(), d.getDY()+d.getvy(), d.getsx(), d.getsy())==false){
						d.move();
					}
				}
				else{		//if is stalking and doesn't collide with wall, continue moving towards dest
					if (wallcollision(d.getDX()+d.getvx(), d.getDY()+d.getvy(), d.getsx(), d.getsy())==false &&
						collide(harry.getX(), harry.getY(), harry.getsx(), harry.getsy(),
								d.getX()+d.getvx(), d.getDY()+d.getvy(), d.getsx(), d.getsy())==false){
						d.move();
					}
					else{ //will collide
						if (d.orthdest() && clearsight(d, d.getdestx(), d.getdesty())==false){	//case of wall between dest and pos of DE, stops stalking
							d.stopstalking();		//prevents the changing dir around an obj to get to dest (should get rid of shaking back and forth on other side of wall)
						}
						//get orthogonal directional components of DE's velocity
						int tempvx = 0;
						int tempvy = 0;
						if (d.getvx()<=0){
							tempvx = (-1);
						}
						else if (d.getvy()>0){
							tempvx = 1;
						}
						if (d.getvy()<=0){
							tempvy = (-1);
						}
						else if (d.getvy()>0){
							tempvy = 1;
						}
						//Check if can move in either direction, move if possible
						if (Math.abs(d.getvx())>=Math.abs(d.getvy())){	//x-component is larger than y, check moving x first then y
							if (tempvx!=0 && wallcollision(d.getDX()+tempvx, d.getDY(), d.getsx(), d.getsy())==false){
								d.move(tempvx, 0);
							}
							else if (tempvy!=0 && wallcollision(d.getDX(), d.getDY()+tempvy, d.getsx(), d.getsy())==false){
								d.move(0,tempvy);
							}
						}
						else{	//y-component is larger than x, check y first
							if (tempvy!=0 && wallcollision(d.getDX(), d.getDY()+tempvy, d.getsx(), d.getsy())==false){
								d.move(0,tempvy);
							}
							else if (tempvx!=0 && wallcollision(d.getDX()+tempvx, d.getDY(), d.getsx(), d.getsy())==false){
								d.move(tempvx, 0);
							}
						}
					}
					//if DE collides with HP, stop stalking
					if (collide(d.getX(), d.getY(), d.getsx(), d.getsy(), d.getdestx(), d.getdesty(), harry.getsx(), harry.getsy())){
						d.stopstalking();
					}
					//if DE is still stalking, make any alterations to direction vectors to continue proceeding to dest if needed
					if (d.isstalking()){
						d.resetdir();
					}
				}
			}
		}
	}
	
	public void getHorc(){
		//checks if harry collides with the horcrux (gets the horcrux)
		if (collide (harry.getX(), harry.getY(), harry.getsx(), harry.getsy(), horc.hx, horc.hy, horc.sizex, horc.sizey)){
			score += horc.ptValue; //adds the pt value of the horcrux to score
			gotHorc = true;
			if (unfoundHorcs.size()>0){
				unfoundHorcs.remove(0); //removes the first point from unfoundHorcs (because you found the horc)
			}
			
		}
	}

	
	public boolean collide (double obj1x, double obj1y, int obj1sizex, int obj1sizey, 
							double obj2x, double obj2y, int obj2sizex, int obj2sizey){		//checks for collisions between 2 objects
		if ((obj1x>=obj2x && obj1x<=obj2x + obj2sizex && obj1y>=obj2y && obj1y<=obj2y+obj2sizey) ||
			(obj1x+obj1sizex>=obj2x && obj1x+obj1sizex<=obj2x + obj2sizex && obj1y>=obj2y && obj1y<=obj2y+obj2sizey) ||
			(obj1x>=obj2x && obj1x<=obj2x + obj2sizex && obj1y+obj1sizey>=obj2y && obj1y+obj1sizey<=obj2y+obj2sizey) ||
			(obj1x+obj1sizex>=obj2x && obj1x+obj1sizex<=obj2x + obj2sizex && obj1y+obj1sizey>=obj2y && obj1y+obj1sizey<=obj2y+obj2sizey)){
			return true;
		}
		return false;
	}
	
	public boolean wallcollision(double dx, double dy, int sx, int sy){		//checks if an object collides with any walls
		if (floorplan[(int)(dy/gridh)][(int)(dx/gridw)]==1 || 
			floorplan[(int)(dy/gridh)][(int)((dx+sx)/gridw)]==1 || 
			floorplan[(int)((dy+sy)/gridh)][(int)(dx/gridw)]==1 || 
			floorplan[(int)((dy+sy)/gridh)][(int)((dx+sx)/gridw)]==1){
			return true;
		}
		return false;
	}
	public boolean wallptcollision(double dx, double dy){		//checks if an object collides with any walls
		if (floorplan[(int)(dy/gridh)][(int)(dx/gridw)]==1){
			return true;
		}
		return false;
	}
	
	public void stalk(){
		for (DeathEater d: posDE){		//if the DE can see HP, start stalking
			if (insight(d, harry.getX(), harry.getY())){
				d.startstalking(harry.getX(), harry.getY());
			}
		}
	}
	public boolean insight(DeathEater d, int xHP, int yHP){	//checks if DE d can see spot xHP, yHP
		if (harry.isinvis()==false && pervision(d, harry.getX(), harry.getY()) && clearsight(d, harry.getX(), harry.getY())){
			return true;
		}
		return false;
	}
	public boolean pervision(DeathEater d, int xHP, int yHP){	//checks if position is in DE's field of vision (90 on both sides of direction)
		double angDEx = d.getvx();		//x of DE's direction triangle
		double angDEy =  d.getvy();		//y of DE's direction triangle
		
		double angHPx = xHP - d.getX();		//x of HP's direction triangle relative to DE's position
		double angHPy = yHP - d.getY();		//y of HP's direction triangle relative to DE's position
		
		double angDE = Math.atan2(angDEy, angDEx);		//angle DE is looking
		double angHP = Math.atan2(angHPy, angHPx);		//angle of HP relative to DE
		
		angDE = (angDE+(2*Math.PI)) % (2*Math.PI);		//makes them all between 0-2pi
		angHP = (angHP+(2*Math.PI)) % (2*Math.PI);

		double angDE1 = angDE-(Math.PI/2);		//furthest angles DE can see
		double angDE2 = angDE+(Math.PI/2);
		
		if (angDE1<0){		//angDE1 can go neg if original angDE is in quadrant 1
			angDE1 = (angDE1+(2*Math.PI));
			angDE2 = (angDE2+(2*Math.PI));
		}

		if (angHP>=angDE1 && angHP<=angDE2){	//if angHP is between the two DE angles, is in vision
			return true;
		}
		if (angHP < angDE1) {		//case where DE angles are both greater than HP (had gone full circle)
			double angHPadj = angHP+2*Math.PI;
			if (angHPadj>=angDE1 && angHPadj<=angDE2){
				return true;
			}
		}
		return false;
		
	}
	
	public boolean clearsight(DeathEater d, int xHP, int yHP){		//checks if DE sight is blocked
		
		double dist = Math.sqrt(Math.pow(xHP - d.getX(), 2)+Math.pow(yHP - d.getY(), 2));
		
		double tempvx = ((xHP - d.getX())/dist);		//direction velocities
		double tempvy = ((yHP - d.getY())/dist);
		double tempdx = d.getX();		//temporary position moving along line to check
		double tempdy = d.getY();
		
		boolean running = true;
		while (running){		//loop through positions in line to check if there is collision with walls
			if (collide(tempdx, tempdy, d.getsx(), d.getsy(), xHP, yHP, harry.getsx(), harry.getsy())){
				return true;
			}
			else if (wallcollision(tempdx, tempdy, 3,3)){		//say sight line has width of 3 pixels
				return false;
			}
			else{
				tempdx+=tempvx;
				tempdy+=tempvy;
			}
		}
		return false;
	}
	
	public void shootDE(){		//DeathEater shoots
		for (DeathEater d: posDE){
			if(d.isstalking() && insight(d, harry.getX(), harry.getY())){		//has to be stalking and can see Harry
				double randnum = Math.random();
				if (randnum>0.90){		//random times to shoot
					Bullet b = new Bullet((int)d.getX(), (int)d.getY(), d.getvx(), d.getvy(), d.getdamage());
					posDEspells.add(b);
				}
			}
		}
	}
	
	public void moveSpells(){	//moves all the bullets on the screen
		if (posHPspells.size()>0){
			for(int i=0; i<posHPspells.size(); i++){
				Bullet b = posHPspells.get(i);
				b.move();
			}
		}
		if(posDEspells.size()>0){
			for(Bullet b: posDEspells){
				b.move();
			}
		}
	}
	
	public void checkshots(){		//checks all bullets for collisions with walls and characters
		for (int i=0; i<posHPspells.size(); i++){		//Harry's bullets
			Bullet b = posHPspells.get(i);
			
			if(b.getX() < 0 || b.getX() > gamew || b.getY() < 0 || b.getY() > gameh){	//if bullet is out of screen, delete it
				posHPspells.remove(i);
				break;
			}
			
			if (floorplan[(int)(b.getY()/gridh)][(int)(b.getX()/gridw)]==1){	//checks if bullets hit walls
				posHPspells.remove(i);
				break;
			}
			
			for (int j=0; j<posDE.size(); j++){		//check if bullets hit any DE
				DeathEater d = posDE.get(j);
				if (b.collide((int)d.getX(), (int)d.getY(), d.getsx(), d.getsy())){
					posHPspells.remove(i);
					d.shot(b);
					if (d.isalive()==false){
						score += d.getkillPts();
						posDE.remove(j);
					}
					break;
				}
			}
		}
		for (int i=0; i<posDEspells.size(); i++){		//DeathEater bullets
			Bullet b = posDEspells.get(i);
			if(b.getX() < 0 || b.getX() > gamew || b.getY() < 0 || b.getY() > gameh){	//if bullet is out of screen, delete it
				posDEspells.remove(i);
				break;
			}
			if (floorplan[(int)(b.getY()/gridh)][(int)(b.getX()/gridw)]==1){	//checks if bullets hit walls
				posDEspells.remove(i);
				break;
			}
			
			if(b.collide(harry.getX(), harry.getY(), harry.getsx(), harry.getsy())){	//checks if bullets hit Harry
				harry.shot(b);		//reduces Harry's health points
				posDEspells.remove(i);
				break;
			}
		}
	}

	
// _____________________NECESSARY FOR LISTENERS_______________________
    public void keyTyped(KeyEvent e) {
    	inchar = e.getKeyChar();
    	System.out.println(inchar);
    }
    public void keyPressed(KeyEvent e) {
    	keypressed=true;
        keys[e.getKeyCode()] = true;
        changeHPdir();
    }
    public void keyReleased(KeyEvent e) {
    	keypressed=false;
        keys[e.getKeyCode()] = false;
    }
	public void mouseClicked(MouseEvent m){}
	public void mouseEntered(MouseEvent m){}
	public void mouseExited(MouseEvent m){}
	public void mousePressed(MouseEvent m){mouse[m.getButton()] = true;}
	public void mouseReleased(MouseEvent m){mouse[m.getButton()] = false;}
	public void mouseMoved(MouseEvent m){mpos.setLocation(m.getX(),m.getY());}
	public void mouseDragged(MouseEvent m){}
//_________________________________________________________________

	@Override
	public void paintComponent(Graphics g){
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(bgpic, 0, 0, this);

		gridw = gamew/dimx;
		gridh = gameh/dimy;

		double sizeper = 0.8;
	
		for (int a=0; a<dimy; a++){
			for (int b=0; b<dimx; b++){
				if (floorplan[a][b]==1){
					g.setColor(Color.black);
					g.fillRect(gridw*b+shiftx+1, gridh*a+shifty+1, gridw-2, gridh-2);
				}
			}
		}
		
		g.setColor(Color.red);
		
		for (Bullet b: posHPspells){
			int bulsizex = b.getsx();
			int bulsizey = b.getsy();
			if (harry.ispowerful()){
				bulsizex*=2;
				bulsizey*=2;
			}
			g.fillOval((int)b.getX()+shiftx, (int)b.getY()+shifty, bulsizex, bulsizey);
		}
		g.setColor(Color.green);
		for (Bullet b: posDEspells){
			g.fillOval((int)b.getX()+shiftx, (int)b.getY()+shifty, b.getsx(), b.getsy());
		}
		
	//DEATH EATER SPRITES//
	//get(displayedFrame/5%4) calculates the next sprite to show (out of 4 sprites)
		for (DeathEater d:posDE){
			if (Math.abs(d.getvy())>Math.abs(d.getvx())){ //if deatheater is moving up and down
				if (d.getvy()>0){  //deatheater moves up
					g.drawImage(d.dmvt.get(displayedFrame/5%4),(int) d.getX()+shiftx,(int) d.getY()+shifty, this); //displays up sprites
				}
				else{ //deatheater moves down
					g.drawImage(d.umvt.get(displayedFrame/5%4),(int) d.getX()+shiftx,(int) d.getY()+shifty, this); //displays down sprites
				}
			}
			else{ //deatheater moves left/right
				if (d.getvx()>0){ 
					g.drawImage(d.rmvt.get(displayedFrame/5%4),(int) d.getX()+shiftx,(int) d.getY()+shifty, this); //displays left sprites
				}
				else{
					g.drawImage(d.lmvt.get(displayedFrame/5%4),(int) d.getX()+shiftx,(int) d.getY()+shifty, this); //displays right sprites
				}
			}
		}

	//HARRY's SPRITES//	
	//get(displayedFrame/5%4) calculates the next sprite to show (out of 4 sprites)	
		if (moving){ //if the player is moving
			if (harry.getvx() == 1){ //if harry is going right
				g.drawImage(harry.rmvt.get(displayedFrame/5%4), harry.getX()+shiftx, harry.getY()+shifty, this); //displays right sprites
		    }
		   	else if (harry.getvx() == -1){ //if hary is going left
				g.drawImage(harry.lmvt.get(displayedFrame/5%4), harry.getX()+shiftx, harry.getY()+shifty, this); //displays left sprites
		    }
		    	
		    else if (harry.getvy() == 1){ //if harry is going down
				g.drawImage(harry.dmvt.get(displayedFrame/5%4), harry.getX()+shiftx, harry.getY()+shifty, this); //displays down sprites
		    }
		    
		    else if (harry.getvy() == -1){ //if harry is going up
					g.drawImage(harry.umvt.get(displayedFrame/5%4), harry.getX()+shiftx, harry.getY()+shifty, this); //displays up sprites
		   	}
		}
		else{ //if harry is not moving 
			g.drawImage(harry.dmvt.get(1), harry.getX()+shiftx, harry.getY()+shifty, this); //harry faces forward (down sprite 1)
		}
	    	
		
	   	if (gotHorc!= true){ //if harry did not get the horcrux in the level yet
	    	g.drawImage(horc.horcPic, horc.hx+shiftx, horc.hy+shifty, this); //display te horcrux on screen
	    }
	    else{ //if harry has the horcrux the magic portal is shown
	    	g.drawImage(stairs, portal.x+shiftx, portal.y+shifty, this);
	    }
	    //for each potion: if harry didnt get it yet... display that potion
	    if (specPotion != null){ 
	    	g.drawImage(potionPic1, (int)(specPotion.x-potsx/2+shiftx), (int)(specPotion.y-potsx/2+shifty), this);
	    }
	    
	    if (speedPotion != null){
	    	g.drawImage(potionPic2, speedPotion.x+shiftx, speedPotion.y+shifty, this);
	    }
	    
	    if (stunPotion != null){
	    	g.drawImage(potionPic3, stunPotion.x+shiftx, stunPotion.y+shifty, this);
	    }
	    
	   	
	   	
//---------UNSHIFTED STUFF
	    //sidebar stuff
	    for (int i =0; i<harry.gethealth(); i ++){
	    	g.drawImage(hpSquare, 790+(2*i), 32, this); //shows how much Health you have left
	    }
	    for (int i =0; i<harry.getspAtk(); i ++){
	    	g.drawImage(atkSquare, 793+(40*i), 56, this); //shows how many sp. attacks you have
	    }
	
	    if (harry.isinvis()){
	    	g.drawImage(speedSquare, 801, 83, this); //shows if you are invisible to enemies
	    }
	    
	    if (harry.isfast()){
	    	g.drawImage(speedSquare, 801, 108, this); //shows if you have super speed
	    }
	    
	    for (Point h:unfoundHorcs){
	    	g.drawImage(horcBox, h.x, h.y, this); //displays a semi transparent
	    											//black box over the horcruxes your havent found in the sidebar
	    }
	    //prints harry's score 
	    g.setColor(new Color(186, 155,40));
	    g.setFont(new Font("Old English Text MT",Font.PLAIN, 40));
	    g.drawString("Score: "+score, 309, 42);
	    
	}
}






