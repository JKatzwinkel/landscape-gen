package landscape;

import java.awt.*;

public class Camera {
	
	public double x;
	public double y;
	public double z;
	
	public Scene scn;
	public double ratio; 
	
	private double degX; // Drehung um die X-Achse
	private double degY; // Drehung um die Y-Achse
	
	private double ux,uy,uz;

	public Camera (double x, double y, double z, Scene s) {
		this.x=x;
		this.y=y;
		this.z=z;
		scn = s;
		ratio = (double)scn.height/(double)scn.width;
		setDegY(0);
	}

	public Point onScreen(double x, double y, double z){
		int px = (int)(640+640*(x-this.x)/(this.z-z));
		int py = (int)(300+640*(this.y-y)/(this.z-z));
		return new Point(px,py);
	}

	public void setDegY(double d){
		degY = d;
		ux = Math.sin((d+90)*Math.PI/180);
		uz = -Math.cos((d+90)*Math.PI/180);
		uy = 0;
	}	
	
	public void setDegX(double d){
		degX = d;
	}

	public void lookAt(double lx, double ly, double lz) {

		double deg_y = Math.atan2((z-lz),(x-lx));

		double hypo = Math.sqrt((lz-z)*(lz-z) + (ly-y)*(ly-y) + (lx-x)*(lx-x));
		double deg_x = Math.asin((ly-y)/hypo);
		System.out.println("ASIN("+(ly-y)+" / "+hypo+") = "+Math.asin((ly-y)/hypo));
//		if ((lz-z) != 0) deg_x = Math.atan((ly-y)/Math.abs(z-lz));

		deg_x = deg_x*180d/Math.PI;
		deg_y = deg_y*180d/Math.PI-90d;

		System.out.println("Neigung um X-Achse: "+deg_x);
		System.out.println("Neigung um Y-Achse: "+deg_y);

		setDegY(deg_y);
		setDegX(deg_x);
	}
	
	public Point3d onVirtuaScreen(double px, double py){
		
		double degyax = degY -40d+80d * px/(double)scn.width; // Winkel um die Y-Achse
		double deg = degX -ratio*50d + ratio*100d * ((double)scn.height-py)/(double)scn.height;
		
		double vx = Math.sin(degyax * Math.PI/180);
		double vz = -Math.cos(degyax * Math.PI/180);
		double vy = 0;
		
		double c = Math.cos(deg*Math.PI/180);
		double s = Math.sin(deg*Math.PI/180);		
		
		double vx_ = vx*(ux*ux + (1-ux*ux) * c)
						+vy*(ux*uy * (1-c) - uz * s)
						+vz*(ux*uz * (1-c) + uy * s);
						
		double vy_ = vx*(ux*uy * (1-c) + uz * s)
						+vy*(uy*uy + (1-uy*uy) * c)
						+vz*(uy*uz * (1-c) - ux * s);
						
		double vz_ = vx*(ux*uz * (1-c) - uy * s)
						+vy*(uy*uz * (1-c) + ux * s)
						+vz*(uz*uz + (1-uz*uz) * c);
		
		
		
		
		
		return new Point3d(vx_*100+x,vy_*100+y,vz_*100+z);		
	}
}
