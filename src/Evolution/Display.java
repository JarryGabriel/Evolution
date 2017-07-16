package Evolution;
import java.awt.Color;


public class Display {
	private Painter painter;
	
	public Display(int sx, int sy){
		this.painter = new Painter(sx,sy);
	}
	
	private Color colorN(int n) {
		if (n==-1) {
			return Color.white;
		}
		if (n==0) {
			return Color.green;
		}
		else if (n==1) {
			return Color.blue;
		}
		else if (n==2) {
			return Color.red;
		}
		return null;
	}
	
	public void draw(int x, int y, int n){
		this.painter.setPixel(x,y,this.colorN(n));
	}
}
