package landscape;

import java.awt.*;

public class Landschaft1 {

	public static void main (String[] args) {
		Scene scene = new Scene(1024,800);

		int res = 2048;
		
		Ground boden = new Ground(res, scene);

		scene.cam = new Camera(res/2,2700,res+300,scene);

		boden.init(3000);

		boden.paint();

		scene.save("shade21","png");



		
	}
}
