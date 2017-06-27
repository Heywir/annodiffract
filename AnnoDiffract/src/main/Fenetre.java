package main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.scene.control.Slider;
import main.Panel.TypeOutil;

import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{

	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JButton findCenter = null;
	private JLabel statusLabel = null;
	private int positionX=0;
	private int positionY=0;
	private JSlider brightSlide;

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
		getMainPanel().getLabel().addMouseMotionListener(this);
		
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
		 brightSlide = new JSlider();
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
		toolBar.add(brightSlide);

		// Listeners
		getMainPanel().addComponentListener(this);
		menuItemOuvrir.addActionListener(this);
		findCenter.addActionListener(this);
		mainPanel.getLabel().addMouseListener(this);
		brightSlide.addChangeListener(this);
		

		return menuBar;
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
						brightSlide.setValue(50);
						if (getMainPanel().isLoaded()) {

							mainPanel.setBright(-1);
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
			mainPanel.setCurrentTool(TypeOutil.POINT);
			System.out.println("Click FC"+mainPanel.getCurrentTool());
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	//A chaque fois que l'utilisateur bouge la souris, la position ou est placÃ©e la souris est mise Ã  jour
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (arg0.getSource() == mainPanel.getLabel()) {
			//System.out.println("OrigiW: " + mainPanel.getBufferedOriginal().getWidth() + " OrigiH: " + mainPanel.getBufferedOriginal().getHeight());
			//System.out.println("ScaledW: " + mainPanel.getBufferedScaled().getWidth() + " SclaedH: " + mainPanel.getBufferedScaled().getHeight());
			Double imgWidth = (double) mainPanel.getBufferedOriginal().getWidth();
			Double imgHeight = (double) mainPanel.getBufferedOriginal().getHeight();
			Double scaledWidth = (double) mainPanel.getBufferedScaled().getWidth();
			Double scaledHeight = (double) mainPanel.getBufferedScaled().getHeight();
			Double ratioX = imgWidth / scaledWidth;
			Double ratioY = imgHeight / scaledHeight;
			Double mouseX = (double) arg0.getX();
			Double mouseY = (double) arg0.getY();
			Double newX = mouseX * ratioX;
			Double newY = mouseY * ratioY;
			statusLabel.setText("MouseX: " + newX + " " + "MouseY: " + newY);
		}
		
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

	private Panel getMainPanel() {
		return mainPanel;
	}

	private void setMainPanel(Panel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(mainPanel.getCurrentTool() == TypeOutil.POINT){
			System.out.println(positionX + " "+positionY);
			if(mainPanel.tmpCircle.ptCircle.size()<2){
				mainPanel.tmpCircle.ptCircle.add(new Point(positionX,positionY));
			}else{
				mainPanel.tmpCircle.ptCircle.add(new Point(positionX,positionY));
				mainPanel.tmpCircle.setDr(true);
				mainPanel.listeCircle.add(mainPanel.tmpCircle);
				mainPanel.tmpCircle = new Circle();
				//mainPanel.tmpCircle.ptCircle.clear();
			}
			if(mainPanel.listeCircle.size()!=0 && mainPanel.tmpCircle.ptCircle.size()==1){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==brightSlide){
			if(mainPanel.isLoaded()){
				mainPanel.setBrightness(5 * (float) brightSlide.getValue() / brightSlide.getMaximum());
			}
		}
	}
    //Main
    public static void main(String[] args) {

        Fenetre window = new Fenetre();
        window.setVisible(true);

    }
}
