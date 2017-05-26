package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.xml.internal.ws.api.Component;

public class Fenetre extends JFrame implements ActionListener, MouseMotionListener{

	JFrame mainWindow = null;
	Panel mainPanel = null;
	JPanel statusPanel = null;
	JMenuBar mainMenuBar = null;
	JMenu menuFile = null;
	JMenuItem menuItemOuvrir = null;
	JLabel statusLabel = null;
	
	public Fenetre() {
		
		// Taille Ecran
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Composants
	
		mainWindow = new JFrame();
		mainPanel = new Panel();
		mainMenuBar = new JMenuBar();
		menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");
		statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();
		
		// Layout
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		// Window Settings
		
		this.setSize((bounds.width/100)*60, (bounds.height/100)*80);
		this.setTitle("AnnoDiffract");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// MenuBar
		
		mainMenuBar.add(menuFile);
		menuFile.add(menuItemOuvrir);
		
		// Status Bar
	
		statusPanel.add(statusLabel, BorderLayout.EAST);
		
		// Listeners
		
		mainPanel.addMouseMotionListener(this);
		menuItemOuvrir.addActionListener(this);
		
		// Ajouts
		
		this.add(mainMenuBar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		
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
						mainPanel.openImage(chooser.getSelectedFile());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
		}	
	}
		
	//Main
	public static void main(String[] args) {
		
		Fenetre window = new Fenetre();
		window.setVisible(true);
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		statusLabel.setText("MouseX: " + arg0.getX() + " " + "MouseY: " + arg0.getY());
		
	}
	
	
}
