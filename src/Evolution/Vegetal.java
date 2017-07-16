package Evolution;
public class Vegetal extends LifeBeing{
	
	public Vegetal(int x, int y, double life) {
		super();
		this.life = life;
		this.lifeMax = life;
		this.x = x;
		this.y = y;
	}
	

	@Override
	void next() {
		this.life -= 0.0001;
	}


}
