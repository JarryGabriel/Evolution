package Evolution;
import java.util.ArrayList;
import java.util.Random;

public class Simulation{
	private Random gen = new Random();
	private ArrayList<Vegetal> vegetals = new ArrayList<Vegetal>();
	private ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
	private Display display;
	private int size;
	
	public Simulation(int numberHerbivores,int numberVegetals,int size) {
		this.size = size;
		ArrayList<int[]> positions = new ArrayList<int[]>();
		while (positions.size() < numberVegetals) {
			int[] pos = new int[2];
			pos[0] = gen.nextInt(this.size);
			pos[1] = gen.nextInt(this.size);
			if (!positions.contains(pos)) {
				positions.add(pos);
				this.vegetals.add(new Vegetal(pos[0],pos[1],gen.nextDouble()+1));
			}
		}
		while (positions.size() - numberVegetals < numberHerbivores) {
			int[] pos = new int[2];
			pos[0] = gen.nextInt(this.size);
			pos[1] = gen.nextInt(this.size);
			if (!positions.contains(pos)) {
				positions.add(pos);
				this.herbivores.add(new Herbivore(10,pos[0],pos[1]));
			}
		}
		this.display= new Display(this.size,this.size);
		this.draw();
	}
	
	private ArrayList<int[]> getPositions(){
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for(int i = 0; i < this.herbivores.size(); i++)
		{
			int x = this.herbivores.get(i).getX();
			int y = this.herbivores.get(i).getY();
			int[] pos = new int[3];
			pos[0] = x;
			pos[1] = y;
			pos[2] = i;
			positions.add(pos);
		}
		for(int i = 0; i < this.vegetals.size(); i++)
		{
			int x = this.vegetals.get(i).getX();
			int y = this.vegetals.get(i).getY();
			int[] pos = new int[3];
			pos[0] = x;
			pos[1] = y;
			pos[2] = i + this.herbivores.size();
			positions.add(pos);
		}
		return positions;
	}
	
	private boolean occupated(int[] pos, ArrayList<int[]> positions){
		for(int i = 0; i < positions.size(); i++) {
			if (pos[0]==positions.get(i)[0] && pos[1]==positions.get(i)[1])
			{
				return true;
			}
		}
		return false;
		
	}
	
	private int index(int[] pos, ArrayList<int[]> positions){
		for(int i = 0; i < positions.size(); i++) {
			if (pos[0]==positions.get(i)[0] && pos[1]==positions.get(i)[1])
			{
				return positions.get(i)[2];
			}
		}
		return -1;
		
	}
	
	
	
	private ArrayList<ArrayList<int[]>> composition(ArrayList<int[]> neighbors){
		ArrayList<ArrayList<int[]>> composition = new ArrayList<ArrayList<int[]>>();
		ArrayList<int[]> positions = this.getPositions();
		ArrayList<int[]> empty = new ArrayList<int[]>();
		ArrayList<int[]> fullVegetal = new ArrayList<int[]>();
		ArrayList<int[]> fullHerbivore = new ArrayList<int[]>();
		for(int i = 0; i < neighbors.size(); i++) {
			int[] current = neighbors.get(i);
			if (!this.occupated(current,positions)){
				empty.add(current);
			}
			else{
				int index = this.index(current, positions);
				int[] item = new int[3];
				item[0] = current[0];
				item[1] = current[1];
				item[2] = index;
				if (index < this.herbivores.size() && this.herbivores.get(index).isAlive()){
					fullHerbivore.add(item);
				}
				else if (index >= this.herbivores.size() && index < this.herbivores.size()+this.vegetals.size() && this.vegetals.get(index-this.herbivores.size()).isAlive()){
					fullVegetal.add(item);
				}
			}
		}
		composition.add(empty);
		composition.add(fullVegetal);
		composition.add(fullHerbivore);
		return composition;
		
	}

	
	
		
	public void draw(){
		for(int i = 0; i < this.herbivores.size(); i++)
		{
			int x = this.herbivores.get(i).getX();
			int y = this.herbivores.get(i).getY();
			this.display.draw(x, y, 1);
		}
		for(int i = 0; i < this.vegetals.size(); i++)
		{
			int x = this.vegetals.get(i).getX();
			int y = this.vegetals.get(i).getY();
			this.display.draw(x, y, 0);
		}
	}
	
