import java.lang.Math;

public class Animal {
	private int vie, maxvie, deplacement;

	public Animal(int initVie, int deplacement) {
		super();
		this.vie = initVie;
		this.maxvie = initVie;
		this.deplacement = deplacement;
	}
	
	public int getDeplacement() {
		return deplacement;
	}
	
	
	public boolean isAlive() {
		return (this.vie == 0);
	}

	public void manger() {
		this.vie = Math.min(this.maxvie,this.vie + 1);
	}
	
	public void next() {
		this.vie = Math.max(0,this.vie - 1);
	}


	
	
	
}
