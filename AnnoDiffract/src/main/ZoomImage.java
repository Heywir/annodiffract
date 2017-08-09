package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Morteum and Heywir 2017@
 *
 */
class ZoomImage extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{
	
	private Fenetre f=null;
	private int pX;
	private int pY;
	private BufferedImage img = null;
	private BufferedImage img2 = null;
	private final JLabel jL = new JLabel();
	private double positionX;
	private double positionY;
	public JSlider brightSlide;
	
	/**
	 * Creer l'objet image
	 * @param f L'objet fenetre est n�cessaire pour lancer le zoom
	 */
	public ZoomImage(Fenetre f){
		this.setF(f);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/loupe.png")));
		// Taille Ecran
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();

		// Window Settings
		this.setSize((bounds.width/100)*25, (bounds.height/100)*35);
		this.setTitle("Zoom");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				
		// Layout
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		JToolBar light = new JToolBar();
		this.add(light,BorderLayout.NORTH);

		//Slider
		brightSlide = new JSlider(JSlider.HORIZONTAL, 0, 255, 1);
		brightSlide.setToolTipText("Luminosité");
		brightSlide.setMajorTickSpacing(50);
		brightSlide.setMinorTickSpacing(10);
		brightSlide.setPaintTicks(true);
		brightSlide.setPaintLabels(true);
		brightSlide.setValue(f.brightSlide.getValue());
		light.add(new JLabel("Brightness"));
		light.add(brightSlide);

		// Layout 
		JPanel p = new JPanel(layout);
		GridBagLayout layout1 = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		p.setLayout(layout1);
				
		// Panel Image
		p.add(jL,c);
				
		this.add(p,BorderLayout.CENTER);
				
		jL.addMouseMotionListener(this);
		jL.addMouseListener(this);
		brightSlide.addChangeListener(this);
				
	}
	/**
	 * Cette m�thode permettra d'avoir une parti de l'image d'origine sur la parti s�lectionner par l'utilisateur
	 * @param img L'image pour avoir une parti de l'image
	 * @param pX Position x du point
	 * @param pY Position y du point
	 */
	public Boolean getSubImage(BufferedImage img, int pX, int pY){
		this.pX = pX-125;
		this.pY = pY-125;
		BufferedImage j = null;
		try{
			j = img.getSubimage(pX-125, pY-125, 250, 250);
			BufferedImage j1 = f.getMainPanel2().getBufferedOriginal().getSubimage(pX-125, pY-125, 250, 250);
			this.img = j;
			jL.setIcon(new ImageIcon(this.img));
			this.img2 = j1;
			//On garde la luminosite ajuste par nouveau clique
			setBrightness();
		//Dans le cas ou l'utilisateur zoom sur les bords de l'image
		}catch( java.awt.image.RasterFormatException e){
			JOptionPane.showMessageDialog(null, "The soft can't zoom on the edge of the image. "
    				, "Error", JOptionPane.ERROR_MESSAGE);
		}
        return j != null;
	}
	
	/**
	 * Cette m�thode permet de d�finir la luminosit� de l'image zoommer en fonction du slider
	 */
	private void setBrightness() {
		RescaleOp op = new RescaleOp(brightSlide.getValue(), 0, null);
		this.img = op.filter(img2, this.img);
		f.getMainPanel2().toGray(this.img);
		jL.setIcon(new ImageIcon(this.img));
		repaint();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		//La m�thode va �tre appel� des qu'il y a changement d'�tat sur le slider
		if(arg0.getSource()==brightSlide){
			if(f.getMainPanel2().isLoaded()){
				setBrightness();
			}
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		//des qu'on bouge la souris sur l'image la position de celle-ci sera mis � jour
		if (arg0.getSource() == jL) {
			setPositionX(arg0.getX());
			setPositionY(arg0.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == jL){
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			g2d.setColor(Color.RED);
			g2d.drawLine((int)positionX-5, (int)positionY, (int)positionX+5, (int)positionY);
			g2d.drawLine((int)positionX, (int)positionY-5, (int)positionX, (int)positionY+5);
			jL.setIcon(new ImageIcon(img));
			if(f.getMainPanel2().tmpCircle.ptCircle.size()<2){
				f.getMainPanel2().tmpCircle.ptCircle.add(
						new Point((int)((f.getMainPanel2().getResX()/f.getMainPanel2().getBufferedOriginal().getWidth())*(pX+positionX)),
								(int)((f.getMainPanel2().getResY()/f.getMainPanel2().getBufferedOriginal().getHeight())*(pY+positionY))));
			}else{
				
				f.getMainPanel2().tmpCircle.ptCircle.add(new Point((int)((f.getMainPanel2().getResX()/f.getMainPanel2().getBufferedOriginal().getWidth())*(pX+positionX)),
						(int)((f.getMainPanel2().getResY()/f.getMainPanel2().getBufferedOriginal().getHeight())*(pY+positionY))));
				f.getMainPanel2().tmpCircle.setDr();
				Circle c = f.getMainPanel2().tmpCircle;
				f.getMainPanel2().listeCircle.add(f.getMainPanel2().tmpCircle);
				f.getMainPanel2().tmpCircle = new Circle();
				f.getMainPanel2().tmpCircle.ptCircle.clear();
				f.setCenterCircle(f.getMainPanel2().circleCenter(
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(0).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(0).getY())),
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(1).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(1).getY())),
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(2).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(2).getY()))));
				f.getMainPanel2().listePointCenter.add(f.getCenterCircle());
				f.setCenterCircle(f.getCenterCicleMoy());
			    this.f.setLambda(new BigDecimal((6.62 *Math.pow(10,-34))/
						(Math.sqrt((2.9149 *Math.pow(10,-49))*(f.getV()*(double)1000)*
								((double)1+(9.7714 *Math.pow(10,-7))*(f.getV()*(double)1000))))));
			    this.f.getMainPanel2().listeMoyen.clear();
			    this.f.getMainPanel2().listeRayon.clear();
			    this.f.getMainPanel2().listeD.clear();
			    this.f.getMainPanel2().listeS.clear();
			    this.f.getMainPanel2().liste2theta.clear();
			    this.f.getMainPanel2().listeMoyenBeam.clear();
			    this.f.CalculMoyAndRadius(f.getCenterCircle(),f.getP(), f.getV(), f.getL());
			}
			f.getMainPanel2().repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private void setF(Fenetre f) {
		this.f = f;
	}

	private void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	private void setPositionY(double positionY) {
		this.positionY = positionY;
	}
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		 
	}

}
																																				//Morteum and Heywir 2017
