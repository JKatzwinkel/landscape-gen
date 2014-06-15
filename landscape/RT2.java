package landscape;

import java.util.Random;
import java.awt.Color;

public class RT2 {
	public static void main(String[] args) {

		String filename = "output";
		try {
			filename = args[0];
		} catch (Exception e) {
			System.out.println("No File Name given");
		}
		System.out.println("Saving under: "+filename);

		String filetype = "png";
		try {
			filetype = args[1];
		} catch (Exception e) {
			System.out.println("Saving as .PNG");
		}

		Scene scn = new Scene(1280, 800);
		scn.setCam(1500,470,2000);

		RayTracer raytrace = new RayTracer(scn);

		Ground ground = new Ground(2048, scn);
		ground.init(1800);

		ground.initTexture();

		Random rnd = new Random();

//		scn.light = new Light(1090,240,1540,10);
		Light light = new Light(400+rnd.nextInt(1200), 100, 1100+rnd.nextInt(700),0.9, new Color(255,100,100));
		light.y = ground.topo[(int)light.x][(int)light.z]+20+rnd.nextInt(200);
		scn.addLight(light);

/*		Light light2 = new Light(400+rnd.nextInt(1200), 100, 1100+rnd.nextInt(700),0.8, new Color(100,100,255));
		light2.y = ground.topo[(int)light2.x][(int)light2.z]+50+rnd.nextInt(200);
		scn.addLight(light2);*/

/*		scn.add(new Tower(1180,1500,ground.topo[1180][1500]+200));
		scn.add(new Tower(1230,1530,ground.topo[1230][1530]+180));
		scn.add(new Tower(1250,1480,ground.topo[1230][1530]+200));*/

		scn.cam.setDegX(-35.5d);		
		scn.cam.setDegY(-23d);


		scn.initGfx();		
		raytrace.render();
		

//		scn.saveFoD("background_fod", "png");
//		scn.save("background69", "png");

		new AntiAliasing(scn).render(Math.sqrt((scn.cam.x-light.x)*(scn.cam.x-light.x)+
							(scn.cam.y-light.y)*(scn.cam.x-light.y)+
							(scn.cam.z-light.z)*(scn.cam.x-light.z) )+50, 7);

		scn.save(filename, filetype);

	}
}

