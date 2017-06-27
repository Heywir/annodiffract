package main;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

import sun.security.util.Length;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

class Panel extends JPanel {
	public enum TypeOutil {
		
		 /* Outil principaux */
		 NORMAL,/* L'utilisateur vient de lancer le software */
		 POINT, /* L'utilisateur va placer des points */
	}
	private TypeOutil currentTool = TypeOutil.NORMAL;
	private JLabel label = null;
	private Image image = null;
	private boolean loaded = false;
	private final Integer ratioX = 65;
	private final Integer ratioY = 90;
	private Image imageScaled = null;
	private BufferedImage bufferedScaled;
	private BufferedImage bufferedScaled2;
	private float bright=-1;
	public Circle tmpCircle = new Circle();
	public final ArrayList<Circle> listeCircle = new ArrayList<>();
	private double resX=0;
	private double resY=0;
	

	Panel() {

		// Layout
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(layout);
		
		// Composants

		// Panel Image
		setLabel(new JLabel());

		// Ajouts
		this.add(getLabel(), c);

	}
	
	//Methode pour ouvrir l'image puis l'afficher avec une bonne dimension
	void openImage(File file) throws Exception {
		try {
			FileInputStream in = new FileInputStream(file.getPath());
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    setImage(load(buffer.array()));
			bufferedOriginal = toBufferedImage(getImage());
		    imageScaled = getImage().getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
		    bufferedScaled = toBufferedImage(imageScaled);
		    BufferedImage tGray = toGray(bufferedScaled);
            setImage(tGray);
            
            getLabel().setIcon(new ImageIcon(bufferedScaled));
            setLoaded(true);
		    repaint();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
	
	//Methode pour charger l'image apres ca recuperation
	private Image load(byte[] data) throws Exception{
	    Image image;
	    SeekableStream stream = new ByteArraySeekableStream(data);
	    String[] names = ImageCodec.getDecoderNames(stream);
	    ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
	    RenderedImage im = dec.decodeAsRenderedImage();
	    image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	    return image;
	  }
	
	void scale() {
		if (imageScaled != null) {
			imageScaled = getImage().getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
		    bufferedScaled = toBufferedImage(imageScaled);
		    bufferedScaled2 = toBufferedImage(imageScaled);
		    if(bright!=-1){
		    	setBrightness(bright);
		    }
		    Image img = bufferedScaled.getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
			getLabel().setIcon(new ImageIcon(img));
			if(!listeCircle.isEmpty()){
				for(int i= 0 ; i < listeCircle.size(); i++ ){
					for(int j=0 ; j < listeCircle.get(i).ptCircle.size();j++){
					//System.out.println(getLabel().getWidth()+"/"+resX+"*"+listeCircle.get(i).ptCircle.get(j).getX()+","+ getLabel().getHeight()+"/"+resY+"*"+listeCircle.get(i).ptCircle.get(j).getY());
					listeCircle.get(i).ptCircle.get(j).setLocation(((getLabel().getWidth()/resX)*listeCircle.get(i).ptCircle.get(j).getX()), ((getLabel().getHeight()/resY)*listeCircle.get(i).ptCircle.get(j).getY()));
					System.out.println(listeCircle.get(i).ptCircle.get(j).getX()+" "+listeCircle.get(i).ptCircle.get(j).getY());
				}
			}
			resX = getLabel().getWidth();
			resY = getLabel().getHeight();
			repaint();
			}
		}
	}

	private BufferedImage toGray(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				Color c = new Color(image.getRGB(j, i));
				int red = (int)(c.getRed() * 0.21);
				int green = (int)(c.getGreen() * 0.72);
				int blue = (int)(c.getBlue() *0.07);
				int sum = red + green + blue;
				Color newColor = new Color(sum,sum,sum);
				image.setRGB(j,i,newColor.getRGB());
			}
		}
		return image;
	}
	
	public void drawCenteredCircle(Graphics2D g, Point centerCircle, int r) {
		  int x = (int) Math.round(centerCircle.getX()-(r));
		  int y = (int) Math.round(centerCircle.getY()-(r));
		  g.drawOval(x,y,2*r,2*r);
		}
	
	public Point circleCenter(Point A, Point B, Point C) { 
		float yDelta_a = B.y - A.y; 
		float xDelta_a = B.x - A.x; 
		float yDelta_b = C.y - B.y; 
		float xDelta_b = C.x - B.x; 
		int centerX;
		int centerY;
		Point center = new Point(0,0); 
		float aSlope = yDelta_a/xDelta_a; 
		float bSlope = yDelta_b/xDelta_b;
		centerX = (int) Math.round((aSlope*bSlope*(A.y - C.y) + bSlope*(A.getX() + B.getX())- aSlope*(B.x+C.x) )/(2*(bSlope-aSlope) ));
		centerY = (int) Math.round(-1*(centerX - (A.x+B.x)/2)/aSlope +  (A.y+B.y)/2);
		center.setLocation(new Point(centerX, centerY)); 
		
		return center;
	}
	
