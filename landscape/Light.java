package landscape;

import java.util.Random;
import java.awt.*;

public class Light implements LandScapeElementInterface{

	
	public double x,y,z;
	public Color col;
	public double sx,sy,sz; //Start point
	public double brightness;
	public double dx,dy,dz;	//Direction
	public double tx,ty,tz;	//Target
	private int mode;
	private double range;
	private LandScapeElementInterface crashobject;
	private int count;
	private int frames;

	
	public Light(double x, double y, double z, double b, Color c) {
		this.x=x;
		this.y=y;
		this.z=z;
		sx=x;sy=y;sz=z;
		this.brightness = b;
		this.col = c;
	}
	
	public void crash(double x, double y, double z, LandScapeElementInterface object){
		mode = 1; 
		dx = (x-this.x)/35;
		dy = (y-this.y)/35;
		dz = (z-this.z)/35;
		crashobject=object;
	}
	
	public void whiz(double range, LandScapeElementInterface obj, int frames) {
		mode = 4;
		this.range = range;
		
		Random rnd = new Random();		
		
		tx = sx-range/2+rnd.nextDouble()*range;
		ty = rnd.nextDouble()*range/2+20;
		tz = sz-range/2+rnd.nextDouble()*range;
		
/*		dx += (tx-x)/300;
		dy += (ty-y)/300;
		dz += (tz-z)/300;*/
		
		crashobject = obj;
		this.frames = frames;
	}
	
	/**
	* Dreht sich un Mittelpunkt m
	*/
	public void circle(double mx, double my, double mz){
		mode = 20;
		
		tx=mx;
		tz=mz;
		ty=my;
	}
	
	public void fire(LandScapeElementInterface obj){
		crashobject = obj;
		y=0;
		Ray tmp = new Ray(x,y,z,0);
		obj.collide(tmp);
		y=tmp.y;
		mode = 25;
		dx=new Random().nextDouble()*4-2;
		dz=new Random().nextDouble()*4-2;
		dy=1;
		brightness=8;
	}
	
	
	
	public void move(){
		x+=dx;
		y+=dy;
		z+=dz;
		
		count++;
		
		Random rnd = new Random();
		
		
		//Crash down
		if (mode == 1){
			dy-=3;
			if (crashobject.collide(new Ray(x,y,z,0))) {
				mode = 2;
				dx=0;
				dy=.5;
				dz=0;
			}
		}
		if (mode == 2){
			brightness += 10;
			if (brightness > 100) mode = 3;
		}
		if (mode == 3){
			brightness*=.6;
		}
		
		
		//Whizz around
		if ((mode >= 4)&&(mode <= 16)) {
	/*		double dist = Math.sqrt((x-tx)*(x-tx)+(y-ty)*(y-ty)+(z-tz)*(z-tz));
			dx = (tx-x)/40+dx/(1000/(dist+1));
			dy = (ty-y)/40+dy/(1000/(dist+1));
			dz = (tz-z)/40+dz/(1000/(dist+1));*/
			dx = (dx+(tx-x)/40)/2;
			dy = (dy+(ty-y)/40)/2;
			dz = (dz+(tz-z)/40)/2;
			
			rnd = new Random();			
			
			brightness += (12-brightness)/4;
			//if (rnd.nextInt(25)<3) brightness*=rnd.nextDouble()*.3+1;
						
			if (count>frames/24+rnd.nextInt(frames/12)) if (mode < 16) {
				rnd = new Random();
				tx = sx-range/2+rnd.nextDouble()*range;
				tz = sz-range/2+rnd.nextDouble()*range;
				ty = rnd.nextDouble()*range/2+20;
				mode ++;
				System.out.println("Mode: "+mode);
				count = 0;
			} else {
				tx = sx;
				ty = sy;
				tz = sz;
			}
			
			if (crashobject.collide(new Ray(x,y,z,0))) {
				dy = 20;
				dx = 0;
				dz = 0;
				brightness *=2;
				rnd = new Random();
				tx = 512-range/2+rnd.nextDouble()*range;
				ty *= 2;
				tz = 512-range/2+rnd.nextDouble()*range;
			}
			
			if (mode == 16) {
				dx = (dx+(tx-x)/10)/2;
				dy = (dy+(ty-y)/10)/2;
				dz = (dz+(tz-z)/10)/2;
			}				
		}
		
		
		//Fly in Cirle
		if (mode == 20) {
			dx = -(tz-z);
			dz = (tx-x);
			double radius = Math.sqrt(dx*dx+dz*dz);
			
			dx/=(radius/3);
			dz/=(radius/3);	
		}
		
		
		//Fire Rockets
		if ((mode >= 25)&&(mode<27)) {
			brightness+=rnd.nextDouble()*.7-.1;
			if (mode<26) {
				dy+=3+rnd.nextDouble()*2;
				if (rnd.nextInt(20)<2) {
					dx=rnd.nextDouble()*6-3;
					dz=rnd.nextDouble()*6-3;
				}
				if ((y>360)&&(rnd.nextInt(30)<4)){
					brightness=40+rnd.nextDouble()*20;
					mode=26;
					dy=rnd.nextDouble();
					dx=rnd.nextDouble()-.5;
					dz=rnd.nextDouble()-.5;
					System.out.println("Boom!");
				}
			} else {
				brightness*=.75;
				if (brightness<4) fire(crashobject);
			}
		}
	}
	
	public boolean collide (Ray r){
		if ((x-r.x)*(x-r.x)+(y-r.y)*(y-r.y)+(z-r.z)*(z-r.z) < 20) {
			r.r+=brightness*col.getRed()/(1d+r.counter/700d);
			r.g+=brightness*col.getGreen()/(1d+r.counter/700d);
			r.b+=brightness*col.getBlue()/(1d+r.counter/800d);
			return true;
		}
		return false;
	}

}
