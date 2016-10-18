import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.ImageIO;


public class GamePanel extends JPanel implements Runnable
{
public static final int PWIDTH = 640;
public static final int PHEIGHT = 480;
private Thread animator;
private boolean running = false;
private boolean gameOver = false; 

private BufferedImage dbImage;
private Graphics2D dbg;


int FPS,SFPS;
int fpscount;

public static Random rnd = new Random();

BufferedImage imagemcharsets;
BufferedImage fundo;
BufferedImage plotfundo;
BufferedImage tileset;

public static BufferedImage imgFumaca;
public static BufferedImage imgFogo;

boolean LEFT, RIGHT,UP,DOWN,FIRE;

int MouseX,MouseY;

int diftime;

float x,y;
float x2,y2;



int vel2 = 80;

int objetivoX = 0;
int objetivoY = 0;

Personagem heroi;
public static ArrayList<Sprite> listaDePersonagens = new ArrayList<Sprite>();
public static ArrayList<Sprite> listaDeProjetil = new ArrayList<Sprite>();
public static ArrayList<Sprite> listaDeParticulas = new ArrayList<Sprite>();

public static TileMapJSON mapa;

int timertiro = 0;

public GamePanel()
{

	setBackground(Color.white);
	setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

	// create game components
	setFocusable(true);

	requestFocus(); // JPanel now receives key events
	
	if (dbImage == null){
		dbImage = new BufferedImage(PWIDTH, PHEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		if (dbImage == null) {
			System.out.println("dbImage is null");
			return;
		}else{
			dbg = (Graphics2D)dbImage.getGraphics();
		}
	}	
	
	
	// Adiciona um Key Listner
	addKeyListener( new KeyAdapter() {
		public void keyPressed(KeyEvent e)
			{ 
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = true;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = true;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = true;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = true;
				}	
			}
		@Override
			public void keyReleased(KeyEvent e ) {
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = false;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = false;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = false;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = false;
				}
			}
	});
	
	addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			MouseX = e.getX();
			MouseY = e.getY();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			MouseX = e.getX();
			MouseY = e.getY();
		}
	});
	
	addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			FIRE = false;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			objetivoX = MouseX;
			objetivoY = MouseY;
			
			FIRE = true;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
	
	

	fundo =  loadImage("wide_r.jpg");
	plotfundo = new BufferedImage(fundo.getWidth(), fundo.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	tileset = loadImage("Bridge.png");
	
	imgFumaca = loadImage("Fumaca.png");
	imgFogo = loadImage("Fogo.png");
	
	imagemcharsets = loadImage("chara1b.png");
	DataBufferByte db = (DataBufferByte)imagemcharsets.getRaster().getDataBuffer();
	byte data[] = db.getData();
	
	int ab = data[1];
	int ag = data[2];
	int ar = data[3];
	
	for(int i = 0; i < data.length; i+=4){
		int a = data[i]&0x00ff;
		int b = data[i+1]&0x00ff;
		int g = data[i+2]&0x00ff;
		int r = data[i+3]&0x00ff;
		
//		int media = (r+g+b)/3;
//		
//		r = media;
//		g = media;
//		b = media;
		
		//255,0,128

//		b = Math.min(255,(int)(b*1.5));
//		g = Math.min(255,(int)(g*1.5));
//		r = Math.min(255,(int)(r*0.75));
		
		if(ab==b&&ag==g&&ar==r){
			data[i] = 0;
		}
		
		data[i+1] = (byte)(b&0x00ff);
		data[i+2] = (byte)(g&0x00ff);
		data[i+3] = (byte)(r&0x00ff);
	}
	

	
	heroi = new Personagem(imagemcharsets, 10, 10,0);
	listaDePersonagens.add(heroi);
	
	MouseX = MouseY = 0;
	
	mapa = new TileMapJSON(tileset,40,30);
	mapa.AbreMapa("mapa1.json");
	
	for(int i = 0;i < 100;i++){
		boolean colidiu = false;
		Inimigo inimigotmp = null;
		
		do{
			colidiu = false;
			inimigotmp = new Inimigo(imagemcharsets, rnd.nextInt(mapa.Largura*16-24), rnd.nextInt(mapa.Altura*16-32), rnd.nextInt(7)+1, rnd.nextInt(200)-100, rnd.nextInt(200)-100);
			
			for(int j = 0; j < listaDePersonagens.size();j++){
				Personagem pers = (Personagem)listaDePersonagens.get(j);
				if(inimigotmp.colideRect(pers)){
					colidiu = true;
					break;
				}
			}
			
			if(colidiu==false){
				int bx = (int)((inimigotmp.x+12)/mapa.tileW);
				int by = (int)((inimigotmp.y+28)/mapa.tileH);
				
				//System.out.println(" bx "+bx+" by "+by);
				
				if(bx<0||by<0||bx>=mapa.Largura||by>=mapa.Altura||mapa.mapa2[by][bx]!=0){
					colidiu=true;
				}
			}
			
		}while(colidiu==true);
		
		listaDePersonagens.add(inimigotmp);
	}
	x = 10;
	y = 100;
	x2 = 10;
	y2 = 200;

} // end of GamePanel()
BufferedImage loadImage(String filename){
	try {
		BufferedImage tmp = ImageIO.read( getClass().getResource(filename) );
		BufferedImage imgfinal = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		imgfinal.getGraphics().drawImage(tmp, 0, 0, null);
		return imgfinal;
	}
	catch(IOException e) {
		System.out.println("Load Image error:");
	}
	return null;
}

public void addNotify()
{
	super.addNotify(); // creates the peer
	startGame(); // start the thread
}

