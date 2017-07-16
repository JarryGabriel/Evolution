package Evolution;

public class Evolution {
	
	public static void main(String[] args) {
		Simulation s = new Simulation(10,100,50);
		for (int i=0; i<1000;i++){
			try {
				Thread.sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			s.step();
		}
		
	}
}
