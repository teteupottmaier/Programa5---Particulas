import java.awt.Color;
import java.awt.Graphics2D;

public class ParticulaFumaca extends Particula {

	public ParticulaFumaca(float x, float y, float velX, float velY, int tempoDeVida, Color cor1, Color cor2) {
		super(x, y, velX, velY, tempoDeVida, cor1, cor2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void desenhaSe(Graphics2D dbg,int telax,int telay){
		// TODO Auto-generated method stub
		float prop = timer/(float)tempoDeVida;
		float propinv = 1-prop;
			
		int alpha = (int)(255*(Math.sqrt(propinv)));
		//System.out.println("alpha "+alpha+" "+propinv);

		int r = Math.min((int)(r1*propinv+r2*prop),255); 
		int g = Math.min((int)(g1*propinv+g2*prop),255); 
		int b = Math.min((int)(b1*propinv+b2*prop),255); 
		
		int sz = 4+(int)(10*prop);
		
		dbg.setColor(new Color(r,g,b,alpha));
		dbg.fillRect( (int)x-telax-sz/2,(int)y-telay-sz/2, sz, sz);
	}

}
