package main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Fenetre extends JFrame implements ActionListener, ComponentListener{

	JFrame mainWindow = null;
	JPanel mainPanel = null;
	JMenuBar mainMenuBar = null;
	JMenu menuFile = null;
	JMenuItem menuItemOuvrir = null;
	JLabel label = null;
	BufferedImage image = null;
	
	public Fenetre() {
		
		// Composants
	
		mainWindow = new JFrame("AnnoDiffract");
		mainPanel = new JPanel();
		mainMenuBar = new JMenuBar();
		menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");
		label = new JLabel();
		
		
		// Layout
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		// Window Settings
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.pack();
		
		// MenuBar
		
		mainMenuBar.add(menuFile);
		menuFile.add(menuItemOuvrir);
		
		// Listeners
		
		mainPanel.addComponentListener(this);
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
					openImage(chooser);
			    }
		}
		
	}
	
	//Method OpenImage
	private void openImage(JFileChooser chooser) {
			
		try {
			//Read Image
			image = ImageIO.read(chooser.getSelectedFile());
		} catch (Exception e) {
			//In case of exception
			e.printStackTrace();
		}
		//Set image in the window
		label.setIcon(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance((mainPanel.getWidth()/100)*80, (mainPanel.getHeight()/100)*80, Image.SCALE_SMOOTH)));
		mainPanel.add(label, BorderLayout.CENTER);
		//reset the window
		mainPanel.revalidate();
	}
		
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		label.setIcon(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance((mainPanel.getWidth()/100)*80, (mainPanel.getHeight()/100)*80, Image.SCALE_SMOOTH)));
		mainPanel.add(label, BorderLayout.CENTER);
		mainPanel.revalidate();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//Main
	public static void main(String[] args) {
		
		Fenetre window = new Fenetre();
		window.setVisible(true);
		
	}
	
	
}
