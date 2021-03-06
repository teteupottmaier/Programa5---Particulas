import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Projetil extends Sprite {
	
	float velx;
	float vely;
	
    float raio = 2;
    
    Personagem pai = null;
    
    int dano = 10;

	public Projetil(int x,int y, float velX,float velY,Personagem pai) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.velx = velX;
		this.vely = velY;
		
		this.pai = pai;
	}
	
	@Override
	public void simulaSe(long DiffTime) {
		// TODO Auto-generated method stub
		float oldx = x;
		float oldy = y;
		
		x+=velx*DiffTime/1000.0;
		y+=vely*DiffTime/1000.0;
		
		double ang = Math.toDegrees(Math.atan2(vely, velx));
			
//		for(int i = 0; i < 15;i++){
//			double ang2 = Math.toRadians(ang-30+GamePanel.rnd.nextInt(60)-180);
//			float v = GamePanel.rnd.nextInt(50)+30;
//			
//			int variacao = GamePanel.rnd.nextInt(15);
//			
//			float x2 = x+(float)Math.cos(Math.toRadians(ang+180))*variacao;
//			float y2 = y+(float)Math.sin(Math.toRadians(ang+180))*variacao;
//			
//			ParticulaFumaca part = new ParticulaFumaca(x2, y2,(float)Math.cos(ang2)*v,(float)Math.sin(ang2)*v, 800,Color.yellow,Color.lightGray);
//			GamePanel.listaDeParticulas.add(part);
//		}	
		
		for(int i = 0; i < 7;i++){
			double ang2 = Math.toRadians(ang-30+GamePanel.rnd.nextInt(60)-180);
			float v = GamePanel.rnd.nextInt(30)+20;
			
			int variacao = GamePanel.rnd.nextInt(15);
			
			float x2 = x+(float)Math.cos(Math.toRadians(ang+180))*variacao;
			float y2 = y+(float)Math.sin(Math.toRadians(ang+180))*variacao;
			
			ParticulaImage part = new ParticulaImage(x2, y2,(float)Math.cos(ang2)*v,(float)Math.sin(ang2)*v, 800,GamePanel.imgFogo,GamePanel.imgFumaca);
			GamePanel.listaDeParticulas.add(part);
		}		
		
		
		
		int bx = (int)((x)/GamePanel.mapa.tileW);
		int by = (int)((y)/GamePanel.mapa.tileH);
		
		if(bx<0||by<0||bx>=GamePanel.mapa.Largura||by>=GamePanel.mapa.Altura||GamePanel.mapa.mapa2[by][bx]!=0){
			x = oldx;
			y = oldy;
			vivo = false;
			
			for(int i = 0; i < 50;i++){
				double ang2 = Math.toRadians(ang-30+GamePanel.rnd.nextInt(60)-180);
				float v = GamePanel.rnd.nextInt(180)+20;
				
				Particula part = new Particula(x, y,(float)Math.cos(ang2)*v,(float)Math.sin(ang2)*v, 400,Color.gray,Color.white);
				GamePanel.listaDeParticulas.add(part);
			}

		}
		
		if((x>GamePanel.mapa.Largura*GamePanel.mapa.tileW-24) || x <= 0){
			x = oldx;
			y = oldy;
			vivo = false;
		}
		if((y>GamePanel.mapa.Altura*GamePanel.mapa.tileH-32) || y <= 0){
			x = oldx;
			y = oldy;
			vivo = false;
		}
		
		for(int i = 0; i <GamePanel.listaDePersonagens.size();i++){
			Personagem pers = (Personagem)GamePanel.listaDePersonagens.get(i);
			if(pai!=pers&&colide(pers)){
				x = oldx;
				y = oldy;
				vivo = false;
				pers.levaDano(dano);
				
				for(int j = 0; j < 50;j++){
					double ang2 = Math.toRadians(ang-30+GamePanel.rnd.nextInt(60)-180);
					float v = GamePanel.rnd.nextInt(180)+20;
					
					Particula part = new Particula(x, y,(float)Math.cos(ang2)*v,(float)Math.sin(ang2)*v, 400,Color.red,new Color(80,0,0));
					GamePanel.listaDeParticulas.add(part);
				}
				for(int j = 0; j < 10;j++){
					double ang2 = Math.toRadians(ang-20+GamePanel.rnd.nextInt(40));
					float v = GamePanel.rnd.nextInt(180)+20;
					
					Particula part = new Particula(x, y,(float)Math.cos(ang2)*v,(float)Math.sin(ang2)*v, 400,Color.red,new Color(80,0,0));
					GamePanel.listaDeParticulas.add(part);
				}				
				
				break;
			}
		}
	}

	@Override
	public void desenhaSe(Graphics2D dbg,int telax,int telay){
		// TODO Auto-generated method stub
		dbg.setColor(Color.black);
		dbg.fillRect( (int)x-telax-2,(int)y-telay-2, 3, 3);
	}
	
	public boolean colide(Personagem pers){
		float dx = (pers.x+pers.cx) - x;
		float dy = (pers.y+pers.cy) - y;
		float sraio = pers.raio+raio;
		
		if(sraio*sraio>(dx*dx+dy*dy)){
			return true;
		}
		return false;
	}
}
