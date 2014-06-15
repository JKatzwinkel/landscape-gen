package landscape;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class AntiAliasing {

	BufferedImage img;
	Scene scn;	

	public AntiAliasing(Scene scn){
		img = new BufferedImage(scn.width, scn.height, BufferedImage.TYPE_INT_RGB);
		this.scn = scn;
	}

	public void render(double focus, double fact){
		int prog=0;
		System.out.println("Starting AntiAliasing with focus "+focus+" and factor "+fact);
		for (int x = 0; x < scn.width; x++){
			for (int y = 0; y < scn.height; y++){
				blur(x,y, fact, focus);
			}
			if (100*x/scn.width>prog+5){
				prog = 100*x/scn.width;
				System.out.print("\r"+"||||||||||||||||||||".substring(0,prog/5)+"                    ".substring(0,20-prog/5)+prog+" %");
			}
		}
		System.out.println("\r");
		scn.picture = img;
		System.out.println("Copying post processed image");
//		save("tiefenschaerfe2", "png");
///		System.out.println("Depth "+scn.getDepth(scn.width/2, scn.height-10)+" at ("+scn.width/2+","+(scn.height-10)+")");
	}

	public int colorRGB(int r, int g, int b){
		return r<<16 | g<<8 | b;
	}

	private void blur(int x, int y, double fact, double focus){
		int r=0;
		int g=0;
		int b=0;
		double depth = scn.getDepth(x,y);
		double range = Math.abs(depth-focus)*fact/1000d+1;
		if (range > 6) range=6;
		int div=0;
		for (int nx = x - (int)Math.round(range); nx < x + (int)Math.round(range); nx++)
			for (int ny = y - (int)Math.round(range); ny < y + (int)Math.round(range); ny++)
				if ((nx >= 0) && (nx < scn.width) && (ny >= 0) && (ny < scn.height)) 
					if (depth<=scn.getDepth(nx,ny)*1.3) if ((nx-x)*(nx-x)+(ny-y)*(ny-y) <= range*range){
						div++;
						int col = scn.picture.getRGB(nx,ny);
						r += col >> 16 & 0xff;
						g += col >> 8 & 0xff;
						b += col & 0xff;
					}
		r /= div;
		g /= div;
		b /= div;
		img.setRGB(x,y, colorRGB(r, g, b) );
			

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
