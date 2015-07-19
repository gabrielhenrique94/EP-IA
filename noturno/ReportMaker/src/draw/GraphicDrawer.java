package draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraphicDrawer {

	private BufferedImage image;
	private GraphicType type;
	private Graphics2D gr;
	private String path;
	private File f;

	public GraphicDrawer(GraphicType type, int width, int height, String path)
			throws IOException {
		this.type = type;
		this.path = path;

		f = new File(path);

		if (f.exists() && f.isFile()) {
			this.image = ImageIO.read(f);
		} else {
			this.image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
		}

		this.gr = image.createGraphics();
	}

	public void draw() {
		if (this.type == GraphicType.BARS)
			drawBars();
		else if (this.type == GraphicType.ONLY_DOT)
			drawDots();
		else if (this.type == GraphicType.LINES)
			drawLines();

	}

	private void drawBars() {

	}

	private void drawLines() {
		
	}

	private void drawDots() {

	}

}
