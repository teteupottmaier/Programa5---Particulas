import java.awt.Color;
import java.awt.Graphics2D;

public class Particula extends Sprite {
	
	float velx;
	float vely;
	
	int tempoDeVida = 0;
	int timer = 0;
	
	Color cor1;
	Color cor2;
	
	int r1 = 0;
	int g1 = 0;
	int b1 = 0;
	
	int r2 = 0;
	int g2 = 0;
	int b2 = 0;

	public Particula(float x,float y, float velX,float velY,int tempoDeVida,Color cor1,Color cor2) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.velx = velX;
		this.vely = velY;
		
		this.tempoDeVida = tempoDeVida;
		this.cor1 = cor1;
		this.cor2 = cor2;
		
		r1 = cor1.getRed();
		g1 = cor1.getGreen();
		b1 = cor1.getBlue();
		
		r2 = cor2.getRed();
		g2 = cor2.getGreen();
		b2 = cor2.getBlue();
	}
	
	@Override
	public void simulaSe(long DiffTime) {
		// TODO Auto-generated method stub
		
		timer+=DiffTime;
		
		x+=velx*DiffTime/1000.0;
		y+=vely*DiffTime/1000.0;
		
		if(timer>tempoDeVida){
			vivo = false;
		}
		
	}

	@Override
	public void desenhaSe(Graphics2D dbg,int telax,int telay){
		// TODO Auto-generated method stub
		float prop = timer/(float)tempoDeVida;
		float propinv = 1-prop;
			
		int alpha = (int)(255*propinv);
		//System.out.println("alpha "+alpha+" "+propinv);

		int r = Math.min((int)(r1*propinv/2+r2*prop*2),255); 
		int g = Math.min((int)(g1*propinv/2+g2*prop*2),255); 
		int b = Math.min((int)(b1*propinv/2+b2*prop*2),255); 
		
		dbg.setColor(new Color(r,g,b,alpha));
		dbg.fillRect( (int)x-telax-2,(int)y-telay-2, 4, 4);
	}
}
