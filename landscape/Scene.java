package landscape;

import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Scene {

	int width, height;

	public BufferedImage fieldOfDepth;
	public BufferedImage picture;
	
	public Graphics2D fod;
	public Graphics2D pict;

	public Camera cam;
	public LinkedList<Light> lights;

	public LinkedList<LandScapeElementInterface> elements;

	public Scene (int w, int h) {
		width = w; height = h;

		elements = new LinkedList<LandScapeElementInterface>();
		lights = new LinkedList<Light>();

		setCam(256,150,1100);

		initGfx();

//		System.out.println( fieldOfDepth.getRGB(100,100) );
	}
	
	public void initGfx() {
		fieldOfDepth = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
		picture = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
		fod = fieldOfDepth.createGraphics();
		pict = picture.createGraphics();
	}
	
	public void fadeImage() {
		for (int x = 0;x<width;x++)
			for (int y=0;y<height;y++){
				int col = picture.getRGB(x,y);
				int r = col >> 16 & 0xff;
				int g = col >> 8 & 0xff;
				int b = col & 0xff;
				
				picture.setRGB(x,y,colorRGB(r/2,g/2,b/2));
			}
	}
	
	/**
	* Fügt Landschaftselemente hinzu
	*/
	public void add (LandScapeElementInterface e){
		System.out.println("Adding Element");
		elements.add(e);
	}

	public void addLight(Light l){
		lights.add(l);
		add(l);
	}

	public void moveLights(){
		for (Light l : lights)
			l.move();
	}

	public void setCam(double x, double y, double z){
		cam = new Camera(x, y, z, this);
	}

	public int colorRGB(int r, int g, int b){
		return r<<16 | g<<8 | b;
	}
	
	public int colorRGB(Color c){
		return colorRGB(c.getRed(),c.getGreen(),c.getBlue());
	}

	public void putDepth(int x, int y, double depth){
		int d = (int)(depth*256);
		int b = d & 0xff;
		int g = d >> 8 & 0xff;
		int r = d >> 16;
		fieldOfDepth.setRGB(x,y, r<<16 | g<<8 | b);
	}

	public double getDepth(int x, int y){
		int depth = fieldOfDepth.getRGB(x,y);
		int r = depth >> 16 & 0xff;
		int g = depth >> 8 & 0xff;
		int b = depth & 0xff;
		return (double)(((r*256)+g)*256+b)/256;
	}	
	
	public void putPixel(int x, int y, Color color){
		picture.setRGB(x,y,colorRGB(color.getRed(), color.getGreen(), color.getBlue()));
	}

	public void putPixel(double x ,double y, Color color){

		int px=(int)(x-.5);
		int py=(int)(y-.5);		

		for (int nx=px;nx<px+2;nx++)
			for (int ny=py;ny<py+2;ny++)
				if ((nx>=0) && (nx<width) && (ny>=0) && (ny<height)) {
					
						int col = picture.getRGB(nx,ny);
						int r = col >> 16 & 0xff;
						int g = col >> 8 & 0xff;
						int b = col & 0xff;

		//				System.out.println(r+" "+g+" "+b+" ");
					
						double fade = ((1-Math.abs(nx+0.5-x)) * (1-Math.abs(ny+0.5-y)));
		//				System.out.println(fade);

						
						r = r + (int)((color.getRed()-r)*fade);
						g = g + (int)((color.getGreen()-g)*fade);
						b = b + (int)((color.getBlue()-b)*fade);

						picture.setRGB(nx,ny,colorRGB(r,g,b));
						
						

				}

/*		for (int nx=px-1;nx<=px+1;nx++)
			for (int ny=py-1;ny<=py+1;ny++)
				if ((nx>=0) && (nx<1280) && (ny>=0) && (ny<800)) {

					double dist = Math.sqrt(Math.pow(nx-x,2)+Math.pow(ny-y,2));
			

					if (dist < 1){
					
						int col = picture.getRGB(nx,ny);
						int r = col >> 16;
						int g = col >> 8 & 255;
						int b = col & 255;
						
						dist=Math.sqrt(dist);

						r = (int)(r + (double)(color.getRed()-r)*(1-dist));
						g = (int)(g + (double)(color.getGreen()-g)*(1-dist));
						b = (int)(b + (color.getBlue()-b)*(1-dist));
						
						picture.setRGB(nx,ny,colorRGB(r,g,b));
					}
				}*/

	//	picture.setRGB(px,py,colorRGB(color.getRed(),color.getGreen(),color.getBlue()));

	}
	
	public void drawPoint(double x, double y, double z, Color color){
		Point p = cam.onScreen(x,y,z);
		double px = p.x;
		double py = p.y;
		//double px = (640+640*(x-cam.x)/(cam.z-z));
		//double py = (300+640*(cam.y-y)/(cam.z-z));

		if ((px>=0) && (px<width) && (py>=0) && (py<height)) 
			if (fieldOfDepth.getRGB((int)px,(int)py) <= (int)z){
				putPixel(px,py,color);
				//picture.setRGB((int)px,(int)py,colorRGB(color));
				fieldOfDepth.setRGB((int)px,(int)py,(int)z);
				//System.out.println((int)z);
			}

	}

	
	public void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
		Point p0 = cam.onScreen(x1,y1,z1);
		Point p1 = cam.onScreen(x2,y2,z2);

		double dist = Math.sqrt(Math.pow(p1.x-p0.x,2) + Math.pow(p1.y-p0.y,2));

		for (double i=0;i<dist;i+=1)
			drawPoint(x1+(x2-x1)*i/dist, y1+(y2-y1)*i/dist, z1+(z2-z1)*i/dist, color);
	}	


	public void drawRectangle(double x1,double y1,double z1,
					double x2,double y2,double z2,
					double x3,double y3,double z3,
					Color c1, Color c2, Color c3) {

		Point p1 = cam.onScreen(x1,y1,z1);
		Point p2 = cam.onScreen(x2,y2,z2);
		Point p3 = cam.onScreen(x3,y3,z3);

		
		boolean exch=true;
		while (exch){
			exch=false;
			if (p1.y>p2.y) {Point t = new Point(p1.x,p1.y);p1=p2;p2=t;exch=true;}
			if (p1.y>p3.y) {Point t = new Point(p1.x,p1.y);p1=p3;p3=t;exch=true;}
			if (p2.y>p3.y) {Point t = new Point(p2.x,p2.y);p2=p3;p3=t;exch=true;}
		}
		

				
		//if (p2.y>p1.y) if (p3.y>p1.y)
		for (double i = p1.y; i < p2.y; i+=1) {
			double line_l = p1.x + (p2.x-p1.x) * (i-p1.y) / ((p2.y-p1.y)+1);
			double line_r = p1.x + (p3.x-p1.x) * (i-p1.y) / ((p3.y-p1.y)+1);
			if (line_l>line_r) {double line_ = line_r;line_r=line_l;line_l=line_;}	
			for (double x = line_l; x <= line_r; x+=1)
				if ((x>=0) && (x<width) && (i>=0) && (i<height)) {
					double z_l = z1 + (z2-z1) * (i-p1.y) / (p2.y-p1.y);
					double z_r = z1 + (z3-z1) * (i-p1.y) / (p3.y-p1.y);
					double z = z_l + (z_r-z_l) * (x-line_l) / (line_r-line_l);
					if (fieldOfDepth.getRGB((int)x,(int)i) < (int)z){
						putPixel(x,i,c1);
						fieldOfDepth.setRGB((int)x,(int)i,(int)z);
					}
				}
			
				
		}

		//if (p2.y>p1.y) if (p3.y>p2.y)
		for (double i = p2.y; i < p3.y;i+=1) {
			double line_l = p1.x + (p3.x-p1.x) * (i-p1.y) / ((p3.y-p1.y)+1);
			double line_r = p2.x + (p3.x-p2.x) * (i-p2.y) / ((p3.y-p2.y)+1);
			if (line_l>line_r) {double line_ = line_r;line_r=line_l;line_l=line_;}	
			for (double x = line_l;x <= line_r; x+=1)
				if ((x>=0) && (x<width) && (i>=0) && (i<height)) {
					double z_l = z1 + (z3-z1) * (i-p1.y) / (p3.y-p1.y);
					double z_r = z2 + (z3-z2) * (i-p1.y) / (p3.y-p2.y);
					double z = z_l + (z_r-z_l) * (x-line_l) / (line_r-line_l);
					if (fieldOfDepth.getRGB((int)x,(int)i) < (int)z){
						putPixel(x,i,c1);
						fieldOfDepth.setRGB((int)x,(int)i,(int)z);
					}
				}
		}

		//} catch (Exception e) {}

	}



	public void save(String filename, String type) {
		try{		
			ImageIO.write( picture, type, new File( filename+"."+type ) );
			System.out.println("Saving under "+filename+"."+type);
		} catch (Exception e) {
			System.out.println("Kann Datei "+filename+"."+type+" nicht anlegen");
		}
	}


	public void saveFoD(String filename, String type) {
		try{		
			ImageIO.write( fieldOfDepth, type, new File( filename+"."+type ) );
			System.out.println("Saving Field of Depth under "+filename+"."+type);
		} catch (Exception e) {
			System.out.println("Kann Datei "+filename+" nicht anlegen");
		}
	}

}
