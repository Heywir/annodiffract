package main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.event.*;

class Fenetre extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{

	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JMenuItem menuGraphOpen = null;
	private JButton findCenter = null;
	private JButton setParam = null;
	private JButton zoom = null;
	private JLabel statusLabel = null;
	private int positionX=0;
	private int positionY=0;
	public JSlider brightSlide;
	private String p = null;
	private String v =null;
	private String l =null;
	private double lambda;
	private Graph graph = null;
	private ZoomImage z=null;
	
	private Fenetre() {
		
		//Prise des parametres du fichier texte
		File F = new File("1.txt"); 
		System.out.println("fsdfs");
		if(F.exists()){
			try{
		    	Scanner sc = new Scanner(F);
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
		}
		Double vDouble=Double.parseDouble(v);
		
		//Calcul lambda
		lambda = (6.62*Math.pow(10,-19))/
				(Math.sqrt((2.9149*Math.pow(10,-19))*(vDouble*1000)*(1+(9.7714*Math.pow(10,-7))*(vDouble*1000))));
		System.out.println(lambda);
		
		// Taille Ecran
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Composants
		setMainPanel(new Panel(this));
		JMenuBar mainMenuBar = buildMenuBar();
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();

		// Window Settings
		this.setSize((bounds.width/100)*50, (bounds.height/100)*80);
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
		mainPanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
	
	}

	private JMenuBar buildMenuBar() {

		// Menu Bar
		JMenuBar newMenuBar = new JMenuBar();
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);

		// Layout
		BorderLayout barLayout = new BorderLayout();
		menuBar.setLayout(barLayout);

		// Menus
		JMenu menuFile = new JMenu("Fichier");
		menuItemOuvrir = new JMenuItem("Ouvrir");

		// Graph
		JMenu menuGraph = new JMenu("Graph");
		menuGraphOpen = new JMenuItem("Ouvrir");
		menuGraphOpen.setEnabled(true);

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

		zoom = new JButton(new ImageIcon(Fenetre.class.getResource("img/loupe.png")));
		zoom.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/loupe.png")));
		zoom.setToolTipText("Get Zoom");
		zoom.setBorder(null);
		zoom.setContentAreaFilled(false);
		
		// Ajouts
		newMenuBar.add(menuFile);
		newMenuBar.add(menuGraph);
		menuBar.add(newMenuBar, BorderLayout.NORTH);
		menuBar.add(toolBar, BorderLayout.CENTER);
		menuFile.add(menuItemOuvrir);
		menuGraph.add(menuGraphOpen);
		toolBar.add(findCenter);
		toolBar.add(setParam);
		toolBar.add(zoom);
		toolBar.add(brightSlide);

		// Listeners
		getMainPanel().addComponentListener(this);
		menuItemOuvrir.addActionListener(this);
		menuGraphOpen.addActionListener(this);
		setParam.addActionListener(this);
		zoom.addActionListener(this);
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
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un entier."
    					+ " Nous allons mettre la valeur par defaut pour le pixel par metre qui est de 1491"
        				, "Mauvaise valeur", n.ERROR_MESSAGE);
    			Ppern="87503";
    		}
    		String V = JOptionPane.showInputDialog(n,"Veuillez rentrer la tension d'acc�l�ration des �lectrons  U (en V).");
    		try{
    			j = Float.parseFloat(V);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un entier. "
    					+ "Nous allons mettre la valeur par defaut pour le voltage"
        				, "Mauvaise valeur", n.ERROR_MESSAGE);
    			V="120000";
    		}
    		String L = JOptionPane.showInputDialog(n,"Veuillez rentrer la longueur de camera en Metre.");
    		try{
    			j = Float.parseFloat(L);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un chiffre. "
    					+ "Nous allons mettre la valeur par d�faut pour la longueur de camera"
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
		    File f = new File("1.txt");
		    
		    JTextField pField = new JTextField(p,7);
		    JTextField vField = new JTextField(v,7);
		    JTextField lField = new JTextField(l,7);

		    JPanel myPanel = new JPanel();
		    myPanel.add(new JLabel("Pixel par Metre :"));
		    myPanel.add(pField);
		    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    myPanel.add(new JLabel("Tension d'acceleration des electrons (en U) :"));
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
	    		    p=pField.getText();
	    		    v=vField.getText();
	    		    l=lField.getText();
	    		    
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(NumberFormatException z){
					JOptionPane.showMessageDialog(null, "Vous n'avez pas rentr� un chiffre. "
	    					+ "Les valeurs sont inchang�"
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
			if(z!=null){
				z.dispose();
			}
		}
		if (e.getSource() == setParam) {
			//Method to find center
			this.changeParam();
		}
		if (e.getSource() == zoom) {
			//Method to find center with zoom
			mainPanel.setCurrentTool(TypeOutil.ZOOM);
			z = new ZoomImage(this);
			z.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			z.setVisible(true);
		}
		if (e.getSource() == menuGraphOpen) {
			if (getMainPanel().getLabel().getIcon() != null && !mainPanel.listeMoyen.isEmpty()) {
				if(graph!=null){
					if(graph.getDataset()!=null){
						graph.getDataset().removeSeries(0);
						graph.XY.clear();
					}
				}
				graph = new Graph("Graph", "Intensite en fonction du rayon",mainPanel.listeDistInter, mainPanel.listeMoyen);
				graph.pack();
				 RefineryUtilities.centerFrameOnScreen( graph );
				graph.setVisible( true );
			}
			else {
				System.out.println("Empty");
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	//A chaque fois que l'utilisateur bouge la souris, la position ou est placÃ©e la souris est mise Ã  jour
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
				mainPanel.tmpCircle.ptCircle.clear();
				if(mainPanel.listeCircle.size()!=0){
					mainPanel.setResX(mainPanel.getLabel().getWidth());
					mainPanel.setResY(mainPanel.getLabel().getHeight());
				}
				Point centerCircle=mainPanel.circleCenter(
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(0).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(0).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(1).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(1).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(2).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(2).getY())));
				System.out.println(centerCircle.getX() + " " + centerCircle.getY());
				String p = null;
				double pDouble = 0;
				String v =null;
				String l =null;
				double vDouble = 0;
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
			    	pDouble = Double.parseDouble(p);
			    	vDouble = Double.parseDouble(v);
			    }catch(FileNotFoundException fnf){
			    	
			    }
			    CalculMoyAndRadius(centerCircle,pDouble, vDouble);
			}
		}else if(mainPanel.getCurrentTool()==TypeOutil.ZOOM){
			if(mainPanel.listeCircle.isEmpty()){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			z.getSubImage(mainPanel.getBufferedOriginal2(), (int)((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*positionX),
					(int)((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*positionY));
		}
	}
	
	/**Methode Qui Calcule la Moyenne par cercle ainsi que le radius**/
	public void CalculMoyAndRadius(Point centerCircle, double pDouble, double vDouble){
		double i=0;
		int k = 0;
		double j=0;
		double lenght;
		mainPanel.listeMoyen.clear();
		mainPanel.listeRayon.clear();
		mainPanel.listeDistInter.clear();
		ArrayList<Point> tmp = mainPanel.getPointWithCenter((int)centerCircle.getX(),(int)centerCircle.getY(),(int)0);
		while(!tmp.isEmpty()){
			//lenght = mainPanel.lenghtFrom2Points(centerCircle, new Point((int)(centerCircle.getX()-i),(int)(centerCircle.getY()-i)));
			tmp = mainPanel.getPointWithCenter((int)centerCircle.getX(),(int)centerCircle.getY(),i);
			Double somme = 0.0 ,moy=0.0;
			for(int h = 0; h<=tmp.size()-1;h++){
				Color color=new Color(mainPanel.getBufferedOriginal().getRGB((int)(tmp.get(h).getX()), (int)tmp.get(h).getY()));
				somme = somme + ((color.getRed() + color.getBlue()+ color.getGreen())/3);
				//System.out.println(color.getRed() +" "+ color.getBlue()+" "+ color.getGreen());
				//System.out.println(somme); 
			}
			if(!tmp.isEmpty()){
				j = (i/(pDouble*(double)39.3701));
				//System.out.println(lenght);
				System.out.println(((lambda*vDouble*(double)100)/j)*(double)Math.pow(10,6));
				mainPanel.listeDistInter.add((double)1/(((lambda*vDouble*(double)100)/j)*(double)Math.pow(10,6)));
				mainPanel.listeRayon.add(j);
				moy = (somme/tmp.size());
				//System.out.println(moy);
				mainPanel.listeMoyen.add(moy);
			}
			i++;
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
				mainPanel.setBrightness(2* (float) brightSlide.getValue() / brightSlide.getMaximum());
			}
		}
	}
    //Main
    public static void main(String[] args) {
    	
        Fenetre window = new Fenetre();
        window.firstUse();
        window.setVisible(true);
        
    }

	public ZoomImage getZ() {
		return z;
	}

	public void setZ(ZoomImage z) {
		this.z = z;
	}
	
	public Panel getMainPanel2() {
		return mainPanel;
	}

}

