package Evolution;
public class Vegetal extends LifeBeing{
	
	public Vegetal(int x, int y) {
		super();
		this.life = 1;
		this.lifeMax = 1;
		this.x = x;
		this.y = y;
	}
	

	@Override
	void next() {
		this.life -= 0.005;
	}


}