	public int lenghtFrom2Points(Point A, Point B) {
		int lenght = (int) Math.sqrt((A.getX()-B.getX())*(A.getX()-B.getX()) + (A.getY()-B.getY())*(A.getY()-B.getY()));
		return lenght; 
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isLoaded()) {
			Graphics2D g2d = bufferedScaled.createGraphics();
			if(!listeCircle.isEmpty()){
				for (Circle aListePoint : listeCircle) {
					for(Point pt : aListePoint.ptCircle){	
						//System.out.println(listePoint.get(i).getX()+" "+listePoint.get(i).getY());
						drawPoint(g2d,pt); 
						}
					if(aListePoint.isDr()){
						Point centerCircle=circleCenter(aListePoint.ptCircle.get(0), aListePoint.ptCircle.get(1), aListePoint.ptCircle.get(2));
						drawPoint(g2d, centerCircle);
						int r = lenghtFrom2Points(centerCircle, aListePoint.ptCircle.get(0));
						drawCenteredCircle(g2d, centerCircle, r);
					}
				}
			}
			if(tmpCircle.ptCircle.size()!=0){
				for(int i=0; i<tmpCircle.ptCircle.size();i++){
					drawPoint(g2d, tmpCircle.ptCircle.get(i));
				}
			}

			g2d.dispose();
			//Image img = getImage().getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
			getLabel().setIcon(new ImageIcon(bufferedScaled));
			drawGraph(g);
		}
		repaint();
	}

	//Methode pour dÃ©ssiner le graph
	private void drawGraph(Graphics g) {
		// ParamÃ¨tres graphe

		// Distance entre axe et text
		int distance = 20;

		// Pour le dÃ©coupage selon l'image
		double indentationY = (bufferedOriginal.getHeight() / 100);
		double indentationX = (bufferedOriginal.getWidth() / 100);
		int tailleInden = 5;

		// Point En haut Ã  gauche
		int yZeroX = getLabel().getLocation().x;
		int yZeroY = getLabel().getLocation().y;

		// En Bas Ã  gauche
		int yFinY = yZeroY + getLabel().getIcon().getIconHeight();

		// En Bas Ã  droite
		int xFinX = yZeroX + getLabel().getIcon().getIconWidth();

		// Longueur

		double xLength = (xFinX - yZeroX) / indentationX;
		double yLength = (yFinY - yZeroY) / indentationY;

		// Dessin

		// ParamÃ¨tres

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.BLACK);

		// Y Axis
		g2.drawLine(yZeroX -1, yZeroY, yZeroX -1, yFinY);
		g2.drawString("Y", yZeroX - 4, yZeroY - 4);

		// X Axis
		g2.drawLine(yZeroX, yFinY, xFinX, yFinY);
		g2.drawString("X", xFinX + 4, yFinY + 4);


	  // Numerotation Y

		int longueurMot;
		for(int i = 0; i < indentationY +1; i++) {
			g2.draw(new Line2D.Double(yZeroX - tailleInden, yZeroY + (i * yLength), yZeroX + tailleInden, yZeroY + (i * yLength)));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), (yZeroX - distance) - longueurMot /2, yZeroY + (int) (i * (yLength)));

		}

		// Numerotation X

		for(int i = 0; i < indentationX +1; i++) {
			g2.draw(new Line2D.Double(yZeroX + (i * xLength), yFinY - tailleInden, yZeroX + (i * xLength), yFinY + tailleInden));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), yZeroX + (int) (i * xLength) - longueurMot /2, yFinY + distance);
		}

	}
	
	private void drawPoint(Graphics2D g2d, Point e) {
		int x1,y1,x2,y2;
		x1 = (int) Math.round(e.getX() - 3);
		y1 = (int) Math.round(e.getY());
		x2 = (int) Math.round(e.getX());
		y2 = (int) Math.round(e.getY());
		g2d.drawLine(x1, y1, x2, y2);
		x1 = (int) Math.round(e.getX());
		y1 = (int) Math.round(e.getY() - 3);
		x2 = (int) Math.round(e.getX());
		y2 = (int) Math.round(e.getY());
		g2d.drawLine(x1, y1, x2, y2);
		x1 = (int) Math.round(e.getX());
		y1 = (int) Math.round(e.getY());
		x2 = (int) Math.round(e.getX() + 3);
		y2 = (int) Math.round(e.getY());
		g2d.drawLine(x1, y1, x2, y2);
		x1 = (int) Math.round(e.getX());
		y1 = (int) Math.round(e.getY());
		x2 = (int) Math.round(e.getX());
		y2 = (int) Math.round(e.getY() + 3);
		g2d.drawLine(x1, y1, x2, y2);
	}
	
	//Change Brightness
	public void setBrightness(float scaleFactor){
		//float value = (float) slider.getValue();
        //float scaleFactor = 2 * value / slider.getMaximum();
        RescaleOp op = new RescaleOp(scaleFactor, 0, null);
        bufferedScaled = op.filter(bufferedScaled2, bufferedScaled);
        toGray(bufferedScaled);
        bright = scaleFactor;
        repaint();
	}
	
	private Image getImage() {
		return image;
	}

	private void setImage(Image image) {
		this.image = image;
	}

	JLabel getLabel() {
		return label;
	}

	private void setLabel(JLabel label) {
		this.label = label;
	}

	boolean isLoaded() {
		return loaded;
	}

	void setLoaded(boolean b) {
		this.loaded = b;
	}

	public TypeOutil getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(TypeOutil point) {
		this.currentTool = TypeOutil.POINT;
	}


	public double getResY() {
		return resY;
	}

	public void setResY(double resY) {
		this.resY = resY;
	}

	public double getResX() {
		return resX;
	}

	public void setResX(double resX) {
		this.resX = resX;
	}
	
	public float getBright(){
		return bright;
	}
	
	public void setBright(float e){
		bright=e;
	}
	
}