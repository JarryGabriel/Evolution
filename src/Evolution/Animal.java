package Evolution;

public class Animal extends LifeBeing {
	
	public Animal(double life,int x,int y) {
		super();
		this.life = life;
		this.lifeMax = life;
		this.x = x;
		this.y = y;
	}
	
	public void move(int x, int y){
		this.x = x;
		this.y = y;
 
	}
	
	public void reproduce(){
	}

	@Override
	void next() {
		this.life -= 0.05;
	}

}
