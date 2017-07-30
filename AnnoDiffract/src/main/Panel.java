package main;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
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
	
	//Variable fenetre
	private final Fenetre f;
	private TypeOutil currentTool = TypeOutil.NORMAL;
	private JLabel label = null;
	
	//Variable qui nous dit si une image à été chargé
	private boolean loaded = false;
	
	//Variable pour les images gardé en mémoires
	private Image image = null;
	private Image imageScaled = null;
	private BufferedImage bufferedOriginal;
	private BufferedImage bufferedOriginal2;
	private BufferedImage bufferedScaled;
	private BufferedImage bufferedScaled2;
	private String fileName;
	
	//Variable luminosité
	private float bright=-1;

	//Variable pour les points et cercles
	public Circle tmpCircle = new Circle();
	public final ArrayList<Circle> listeCircle = new ArrayList<>();
	private Point zonezoom=null;
	
	//Liste Intensité
	public final ArrayList<Double> listeMoyen = new ArrayList<>();
	public final ArrayList<Double> listeMoyenBeam = new ArrayList<>();
	public final ArrayList<Double> listeSomme = new ArrayList<>();
	public final ArrayList<Double> listeSommeBeam = new ArrayList<>();
	
	//Liste Distance
	public final ArrayList<Double> listeRayon = new ArrayList<>();
	public final ArrayList<Double> listeD = new ArrayList<>();
	public final ArrayList<Double> listeS = new ArrayList<>();
	public final ArrayList<Double> liste2theta = new ArrayList<>();
	
	//Autre Liste
	public final ArrayList<Point> listePointCenter = new ArrayList<>();
	
	//Variable resolution
	private double resX=0;
	private double resY=0;
	

	public Panel(Fenetre f) {
		
		this.f = f; 
		
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
			listePointCenter.clear();
			zonezoom = null;
			tmpCircle.ptCircle.clear();
			listeCircle.clear();
			listeMoyen.clear();
			listeRayon.clear();
			listeS.clear();
			listeD.clear();
			FileInputStream in = new FileInputStream(file.getPath());
			fileName = file.getName().replaceFirst("[.][^.]+$", "");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
		    channel.read(buffer);
		    setImage(load(buffer.array()));
			bufferedOriginal = toBufferedImage(getImage());
			
			//Redesiner l'image sur une autre variable pour ne pas modifier l'image originale 
			bufferedOriginal2  = new BufferedImage(bufferedOriginal.getWidth(),
			bufferedOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			Graphics g = bufferedOriginal2.createGraphics();
			g.drawImage(bufferedOriginal, 0, 0, null);
			Dimension d = resizeImage();
			imageScaled = getImage().getScaledInstance(d.width, -1,  Image.SCALE_SMOOTH);

		    //Convert Image to Gray
		    bufferedScaled = toBufferedImage(imageScaled);
		    bufferedScaled2 = toBufferedImage(imageScaled);
		    BufferedImage tGray = toGray(bufferedScaled);
		    toGray(bufferedOriginal);
		    toGray(bufferedOriginal2);
		    toGray(bufferedScaled2);
		    setImage(tGray);
            
		    //Put Image Rezize On Panel
            getLabel().setIcon(new ImageIcon(bufferedScaled));
            setLoaded(true);
            in.close();
			
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
	
	private Dimension resizeImage(){
		float Calneww, Calnewh, imWidth = bufferedOriginal.getWidth(), imHeight = bufferedOriginal.getHeight();
		int neww,newh;
		Calneww = (float) ((imWidth/imHeight)*(f.getHeight()/1.5));
		Calnewh = (float) ((imHeight/imWidth)*(f.getWidth()/1.5));
			
		neww = Math.round(Calneww);
		newh = Math.round(Calnewh);
		return new Dimension(neww, newh);
		
	}
	
	// Methode Pour Mettre Les points a la bonne position par rapport a la nouvelle taille de fenetre
	void scale() {
		if (imageScaled != null) {
			float Calneww, Calnewh, imWidth = bufferedOriginal.getWidth(), imHeight = bufferedOriginal.getHeight();
			int neww,newh;
			Calneww = (float) ((imWidth/imHeight)*(f.getHeight()/1.5));
			Calnewh = (float) ((imHeight/imWidth)*(f.getWidth()/1.5));
				
			neww = Math.round(Calneww);
			newh = Math.round(Calnewh);
			Dimension d = new Dimension(neww,  newh);
			imageScaled = bufferedOriginal.getScaledInstance(d.width, -1,  Image.SCALE_SMOOTH);
			bufferedScaled = toBufferedImage(imageScaled);
			toGray(bufferedScaled);
		    bufferedScaled2 = toGray(toBufferedImage(imageScaled));
		    if(bright!=-1){
		    	setBrightness(bright);
		    }
		    Image img = bufferedScaled;
			getLabel().setIcon(new ImageIcon(img));
			if(zonezoom != null){
				zonezoom.setLocation((getLabel().getWidth()/resX)*zonezoom.getX(), (getLabel().getHeight()/resY)*zonezoom.getY());
			}
			if(!listeCircle.isEmpty()){
				for (Circle aListeCircle : listeCircle) {
					for (int j = 0; j < aListeCircle.ptCircle.size(); j++) {
						aListeCircle.ptCircle.get(j).setLocation(((getLabel().getWidth() / resX) * aListeCircle.ptCircle.get(j).getX()), ((getLabel().getHeight() / resY) * aListeCircle.ptCircle.get(j).getY()));
					}
				}
			}
		}
		resX = getLabel().getWidth();
		resY = getLabel().getHeight();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isLoaded()) {
			Graphics2D g2d = bufferedScaled.createGraphics();
			g2d.setColor(Color.BLUE);
			if(!listeCircle.isEmpty()){
				for (Circle aListePoint : listeCircle) {
					for(Point pt : aListePoint.ptCircle){	
						drawPoint(g2d,pt); 
					}
					if(aListePoint.isDr()){
						Point centerCircle=circleCenter(aListePoint.ptCircle.get(0), aListePoint.ptCircle.get(1), aListePoint.ptCircle.get(2));
						drawPoint(g2d, centerCircle);
						double r = lenghtFrom2Points(centerCircle, aListePoint.ptCircle.get(0));
						drawCenteredCircle(g2d, centerCircle, r);
					}
				}
			}
			if(tmpCircle.ptCircle.size()!=0){
				for(int i=0; i<tmpCircle.ptCircle.size();i++){
					drawPoint(g2d, tmpCircle.ptCircle.get(i));
				}
			}
			if(zonezoom!=null){
				int tmp= (int)((getResX()/getBufferedOriginal().getWidth())*125);
				g2d.drawLine((int)zonezoom.getX()-tmp, (int)zonezoom.getY()-tmp, (int)zonezoom.getX()-tmp, (int)zonezoom.getY()+tmp);
				g2d.drawLine((int)zonezoom.getX()-tmp, (int)zonezoom.getY()-tmp, (int)zonezoom.getX()+tmp, (int)zonezoom.getY()-tmp);
				g2d.drawLine((int)zonezoom.getX()+tmp, (int)zonezoom.getY()-tmp, (int)zonezoom.getX()+tmp, (int)zonezoom.getY()+tmp);
				g2d.drawLine((int)zonezoom.getX()-tmp, (int)zonezoom.getY()+tmp, (int)zonezoom.getX()+tmp, (int)zonezoom.getY()+tmp);
			}
			Image img = bufferedScaled;
			getLabel().setIcon(new ImageIcon(img));
			drawGraph(g);
		}
	}
	
	// Methode d'Andres Pour avoir tout les points aux bords d'un cercle
	public ArrayList<Point> getPointWithCenter(int x_centre, int y_centre, double r){
		
		ArrayList<Point> pixels = new ArrayList<>();
	    
		int width = bufferedOriginal.getWidth();
		int height = bufferedOriginal.getHeight();
		
	    double x = 0;
	    double y = r;
	    double d = r - 1;
	    
	    while(y >= x)
	    {
	    	if((x_centre + x > 0 && width > x_centre + x ) && ( y_centre + y > 0 && height > y_centre + y ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre + y )));
	    	}
	    	if((x_centre + y > 0 && width > x_centre + y ) && ( y_centre + x > 0 && height > y_centre + x ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre + x)));
	    	}
	    	if((x_centre - x > 0 && width > x_centre - x ) && ( y_centre + y > 0 && height > y_centre + y ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre + y )));
	    	}
	    	if((x_centre - y > 0 && width > x_centre - y ) && ( y_centre + x > 0 && height > y_centre + x ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre + x )));
	    	}
	    	if((x_centre + x > 0 && width > x_centre  + x ) && ( y_centre - y > 0 && height > y_centre - y ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre - y )));
	    	}
	    	if((x_centre + y > 0 && width > x_centre  + y ) && ( y_centre - x > 0 && height > y_centre - x ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre - x) ));
	    	}
	    	if((x_centre - x > 0 && width > x_centre  - x ) && ( y_centre - y > 0 && height > y_centre - y ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre - y) ));
	    	}
	    	if((x_centre - y > 0 && width > x_centre  - y ) && ( y_centre - x > 0 && height > y_centre - x ) ){
	    		pixels.add( new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre - x) ));
	    	}
	        
	    	// Tant qu'on a pas parcouru tout le cercle on continu
	        if (d >= 2*x){
	            d -= 2*x + 1;
	            x ++;
	        }else if (d < 2 * (r-y)){
	            d += 2*y - 1;
	            y --;
	        }else{
	            d += 2*(y - x - 1);
	            y --;
	            x ++;
	        }
	    }
		return pixels;
	}
	
	//Méthode calculant le centre en fonction de trois points données
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
		centerY = Math.round(-1*(centerX - (A.x+B.x)/2)/aSlope +  (A.y+B.y)/2);
		center.setLocation(new Point(centerX, centerY)); 
		
		return center;
	}
	
	//Calcule la distance entre deux points données 
	public double lenghtFrom2Points(Point A, Point B) {
		return (double) (float)Math.sqrt((A.getX()-B.getX())*(A.getX()-B.getX()) + (A.getY()-B.getY())*(A.getY()-B.getY()));
	}
	
	//Cette méthode  permet de transformer l'image en nuance de gris
		public BufferedImage toGray(BufferedImage image) {
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
	
	// Méthode dessinant le cercle
		private void drawCenteredCircle(Graphics2D g, Point centerCircle, double r) {
			  int x = (int) Math.round(centerCircle.getX()-(r));
			  int y = (int) Math.round(centerCircle.getY()-(r));
			  g.drawOval(x,y,2*(int)r,2*(int)r);
			}

	//Methode pour dessiner le graph
	private void drawGraph(Graphics g) {
		// Parametres graphe

		// Distance entre axe et text
		int distance = 20;

		// Pour le decoupage selon l'image
		double indentationY = (bufferedOriginal.getHeight() / 10);
		double indentationX = (bufferedOriginal.getWidth() / 10);
		int tailleInden = 5;

		// Point En haut a  gauche
		int yZeroX = getLabel().getLocation().x;
		int yZeroY = getLabel().getLocation().y;

		// En Bas a  gauche
		int yFinY = yZeroY + getLabel().getIcon().getIconHeight();

		// En Bas a  droite
		int xFinX = yZeroX + getLabel().getIcon().getIconWidth();

		// Longueur
		double xLength = (xFinX - yZeroX) / indentationX;
		double yLength = (yFinY - yZeroY) / indentationY;

		// Dessin

		// ParamÃƒÂ¨tres

		
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
		for(int i = 0; i < indentationY +1; i=i+15) {
			g2.draw(new Line2D.Double(yZeroX - tailleInden, yZeroY + (i * yLength), yZeroX + tailleInden, yZeroY + (i * yLength)));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*10));
			g2.drawString(Integer.toString(i*10), (yZeroX - distance) - longueurMot /2, yZeroY + (int) (i * (yLength)));

		}

		// Numerotation X

		for(int i = 0; i < indentationX +1; i=i+15) {
			g2.draw(new Line2D.Double(yZeroX + (i * xLength), yFinY - tailleInden, yZeroX + (i * xLength), yFinY + tailleInden));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*10));
			g2.drawString(Integer.toString(i*10), yZeroX + (int) (i * xLength) - longueurMot /2, yFinY + distance);
		}

	}
	
	//Dessine un point sur l'image
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
	
	//Change la luminosite
	public void setBrightness(float scaleFactor){
        RescaleOp op = new RescaleOp(2*scaleFactor, 0, null);
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
		this.currentTool = point;
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

	public BufferedImage getBufferedOriginal() {
		return bufferedOriginal;
	}

	public BufferedImage getBufferedScaled() {
		return bufferedScaled;
	}
	
	public BufferedImage getBufferedScaled2() {
		return bufferedScaled2;
	}

	public BufferedImage getBufferedOriginal2() {
		return bufferedOriginal2;
	}

	public void setBufferedOriginal2(BufferedImage bufferedOriginal2) {
		this.bufferedOriginal2 = bufferedOriginal2;
	}

	public Point getZonezoom() {
		return zonezoom;
	}

	public void setZonezoom(Point zonezoom) {
		this.zonezoom = zonezoom;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}																																							//Morteum and Heywir 2017
