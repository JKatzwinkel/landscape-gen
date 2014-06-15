package landscape;

import java.util.Random;
import java.awt.Color;
import java.io.File;

public class RT710_shots {
	public static void main(String[] args){

		Random rnd = new Random();
		
		Scene scn = new Scene(1280,800);

		RayTracer raytrace = new RayTracer(scn);

		int size = 4096;

		Ground ground = new Ground(size, scn);
		ground.init(2000+rnd.nextInt(1000));

		ground.initTexture();	

		Light light = new Light(size/2-512+rnd.nextInt(1024),250,size/2-512+rnd.nextInt(1024),10, new Color(13,12,17));
		light.y = ground.topo[(int)light.x][(int)light.z]+20;
		scn.addLight(light);

//		light.whiz(1000d, ground, 1800);


		int index = 0;


		for (int i=0;i<20;i++){

			scn.cam.x = size/2-900+rnd.nextDouble()*1800;
			scn.cam.z = size/2-900+rnd.nextDouble()*1800;
			scn.cam.y = ground.topo[(int)scn.cam.x][(int)scn.cam.z]+10+rnd.nextDouble()*1000;

			light = new Light(size/2-512+rnd.nextInt(1024),250,size/2-512+rnd.nextInt(1024),8+rnd.nextDouble()*12, new Color(10+rnd.nextInt(20),10+rnd.nextInt(20),10+rnd.nextInt(20)));
			light.y = ground.topo[(int)light.x][(int)light.z]+10+rnd.nextInt(100);

			scn.cam.lookAt(light.x-40+rnd.nextInt(80),light.y-40+rnd.nextInt(80),light.z-20+rnd.nextInt(40));
			
			scn.initGfx();		
			raytrace.render();

			double dist = Math.sqrt((light.x-scn.cam.x)*(light.x-scn.cam.x) + (light.y-scn.cam.y)*(light.y-scn.cam.y) + (light.z-scn.cam.z)*(light.z-scn.cam.z));

			if (rnd.nextInt(5)>0) new AntiAliasing(scn).render(dist*(.85+rnd.nextDouble()*.3),5+rnd.nextDouble()*6);
				else new AntiAliasing(scn).render(30+rnd.nextDouble()*1500,4+rnd.nextDouble()*7);


			
			while ((new File(filename(index)+".png")).exists()) index++;

			scn.save(filename(index), "png");
			index++;


		}
	}

	static String filename(int i) {
		return "24/shot"+"0000".substring(0,4-(""+i).length())+i;
	}
}
