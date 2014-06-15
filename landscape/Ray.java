package landscape;

import java.awt.*;


public class Ray {

	public Scene scn;

	public double x, y, z;
	public double dx,dy,dz;
	public double r,g,b;
	
	public int counter;
	//Ob der Strahl direkt aus der Kamera kommt oder zur Belichtung ist	
	public boolean camera;
	public int max;
	
	public Ray(double x, double y, double z, int max){
		this.x=x;
		this.y=y;
		this.z=z;
		counter=0;
		r = 50;g=47;b=30;
		camera = true;
		this.max = max;
	}

	public Ray(double x, double y, double z, boolean cm, int max){
		this.x=x;
		this.y=y;
		this.z=z;
		counter=0;
		r = 0;g=0;b=0;
		camera = cm;
		this.max = max;
	}
	
	public void color(double r, double g, double b) {
	 	this.r=r;
	 	this.g=g;
	 	this.b=b;
	}
	
	/**
	* Legt die Richtung des Strahls fest. Länge des Richtungsvektors konstant
	*/
	public void direct(double x, double y, double z, double l){
		dx = x-this.x;
		dy = y-this.y;
		dz = z-this.z;
		
		double length = Math.sqrt( dx*dx + dy*dy + dz*dz );
	
		if (l!=0){
			dx = dx/(length/l);
			dy = dy/(length/l);
			dz = dz/(length/l);
		} else 
			while (dx*dx + dy*dy + dz*dz > 9 ) {
				dx*=.5;
				dy*=.5;
				dz*=.5;
			}
		
		
		x+=dx*2;
		y+=dy*2;
		z+=dz*2;
	}
	
	public void trace(Scene scn) {
		this.scn = scn;	
	
		double cc = (1d+counter/1000d);

		x+=dx*cc;
		y+=dy*cc;
		z+=dz*cc;
		
/*		dx*=1.05;
		dy*=1.05;
		dz*=1.05;*/
		
		r+=.18* cc*.84  -  (.17+y/14000d)* (.5+counter/500d);
//		r+=.18* cc*1.34  -  (.17+y/19000d)* (1d+counter/330d)/2;
		g+=.165*cc*.83  -  (.179+y/10500d)*(.5+counter/500d);
		b+=.19* cc*.83  -  (.177+y/11500d)*(.5+counter/500d);
//		b+=.19* cc*1.43  -  (.177+y/17000d)*(1d+counter/330d)/2;

	
		
//		double distlight=Math.sqrt((scn.light.x-x)*(scn.light.x-x) + (scn.light.y-y)*(scn.light.y-y) + (scn.light.z-z)*(scn.light.z-z))/2;		
		
//		r+=scn.light.brightness/(distlight+0.001)*(1d+counter/1000);	
//		g+=scn.light.brightness/(distlight+0.001)*(1d+counter/1000);	
//		b+=scn.light.brightness/(distlight+0.001)*(1d+counter/1000);	
		
		counter++;
	//	System.out.println("Trace Ray");
		for (LandScapeElementInterface e : scn.elements){
	//		System.out.println("Check Kollision");
			if (e.collide(this)) {
//				System.out.println("Hit the Ground: "+(int)x);
				counter = max;
				
				
				if (camera) {
					for (Light l : scn.lights){
						Ray light = new Ray(x,y,z,false,max);
						light.direct(l.x,l.y,l.z,.9);
						this.addRay(light,.6);
					}
				}
			}
		}
	}
	
	public void addRay(Ray ray, double opacity) {
		while (ray.counter < ray.max)
			ray.trace(scn);
		r+=ray.r*opacity;
		g+=ray.g*opacity;
		b+=ray.b*opacity;
	}
	

	//Problematisch
	public Color color(){
		if (r<0) r=0;
		if (g<0) g=0;
		if (b<0) b=0;
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;
		return new Color((int)r,(int)g,(int)b);
	}

	public double distance(){
		return Math.sqrt( (scn.cam.x-x)*(scn.cam.x-x) + (scn.cam.y-y)*(scn.cam.y-y) + (scn.cam.z-z)*(scn.cam.z-z) );
	}

}
