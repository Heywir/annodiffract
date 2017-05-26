package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.media.jai.PlanarImage;
import javax.sound.sampled.Line;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class Panel extends JPanel{
	
	private JLabel label = null;
	private JLabel xAxis = null;
	private JLabel yAxis = null;
	private Image image = null;
	private boolean loaded = false;
	
	public Panel(){
				
		// Taille Ecran
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
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
	public void openImage(File file) throws Exception {
		try {
			FileInputStream in = new FileInputStream(file.getPath());
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    setImage(load(buffer.array()));
		    Image imageScaled = getImage().getScaledInstance((this.getWidth()/100)*65, -1,  Image.SCALE_SMOOTH);
		    setImage(imageScaled);
		    getLabel().setIcon(new ImageIcon(imageScaled));
		    setLoaded(true);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isLoaded()) {
			ImageIcon test = new ImageIcon(getImage());
			System.out.println("Label X " + getLabel().getLocation().x + " " + "Label Y " + getLabel().getLocation().y);
			getxAxis().setText("X Axis");
			getyAxis().setText("Y Axis");
			
		}
	}
	
	//Methode pour charger l'image après ca récuperation	
	private Image load(byte[] data) throws Exception{
	    Image image = null;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	  }

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public JLabel getxAxis() {
		return xAxis;
	}

	public void setxAxis(JLabel grid) {
		this.xAxis = grid;
	}

	public JLabel getyAxis() {
		return yAxis;
	}

	public void setyAxis(JLabel yAxis) {
		this.yAxis = yAxis;
	}
	
}
