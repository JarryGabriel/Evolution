package Evolution;

import java.util.ArrayList;

abstract class LifeBeing {
	protected double life, lifeMax;
	protected int x,y;
	

	public boolean isAlive() {
		return (this.life >= 0);
	}
	
	public ArrayList<int[]> getNeighbors(int size){
		ArrayList<int[]> neighbors = new ArrayList<int[]>();
		int xm=0,xM=2,ym=0,yM=2;
		if (this.x == size -1){
			xM = 1;
		}
		if (this.x == 0){
			xm = 1;
		}
		if (this.y == size -1){
			yM = 1;
		}
		if (this.y == 0){
			ym = 1;
		}
		for(int i = xm; i <= xM; i++)
		{
			for(int j = ym; j <= yM; j++)
			{
				if (!(i == 1 && j == 1))
				{
					int[] pos = new int[2];
					pos[0] = x - 1 + i;
					pos[1] = y - 1 + j;
					neighbors.add(pos);
				}
			}
		} 
		return neighbors;
	}
	
	public void die(){
		this.life = 0;
	}
	
	abstract void next();



	public int getX() {
		return x;
	}



	public int getY() {
		return y;
	}
	
}
