package landscape;

import java.util.Random;
import java.awt.Color;

public class RT7_anim {
	public static void main(String[] args){

		Random rnd = new Random();
		
		Scene scn = new Scene(360,200);

		RayTracer raytrace = new RayTracer(scn);

		Ground ground = new Ground(1024, scn);
		ground.init(1500);

		ground.initTexture();	

		Light light = new Light(512,ground.topo[512][700]+20+rnd.nextInt(80),700,10, new Color(10,10,19));

		scn.addLight(light);

		light.whiz(700d, ground, 350);

		scn.cam.x = 512;
		scn.cam.y = 130d;
		scn.cam.z = 1000;

		double lx = light.x;
		double ly = light.y;
		double lz = light.z;

		double lxv = 0;
		double lyv = 0;
		double lzv = 0;		

		for (int i=0;i<400;i++){
			
			scn.cam.lookAt(lx,ly,lz);


			scn.initGfx();		
			raytrace.render();
			new AntiAliasing(scn).render(512,7);

			scn.save("19/rays"+"000".substring(0,3-(""+i).length())+i, "gif");

			scn.moveLights();
			

			lxv = lxv*4/5+(light.x-lx)/50;
			lyv = lxv*4/5+((light.y-15)-ly)/50;
			lzv = lzv*4/5+(light.z-lz)/50;

			lx += lxv;
			ly += lyv;
			lz += lzv;
		}
	}
}
