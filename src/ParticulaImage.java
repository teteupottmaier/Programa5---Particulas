import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParticulaImage extends Particula{
	BufferedImage image1;
	BufferedImage image2;
	
	public ParticulaImage(float x, float y, float velX, float velY, int tempoDeVida,BufferedImage img1,BufferedImage img2) {
		super(x, y, velX, velY, tempoDeVida, Color.white, Color.black);
		// TODO Auto-generated constructor stub
		this.image1 = img1;
		this.image2 = img2;
	}
	
	@Override
	public void desenhaSe(Graphics2D dbg,int telax,int telay){
		// TODO Auto-generated method stub
		float prop = timer/(float)tempoDeVida;
		float propinv = 1-prop;
			
		float alpha = propinv;
		//System.out.println("alpha "+alpha+" "+propinv);
		
		int sz = 6+(int)(24*prop);
		//dbg.fillRect( (int)x-telax-sz/2,(int)y-telay-sz/2, sz, sz);
		
		Composite comp = dbg.getComposite();
		
			AlphaComposite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		    dbg.setComposite(c);
		      
			dbg.drawImage(image1, (int)x-telax-sz/2,(int)y-telay-sz/2, sz, sz, null);
			
			c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f-alpha);
		    dbg.setComposite(c);
		      
			dbg.drawImage(image2, (int)x-telax-sz/2,(int)y-telay-sz/2, sz, sz, null);
		dbg.setComposite(comp);
		
	}

}
