package main;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.RescaleOp;

class Fenetre extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{

	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JMenuItem menuGraphOpen = null;
	private JButton findCenter = null;
	private JButton setParam = null;
	private JButton zoom = null;
	private JLabel statusLabel = null;
	private JLabel outilLabel;
	private int positionX=0;
	private int positionY=0;
	public JSlider brightSlide;
	private String p = null;
	private String v =null;
	private String l =null;
	private BigDecimal lambda;
	private ZoomImage z=null;
	private ArrayList<Double> tmpBeamStop = new ArrayList<>();
	private double minBS=-1;
	private double maxBS=-1;
	private Point centerCircle;


	
	private Fenetre() {
		
		//Prise des parametres du fichier texte
		File F = new File("1.txt"); 
		if(F.exists()){
			try{
		    	Scanner sc = new Scanner(F);
		    	p =  sc.nextLine();
		    	p = p.replace("Pixel par Metre : ","");
		    	//System.out.println(p);
		    	sc.nextLine();
		    	v =  sc.nextLine();
		    	v = v.replaceAll("Tension d'acceleration des electrons U : ", "");
		    	//System.out.println(v);
		    	sc.nextLine();
		    	l =  sc.nextLine();
		    	l = l.replaceAll("Longueur de camera en Metre : ", "");
		    	//System.out.println(l);
		    	sc.close();
		    	Double vDouble=Double.parseDouble(v);
		    	
		    }catch(FileNotFoundException ignored){
		    	
		    }
		}
		
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
		// En attendant
		//this.setResizable(false);

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

		// Label
		outilLabel = new JLabel("Tool: " + TypeOutil.NORMAL.toString());
		outilLabel.setForeground(Color.RED);

		// ToolBar Bouttons
		brightSlide = new JSlider();
		Dimension d = brightSlide.getPreferredSize();
		brightSlide.setPreferredSize(new Dimension(d.width/3, d.height));

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
		toolBar.add(new JLabel("Brightness:"));
		toolBar.add(brightSlide);
		toolBar.add(outilLabel);

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
	
	private void firstUse(){
		File F = new File("1.txt"); 
		if(!F.exists()){
    		int i;
    		float j;
    		JOptionPane n = new JOptionPane("Coucou");
    		JOptionPane.showMessageDialog(n, "Bonjour cela est votre premiere utilisation du logiciel,"
    				+ " veuillez rentrer les informations correspondantes"
    				, "First Use", JOptionPane.INFORMATION_MESSAGE);
    		String Ppern = JOptionPane.showInputDialog(n,"Rentrer le pixel par metre de l'image");
    		try{
    			i = Integer.parseInt(Ppern);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un entier."
    					+ " Nous allons mettre la valeur par defaut pour le pixel par metre qui est de 1491"
        				, "Mauvaise valeur", JOptionPane.ERROR_MESSAGE);
    			Ppern="87503";
    		}
    		String V = JOptionPane.showInputDialog(n,"Veuillez rentrer la tension d'accï¿½lï¿½ration des ï¿½lectrons  U (en V).");
    		try{
    			j = Float.parseFloat(V);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un entier. "
    					+ "Nous allons mettre la valeur par defaut pour le voltage"
        				, "Mauvaise valeur", JOptionPane.ERROR_MESSAGE);
    			V="120000";
    		}
    		//Calcul lambda
    		lambda = new BigDecimal((6.62 *Math.pow(10,-34))/
					(Math.sqrt((2.9149 *Math.pow(10,-49))*((double)Float.parseFloat(V)*(double)1000)*
							((double)1+(9.7714 *Math.pow(10,-7))*((double)Float.parseFloat(V)*(double)1000)))));
			
    		String L = JOptionPane.showInputDialog(n,"Veuillez rentrer la longueur de camera en Metre.");
    		try{
    			j = Float.parseFloat(L);
    		}catch(NumberFormatException z){
    			JOptionPane.showMessageDialog(n, "Vous n'avez pas rentrer un chiffre. "
    					+ "Nous allons mettre la valeur par dï¿½faut pour la longueur de camera"
        				, "Mauvaise valeur", JOptionPane.ERROR_MESSAGE);
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
	
		private void changeParam(){
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
					JOptionPane.showMessageDialog(null, "Vous n'avez pas rentré un chiffre. "
	    					+ "Les valeurs sont inchangé"
	        				, "Mauvaise valeur", JOptionPane.ERROR_MESSAGE);
				}
				if(!mainPanel.listeCircle.isEmpty()){
					centerCircle=mainPanel.circleCenter(
							new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getX()),
									(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getY())),
							new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getX()),
									(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getY())),
							new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getX()),
									(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getY())));
					
					Double pDouble = Double.parseDouble(p), vDouble = Double.parseDouble(v), lDouble = Double.parseDouble(l);
					//Calcul lambda
		    		lambda = new BigDecimal((6.62 *Math.pow(10,-34))/
							(Math.sqrt((2.9149 *Math.pow(10,-49))*(vDouble*(double)1000)*
									((double)1+(9.7714 *Math.pow(10,-7))*(vDouble*(double)1000)))));
					
					System.out.println(pDouble+" "+vDouble+" "+ lDouble);
					CalculMoyAndRadius(centerCircle, pDouble, vDouble, lDouble);
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
		/*if (e.getSource() == findCenter) {
			//Method to find center
			if(mainPanel.isLoaded()){
				mainPanel.setCurrentTool(TypeOutil.POINT);
				if(z!=null){
					z.dispose();
				}
			}
		}*/
		if (e.getSource() == setParam) {
			//Method to find center
			this.changeParam();
		}
		if (e.getSource() == zoom) {
			//Method to find center with zoom
			if(mainPanel.isLoaded()){
				mainPanel.setCurrentTool(TypeOutil.ZOOM);
				outilLabel.setText("Tool: " + TypeOutil.ZOOM.toString());
				if (z == null) {
					z = new ZoomImage(this);
					z.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					z.setLocationRelativeTo(null);
					z.setVisible(true);
				}
				else {
					if (!z.isVisible()) {
						z.setVisible(true);
					}
				}
			}
		}
		if (e.getSource() == findCenter) {
			//Method to find center with zoom
			if(mainPanel.isLoaded()){
				mainPanel.setCurrentTool(TypeOutil.BEAMSTOP);
				outilLabel.setText("Tool: " + TypeOutil.BEAMSTOP.toString());
			}
		}
		if (e.getSource() == menuGraphOpen) {
			if (getMainPanel().getLabel().getIcon() != null && !mainPanel.listeMoyen.isEmpty()) {
				/*if(graph!=null){
					if(graph.getDataset()!=null){
						graph.getDataset().removeSeries(0);
						graph.XY.clear();
					}
				}*/
				Graph graph = new Graph(mainPanel.listeMoyen, mainPanel.listeRayon,
						mainPanel.listeD, mainPanel.listeS, mainPanel.liste2theta, mainPanel.listeMoyenBeam);
				graph.pack();
				 RefineryUtilities.centerFrameOnScreen(graph);
				graph.setVisible( true );
			}else{
				System.out.println("Empty");
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	//A chaque fois que l'utilisateur bouge la souris, la position ou est placer la souris est mise a jour
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (arg0.getSource() == mainPanel.getLabel()) {
			positionX = arg0.getX();
			positionY = arg0.getY();
			statusLabel.setText("MouseX: " + arg0.getX() + " " + "MouseY: " + arg0.getY());
		}
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//** Si on est sur l'outil pour placer des points sans le zoom, on placera les points
		//** en fonction de la position de la souris
		if(mainPanel.getCurrentTool() == TypeOutil.POINT){
			//System.out.println(positionX + " "+positionY);
			if(mainPanel.tmpCircle.ptCircle.size()<2){
				mainPanel.tmpCircle.ptCircle.add(new Point(positionX,positionY));
			}else{
				//** Si c'est le troisième point que l'utilisateur place nous placons le cercle dans
				//** un tableau pour pouvoir le repaint lorsque la fenetre change de taille
				mainPanel.tmpCircle.ptCircle.add(new Point(positionX,positionY));
				mainPanel.tmpCircle.setDr();
				Circle c = mainPanel.tmpCircle;
				mainPanel.listeCircle.add(mainPanel.tmpCircle);
				mainPanel.tmpCircle = new Circle();
				mainPanel.tmpCircle.ptCircle.clear();
				if(mainPanel.listeCircle.size()!=0){
					mainPanel.setResX(mainPanel.getLabel().getWidth());
					mainPanel.setResY(mainPanel.getLabel().getHeight());
				}
				//** Ici on calcule le centre avec les coordonnées de la vrai image pour nos calcul
				centerCircle=mainPanel.circleCenter(
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(0).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(0).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(1).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(1).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*c.ptCircle.get(2).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*c.ptCircle.get(2).getY())));
				String p;
				double pDouble = 0;
				String v;
				String l;
				double vDouble = 0;
				double lDouble = 0;
			    File f = new File("1.txt");
			    //** On reprend ce que l'utilisateur a mis en paramètres et on les change en double
			    try{
			    	Scanner sc = new Scanner(f);
			    	p =  sc.nextLine();
			    	p = p.replace("Pixel par Metre : ","");
			    	//System.out.println(p);
			    	sc.nextLine();
			    	v =  sc.nextLine();
			    	v = v.replaceAll("Tension d'acceleration des electrons U : ", "");
			    	//System.out.println(v);
			    	sc.nextLine();
			    	l =  sc.nextLine();
			    	l = l.replaceAll("Longueur de camera en Metre : ", "");
			    	//System.out.println(l);
			    	sc.close();
			    	pDouble = Double.parseDouble(p);
			    	vDouble = Double.parseDouble(v);
			    	lDouble = Double.parseDouble(l);
			    }catch(FileNotFoundException ignored){
			    	
			    }
			    lambda = new BigDecimal((6.62 *Math.pow(10,-34))/
						(Math.sqrt((2.9149 *Math.pow(10,-49))*(vDouble*(double)1000)*
								((double)1+(9.7714 *Math.pow(10,-7))*(vDouble*(double)1000)))));
			    //** Ici on calcule la moyenne d'intensité de tout les cercles ainsi que leur rayon
			    //** et trois autres paramètres étant la Distance interarticulaire l'Angle de diffraction 2theta
			    //** et le Vecteur de diffusion S
			    CalculMoyAndRadius(centerCircle,pDouble, vDouble, lDouble);
			}
		}else if(mainPanel.getCurrentTool()==TypeOutil.ZOOM){
			if(mainPanel.listeCircle.isEmpty()){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			int x = (int)((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*positionX);
			int y = (int)((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*positionY);
			int tmp= (int)((mainPanel.getResX()/mainPanel.getBufferedOriginal().getWidth())*125);
			z.getSubImage(mainPanel.getBufferedOriginal2(), x,y);
			x = (int)((mainPanel.getResX()/mainPanel.getBufferedOriginal().getWidth())*x);
			y = (int)((mainPanel.getResY()/mainPanel.getBufferedOriginal().getHeight())*y);
			mainPanel.setZonezoom(new Point(x, y));
			RescaleOp op = new RescaleOp(1, 1, null);
			op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
			mainPanel.toGray(mainPanel.getBufferedScaled());
			//mainPanel.repaint();
		}else if(mainPanel.getCurrentTool()==TypeOutil.BEAMSTOP){
				if(mainPanel.listeCircle.isEmpty()){
					mainPanel.setResX(mainPanel.getLabel().getWidth());
					mainPanel.setResY(mainPanel.getLabel().getHeight());
				}
				if(tmpBeamStop.isEmpty()){
					double x = (((double)mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*(positionX));
					double y = (((double)mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*(positionY));
					System.out.println(mainPanel.getBufferedOriginal().getWidth()+" "+mainPanel.getResX());
					System.out.println(x+" "+y);
					Color color=new Color(mainPanel.getBufferedOriginal().getRGB((int)(Math.round(x)), (int)(Math.round(y))));
					double c = ((color.getRed() + color.getBlue()+ color.getGreen())/3);
					tmpBeamStop.add(c);
				}else{
					double x = (((double)mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*(positionX));
					double y = (((double)mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*(positionY));
					Color color=new Color(mainPanel.getBufferedOriginal().getRGB((int)(Math.round(x)), (int)(Math.round(y))));
					double c = ((color.getRed() + color.getBlue()+ color.getGreen())/3);
					if(c<tmpBeamStop.get(0)){
						minBS = c;
						maxBS=tmpBeamStop.get(0);
					}else{
						maxBS = c;
						minBS=tmpBeamStop.get(0);
					}
					double pDouble = Double.parseDouble(p);
					double vDouble = Double.parseDouble(v);
					double lDouble = Double.parseDouble(l); 
					if(!mainPanel.listeMoyen.isEmpty()){
						centerCircle=mainPanel.circleCenter(
								new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getX()),
										(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getY())),
								new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getX()),
										(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getY())),
								new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getX()),
										(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getY())));
						
						CalculMoyAndRadius(centerCircle, pDouble, vDouble, lDouble);
					}
					tmpBeamStop.clear();
					JOptionPane.showMessageDialog(null, "BeamStop Ok. ", "Vous avez d�fini le BeamStop", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	
	//** Ici on calcule la moyenne d'intensité de tout les cercles ainsi que leur rayon
    //** et trois autres paramètres étant la Distance interarticulaire l'Angle de diffraction 2theta et le Vecteur de diffusion S  
    public void CalculMoyAndRadius(Point centerCircle, double pDouble, double vDouble, double lDouble){
		int l;
    	double i=0;
		double j;
		double theta2;
		double lenght;
		pDouble = (pDouble* 39.370079);
		//System.out.println(centerCircle.getX()+" "+centerCircle.getY());
		mainPanel.listeMoyen.clear();
		mainPanel.listeRayon.clear();
		mainPanel.listeD.clear();
		mainPanel.listeS.clear();
		mainPanel.liste2theta.clear();
		mainPanel.listeMoyenBeam.clear();
		ArrayList<Point> tmp ;
		while(i<mainPanel.getBufferedOriginal().getWidth()){
			l=0;
			lenght = mainPanel.lenghtFrom2Points(centerCircle, new Point((int)(centerCircle.getX()+i), (int)centerCircle.getY()));
			tmp = mainPanel.getPointWithCenter((int)centerCircle.getX(),(int)centerCircle.getY(),lenght);
			Double somme = 0.0,sommeBeam= 0.0 ,moy, moyBeam;
			for(int h = 0; h<=tmp.size()-1;h++){
				Color color=new Color(mainPanel.getBufferedOriginal().getRGB((int)(tmp.get(h).getX()), (int)tmp.get(h).getY()));
				int c = (color.getRed() + color.getBlue()+ color.getGreen())/3;
				if(minBS !=-1){
					if(c<minBS || c>maxBS){
						sommeBeam = sommeBeam + c;
						l=l+1;
					}
				}
				somme = somme + c;
			}
			if(!tmp.isEmpty()){
				j = (lenght/pDouble);
				theta2 = Math.tan((j/(lDouble*(double)1000))/((double)180)*Math.PI);
				mainPanel.liste2theta.add(theta2); 
				mainPanel.listeS.add(((double)2*Math.toRadians(Math.sin(((theta2/(double)180)*Math.PI))))/lambda.doubleValue());
				mainPanel.listeD.add((((lambda.doubleValue()*lDouble*(double)100)/j)* Math.pow(10,5)));
				mainPanel.listeRayon.add(j);
				moy = (somme/tmp.size());
				moyBeam = (sommeBeam/l);
				mainPanel.listeMoyen.add(moy);
				mainPanel.listeMoyenBeam.add(moyBeam);
				
			}
			i++;
		}  
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

    @Override
	public void componentHidden(ComponentEvent e) {
		
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {

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

    
	public ZoomImage getZ() {
		return z;
	}

	public void setZ(ZoomImage z) {
		this.z = z;
	}
	
	public Panel getMainPanel2() {
		return mainPanel;
	}

	public double getMinBS() {
		return minBS;
	}

	public void setMinBS(double minBS) {
		this.minBS = minBS;
	}

	public double getMaxBS() {
		return maxBS;
	}

	public void setMaxBS(double maxBS) {
		this.maxBS = maxBS;
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
	
	public BigDecimal getLambda(){
		return lambda;
	}

	public void setLambda(BigDecimal lambda){
		this.lambda=lambda;
	}
	
	//Main
	public static void main(String[] args) {

		Fenetre window = new Fenetre();
		window.firstUse();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

}

