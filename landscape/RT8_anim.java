package landscape;

import java.util.Random;
import java.awt.Color;

public class RT8_anim {
	public static void main(String[] args){

		Random rnd = new Random();
		
		Scene scn = new Scene(320,200);

		RayTracer raytrace = new RayTracer(scn);

		Ground ground = new Ground(1024, scn);
		ground.init(1500);

		ground.initTexture();	

		Light light = new Light(512,ground.topo[512][600]+20+rnd.nextInt(80),600,10, new Color(13,13,16));
		scn.addLight(light);

		light.whiz(900d, ground, 480);

		double lx = light.x;
		double ly = light.y;
		double lz = light.z;


		for (int i=0;i<500;i++){
			scn.cam.x = 512+450*Math.sin(((double)i*360/500)*Math.PI/180);	
			scn.cam.y = 120;
			scn.cam.z = 512+450*Math.cos(((double)i*360/500)*Math.PI/180);;

			scn.cam.lookAt(lx,ly,lz);
			
			scn.initGfx();		
			raytrace.render();

			double dist = Math.sqrt((lx-scn.cam.x)*(lx-scn.cam.x) + (ly-scn.cam.y)*(ly-scn.cam.y) + (lz-scn.cam.z)*(lz-scn.cam.z));

			new AntiAliasing(scn).render(dist,7);

			scn.save("22/rays"+"000".substring(0,3-(""+i).length())+i, "gif");

			scn.moveLights();
			
			lx = lx+(light.x-lx)/30;
			ly = ly+(light.y-ly)/30;
			lz = lz+(light.z-lz)/30;

		}
	}
}
