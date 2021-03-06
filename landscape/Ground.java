package landscape;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Ground implements LandScapeElementInterface {
	
	double [][] topo;
	private Scene scn;
	public int size;	
	private GroundTexture texture; 

	public Ground (int size, Scene scn) {
		this.scn = scn;
		topo = new double[size+1][size+1];
		this.size=size;
	}

	public void initTexture(){
		texture = new GroundTexture(size, this);
	}


	public void init (int maxheight)  {

		System.out.println("Calculating Height Field...");
	
		scn.add(this);

		double [][] tmp = new double[size+1][size+1];

		for (int i = 0;i<size+1;i++)
 			for (int j = 0;j<size+1;j++){
				if ((i!=0)&&(i!=size)&&(j!=0)&&(j!=size)) topo[i][j] = -1; else topo[i][j]=0;
				tmp[i][j] = -1;
			}


		Random rnd = new Random();


//		topo [0][0] = 0;//rnd.nextDouble()*maxheight;
//		topo [size][0] = 0;//rnd.nextDouble()*maxheight;
//		topo [0][size] = 0;//rnd.nextDouble()*maxheight;
//		topo [size][size] = 0;//rnd.nextDouble()*maxheight;
		//topo [size/2][size/2] = rnd.nextDouble()*maxheight;

		for (int step=size/2;step>0;step/=2){
			for (int x=0;x<size+1;x+=step)
				for (int y=0;y<size+1;y+=step){
					double sum=0;
					int neighbours=0;
					if (topo[x][y]==-1) {
						for (int nx=-1;nx<=1;nx++)
						for (int ny=-1;ny<=1;ny++)
							if ((nx!=0) || (ny!=0))
								if ((x+nx*step>=0) && (x+nx*step<=size))
									if ((y+ny*step>=0) && (y+ny*step<=size))
										if (topo[x+nx*step][y+ny*step]!= -1){
											neighbours++;
											sum=sum+topo[x+nx*step][y+ny*step];
										}
					} else tmp[x][y]=topo[x][y];
					if (neighbours>0) {
						double var = (double)step/((double)size)*maxheight;
						tmp[x][y] = sum/(double)neighbours-var/2+rnd.nextDouble()*var;
						if (tmp[x][y]<0) tmp[x][y]=0;
					}
				}

			for (int y=0;y<size+1;y+=step)
				for (int x=0;x<size+1;x+=step)
					topo[x][y] = tmp[x][y];
		}
	}

	
	

	public void paint(){
		
		for (int j=0;j<size;j++)
			for (int i=0;i<size;i++){
/*				Point p0 = scn.cam.onScreen(i,topo[i][j],j);
				Point p1 = scn.cam.onScreen(i+1,topo[i+1][j],j);
				
				double dist = Math.sqrt(Math.pow(p1.x-p0.x,2)+Math.pow(p1.y-p0.y,2));

				for (double ii=0;ii<dist;ii+=1){
					scn.drawPoint(i+ii/dist, topo[i][j]+(topo[i+1][j]-topo[i][j])*ii/dist, j, new Color(j/3,j/3,j/3));
				}
*/
				int base = (int)((j+(topo[i+1][j]-topo[i][j])*50) *255) / size;
				if (base < 0) base = 0;
				if (base > 255) base = 255;
				
				Color col = new Color(base,base,base);

				scn.drawLine(i,topo[i][j],j, i+1,topo[i+1][j],j, col);
				scn.drawLine(i,topo[i][j],j, i,topo[i][j+1],j+1, col);

//				scn.drawRectangle(i,topo[i][j],j, i+1,topo[i+1][j],j, i,topo[i][j+1],j+1, col,col,col);
//				scn.drawRectangle(i+1,topo[i+1][j+1],j+1, i+1,topo[i+1][j],j, i,topo[i][j+1],j+1, col,col,col);

				//scn.putPixel(i, topo[i][j], j, j/2);
			}
	}
	
	public boolean collide(Ray r){
		//Wasser
		if (r.y < 4) {
			r.y=4;
			if (r.camera) {
				r.r-=50;//30;
				r.g-=35;//20;
				r.b-=30;//16;
			}
			Ray reflect = new Ray(r.x,r.y,r.z,true,r.max-r.counter);
			reflect.direct(r.x+r.dx,r.y+Math.sqrt(r.dy*r.dy),r.z+r.dz,.7);

			r.addRay(reflect,.6);
			return true;
		}
			
		if ((r.x >= 0) && (r.x < size-1) && (r.z >= 0) && (r.z < size-1)) {
				double h1 = topo[(int)r.x][(int)r.z]
								+(topo[(int)r.x+1][(int)r.z]-topo[(int)r.x][(int)r.z])
								* (r.x-(int)r.x);
				double h2 = topo[(int)r.x][(int)r.z+1]
								+(topo[(int)r.x+1][(int)r.z+1]-topo[(int)r.x][(int)r.z+1])
								* (r.x-(int)r.x);
									
								
				if (r.y < h1+(h2-h1)*(r.z-(int)r.z)) {
					r.y = h1+(h2-h1)*(r.z-(int)r.z);
					if (texture != null) if (r.camera) {
						Color col = texture.getColorAt(r.x,r.z);
						r.r = r.r+(-100+col.getRed())*1.8;
						r.g = r.g+(-100+col.getGreen())*1.8;
						r.b = r.b+(-100+col.getBlue())*1.8;
					}
					return true;
				} 
			}
		return false;
	}

}
