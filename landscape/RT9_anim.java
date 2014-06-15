package landscape;

import java.util.Random;
import java.awt.Color;

public class RT9_anim {
	public static void main(String[] args){

		Random rnd = new Random();
		
		Scene scn = new Scene(300,150);

		RayTracer raytrace = new RayTracer(scn);

		Ground ground = new Ground(1024, scn);
		ground.init(1700);

		ground.initTexture();	

		Light light = new Light(512,250,512,10, new Color(13,12,17));
		light.y = ground.topo[(int)light.x][(int)light.z]+20;
		scn.addLight(light);

		light.whiz(1000d, ground, 1800);

		double lx = light.x;
		double ly = light.y;
		double lz = light.z;
		double lxv = 0;
		double lyv = 0;
		double lzv = 0;	

		scn.cam.y = 1000d;
		scn.cam.x = 1000d;
		scn.cam.z = 1000d;

		double cvy = 0;
		double cvx = 0;
		double cvz = 0;

		int state = 0;

		for (int i=0;i<2000;i++){

			if (state == 0){
				cvx = cvx*2d/3d + (970-scn.cam.x)/400;
				cvz = cvz*2d/3d + ((1000-i)-scn.cam.z)/400;
				if (i>=980) state = 1;			
			} else if (state == 1) {
				cvx = cvx*2d/3d + ((1950-i)-scn.cam.x)/400;
				cvz = cvz*2d/3d + (20-scn.cam.z)/400;
				if (i>=1460) state = 2;
			} else if (state == 2) {
				cvx = cvx*2d/3d + (512-scn.cam.x)/400;
				cvz = cvz*2d/3d + ((20+(i-1470))-scn.cam.z)/400;
				if (i>=1930) state = 3;
			}
		

			scn.cam.y += cvy;
			scn.cam.x += cvx;
			scn.cam.z += cvz;

			cvy = cvy*2d/3d + ((ground.topo[(int)(scn.cam.x)][(int)(scn.cam.z)]+50)-scn.cam.y)/400;
			System.out.println("Y-Value speed: "+cvy);	

			scn.cam.lookAt(lx,ly,lz);
			
			scn.initGfx();		
			raytrace.render();

			double dist = Math.sqrt((lx-scn.cam.x)*(lx-scn.cam.x) + (ly-scn.cam.y)*(ly-scn.cam.y) + (lz-scn.cam.z)*(lz-scn.cam.z));

			new AntiAliasing(scn).render(dist,8);

			scn.save("23/rays"+"0000".substring(0,4-(""+i).length())+i, "gif");

			scn.moveLights();
			

			lxv = lxv*4/5+(light.x-lx)/40;
			lyv = lxv*4/5+(light.y-ly)/40;
			lzv = lzv*4/5+(light.z-lz)/40;

			lx += lxv;
			ly += lyv;
			lz += lzv;
		}
	}
}