	private ArrayList<Integer> possibilities(ArrayList<ArrayList<int[]>> composition){
		ArrayList<Integer> possibilities = new ArrayList<Integer>();
		ArrayList<int[]> empty = composition.get(0);
		ArrayList<int[]> fullHerbivore = composition.get(2); 
		ArrayList<int[]> fullVegetal = composition.get(1); 
		if (empty.size()>=1){
			possibilities.add(0);
		}
		if (fullHerbivore.size()>=1 && empty.size()>=1){
			possibilities.add(1);
		}
		if (fullVegetal.size()>=1){
			possibilities.add(2);
		}
		return possibilities;
	}

	private void correct(){
		ArrayList<Vegetal> newVegetals = new ArrayList<Vegetal>();
		for(int i = 0; i < this.vegetals.size(); i++)
		{
			Vegetal current = this.vegetals.get(i);
			if (current.isAlive()){
				newVegetals.add(current);
			}
			else{
				//System.out.println("Vegetal "+i+" dies");
				this.display.draw(current.getX(), current.getY(), -1);
			}
		}
		this.vegetals = newVegetals;
		ArrayList<Herbivore> newHerbivores = new ArrayList<Herbivore>();
		for(int i = 0; i < this.herbivores.size(); i++)
		{
			Herbivore current = this.herbivores.get(i);
			if (current.isAlive()){
				newHerbivores.add(current);
			}
			else{
				//System.out.println("Herbivore "+i+" dies");
				this.display.draw(current.getX(), current.getY(), -1);
			}
		}
		this.herbivores = newHerbivores;
	}
	
	private int probability(ArrayList<Integer> possibilities){
		int value =-1;
		boolean reproduction = false;
		if (possibilities.size() == 1 && possibilities.contains(1)){
			reproduction = true;
		}
		do {
			double r1 = gen.nextDouble();
			if (r1 <= 0.5){
				value = 0;
			}
			else if (r1 <= 1-0.27/this.herbivores.size()){
				value = 2;
			}
			else {
				value = 1;
			}
		}while (!possibilities.contains(value) || reproduction);
		return value;
	}
	
	public void step(){
		
		for(int i = 0; i < this.vegetals.size(); i++)
		{
			Vegetal current = this.vegetals.get(i);
			current.next();
			ArrayList<int[]> empty = this.composition(current.getNeighbors(this.size)).get(0);
			int n = empty.size();
			if (n >= 1){
				double r1 = gen.nextDouble();
				if (r1 <= 5./(1*vegetals.size())){
					int r2 = gen.nextInt(n);
					int[] pos = empty.get(r2);
					Vegetal vegetal = new Vegetal(pos[0],pos[1],gen.nextDouble()+1);
					this.vegetals.add(vegetal);
					//System.out.println("Vegetal "+i+" reproduces, life "+current.life);
					
				}
			}
		}
		ArrayList<Integer> done = new ArrayList<Integer>();
		int[] pos;
		int r1,r2,index;
		for(int i = 0; i < this.herbivores.size(); i++)
		{
			Herbivore current = this.herbivores.get(i);
			current.next();
			if (!done.contains(i) && current.isAlive()){
				ArrayList<ArrayList<int[]>> composition = this.composition(current.getNeighbors(this.size));
				ArrayList<Integer> possibilities = this.possibilities(composition);
				
				if (possibilities.size()>= 1){
					index = this.probability(possibilities);
					if (index == 0){
						ArrayList<int[]> empty = composition.get(0);
						r2 = gen.nextInt(empty.size());
						pos = empty.get(r2);
						this.display.draw(current.getX(),current.getY(),-1);
						current.move(pos[0], pos[1]);
						//System.out.println("Herbivore "+i+" moves, life "+current.life);
					}
					if (index == 1){
						current.reproduce();
						ArrayList<int[]> fullHerbivores = composition.get(2);
						ArrayList<int[]> empty = composition.get(0);
						r2 = gen.nextInt(fullHerbivores.size());
						pos = fullHerbivores.get(r2);
						Herbivore other = this.herbivores.get(pos[2]);
						other.reproduce();
						done.add(pos[2]);
						r2 = gen.nextInt(empty.size());
						pos = empty.get(r2);
						Herbivore herbivore = new Herbivore(10,pos[0],pos[1]);
						this.herbivores.add(herbivore);
						//System.out.println("Herbivore "+i+" reproduces, life "+current.life);
					}
					if (index == 2){
						ArrayList<int[]> fullVegetal = composition.get(1);
						r2 = gen.nextInt(fullVegetal.size());
						pos = fullVegetal.get(r2);
						Vegetal vegetal = this.vegetals.get(pos[2]-this.herbivores.size());
						current.eat(vegetal);
						//System.out.println("Herbivore "+i+" eats, life "+current.life);
					}
				}
			}
		}
		this.correct();
		this.draw();
		
		
	}
}


