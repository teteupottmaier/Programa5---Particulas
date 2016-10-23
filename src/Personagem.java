import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Personagem extends Sprite {
	
	BufferedImage img;
	
	int frametimer = 0;
	int frame = 0;
	int intervalo = 250;
	int anim = 0;
	
	int charx = 0;
	int chary = 0;
	int charw = 72;
    int charh = 128;
    
    int vel = 100;
	int velx;
	int vely;
    
    boolean LEFT, RIGHT,UP,DOWN;
    
    float raio = 10;
    float cx = 12; 
    float cy = 16;
    
    int life = 100;

	public Personagem(BufferedImage img,int x,int y, int character) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.img = img;
		
		charx = character%4;
		chary = character/4;
	}
	
	@Override
	public void simulaSe(long DiffTime) {
		// TODO Auto-generated method stub
		frametimer+=DiffTime;
		frame = (frametimer/intervalo)%3;
		
		float oldx = x;
		float oldy = y;
		
		x+=velx*DiffTime/1000.0;
		y+=vely*DiffTime/1000.0;
				
		int bx = (int)((x+12)/GamePanel.mapa.tileW);
		int by = (int)((y+28)/GamePanel.mapa.tileH);
		
		//System.out.println(" bx "+bx+" by "+by);
		
		if(bx<0||by<0||bx>=GamePanel.mapa.Largura||by>=GamePanel.mapa.Altura||GamePanel.mapa.mapa2[by][bx]!=0){
			x = oldx;
			y = oldy;
			velx = -velx;
			vely = -vely;
		}
		
		if((x>GamePanel.mapa.Largura*GamePanel.mapa.tileW-24) || x <= 0){
			x = oldx;
			y = oldy;
			velx = -velx;
			vely = -vely;
		}
		if((y>GamePanel.mapa.Altura*GamePanel.mapa.tileH-32) || y <= 0){
			x = oldx;
			y = oldy;
			velx = -velx;
			vely = -vely;
		}
		
		for(int i = 0; i <GamePanel.listaDePersonagens.size();i++){
			Personagem pers = (Personagem)GamePanel.listaDePersonagens.get(i);
			if(pers!=this && colideRect(pers)){
				x = oldx;
				y = oldy;
				velx = -velx;
				vely = -vely;
				break;
			}
		}
	}

	@Override
	public void desenhaSe(Graphics2D dbg,int telax,int telay){
		// TODO Auto-generated method stub
		dbg.drawImage(img, (int)x-telax,(int)y-telay,(int)x+24-telax,(int)y+32-telay,charx*charw+frame*24,chary*charh+anim*32,charx*charw+frame*24+24,chary*charh+anim*32+32,null);
		dbg.setColor(Color.red);
		dbg.drawOval((int)(x-telax-raio+cx),(int)(y-telay-raio+cy), (int)raio*2, (int)raio*2);
		dbg.setColor(Color.yellow);
		dbg.drawRect((int)(x-telax),(int)(y-telay), 24, 32);
		
		dbg.setColor(Color.gray);
		//dbg.fillRect(5, 10, 200, 25);
		dbg.fillRect((int)x-telax,(int)y-telay -8 , 25, 10);
	}
	
	public boolean colide(Personagem pers){
		float dx = (pers.x+pers.cx) - (x+cx);
		float dy = (pers.y+pers.cy) - (y+cy);
		float sraio = pers.raio+raio;
		
		if(sraio*sraio>(dx*dx+dy*dy)){
			return true;
		}
		return false;
	}
	
	public boolean colideRect(Personagem pers){
		float x1 = x;
		float x2 = x+24;
		float x3 = pers.x;
		float x4 = pers.x+24;
		float y1 = y;
		float y2 = y+32;
		float y3 = pers.y;
		float y4 = pers.y+32;
		
		if(x3<x2&&x4>x1&&y3<y2&&y4>y1){
			return true;
		}
		
		return false;
	}	
	
	
	public void levaDano(int dano){
		life-=dano;
		if(life<=0){
			vivo = false;
		}
	}
}
