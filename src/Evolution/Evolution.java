package Evolution;

public class Evolution {
	
	public static void main(String[] args) {
		Simulation s = new Simulation(20,400,50);
		for (int i=0; i<10000;i++){
			try {
				Thread.sleep(1);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			s.step();
		}
		
	}
}
