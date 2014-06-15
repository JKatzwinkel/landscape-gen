package landscape;

import java.util.Random;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;

public class GroundTexture {

	private BufferedImage img;

	public GroundTexture(int size, Ground grnd){

		System.out.println("Calculating Ground Texture...");		

		img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		double r,g,b;
		Random rnd=new Random();

		System.out.print("Initializing...");

		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				double slope = 0;
				for (int nx=x-1;nx<x+1;nx++)
					for (int ny=y-1;ny<y+1;ny++)
						if ((nx>=0) && (nx < size)) if ((ny>=0) && (ny < size))
							slope += Math.abs(grnd.topo[nx][ny]-grnd.topo[x][y]);
				if (slope<1) slope=1;
				g=5d+80d/(grnd.topo[x][y]/2000d+1d)+rnd.nextDouble()*30d/(slope);
				r=g*(1.17+(-.3+(rnd.nextDouble()*.3))/(grnd.topo[x][y]/200d+1d)/(slope/5+1));
				b=r*(.7+(-.3+(rnd.nextDouble()*.2))/(grnd.topo[x][y]/200d+1d)/(slope/5+1));

				img.setRGB(x,y, colorRGB((int)r, (int)g, (int)b) );
			}
		}



/**/


//		save("bodentextur2","png");

		System.out.print("                \rRandom Growth");

		for (int i=0;i<size*size*8;i++){
			if (i%(size*size/2)==0)System.out.print(".");
			int x=3+rnd.nextInt(size-6);
			int y=3+rnd.nextInt(size-6);
			int nx=x-1+rnd.nextInt(3);
			int ny=y-1+rnd.nextInt(3);
			while ((nx<0)||(nx>=size)||(ny<0)||(ny>=size)){
				nx=x-1+rnd.nextInt(3);
				ny=y-1+rnd.nextInt(3);
			}
			Color dis = getColorAt(x,y);
			Color col = getColorAt(nx,ny);
			if (grnd.topo[nx][ny]>=4.5) 
			   if (col.getGreen()/col.getRed() > dis.getGreen()/dis.getRed()) if (Math.abs(grnd.topo[x][y]-grnd.topo[nx][ny])<.65){
				int c = colorRGB(col.getRed(), col.getGreen(), col.getBlue());
				//img.setRGB(x,y, colorRGB(col.getRed(), col.getGreen(), col.getBlue()));
				//img.setRGB(x-1+rnd.nextInt(3),y-1+rnd.nextInt(3), colorRGB(col.getRed(), col.getGreen(), col.getBlue()));
				for (nx=x-1-rnd.nextInt(3);nx<1+x+rnd.nextInt(3);nx++)
					for (ny=y-1-rnd.nextInt(3);ny<y+1+rnd.nextInt(3);ny++){
						img.setRGB(nx,ny,c);
						grnd.topo[nx][ny] += 1.8;
					}
			}
			
		}

		System.out.print("                 \rAntialiasing...");
/*		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				double sumr = 0;
				double sumg = 0;
				double sumb = 0;
				double div = 0;
				for (int nx=x-1;nx<x+1;nx++)
					for (int ny=y-1;ny<y+1;ny++)
						if ((nx>=0) && (nx < size)) if ((ny>=0) && (ny < size)) {
							Color col = getColorAt(nx,ny);
							sumr+=col.getRed();
							sumg+=col.getGreen();
							sumb+=col.getBlue();
							div++;
						}
				r=sumr/div;
				g=sumg/div;
				b=sumb/div;
				img.setRGB(x,y, colorRGB((int)r, (int)g, (int)b) );
				
			}
		}*/


		System.out.print("        \r");
//		save("bodentextur3","png");
		save("bodentextur","png");
	}

	public int colorRGB(int r, int g, int b){
		return r<<16 | g<<8 | b;
	}

	public Color getColorAt(int x, int y){
		int col = img.getRGB(x,y);
		int r = col >> 16 & 0xff;
		int g = col >> 8 & 0xff;
		int b = col & 0xff;

		return new Color(r,g,b);	
	}

	public Color getColorAt(double x, double y){
		int col = img.getRGB((int)x,(int)y);
		double r = col >> 16 & 0xff;
		double g = col >> 8 & 0xff;
		double b = col & 0xff;

		int col_r = img.getRGB((int)x+1,(int)y);
		double rr = col_r >> 16 & 0xff;
		double gr = col_r >> 8 & 0xff;
		double br = col_r & 0xff;

		double fact = x-Math.floor(x);

		double rf = r+(rr-r)*fact;
		double gf = g+(gr-g)*fact;
		double bf = b+(br-b)*fact;

		
		col = img.getRGB((int)x, (int)y+1);
		r = col >> 16 & 0xff;
		g = col >> 8 & 0xff;
		b = col & 0xff;

		col_r = img.getRGB((int)x+1, (int)y+1);
		rr = col_r >> 16 & 0xff;
		gr = col_r >> 8 & 0xff;
		br = col_r & 0xff;
		
		double rf2 = r+(rr-r)*fact;
		double gf2 = g+(gr-g)*fact;
		double bf2 = b+(br-b)*fact;

		fact = y-Math.floor(y);

		double rff = rf+(rf2-rf)*fact;
		double gff = gf+(gf2-gf)*fact;
		double bff = bf+(bf2-bf)*fact;

		return new Color((int)rff,(int)gff,(int)bff);
	}


	public void save(String filename, String type){
		try{		
			ImageIO.write( img, type, new File( filename+"."+type ) );
			System.out.println("Saving under "+filename+"."+type);
		} catch (Exception e) {
			System.out.println("Kann Datei "+filename+"."+type+" nicht anlegen");
		}
	}


}
