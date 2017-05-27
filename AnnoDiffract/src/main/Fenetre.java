package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


class Fenetre extends JFrame implements ActionListener, MouseMotionListener{

	private JFrame mainWindow = null;
	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JLabel statusLabel = null;
	
	private Fenetre() {
		
		// Taille Ecran
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Composants

		mainWindow = new JFrame();
		mainPanel = new Panel();
		JMenuBar mainMenuBar = new JMenuBar();
		JMenu menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();
		
		// Layout
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		// Window Settings
		
		this.setSize((bounds.width/100)*60, (bounds.height/100)*80);
		this.setTitle("AnnoDiffract");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

	    // Si on clique sur Ouvrir
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
						e1.printStackTrace();
					}
			    }
		}	
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	//A chaque fois que l'utilisateur bouge la souris, la position ou est placée la souris est mise à jour
	@Override
	public void mouseMoved(MouseEvent arg0) {
		statusLabel.setText("MouseX: " + arg0.getX() + " " + "MouseY: " + arg0.getY());
		
	}

    public JFrame getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(JFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    //Main
    public static void main(String[] args) {

        Fenetre window = new Fenetre();
        window.setVisible(true);

    }
}
