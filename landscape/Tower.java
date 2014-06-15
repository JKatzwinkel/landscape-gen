package landscape;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Tower implements LandScapeElementInterface {

	double x;
	double z;
	double height;

	public Tower(double x, double z, double height){
		this.x=x;
		this.z=z;
		this.height=height;
	}

	public boolean collide(Ray r){
		if ((r.y < height) && (r.z < z+7) && (r.z > z-7) && (r.x > x-7) && (r.x < x+7)) {
			if (r.camera) {
				r.r+=5;
				r.g+=5;
			}
			

			return true;
		}
		return false;
	}
}
