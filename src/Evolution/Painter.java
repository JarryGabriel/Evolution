package Evolution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Scrollable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The Painter class allows one to draw into a simple window, mainly via
 * {@link #setPixel()} methods that change the value of an individual
 * pixel at a given location.
 * 
 * @author Marc-Antoine Weisser
 * @author Christophe Jacquet
 */
public class Painter {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 600;

	private static final int DEFAULT_DELAY = 50;
	
	private static final Color MONOCHROME_PIXEL_ON = Color.BLUE;
	private static final Color MONOCHROME_PIXEL_OFF = Color.WHITE;
	
	private static final int MONOCHROME_PIXEL_ON_RGB = MONOCHROME_PIXEL_ON.getRGB();
	private static final int MONOCHROME_PIXEL_OFF_RGB = MONOCHROME_PIXEL_OFF.getRGB();

	private final GridPanel grid;
	
	/**
	 * Constructs a new painter window, of given dimensions.
	 * 
	 * @param height the height of the window, in pixels
	 * @param width the width of the window, in pixels
	 */
	public Painter(int width, int height) {
		this.grid = new GridPanel(width, height);
		SimpleFrame frame = new SimpleFrame(this.grid);
		frame.setSize(610,632);
		
		
	}
	
	/**
	 * Creates a new painter window of default dimensions.
	 */
	public Painter() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Pauses the program for 1/20 of a second.
	 * */
	public static void delay() {
		try {
			Thread.sleep(DEFAULT_DELAY);
		} catch (InterruptedException e) {
			System.err.println("Error during sleep");
			System.exit(-1);
		}
	}

	/**
	 * Pauses the program for the specified duration.
	 * 
	 * @param d the duration, expressed in milliseconds
	 */
	public static void delay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println("Error during sleep");
			System.exit(-1);
		}
	}

	/**
	 * Sets a pixel on or off.
	 * 
	 * The origin of the axes is the bottom left corner.
	 * 
	 * @param x the column of the pixel
	 * @param y the row of the pixel
	 * @param value the boolean value of the pixel ({@code true} means "on",
	 *            {@code false} means "off")
	 */
	public void setPixel(int x, int y, boolean value) {
		this.grid.setPixel(x, y, value ? MONOCHROME_PIXEL_ON_RGB : MONOCHROME_PIXEL_OFF_RGB);
	}
	
	/**
	 * Sets the color of a pixel.
	 * 
	 * The origin of the axes is the bottom left corner.
	 * 
	 * @param x the column of the pixel
	 * @param y the row of the pixel
	 * @param color the color of the pixel
	 */
	public void setPixel(int x, int y, Color color) {
		this.grid.setPixel(x, y, color.getRGB());
	}
	
	/**
	 * Clears the painter window.
	 */
	public void clear() {
		this.grid.clear();
	}

	@SuppressWarnings("serial")
	private class SimpleFrame extends JFrame {

		private static final String DEFAULT_FRAME_TITLE = "Painter";

		private GridPanel gp;

		public SimpleFrame(GridPanel grid) {
			super(DEFAULT_FRAME_TITLE);

			this.gp = grid;

			this.setDefaultCloseOperation(EXIT_ON_CLOSE);

			this.setLayout(new BorderLayout());
			this.add(new JScrollPane(this.gp), BorderLayout.CENTER);

			final JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 90);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					SimpleFrame.this.gp.setSize(slider.getValue());
					repaint();
				}
			});
			
			final JPanel pnlSlider = new JPanel(new BorderLayout());
			pnlSlider.add(new JLabel("  Zoom:  "), BorderLayout.WEST);
			pnlSlider.add(slider, BorderLayout.CENTER);
			//this.add(pnlSlider, BorderLayout.SOUTH);
			this.gp.setSize(slider.getValue());

			pack();
			// center the window
			setLocationRelativeTo(null);
			setVisible(true);
		}

	}

	@SuppressWarnings("serial")
	private class GridPanel extends JPanel implements Scrollable, Runnable {

		private double zoomFactor;
		private final BufferedImage image;
		private final int height;
		private final int width;
		private boolean touched = true;
		
			
		public void setSize(int size) {
			this.zoomFactor = Math.pow(4, size * .02);
			setPreferredSize(new Dimension(
					(int) (this.zoomFactor * this.width),
					(int) (this.zoomFactor * this.height)));
			revalidate();
		}

		public GridPanel(int width, int height) {
			setPreferredSize(new Dimension(width, height));
			this.image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			
			setBackground(MONOCHROME_PIXEL_OFF);
			
			// initially the image is cleared
			clear();

			this.height = height;
			this.width = width;
			// run updater thread
			new Thread(this).start();
		}
		
		private synchronized final void setTouched() {
			this.touched = true;
		}
		
		public void clear() {
			Graphics g = this.image.getGraphics();
			g.setColor(MONOCHROME_PIXEL_OFF);
			g.fillRect(0, 0, this.width, this.height);
			setTouched();
		}
		
		public void setPixel(int x, int y, int rgb) {
			// check if the coordinates (row, col) are within bounds
			if(x >= 0 && y >= 0 && y < this.height && x < this.width) {
				this.image.setRGB(x, this.height - 1 - y, rgb);
				setTouched();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2d = null;
			AffineTransform initialTransform = null;
			
			double zf = this.zoomFactor;
			
			// zoom, but only if the zoom level is above a 4% threshold
			if(zf > 1.04) {
				g2d = (Graphics2D) g;

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);

				initialTransform = g2d.getTransform();
				g2d.scale(zf, zf);
			}

			g.drawImage(this.image, 0, 0, this.width, this.height, null);
			
			// draw pixel grid above a *6 zoom factor
			if(zf >= 6) {
				g2d.setTransform(initialTransform);
				g.setColor(Color.LIGHT_GRAY);
				
				int realYMax = (int)(this.height * this.zoomFactor)-1;
				for(int col=1; col<this.width; col++) {
					g.drawLine((int)(col * zf), 0, (int)(col * zf), realYMax);
				}
				
				int realXMax = (int)(this.width * zf)-1;
				for(int row=1; row<this.height; row++) {
					g.drawLine(0, (int)(row * zf), realXMax, (int)(row * zf));
				}
			}
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					// refresh at approx. 50 Hz
					Thread.sleep(20);
				} catch (InterruptedException e) {}
				
				boolean wasTouched;
				synchronized(this) {
					wasTouched = this.touched;
					this.touched = false;
				}
				
				if(wasTouched) {
					repaint();
				}
			}
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(this.width, this.height);
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return (int) (this.zoomFactor * 10);
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return (int) (this.zoomFactor * 100);
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}
}