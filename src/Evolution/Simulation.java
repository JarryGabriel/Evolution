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
				this.vegetals.add(new Vegetal(pos[0],pos[1]));
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
	
	private ArrayList<int[]> reduce(ArrayList<int[]> neighbors){
		ArrayList<int[]> positions = this.getPositions();
		ArrayList<int[]> newNeighbors = new ArrayList<int[]>();
		for(int i = 0; i < neighbors.size(); i++) {
			int[] current = neighbors.get(i);
			if (!this.occupated(current,positions)){
				newNeighbors.add(current);
			}
		}
		return newNeighbors;
		
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
	

	public void step(){
		
		for(int i = 0; i < this.vegetals.size(); i++)
		{
			Vegetal current = this.vegetals.get(i);
			int x = current.getX();
			int y = current.getY();
			ArrayList<int[]> neighbors = this.reduce(current.getNeighbors(this.size));
			int n = neighbors.size();
			if (n >= 1){
				double r1 = gen.nextDouble();
				if (r1 <= 0.25){
					int r2 = gen.nextInt(n);
					int[] pos = neighbors.get(r2);
					Vegetal vegetal = new Vegetal(pos[0],pos[1]);
					this.vegetals.add(vegetal);
				}
			}
				
			
		}
		this.draw();
		
		
	}
}


