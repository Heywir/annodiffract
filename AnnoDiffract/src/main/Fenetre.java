package main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Slider;
import main.Panel.TypeOutil;

import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{

	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JButton findCenter = null;
	private JButton setParam = null;
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
		
		
		setParam = new JButton(new ImageIcon(Fenetre.class.getResource("img/gear.png")));
		setParam.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/gearPressed.png")));
		setParam.setToolTipText("Set Parameters");
		setParam.setBorder(null);
		setParam.setContentAreaFilled(false);

		// Ajouts
		menuBar.add(menuFile, BorderLayout.NORTH);
		menuBar.add(toolBar, BorderLayout.CENTER);
		menuFile.add(menuItemOuvrir);
		toolBar.add(findCenter);
		toolBar.add(setParam);
		toolBar.add(brightSlide);

		// Listeners
		getMainPanel().addComponentListener(this);
		menuItemOuvrir.addActionListener(this);
		setParam.addActionListener(this);
		findCenter.addActionListener(this);
		mainPanel.getLabel().addMouseListener(this);
		brightSlide.addChangeListener(this);
		

		return menuBar;
	}
	
	public void firstUse(){
		File F = new File("1.txt"); 
		if(!F.exists()){
    		int i;
    		float j;
    		JOptionPane n = new JOptionPane("Coucou");
    		JOptionPane.showMessageDialog(n, "Bonjour cela est votre premiere utilisation du logiciel,"
    				+ " veuillez rentrer les informations correspondantes"
    				, "First Use", n.INFORMATION_MESSAGE);
    		String Ppern = JOptionPane.showInputDialog(n,"Rentrer le pixel par metre de l'image");
    		try{
    			i = Integer.parseInt(Ppern);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentré un entier."
    					+ " Nous allons mettre la valeur par défaut pour le pixel par metre qui est de 1491"
        				, "Mauvaise valeur", n.ERROR_MESSAGE);
    			Ppern="1491";
    		}
    		String V = JOptionPane.showInputDialog(n,"Veuillez rentrer la tension d'accélération des électrons  U (en V).");
    		try{
    			j = Float.parseFloat(V);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentré un entier. "
    					+ "Nous allons mettre la valeur par défaut pour le voltage"
        				, "Mauvaise valeur", n.ERROR_MESSAGE);
    			V="120000";
    		}
    		String L = JOptionPane.showInputDialog(n,"Veuillez rentrer la longueur de caméra en Metre.");
    		try{
    			j = Float.parseFloat(L);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentré un chiffre. "
    					+ "Nous allons mettre la valeur par défaut pour la longueur de caméra"
        				, "Mauvaise valeur", n.ERROR_MESSAGE);
    			L="0.05";
    		}
    		try{
    		    PrintWriter writer = new PrintWriter(F, "UTF-8");
    		    writer.println("Pixel par Metre : "+ Ppern);
    		    writer.println("====================================");
    		    writer.println("Tension d'acceleration des electrons U : "+ V);
    		    writer.println("====================================");
    		    writer.println("Longueur de camera en Metre : "+ L);
    		    writer.close();
    		} catch (IOException e) {
    		   // do something
    		}
    	}
	}
	
		public void changeParam(){
			String p = null;
			String v =null;
			String l =null;
		    File f = new File("1.txt");
		    try{
		    	Scanner sc = new Scanner(f);
		    	p =  sc.nextLine();
		    	p = p.replace("Pixel par Metre : ","");
		    	System.out.println(p);
		    	sc.nextLine();
		    	v =  sc.nextLine();
		    	v = v.replaceAll("Tension d'acceleration des electrons U : ", "");
		    	System.out.println(v);
		    	sc.nextLine();
		    	l =  sc.nextLine();
		    	l = l.replaceAll("Longueur de camera en Metre : ", "");
		    	System.out.println(l);
		    	sc.close();
		    	
		    }catch(FileNotFoundException fnf){
		    	
		    }
		    JTextField pField = new JTextField(p,7);
		    JTextField vField = new JTextField(v,7);
		    JTextField lField = new JTextField(l,7);

		    JPanel myPanel = new JPanel();
		    myPanel.add(new JLabel("Pixel par Metre :"));
		    myPanel.add(pField);
		    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    myPanel.add(new JLabel("Tension d'accélération des électrons U:"));
		    myPanel.add(vField);
		    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    myPanel.add(new JLabel("Longueur de camera :"));
		    myPanel.add(lField);
		      
		      int result = JOptionPane.showConfirmDialog(null, myPanel, 
		               "Option", JOptionPane.OK_CANCEL_OPTION);
		      if (result == JOptionPane.OK_OPTION) {
		    	  PrintWriter writer;
		    	  float j;
				try {
					j = Float.parseFloat(pField.getText());
					j = Float.parseFloat(vField.getText());
					j = Float.parseFloat(lField.getText());
					writer = new PrintWriter(f, "UTF-8");
					writer.println("Pixel par Metre : "+ pField.getText());
	    		    writer.println("====================================");
	    		    writer.println("Tension d'acceleration des electrons U : "+ vField.getText());
	    		    writer.println("====================================");
	    		    writer.println("Longueur de camera en Metre : "+ lField.getText());
	    		    writer.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(NumberFormatException z){
					JOptionPane.showMessageDialog(null, "Vous n'avez pas rentré un chiffre. "
	    					+ "Les valeurs sont inchangé"
	        				, "Mauvaise valeur", JOptionPane.ERROR_MESSAGE);
				}
	    		    
		       }
		}
	
	//Action On button
	@Override
	public void actionPerformed(ActionEvent e) {

	    // Si on clique sur Ouvrir
		if (e.getSource() == menuItemOuvrir) {
			
			//Variable Chooser
			JFileChooser chooser = new JFileChooser();
			//Filter
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "Jpeg", "jpg", "tif", "Tiff","PNG","png");
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
		if (e.getSource() == setParam) {
			//Method to find center
			this.changeParam();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	//A chaque fois que l'utilisateur bouge la souris, la position ou est placÃƒÂ©e la souris est mise ÃƒÂ  jour
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (arg0.getSource() == mainPanel.getLabel()) {
			positionX = arg0.getX();
			positionY = arg0.getY();
			statusLabel.setText("MouseX: " + arg0.getX() + " " + "MouseY: " + arg0.getY());
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
				Circle c = mainPanel.tmpCircle;
				mainPanel.listeCircle.add(mainPanel.tmpCircle);
				
				mainPanel.tmpCircle = new Circle();
				Graphics2D g2d = mainPanel.bufferedScaled.createGraphics();
				Point centerCircle=mainPanel.circleCenter(c.ptCircle.get(0), c.ptCircle.get(1), c.ptCircle.get(2));
				mainPanel.getAllpointWithCenter(g2d, centerCircle);
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
        window.firstUse();
        window.setVisible(true);
        
    }
}