private void startGame()
// initialise and start the thread
{
	if (animator == null || !running) {
		animator = new Thread(this);
		animator.start();
	}
} // end of startGame()

public void stopGame()
// called by the user to stop execution
{ running = false; }


public void run()
/* Repeatedly update, render, sleep */
{
	running = true;
	
	long DifTime,TempoAnterior;
	
	int segundo = 0;
	DifTime = 0;
	TempoAnterior = System.currentTimeMillis();
	
	while(running) {
	
		gameUpdate(DifTime); // game state is updated
		gameRender(); // render to a buffer
		paintImmediately(0, 0, 640, 480); // paint with the buffer
	
		try {
			Thread.sleep(0); // sleep a bit
		}	
		catch(InterruptedException ex){}
		
		DifTime = System.currentTimeMillis() - TempoAnterior;
		TempoAnterior = System.currentTimeMillis();
		
		if(segundo!=((int)(TempoAnterior/1000))){
			FPS = SFPS;
			SFPS = 1;
			segundo = ((int)(TempoAnterior/1000));
		}else{
			SFPS++;
		}
	
	}
System.exit(0); // so enclosing JFrame/JApplet exits
} // end of run()

int timerfps = 0;

int passo = 0;

private void gameUpdate(long DiffTime)
{ 
	diftime = (int)DiffTime;
	passo+=DiffTime;
	timertiro+=DiffTime;
	
	if(FIRE&&timertiro>500){
		int veltiro = 500;
		float dx = (mapa.MapX+MouseX) - (heroi.x+heroi.cx);
		float dy = (mapa.MapY+MouseY) - (heroi.y+heroi.cy);
		double ang = Math.atan2(dy, dx);
		
		float vx = (float)(veltiro*Math.cos(ang));
		float vy = (float)(veltiro*Math.sin(ang));
		
		Projetil proj = new Projetil((int)(heroi.x+heroi.cx), (int)(heroi.y+heroi.cy), vx, vy,heroi);
		listaDeProjetil.add(proj);
		
		double ang2 = ang+Math.PI/20;
		vx = (float)(veltiro*Math.cos(ang2));
		vy = (float)(veltiro*Math.sin(ang2));
		proj = new Projetil((int)(heroi.x+heroi.cx), (int)(heroi.y+heroi.cy), vx, vy,heroi);
		listaDeProjetil.add(proj);
		
		ang2 = ang-Math.PI/20;
		vx = (float)(veltiro*Math.cos(ang2));
		vy = (float)(veltiro*Math.sin(ang2));
		proj = new Projetil((int)(heroi.x+heroi.cx), (int)(heroi.y+heroi.cy), vx, vy,heroi);
		listaDeProjetil.add(proj);
		
		timertiro = 0;
	}
	
	
	if(LEFT){
		heroi.velx = -heroi.vel;
		heroi.vely = 0;
		heroi.anim = 3;
	}else if(RIGHT){
		heroi.velx = heroi.vel;
		heroi.vely = 0;
		heroi.anim = 1;
	}else if(UP){
		heroi.vely =  -heroi.vel;
		heroi.velx = 0;
		heroi.anim = 0;
	}else if(DOWN){
		heroi.vely =  +heroi.vel;
		heroi.velx = 0;
		heroi.anim = 2;
	}else{
		heroi.vely = 0;
		heroi.velx = 0;
	}
	
	for(int i = 0; i < listaDePersonagens.size();i++){
		Sprite s = listaDePersonagens.get(i);
		s.simulaSe(DiffTime);
		if(s.vivo==false){
			listaDePersonagens.remove(i);
			i--;
		}
	}
	for(int i = 0; i < listaDeProjetil.size();i++){
		Sprite s = listaDeProjetil.get(i);
		s.simulaSe(DiffTime);
		if(s.vivo==false){
			listaDeProjetil.remove(i);
			i--;
		}
		
	}
	
	for(int i = 0; i < listaDeParticulas.size();i++){
		Sprite s = listaDeParticulas.get(i);
		s.simulaSe(DiffTime);
		if(s.vivo==false){
			listaDeParticulas.remove(i);
			i--;
		}
		
	}

	mapa.Posiciona((int)(heroi.x-PWIDTH/2),(int)(heroi.y-PHEIGHT/2));
}

private void gameRender()
// draw the current frame to an image buffer
{
	dbg.setColor(Color.white);
	dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
	
	mapa.DesenhaSe(dbg);
	
	for(int i = 0; i < listaDePersonagens.size();i++){
		listaDePersonagens.get(i).desenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDeProjetil.size();i++){
		listaDeProjetil.get(i).desenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	for(int i = 0; i < listaDeParticulas.size();i++){
		listaDeParticulas.get(i).desenhaSe(dbg,mapa.MapX,mapa.MapY);
	}
	
	dbg.setColor(Color.blue);
	dbg.drawString("FPS "+FPS+ " "+MouseX+" "+MouseY+" LEFT "+LEFT+" RIGHT "+RIGHT+" ->"+listaDeParticulas.size(), 10, 20);
}


public void paintComponent(Graphics g)
{
	super.paintComponent(g);
	if (dbImage != null)
		g.drawImage(dbImage, 0, 0, null);
}


public static void main(String args[])
{
	GamePanel ttPanel = new GamePanel();

  // create a JFrame to hold the timer test JPanel
  JFrame app = new JFrame("Swing Timer Test");
  app.getContentPane().add(ttPanel, BorderLayout.CENTER);
  app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  app.pack();
  app.setResizable(false);  
  app.setVisible(true);
} // end of main()

} // end of GamePanel class

