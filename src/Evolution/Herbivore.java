package Evolution;
public class Herbivore extends Animal implements EatVegetals {

	public Herbivore(double life, int x, int y) {
		super(life,x,y);
	}

	@Override
	public void eat(Vegetal vegetal) {
		vegetal.die();
		this.life = Math.min(this.lifeMax, this.life+1);

	}
	

}
