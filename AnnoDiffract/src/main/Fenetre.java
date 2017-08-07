package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.RescaleOp;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.ui.RefineryUtilities;

class Fenetre extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel mainPanel = null;
	private JMenuItem menuItemOuvrir = null;
	private JMenuItem menuGraphOpen = null;
	private JMenuItem menuGItemQuit = null;
	private JButton findCenter = null;
	private JButton setParam = null;
	private JButton zoom = null;
	private JButton beam = null;
	private JButton clean = null;
	private JButton cleanAll = null;
	private JLabel statusLabel = null;
	private JLabel statusDialogLabel = null;
	private JLabel outilLabel;
	private int positionX=0;
	private int positionY=0;
	public JSlider brightSlide;
	private double p = 244;
	private double v =200;
	private double l =100;
	private BigDecimal lambda;
	private ZoomImage z=null;
	private double minBS=-1;
	private double maxBS=-1;
	private Point centerCircle;


	
	private Fenetre() {
		
		// Taille Ecran
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		
		// Composants
		setMainPanel(new Panel(this));
		JMenuBar mainMenuBar = buildMenuBar();
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();
		statusDialogLabel = new JLabel();
		

		// Window Settings
		this.setSize((bounds.width/100)*50, (bounds.height/100)*80);
		this.setTitle("AnnoDiffract");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/i.png")));
		// Layout
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		// Status Bar
		statusPanel.add(statusLabel, BorderLayout.EAST);
		statusPanel.add(statusDialogLabel, BorderLayout.WEST);
		//statusDialogLabel.setText("Hello Open an image of diffraction");
		
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
		JMenu menuFile = new JMenu("File");
		menuItemOuvrir = new JMenuItem("Open");
		menuGItemQuit = new JMenuItem("Quit");

		// Graph
		JMenu menuGraph = new JMenu("Graph");
		menuGraphOpen = new JMenuItem("Open");
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
		
		beam = new JButton(new ImageIcon(Fenetre.class.getResource("img/beam.png")));
		beam.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/beampressed.png")));
		beam.setToolTipText("Correction On BeamStop");
		beam.setBorder(null);
		beam.setContentAreaFilled(false);
		
		cleanAll = new JButton(new ImageIcon(Fenetre.class.getResource("img/gomme.png")));
		cleanAll.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/gommepressed.png")));
		cleanAll.setToolTipText("Reset all");
		cleanAll.setBorder(null);
		cleanAll.setContentAreaFilled(false);
		
		clean = new JButton(new ImageIcon(Fenetre.class.getResource("img/retour1.png")));
		clean.setPressedIcon(new ImageIcon(Fenetre.class.getResource("img/retour.png")));
		clean.setToolTipText("Reset all");
		clean.setBorder(null);
		clean.setContentAreaFilled(false);
		
		// Ajouts
		newMenuBar.add(menuFile);
		newMenuBar.add(menuGraph);
		menuBar.add(newMenuBar, BorderLayout.NORTH);
		menuBar.add(toolBar, BorderLayout.CENTER);
		menuFile.add(menuItemOuvrir);
		menuFile.add(menuGItemQuit);
		menuGraph.add(menuGraphOpen);
		toolBar.add(findCenter);
		toolBar.add(zoom);
		toolBar.add(beam);
		toolBar.add(clean);
		toolBar.add(cleanAll);
		toolBar.add(setParam);
		toolBar.add(new JLabel("Brightness:"));
		toolBar.add(brightSlide);
		toolBar.add(outilLabel);

		// Listeners
		getMainPanel().addComponentListener(this);
		menuItemOuvrir.addActionListener(this);
		menuGItemQuit.addActionListener(this);
		menuGraphOpen.addActionListener(this);
		setParam.addActionListener(this);
		zoom.addActionListener(this);
		findCenter.addActionListener(this);
		beam.addActionListener(this);
		clean.addActionListener(this);
		cleanAll.addActionListener(this);
		mainPanel.getLabel().addMouseListener(this);
		brightSlide.addChangeListener(this);

		return menuBar;
	}
	
	//Methode pour changer le parametre
	private void changeParam(){
	    
	    JTextField pField = new JTextField(String.valueOf(p),7);
	    JTextField vField = new JTextField(String.valueOf(v),7);
	    JTextField lField = new JTextField(String.valueOf(l),7);
	
	    JPanel myPanel = new JPanel();
	    myPanel.add(new JLabel("PPI :"));
	    myPanel.add(pField);
	    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	    myPanel.add(new JLabel("Acceleration voltage of electrons(kV) :"));
	    myPanel.add(vField);
	    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	    myPanel.add(new JLabel("Camera Lenght (cm) :"));
	    myPanel.add(lField);
		      
	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Option", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
			try {
				Float.parseFloat(pField.getText());
				Float.parseFloat(vField.getText());
				Float.parseFloat(lField.getText());
    		    p=Double.parseDouble(pField.getText());
    		    v=Double.parseDouble(vField.getText());
    		    l=Double.parseDouble(lField.getText());
    		    JOptionPane.showMessageDialog(null, "The Settings changed", "Good", JOptionPane.INFORMATION_MESSAGE);

			} catch(NumberFormatException z){
				JOptionPane.showMessageDialog(null, "You didn't enter a number. "
    					+ "The Settings aren't changed"
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
			
			if(!mainPanel.listeCircle.isEmpty()){
				centerCircle=mainPanel.circleCenter(
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getY())));
				mainPanel.listePointCenter.add(centerCircle);
				centerCircle = getCenterCicleMoy();
				//Calcul lambda
	    		lambda = new BigDecimal((6.62 *Math.pow(10,-34))/
						(Math.sqrt((2.9149 *Math.pow(10,-49))*(v*(double)1000)*
								((double)1+(9.7714 *Math.pow(10,-7))*(v*(double)1000)))));
				
				CalculMoyAndRadius(centerCircle, p, v, l);
				
			}
	       }
	}
	//Cette methode permet d'obtenir la moyenne de tout les centre défini par l'utilisateur
	public Point getCenterCicleMoy(){
		Point centerCircle;
		double x=0,y=0;
		for(int i=0;i<mainPanel.listePointCenter.size();i++){
			x=x+mainPanel.listePointCenter.get(i).getX();
			y=y+mainPanel.listePointCenter.get(i).getY();
		}
		x=x/(double)mainPanel.listePointCenter.size();
		y=y/(double)mainPanel.listePointCenter.size();
		centerCircle = new Point((int)Math.round(x), (int)Math.round(y));
		return centerCircle;
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
						//Remets par defaut le slider quand on ouvre une image
						brightSlide.setValue(50);
						if (getMainPanel().isLoaded()) {

							mainPanel.setBright(-1);
							getMainPanel().getLabel().setIcon(null);
							getMainPanel().setLoaded(false);
						}
						getMainPanel().openImage(chooser.getSelectedFile());
						statusDialogLabel.setText("Set 3 point on the image to get the center (you can also draw another circle to get a more accurate center)");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
		}
		if (e.getSource() == menuGItemQuit) {
			//Quit the application
			this.dispose();
		}
		if (e.getSource() == findCenter) {
			//Method to find center
			if(mainPanel.isLoaded()){
				mainPanel.setCurrentTool(TypeOutil.POINT);
				outilLabel.setText("Tool: " + TypeOutil.POINT.toString());
				if(z!=null){
					z.dispose();
				}
			}else{
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == setParam) {
			//Call Method to set settings
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
					//In case of the user close the window for zoom and want to open it
					if (!z.isVisible()) {
						z.setVisible(true);
					}
				}
			}else{
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == beam) {
			//Method to find center with zoom
			if(mainPanel.isLoaded()){
				mainPanel.setCurrentTool(TypeOutil.BEAMSTOP);
				outilLabel.setText("Tool: " + TypeOutil.BEAMSTOP.toString());
			}else{
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (e.getSource() == cleanAll) {
			//Method to find center with zoom
			if(mainPanel.isLoaded()){
				mainPanel.liste2theta.clear();
				mainPanel.listeCircle.clear();
				mainPanel.listeD.clear();
				mainPanel.listeMoyen.clear();
				mainPanel.listeMoyenBeam.clear();
				mainPanel.listePointCenter.clear();
				mainPanel.listeRayon.clear();
				mainPanel.listeS.clear();
				mainPanel.listeSomme.clear();
				mainPanel.listeSommeBeam.clear();
				mainPanel.tmpCircle.ptCircle.clear();
				RescaleOp op = new RescaleOp(1, 1, null);
				op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
				mainPanel.toGray(mainPanel.getBufferedScaled());
				op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
				mainPanel.toGray(mainPanel.getBufferedScaled());
				statusDialogLabel.setText("Set 3 point on the image to get the center (you can also draw another circle to get a more accurate center)");
				
			}else{
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (e.getSource() == clean) {
			//Method to find center with zoom
			if(mainPanel.isLoaded()){
				if(!mainPanel.listeCircle.isEmpty()){
					mainPanel.listeCircle.remove(mainPanel.listeCircle.size()-1);
					RescaleOp op = new RescaleOp(1, 1, null);
					op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
					mainPanel.toGray(mainPanel.getBufferedScaled());
					op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
					mainPanel.toGray(mainPanel.getBufferedScaled());
					mainPanel.repaint();
					if(mainPanel.listeCircle.isEmpty()){
						statusDialogLabel.setText("Set 3 point on the image to get the center (you can also draw another circle to get a more accurate center)");
					}
				}
			}else{
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
	        			, "Not Good", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (e.getSource() == menuGraphOpen) {
			if (getMainPanel().getLabel().getIcon() != null && !mainPanel.listeMoyen.isEmpty()) {
				Graph graph = new Graph(this);
				graph.pack();
				 RefineryUtilities.centerFrameOnScreen(graph);
				graph.setVisible( true );
			}else if(!mainPanel.isLoaded()){
				JOptionPane.showMessageDialog(null, "You didn't open an Image. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null, "No point center was set. "
        				, "Not Good", JOptionPane.ERROR_MESSAGE);
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
			if(mainPanel.listeCircle.isEmpty()){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			positionX = arg0.getX();
			positionY = arg0.getY();
			statusLabel.setText("MouseX: " + Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*arg0.getX()) + " " + "MouseY: " + Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*arg0.getY()));
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
			    lambda = new BigDecimal((6.62 *Math.pow(10,-34))/
						(Math.sqrt((2.9149 *Math.pow(10,-49))*(v*(double)1000)*
								((double)1+(9.7714 *Math.pow(10,-7))*(v*(double)1000)))));
			    //** Ici on calcule la moyenne d'intensité de tout les cercles ainsi que leur rayon
			    //** et trois autres paramètres étant la Distance interarticulaire l'Angle de diffraction 2theta
			    //** et le Vecteur de diffusion S
			    CalculMoyAndRadius(centerCircle,p, v, l);
			}
		}else if(mainPanel.getCurrentTool()==TypeOutil.ZOOM){
			if(mainPanel.listeCircle.isEmpty()){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			int x = (int)((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*positionX);
			int y = (int)((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*positionY);
			z.getSubImage(mainPanel.getBufferedOriginal2(), x,y);
			x = (int)((mainPanel.getResX()/mainPanel.getBufferedOriginal().getWidth())*x);
			y = (int)((mainPanel.getResY()/mainPanel.getBufferedOriginal().getHeight())*y);
			mainPanel.setZonezoom(new Point(x, y));
			RescaleOp op = new RescaleOp(1, 1, null);
			op.filter(mainPanel.getBufferedScaled2(), mainPanel.getBufferedScaled());
			mainPanel.toGray(mainPanel.getBufferedScaled());
		}else if(mainPanel.getCurrentTool()==TypeOutil.BEAMSTOP){
			if(mainPanel.listeCircle.isEmpty()){
				mainPanel.setResX(mainPanel.getLabel().getWidth());
				mainPanel.setResY(mainPanel.getLabel().getHeight());
			}
			double x = (((double)mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*(positionX));
			double y = (((double)mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*(positionY));
			getBeamStop((int)Math.round(x),(int)Math.round(y));
			double pDouble = p;
			double vDouble = v;
			double lDouble = l; 
			if(!mainPanel.listeMoyen.isEmpty()){
				centerCircle=mainPanel.circleCenter(
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(0).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(1).getY())),
						new Point((int)Math.round((mainPanel.getBufferedOriginal().getWidth()/mainPanel.getResX())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getX()),
								(int)Math.round((mainPanel.getBufferedOriginal().getHeight()/mainPanel.getResY())*mainPanel.listeCircle.get(mainPanel.listeCircle.size()-1).ptCircle.get(2).getY())));
				
				CalculMoyAndRadius(centerCircle, pDouble, vDouble, lDouble);
				statusDialogLabel.setText("Beamstop Define. You can now Open, the graph");
			}
			JOptionPane.showMessageDialog(null, "You define the beamstop", "BeamStop Ok. ", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	//Cette methode permet de definir le beamstop en fonction d'un point grace a l'utilisateur
	public void getBeamStop(int x,int y){
		ArrayList<Integer> h = new ArrayList<>();
		Color c ;
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-2,y-2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-1,y-2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x,y-2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+1,y-2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+2,y-2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-2,y-1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-1,y-1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x,y-1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+1,y-1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+2,y-1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-2,y));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-1,y));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x,y));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+1,y));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+2,y));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-2,y+1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-1,y+1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x,y+1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+1,y+1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+2,y+1));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-2,y+2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x-1,y+2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x,y+2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+1,y+2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		c = new Color(getMainPanel().getBufferedOriginal().getRGB(x+2,y+2));
		h.add((int)Math.round((c.getBlue()+c.getRed()+c.getRed())/3));
		double moy=0,som=0,ecart;
		//Nous calculons la somme
		for(int i=0;i<h.size();i++){
			som = som+(double)h.get(i);
		}
		//Nous calculons la moyenne
		moy = som/(double)h.size();
		som = 0;
		//Nous calculons l'écart-type
		for(int i=0;i<h.size();i++){
			som = som+(((double)h.get(i)-moy)*((double)h.get(i)-moy));
		}

		//Nous calculons l'intervalle grace a la moyenne et a l'ecart-type
		ecart = Math.sqrt(som/(double)h.size());
		minBS = Math.round(moy-ecart);
		maxBS = Math.round(moy+ecart);
	}
	
	//** Ici on calcule la moyenne d'intensité de tout les cercles ainsi que leur rayon
    //** et trois autres paramètres étant la Distance interarticulaire l'Angle de diffraction 2theta et le Vecteur de diffusion S  
    public void CalculMoyAndRadius(Point centerCircle, double pDouble, double vDouble, double lDouble){
		statusDialogLabel.setText("Calculate the intensity. Wait a moment !!");
    	int l;
    	double i=0;
		double j;
		BigDecimal theta2;
	    double lenght;
		pDouble = (pDouble* (double)39.370079);
		//On remet à jour les listes (on les vides) quand on appelle cette methode
		mainPanel.listeMoyen.clear();
		mainPanel.listeSomme.clear();
		mainPanel.listeRayon.clear();
		mainPanel.listeD.clear();
		mainPanel.listeS.clear();
		mainPanel.liste2theta.clear();
		mainPanel.listeMoyenBeam.clear();
		mainPanel.listeSommeBeam.clear();
		// cette liste contiendra les point de chaque cercle
		ArrayList<Point> tmp ;
		//Il n'est pas intéresant de parcourir tout les cercles car à un certain points il n'y a plus de cercle
		//Donc nous nous arrètons a la taille maximum X de l'image
		while(i<mainPanel.getBufferedOriginal().getWidth()){
			l=0;
			//Calcul du rayon
			lenght = mainPanel.lenghtFrom2Points(centerCircle, new Point((int)(centerCircle.getX()+i), (int)centerCircle.getY()));
			//Obtention de tout les points du cercle
			tmp = mainPanel.getPointWithCenter((int)centerCircle.getX(),(int)centerCircle.getY(),lenght);
			Double somme = 0.0,sommeBeam= 0.0 ,moy, moyBeam;
			for(int h = 0; h<=tmp.size()-1;h++){
				//Pour chaque point du cercle nous prenons  la valeur RGB et nous en faisons la moyenne pour avoir une 
				// valeur en Nuance de gris
				Color color=new Color(mainPanel.getBufferedOriginal().getRGB((int)(tmp.get(h).getX()), (int)tmp.get(h).getY()));
				int c = (color.getRed() + color.getBlue()+ color.getGreen())/3;
				//Si le beamtop n'a pas été défini la somme restera égale à 0 pour la correction Beamstop
				if(minBS !=-1){
					if(c<minBS || c>maxBS){
						sommeBeam = sommeBeam + c;
						l=l+1;
					}
				}
				somme = somme + c;
			}
			//Ici nous calculons les valeurs nécessaire pour le graphe ensuite nous 
			// les ajoutons a des listes specifique
			if(!tmp.isEmpty()){
				// Le lambda calcule autre part lors du lancement de la methode ou d'un changement de parametre dans les options
				// Conversion du rayon en Metre pDouble = (p* (double)39.370079) p etant le PPI défini par l'utilisateur
				j = (lenght/pDouble);
				//Calculs de 2 Theta
				theta2 = BigDecimal.valueOf(Math.toRadians(Math.atan((j/((double)lDouble/(double)100))/((double)180)*Math.PI)));
				mainPanel.liste2theta.add(theta2.doubleValue()); 
				//Calcul de d
				double d =((lambda.doubleValue()*(double)lDouble*(double)100)/j)* Math.pow(10,6);
				mainPanel.listeD.add(d);
				mainPanel.listeRayon.add(j);
				//Calcul Vecteur de diffusion
				mainPanel.listeS.add(((lambda.doubleValue()/d)/lambda.doubleValue()));
				//Calcul Moyenne d'intensité avec et sans correction BeamStop
				moy = (somme/tmp.size());
				moyBeam = (sommeBeam/l);
				mainPanel.listeMoyen.add(moy);
				mainPanel.listeSomme.add(somme);
				
				//Si le beamstop n'a pas ete defini nous n'entrons rien dans ces listes elles
				//sont vides par defaut
				if(minBS != -1){
					mainPanel.listeMoyenBeam.add(moyBeam);
					mainPanel.listeSommeBeam.add(sommeBeam);
				}
			}
			i++;
		}
		if(minBS == -1){
			statusDialogLabel.setText("You can now open the Graph or define the Beamstop (which is important) !!");
		}else{
			statusDialogLabel.setText("You can now open the Graph (Beamstop Define)");
		}
	}

	@Override
	//Appeler lorsque le slider change d'etat
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
	
	public Double getP(){
		return p;
	}
	
	public Double getV(){
		return v;
	}
	
	public Double getL(){
		return l;
	}
	
	public Point getCenterCircle(){
		return centerCircle;
	}
	
	public void setCenterCircle(Point centerCircle){
		this.centerCircle=centerCircle;
	}
	
	//Main
	public static void main(String[] args) {

		Fenetre window = new Fenetre();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

}																																																		//Morteum and Heywir 2017

