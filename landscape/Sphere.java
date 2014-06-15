package landscape;

import java.awt.Color;

public class Sphere implements LandScapeElementInterface {
	
	public double radius;
	public double x, y, z;
	public Color color;	
	
	public Sphere(double x, double y, double z, double r, Color c) {
		this.x=x;
		this.y=y;
		this.z=z;
		radius = r;
		color = c;
	}
	
	
	public boolean collide(Ray r) {
		if (r.x<x-radius) return false;
		if (r.x>x+radius) return false;
		if (r.y<y-radius) return false;
		if (r.y>y+radius) return false;
		if (r.z<z-radius) return false;
		if (r.z>z+radius) return false;
		
		if ((r.x-x)*(r.x-x)+(r.y-y)*(r.y-y)+(r.z-z)*(r.z-z) < radius*radius) {
			if (r.camera) {
				double dist = Math.sqrt((r.x-x)*(r.x-x)+(r.y-y)*(r.y-y)+(r.z-z)*(r.z-z));
				r.x = x+(r.x-x)*(radius/dist);
				r.y = y+(r.y-y)*(radius/dist);
				r.z = z+(r.z-z)*(radius/dist);
				r.r -= 256-color.getRed();
				r.g -= 256-color.getGreen();
				r.b -= 256-color.getBlue();
			}
			return true;
		}
		return false;
	}

}