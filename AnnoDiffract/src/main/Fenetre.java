package main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class Fenetre extends JFrame implements ActionListener{

	JFrame mainWindow = null;
	JPanel mainPanel = null;
	JMenuBar mainMenuBar = null;
	JMenu menuFile = null;
	JMenuItem menuItemOuvrir = null;
	JLabel label = null;
	Image image = null;
	
	public Fenetre() {
		
		// Composants
	
		mainWindow = new JFrame();
		mainPanel = new JPanel();
		mainMenuBar = new JMenuBar();
		menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");
		label = new JLabel();
		
		
		// Layout
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		// Window Settings
		
		this.setTitle("AnnoDiffract");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.pack();
		
		// MenuBar
		
		mainMenuBar.add(menuFile);
		menuFile.add(menuItemOuvrir);
		
		// Listeners
		
		menuItemOuvrir.addActionListener(this);
		
		// Ajouts
		
		this.add(mainMenuBar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		
	}
	
	//Action On button
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == menuItemOuvrir) {
			
			//Variable Chooser
			JFileChooser chooser = new JFileChooser();
			//Filter
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "Jpeg", "jpg", "tif", "Tiff");
			chooser.setFileFilter(filter);
			//EndFilter
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			//Case of file the user choose
			if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						openImage(chooser.getSelectedFile());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			    }
		}
		
	}
	
	//Method OpenImage qui doit pouvoir gêrer les Tiff
	private void openImage(File file) throws Exception {
		try {
			FileInputStream in = new FileInputStream(file.getPath());
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    image = load(buffer.array());
		    Image imageScaled = image.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
		    System.out.println("image: " + file.getPath() + "\n" + image);
		    JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon( imageScaled )) );
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	private Image load(byte[] data) throws Exception{
	    Image image = null;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	  }


	//Main
	public static void main(String[] args) {
		
		Fenetre window = new Fenetre();
		window.setVisible(true);
		
	}
	
	
}
