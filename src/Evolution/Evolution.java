package Evolution;

public class Evolution {
	
	public static void main(String[] args) {
		Simulation s = new Simulation(10,30,50);
		for (int i=0; i<10;i++){
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			s.step();
		}
		
	}
}
