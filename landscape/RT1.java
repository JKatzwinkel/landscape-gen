package landscape;

import java.util.Random;
import java.awt.Color;

public class RT1 {
	public static void main(String[] args) {
		
//		for (int i=0;i<11;i++)			
	
		
		Scene scn = new Scene(380, 209);
		scn.setCam(500,110,990);
		RayTracer raytrace = new RayTracer(scn);
		Ground ground = new Ground(1024, scn);
		ground.init(700);
//		scn.light = new Light(900,500,500,10d);
		Light light = new Light(522,80,840,12, new Color(10,10,10));
		scn.addLight(light);
		
//		scn.light.crash(490,0,995,ground);
		light.whiz(600d, ground, 200);
//		scn.light.circle(500, 80, 880);
//		scn.light.fire(ground);

/*		for (int i=0;i<8;i++){
			Random rnd = new Random();
			Sphere s = new Sphere(rnd.nextDouble()*512+256, rnd.nextDouble()*200, rnd.nextDouble()*512+256, 10+rnd.nextDouble()*20, new Color(rnd.nextInt(128),rnd.nextInt(128),rnd.nextInt(128)));
			scn.add(s);
		}*/
		
//		Sphere sphere = new Sphere(440, 120, 1100, 32, new Color(200,230,255));
//		scn.add(sphere);
			
		
		scn.cam.setDegX(-22d);		
		
		for (int i = 0; i < 200; i++){
			double d = -(double)i*360d/200d;
			scn.cam.setDegY(d);
			scn.cam.setDegX(-22d+6*(Math.cos(i*(Math.PI*2)/200d-Math.PI/2)));
			
			scn.cam.x = 512d+380d*Math.sin((d+180d)*Math.PI/180d);
			scn.cam.z = 612d-380d*Math.cos((d+180d)*Math.PI/180d);
						
//			scn.fadeImage();
			scn.initGfx();		
			raytrace.render();
			scn.save("12/raytrace"+"000".substring(0,3-(""+i).length())+i, "gif");
//			scn.cam.z-=1;
//			scn.light.y+=2;
//			scn.light.x+=.7;
//			scn.light.brightness*=1.7;
			scn.moveLights();
			//scn.cam.degY+=1;
			
			
//			sphere.x+=1;
//			sphere.z-=4;
			
//			scn.cam.x+=.4;
//			scn.cam.y+=.1;
		}
	}
}
