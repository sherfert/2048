package p2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * The Panel drawing 4x4 numbers in a more a less fashionable way. Less. Yes,
 * less fashionable.
 * 
 * @author Satia
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	/** 4 tiles in a row, and also in a column. */
	public static final int NUMBER_OF_TILES_IN_A_ROW = 4;
	/** The preferred tile size. */
	public static final int PREFERRED_TILE_SIZE = 107;
	/** Not more than 85 % of the screen will be filled. */
	public static final double MAX_SCREEN_FILL = 0.85;

	/**
	 * Size of a tile on pixels.
	 */
	private static int tileSize;
	static {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		if (screenSize.height / NUMBER_OF_TILES_IN_A_ROW * MAX_SCREEN_FILL > PREFERRED_TILE_SIZE
				&& screenSize.width / NUMBER_OF_TILES_IN_A_ROW
						* MAX_SCREEN_FILL > PREFERRED_TILE_SIZE) {
			tileSize = PREFERRED_TILE_SIZE;
		} else {
			tileSize = screenSize.height < screenSize.width ? (int) (screenSize.height
					/ NUMBER_OF_TILES_IN_A_ROW * MAX_SCREEN_FILL)
					: (int) (screenSize.width / NUMBER_OF_TILES_IN_A_ROW * MAX_SCREEN_FILL);
		}
	}

	/**
	 * The shown image.
	 */
	private Image image;

	/**
	 * The graphics element.
	 */
	private Graphics graphics;
	
	/**
	 * The images for the tiles.
	 */
	private Map<Integer, BufferedImage> images;

	/**
	 * Creates a panel.
	 */
	public MainPanel() {
		setPreferredSize(new Dimension(NUMBER_OF_TILES_IN_A_ROW * tileSize,
				NUMBER_OF_TILES_IN_A_ROW * tileSize));
		images = new HashMap<Integer, BufferedImage>();
	}

	/**
	 * Initializes the panel's GUI.
	 */
	public void initializeGui() {
		image = createImage(NUMBER_OF_TILES_IN_A_ROW * tileSize,
				NUMBER_OF_TILES_IN_A_ROW * tileSize);
		graphics = image.getGraphics();

		drawSeparationLines();
	}

	/**
	 * Draw the lines separating the tiles.
	 */
	private void drawSeparationLines() {
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
		// Draw separation lines, first x then y
		for (int i = 1; i < NUMBER_OF_TILES_IN_A_ROW; i++) {
			graphics.drawLine(tileSize * i, 0, tileSize * i,
					NUMBER_OF_TILES_IN_A_ROW * tileSize);
			graphics.drawLine(0, tileSize * i, NUMBER_OF_TILES_IN_A_ROW
					* tileSize, tileSize * i);
		}
	}

	/**
	 * Paints the numbers into the tiles.
	 * 
	 * @param numbers
	 *            the number. Must not be greater than 4x4 or an
	 *            ArrayIndexOutOfBoundsException will be thrown.
	 */
	public void paintNumbers(int[][] numbers) {
		
		for (int i = 0; i < NUMBER_OF_TILES_IN_A_ROW; i++) {
			for (int j = 0; j < NUMBER_OF_TILES_IN_A_ROW; j++) {
				Rectangle viewRect = new Rectangle(j * tileSize, i * tileSize,
						tileSize, tileSize);
				// Clear rect first
				graphics.clearRect(viewRect.x + 1, viewRect.y + 1,
						viewRect.width - 1, viewRect.height - 1);

				if (numbers[i][j] != 0) {
					// First try to get an image
					BufferedImage img = null;
					if(images.containsKey(numbers[i][j])) {
						img = images.get(numbers[i][j]);
					} else {
						URL path = getClass().getResource("/" + numbers[i][j]  + ".png");
						try {
							img = ImageIO.read(path);
							images.put(numbers[i][j], img);
						} catch (IOException e) {
							// No image available or IO error
						}
					}
					
					if(img != null) {
						// If we got an image, paint it
						graphics.drawImage(img, viewRect.x + 1, viewRect.y + 1,
								viewRect.width - 1, viewRect.height - 1, null);
					} else {
						// Otherwise print a string
						Rectangle textRect = new Rectangle();
						String text = "" + numbers[i][j];

						text = SwingUtilities.layoutCompoundLabel(this,
								graphics.getFontMetrics(), text, null,
								SwingConstants.CENTER, SwingConstants.CENTER,
								SwingConstants.CENTER, SwingConstants.CENTER,
								viewRect, new Rectangle(), textRect, 0);
						graphics.drawString(text, textRect.x, textRect.y
								+ textRect.height);
					}
				}
			}
		}

		repaint();
	}

	/**
	 * @param g
	 *            the graphics to paint
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
}
