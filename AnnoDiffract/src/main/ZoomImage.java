package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZoomImage extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, ChangeListener{
	private Fenetre f=null;
	private JPanel p=null;
	private int pX;
	private int pY;
	private BufferedImage img = null;
	private BufferedImage img2 = null;
	private JLabel jL = new JLabel();
	private double positionX;
	private double positionY;
	
	public ZoomImage(Fenetre f){
		this.setF(f);
		
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
				
				p = new JPanel(layout);
				setContentPane(p);
				// Layout 
				
				GridBagLayout layout1 = new GridBagLayout();
				GridBagConstraints c = new GridBagConstraints();
				p.setLayout(layout1);
				

				// Panel Image
				p.add(jL);

				// Ajouts
				p.setSize(new Dimension(this.getWidth(), this.getHeight()));
				
				jL.addMouseMotionListener(this);
				jL.addMouseListener(this);
				f.brightSlide.addChangeListener(this);
				
	}
	
	public void getSubImage(BufferedImage img, int pX, int pY){
		this.pX = pX-125;
		this.pY = pY-125;
		BufferedImage j = img.getSubimage(pX-125, pY-125, 250, 250);
		BufferedImage j1 = f.getMainPanel2().getBufferedOriginal().getSubimage(pX-125, pY-125, 250, 250);
		this.img = j;
		jL.setIcon(new ImageIcon(this.img));
		this.img2 = j1;
	}
	
	
	@Override
	public void stateChanged(ChangeEvent arg0) {

		if(arg0.getSource()==f.brightSlide){
			if(f.getMainPanel2().isLoaded()){
				RescaleOp op = new RescaleOp(((float)4 * (float) f.brightSlide.getValue() / (float)f.brightSlide.getMaximum()), 0, null);
				this.img = op.filter(img2, img);
				f.getMainPanel2().toGray(img);
				Image img = this.img;
				jL.setIcon(new ImageIcon(img));
				repaint();
			}
		}
		
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

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println(positionX+"  "+positionY);
		if (arg0.getSource() == jL) {
			setPositionX(arg0.getX());
			setPositionY(arg0.getY());
			//System.out.println(positionX+"  "+positionY);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == jL){
			Graphics g2d = img.getGraphics();
			g2d.setColor(Color.BLUE);
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
				f.getMainPanel2().tmpCircle.setDr(true);
				Circle c = f.getMainPanel2().tmpCircle;
				f.getMainPanel2().listeCircle.add(f.getMainPanel2().tmpCircle);
				f.getMainPanel2().tmpCircle = new Circle();
				f.getMainPanel2().tmpCircle.ptCircle.clear();
				Point centerCircle=f.getMainPanel2().circleCenter(
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(0).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(0).getY())),
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(1).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(1).getY())),
						new Point((int)Math.round((f.getMainPanel2().getBufferedOriginal().getWidth()/f.getMainPanel2().getResX())*c.ptCircle.get(2).getX()),
								(int)Math.round((f.getMainPanel2().getBufferedOriginal().getHeight()/f.getMainPanel2().getResY())*c.ptCircle.get(2).getY())));
				System.out.println(centerCircle.getX() + " " + centerCircle.getY());
				String p = null;
				double pDouble = 0;
				String v =null;
				String l =null;
				double vDouble = 0;
				double lDouble = 0;
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
			    	lDouble = Double.parseDouble(l);
			    }catch(FileNotFoundException fnf){
			    	
			    }
			    this.f.CalculMoyAndRadius(centerCircle,pDouble, vDouble, lDouble);
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



	public int getpY() {
		return pY;
	}


	public void setpY(int pY) {
		this.pY = pY;
	}


	public Fenetre getF() {
		return f;
	}


	public void setF(Fenetre f) {
		this.f = f;
	}


	public int getpX() {
		return pX;
	}


	public void setpX(int pX) {
		this.pX = pX;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

}
