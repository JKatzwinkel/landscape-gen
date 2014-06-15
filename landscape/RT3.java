package landscape;

import java.util.Random;
import java.awt.Color;

public class RT3 {
	public static void main(String[] args) {

		Random rnd = new Random();

		Scene scn = new Scene(320, 200);
		scn.setCam(1500,380,2000);

		RayTracer raytrace = new RayTracer(scn);

		Ground ground = new Ground(2048, scn);
		ground.init(2500);

		ground.initTexture();

		Light light = new Light(1090,240,1540,10, new Color(10,10,9));
		light.y = ground.topo[(int)light.x][(int)light.z]+20+rnd.nextInt(80);
		scn.addLight(light);

		double degy = -18d;
		double degx = -27d;
		double speed = 0;

		scn.cam.setDegX(degx);		
		scn.cam.setDegY(degy);

		light.whiz(400d, ground, 700);

		for (int i=0;i<700;i++){

			scn.initGfx();		
			raytrace.render();
			new AntiAliasing(scn).render(560-(double)i/1.5,6);

			scn.save("16/rays"+"000".substring(0,3-(""+i).length())+i, "png");

			if (i<653) {if (speed < .7) speed += .02;}
				else if (speed > 0) speed -= .015;

			scn.cam.x += .5*speed;
			scn.cam.y -= .2*speed;
			scn.cam.z -= 3*speed;

			degy-=(.05+speed/3);
			degx-=(.01+speed/8);

			scn.cam.setDegY(degy);
			scn.cam.setDegX(degx);

			scn.moveLights();
		}
	}
}
