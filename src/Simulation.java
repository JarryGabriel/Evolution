public class Simulation{
	private Carnivore[] carnivores;
	private Herbivore[] herbivores;
	
	public Simulation(int numberCarnivores, int numberHerbivores) {
		this.carnivores = new Carnivore[numberCarnivores];
		this.herbivores = new Herbivore[numberHerbivores];
	}

	public Carnivore[] getCarnivores() {
		return carnivores;
	}

	public Herbivore[] getHerbivores() {
		return herbivores;
	}
	
	
}
