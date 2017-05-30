package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements ActionListener, MouseMotionListener, ComponentListener{

	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JButton findCenter = null;
	private JLabel statusLabel = null;
	private JFileChooser chooser = null;
	
	private Fenetre() {
		
		// Taille Ecran

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Composants

		setMainPanel(new Panel());
		JMenuBar mainMenuBar = buildMenuBar();
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();

		// Window Settings
		
		this.setSize((bounds.width/100)*90, (bounds.height/100)*90);
		this.setTitle("AnnoDiffract");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Layout

		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		// Status Bar
	
		statusPanel.add(statusLabel, BorderLayout.EAST);
		
		// Listeners
		
		getMainPanel().addMouseMotionListener(this);
		
		// Ajouts
		
		this.add(mainMenuBar, BorderLayout.NORTH);
		this.add(getMainPanel(), BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
	
	}

	private JMenuBar buildMenuBar() {

		// Menu Bar
		JMenuBar menuBar = new JMenuBar();
		//menuBar.setBorder(null);

		// Layout

		BorderLayout barLayout = new BorderLayout();
		menuBar.setLayout(barLayout);

		// Menus
		JMenu menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");

		// ToolBar

		JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// ToolBar Bouttons
		// 32 x 32

		// Find Center
		findCenter = new JButton(new ImageIcon(Fenetre.class.getResource("img/fc.png")));
		findCenter.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/fcPressed.png")));
		findCenter.setToolTipText("Find Center");
		findCenter.setBorder(null);
		findCenter.setContentAreaFilled(false);

		// Ajouts

		menuBar.add(menuFile, BorderLayout.NORTH);
		menuBar.add(toolBar, BorderLayout.CENTER);
		menuFile.add(menuItemOuvrir);
		toolBar.add(findCenter);

		// Listeners

		getMainPanel().addComponentListener(this);
		menuItemOuvrir.addActionListener(this);
		findCenter.addActionListener(this);

		return menuBar;
	}

	//Action On button
	@Override
	public void actionPerformed(ActionEvent e) {

	    // Si on clique sur Ouvrir
		if (e.getSource() == menuItemOuvrir) {
			
			//Variable Chooser
			chooser = new JFileChooser();
			//Filter
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "Jpeg", "jpg", "tif", "Tiff");
			chooser.setFileFilter(filter);
			//EndFilter
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			//Case of file the user choose
			if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						if (getMainPanel().isLoaded()) {
							getMainPanel().getLabel().setIcon(null);
							getMainPanel().setLoaded(false);
						}
						getMainPanel().openImage(chooser.getSelectedFile());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
		}
		if (e.getSource() == findCenter) {
			//Method to find center
			System.out.println("Click FC");
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

    //Main
    public static void main(String[] args) {

        Fenetre window = new Fenetre();
        window.setVisible(true);

    }

	@Override
	public void componentHidden(ComponentEvent e) {
		
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		
		if (e.getSource() == getMainPanel()) {
			mainPanel.scale();
		}
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Panel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
