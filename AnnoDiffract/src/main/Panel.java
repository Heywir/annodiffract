package main;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class Panel extends JPanel {
	
	private JLabel label = null;
	private JLabel xAxis = null;
	private JLabel yAxis = null;
	private Image image = null;
	private boolean loaded = false;
	private Integer ratioX = 65;
	private Integer ratioY = 90;
	private Image imageScaled = null;
	private Image afterScale = null;

	Panel() {

		// Layout
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(layout);
		
		// Composants
		
		// Panel Image
		
		setLabel(new JLabel());
		
		// Axe X
		
		setxAxis(new JLabel());
		
		// Axe Y
		
		setyAxis(new JLabel());
		
		// Ajouts
		
		// Image
		
		c.gridx = 1;
		c.gridy = 0;
		this.add(getLabel(), c);
		
		// X
		c.gridx = 1;
		c.gridy = 1;
		this.add(getxAxis(), c);
		
		// Y
		c.gridx = 0;
		c.gridy = 0;
		this.add(getyAxis(), c);
	
		
	}
	
	//Methode pour ouvrir l'image puis l'afficher avec une bonne dimension
	void openImage(File file) throws Exception {
		try {
			FileInputStream in = new FileInputStream(file.getPath());
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    setImage(load(buffer.array()));
		    imageScaled = getImage().getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
		    setImage(imageScaled);
		    getLabel().setIcon(new ImageIcon(imageScaled));
		    setLoaded(true);
		    repaint();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Methode pour charger l'image apr�s ca r�cuperation	
	private Image load(byte[] data) throws Exception{
	    Image image;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	  }
	
	public void scale() {
		
		if (imageScaled != null) {
			Image img = getImage().getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
			getLabel().setIcon(new ImageIcon(img));
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (isLoaded()) {
			drawGraph(g);
		}
	}

	//Methode pour déssiner le graph
	private void drawGraph(Graphics g) {
		// Paramètres graphe

		// Distance entre axe et text
		int distance = 20;

		// Pour le découpage selon l'image
		int indentationY = (getLabel().getHeight()) / 100;
		int indentationX = (getLabel().getWidth() / 100);
		int tailleInden = 5;

		// Point En haut à gauche
		int yZeroX = getLabel().getLocation().x;
		int yZeroY = getLabel().getLocation().y;

		// En Bas à gauche
		int yFinY = yZeroY + getLabel().getIcon().getIconHeight();

		// En Bas à droite
		int xFinX = yZeroX + getLabel().getIcon().getIconWidth();

		// Longueur

		int xLength = (xFinX - yZeroX) / indentationX;
		int yLength = (yFinY - yZeroY) / indentationY;

		// Dessin

		// Paramètres

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.BLACK);

		// Y Axis
		g2.drawLine(yZeroX -1, yZeroY, yZeroX -1, yFinY);
		g2.drawString("Y", yZeroX - 4, yZeroY - 4);

		// X Axis
		g2.drawLine(yZeroX, yFinY, xFinX, yFinY);
		g2.drawString("X", xFinX + 4, yFinY + 4);

		// Numérotation Y

		int longueurMot;
		for(int i = 0; i < indentationY +1; i++) {
			g2.drawLine(yZeroX - tailleInden, yZeroY + (i * yLength), yZeroX + tailleInden, yZeroY + (i * yLength));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), (yZeroX - distance) - longueurMot /2, yZeroY + (i * yLength));

		}

		// Numérotation X

		for(int i = 0; i < indentationX +1; i++) {
			g2.drawLine(yZeroX + (i * xLength), yFinY - tailleInden, yZeroX + (i * xLength), yFinY + tailleInden);
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), yZeroX + (i * xLength) - longueurMot /2, yFinY + distance);
		}

	}

	private Image getImage() {
		return image;
	}

	private void setImage(Image image) {
		this.image = image;
	}

	JLabel getLabel() {
		return label;
	}

	void setLabel(JLabel label) {
		this.label = label;
	}

	boolean isLoaded() {
		return loaded;
	}

	void setLoaded(boolean b) {
		this.loaded = b;
	}

	private JLabel getxAxis() {
		return xAxis;
	}

	private void setxAxis(JLabel grid) {
		this.xAxis = grid;
	}

	private JLabel getyAxis() {
		return yAxis;
	}

	private void setyAxis(JLabel yAxis) {
		this.yAxis = yAxis;
	}
	
}
