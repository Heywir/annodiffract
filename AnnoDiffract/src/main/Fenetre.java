package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class Fenetre extends JFrame implements ActionListener{

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
		
		menuItemOuvrir.addActionListener(this);
		
		// Ajouts
		
		this.add(mainMenuBar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) {
		
		Fenetre window = new Fenetre();
		window.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == menuItemOuvrir) {
			
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "Jpeg", "jpg", "tif", "Tiff");
			chooser.setFileFilter(filter);
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
			 	    System.out.println("Yes");
					openImage(chooser);
			    }
		}
		
	}

	private void openImage(JFileChooser chooser) {
			try {
				image = ImageIO.read(chooser.getSelectedFile());
			} catch (Exception e) {
				e.printStackTrace();
			}
			label.setIcon(new ImageIcon(image));
			mainPanel.add(label, BorderLayout.CENTER);
			mainPanel.revalidate();
		
	}
	
}
