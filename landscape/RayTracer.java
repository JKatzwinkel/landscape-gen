package landscape;

import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class RayTracer {
	
	public Scene scn;

	public RayTracer (Scene scn) {
		this.scn = scn; 
	}

	

	public int colorRGB(int r, int g, int b){
		return r<<16 | g<<8 | b;
	}

	
	public void render(){
		System.out.println("Starting Renderer");
		int prog = 0;

		for (int i=0;i<scn.width;i++) {
			for (int j=0;j<scn.height;j++){
				Ray r = new Ray(scn.cam.x, scn.cam.y, scn.cam.z, 2000);
		//		r.direct(scn.cam.x-scn.width/2+i, scn.cam.y-scn.height/2+(scn.height-j), scn.cam.z-100, .5);
				Point3d to = scn.cam.onVirtuaScreen(i,j);
				r.direct(to.x, to.y, to.z, 1);
				
				while (r.counter < r.max) {
					r.trace(scn);
				}
				
				scn.putPixel(i,j,r.color());
				scn.putDepth(i,j,r.distance());
/*				if ((i==j) && (i % 20 ==0)) {
					System.out.println("("+i+","+j+")");
					System.out.println(" Distance to Camera: "+r.distance()+", put "+scn.getDepth(i,j)+" in FoD");
				}*/
				//scn.putPixel(i,j,r.color());

			}
			if (i*100/scn.width+1>=prog+5){
				prog = i*100/scn.width+1;
				System.out.print("\r"+"||||||||||||||||||||".substring(0,prog/5)+"                    ".substring(0,20-prog/5)+prog+" %");
			}
		}
		System.out.println("\r");
	}

}
