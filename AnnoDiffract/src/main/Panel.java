package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class Panel extends JPanel {
	
	private JLabel label = null;
	private JLabel xAxis = null;
	JLabel yAxis = null;
	private Image image = null;
	private boolean loaded = false;

	public Panel(){
		
		// Taille Ecran
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Layout
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		// Composants
		
		// Panel Image
		
		setLabel(new JLabel());
		
		// Axe X
		
		setxAxis(new JLabel());
		
		// Axe Y
		
		yAxis = new JLabel();
		
		
		// Ajouts
		
		this.add(getLabel());
		this.add(getxAxis());
		this.add(yAxis);
		
		// Contraintes
		
		// Image
		layout.putConstraint(SpringLayout.NORTH, getLabel(), (bounds.height)/20, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, getLabel(), (bounds.width)/5, SpringLayout.WEST, this);
		
		// X Axis
		layout.putConstraint(SpringLayout.NORTH, getxAxis(), 0, SpringLayout.SOUTH, getLabel());
		layout.putConstraint(SpringLayout.WEST, getxAxis(), (bounds.width)/5, SpringLayout.WEST, this);
		
		// Y Axis
		layout.putConstraint(SpringLayout.NORTH, yAxis, (bounds.height)/20, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, yAxis, 0, SpringLayout.WEST, getLabel());
		
		
	}
	
	//Methode pour ouvrir l'image puis l'afficher avec une bonne dimension
	public void openImage(File file) throws Exception {
		try {
			FileInputStream in = new FileInputStream(file.getPath());
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    setImage(load(buffer.array()));
		    Image imageScaled = getImage().getScaledInstance((this.getWidth()/100)*60, -1,  Image.SCALE_SMOOTH);
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
			getxAxis().setText("X Axis");
			yAxis.setText("Y Axis");
			
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

	
}
