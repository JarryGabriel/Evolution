package Evolution;
public class Carnivore extends Animal implements EatAnimals {


	public Carnivore(double life, int x, int y) {
		super(life,x,y);
	}

	@Override
	public void eat(Animal animal) {
		this.life = Math.min(this.lifeMax, this.life+3);

	}

}
