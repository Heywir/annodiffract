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
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * 
 * @author Morteum and Heywir 2017@
 *
 */
public class Panel extends JPanel {
	
	//Variable fenetre
	private final Fenetre f;
	private TypeOutil currentTool = TypeOutil.NORMAL;
	private JLabel label = null;
	
	//Variable qui nous dit si une image � �t� charg�
	private boolean loaded = false;
	
	//Variable pour les images gard� en m�moires
	private Image image = null;
	private BufferedImage bufferedOriginal;
	private Raster raster;
	private BufferedImage bufferedOriginal2;
	private BufferedImage bufferedScaled;
	private BufferedImage bufferedScaled2;
	private String fileName;
	
	//Variable pour les points et cercles
	public Circle tmpCircle = new Circle();
	public final ArrayList<Circle> listeCircle = new ArrayList<>();
	private Point zoneZoom=null;
	
	//Liste Intensit�
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
	public final ArrayList<Point> listePointOnBeamStop = new ArrayList<>();
	
	
	//Variable resolution
	private double resX=0;
	private double resY=0;

	//Variable brightness
	float brightFactor = 1;
	

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
	
	/**
	 * Methode pour ouvrir l'image puis l'afficher avec une bonne dimension
	 * @param file Fichier image
	 * @throws Exception L'exception
	 */
	void openImage(File file) throws Exception {
		try {
			listePointCenter.clear();
			zoneZoom = null;
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
			raster = bufferedOriginal.getData();

			bufferedOriginal2  = new BufferedImage(bufferedOriginal.getWidth(),
			bufferedOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
			
			Graphics g = bufferedOriginal2.createGraphics();
			g.drawImage(bufferedOriginal, 0, 0, null);
			Dimension d = resizeImage();
			Image imageScaled = getImage().getScaledInstance(d.width, -1, Image.SCALE_SMOOTH);

		    //Convert Image to Gray
		    bufferedScaled = toBufferedImage(imageScaled);
		    bufferedScaled2 = toBufferedImage(imageScaled);
		    BufferedImage tGray = toGray(bufferedScaled);
		    toGray(bufferedScaled2);
		    setImage(tGray);
            
		    //Put Image Rezize On Panel
            getLabel().setIcon(new ImageIcon(bufferedScaled));
            setLoaded(true);
            in.close();
            repaint();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  Mets l'image a jour
	 */
	public void setNewImage(){
		Image img = bufferedScaled;
		getLabel().setIcon(new ImageIcon(img));
		repaint();
	}
	
	/**
	 *  Converti une image en BufferedImage
	 * @param img : Image a convertir
	 * @return un BufferedImage
	 */
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
	
	/**
	 * Methode pour charger l'image apres ca recuperation
	 * @param data
	 * @return
	 * @throws Exception
	 */
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isLoaded()) {
			Graphics2D g2d = bufferedScaled.createGraphics();
			g2d.setColor(Color.RED);
			if(!listePointOnBeamStop.isEmpty() && f.getSeeBeamstopPoint()){
				for(int i=0; i<listePointOnBeamStop.size();i++){
					Point p = converToPointScaled(listePointOnBeamStop.get(i).x, listePointOnBeamStop.get(i).y);
					g2d.drawLine(p.x, p.y, p.x, p.y);
				}
			}
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
			drawGraph(g);
			if(zoneZoom!=null){
				int tmp= (int)((getResX()/getBufferedOriginal().getWidth())*125);
				g2d.drawLine((int)zoneZoom.getX()-tmp, (int)zoneZoom.getY()-tmp, (int)zoneZoom.getX()-tmp, (int)zoneZoom.getY()+tmp);
				g2d.drawLine((int)zoneZoom.getX()-tmp, (int)zoneZoom.getY()-tmp, (int)zoneZoom.getX()+tmp, (int)zoneZoom.getY()-tmp);
				g2d.drawLine((int)zoneZoom.getX()+tmp, (int)zoneZoom.getY()-tmp, (int)zoneZoom.getX()+tmp, (int)zoneZoom.getY()+tmp);
				g2d.drawLine((int)zoneZoom.getX()-tmp, (int)zoneZoom.getY()+tmp, (int)zoneZoom.getX()+tmp, (int)zoneZoom.getY()+tmp);
			}
		}
	}

	/**
	 * Remet a zero les listes quand on recalcules les donn�es
	 */
	public void setZeroList(){
		listeMoyen.clear();
		listeSomme.clear();
		listeRayon.clear();
		listeD.clear();
		listeS.clear();
		liste2theta.clear();
		listeMoyenBeam.clear();
		listeSommeBeam.clear();
		listePointCenter.clear();
		listePointOnBeamStop.clear();
	}

	/**
	 *  Methode d'Andres Pour avoir tout les points aux bords d'un cercle
	 * @param x_centre Position x du centre
	 * @param y_centre Position y du centre
	 * @param r Rayon du cercle
	 * @return Une liste de point
	 */
	public ArrayList<Point> getPointWithCenter(int x_centre, int y_centre, double r){

		ArrayList<Point> pixels = new ArrayList<>();
		int width = bufferedOriginal.getWidth();
		int height = bufferedOriginal.getHeight();
		int[] pixelColor = new int[4];
	    int c;
		double x = 0;
	    double y = r;
	    double d = r - 1;
	    while(y >= x){
	    	
	    	if((x_centre + x > 0 && width > x_centre + x ) && ( y_centre + y > 0 && height > y_centre + y ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel((int)Math.round(x_centre + x), (int)Math.round(y_centre + y ), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if(!(c<f.getMinBS()||c>f.getMaxBS())){
						listePointOnBeamStop.add(new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre + y )));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre + y )));
	    	}
	    	if((x_centre + y > 0 && width > x_centre + y ) && ( y_centre + x > 0 && height > y_centre + x ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel((int)Math.round(x_centre + y), (int)Math.round(y_centre + x), pixelColor);
	    			c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add(new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre + x)));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre + x)));
	    	}
	    	if((x_centre - x > 0 && width > x_centre - x ) && ( y_centre + y > 0 && height > y_centre + y ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel((int)Math.round(x_centre - x), (int)Math.round(y_centre + y ), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add(new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre + y ) ));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre + y )));
	    	}
	    	if((x_centre - y > 0 && width > x_centre - y ) && ( y_centre + x > 0 && height > y_centre + x ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel((int)Math.round(x_centre - y), (int)Math.round(y_centre + x ), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add(new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre + x ) ));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre + x )));
	    	}
	    	if((x_centre + x > 0 && width > x_centre  + x ) && ( y_centre - y > 0 && height > y_centre - y ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel( (int)Math.round(x_centre + x), (int)Math.round(y_centre - y ), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add( new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre - y ) ));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre + x), (int)Math.round(y_centre - y )));
	    	}
	    	if((x_centre + y > 0 && width > x_centre  + y ) && ( y_centre - x > 0 && height > y_centre - x ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel( (int)Math.round(x_centre + y), (int)Math.round(y_centre - x), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add(  new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre - x) ));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre + y), (int)Math.round(y_centre - x) ));
	    	}
	    	if((x_centre - x > 0 && width > x_centre  - x ) && ( y_centre - y > 0 && height > y_centre - y ) ){
	    		if(f.getMinBS()!=-1){
	    			raster.getPixel( (int)Math.round(x_centre - x), (int)Math.round(y_centre - y), pixelColor);
					 c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if( !(c<f.getMinBS()||c>f.getMaxBS()) ){
						listePointOnBeamStop.add(  new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre - y) ));
					}
	    		}
	    		pixels.add( new Point( (int)Math.round(x_centre - x), (int)Math.round(y_centre - y) ));
	    	}
	    	if((x_centre - y > 0 && width > x_centre  - y ) && ( y_centre - x > 0 && height > y_centre - x ) ){
		    	if(f.getMinBS()!=-1){
	    			raster.getPixel( (int)Math.round(x_centre - y), (int)Math.round(y_centre - x), pixelColor);
					c = pixelColor[0]+pixelColor[1]+pixelColor[2]+pixelColor[3];
					if(!(c<f.getMinBS()||c>f.getMaxBS())){
						listePointOnBeamStop.add(  new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre - x) ));
					}
		    	}
	    		pixels.add( new Point( (int)Math.round(x_centre - y), (int)Math.round(y_centre - x) ));
	    	}
	        
	    	// Tant que la valeur x n'est pas plus grande que y on continu le parcours
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
	    //repaint();
	    setNewImage();
		return pixels;
	}

	public Point converToPointScaled( int x, int y){
		x = (int)Math.round((resX/bufferedOriginal.getWidth())*x);
		y = (int)Math.round((resY/bufferedOriginal.getHeight())*y);
		
		return new Point(x,y);
	}
	
	/**
	 * M�thode calculant le centre en fonction de trois points donn�es
	 * @param A Point A
	 * @param B Point B
	 * @param C Point C
	 * @return Retourne le centre
	 */
	//Source equation https://www.quora.com/Given-3-points-in-the-Cartesian-plane-how-can-you-find-the-coordinates-of-the-center-of-the-circle-that-intersects-all-three-points-if-there-exists-such-a-circle
	public Point circleCenter(Point A, Point B, Point C) { 
		int centerX;
		int centerY;
		double d=(A.x - B.x)*(B.y - C.y) - (B.x-C.x)*(A.y-B.y);
		double u = (A.x*A.x - B.x*B.x +A.y*A.y- B.y*B.y)/(double)2; 
		double v = (B.x*B.x - C.x*C.x +B.y*B.y- C.y*C.y)/(double)2;
		centerX = (int)Math.round((u*(B.y-C.y)-v*(A.y-B.y))/d);
		centerY = (int)Math.round((v*(A.x-B.x)-u*(B.x-C.x))/d);
		return new Point(centerX,centerY);
	}
	
	/**
	 * Calcule la distance entre deux points donn�es 
	 * @param A Point A
	 * @param B Point B
	 * @return retourne la distance en Double
	 */
	public double lenghtFrom2Points(Point A, Point B) {
		return (double) (float)Math.sqrt((A.getX()-B.getX())*(A.getX()-B.getX()) + (A.getY()-B.getY())*(A.getY()-B.getY()));
	}
	
	/**
	 * Cette m�thode  permet de transformer l'image en nuance de gris
	 * @param image
	 * @return L'image en Nuance de Gris 
	 */
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
	
	/**
	 *  M�thode dessinant le cercle
	 * @param g La variable de dessin 
	 * @param centerCircle Centre du cercle
	 * @param r Rayon du cercle
	 */
		private void drawCenteredCircle(Graphics2D g, Point centerCircle, double r) {
			  int x = (int) Math.round(centerCircle.getX()-(r));
			  int y = (int) Math.round(centerCircle.getY()-(r));
			  g.drawOval(x,y,2*(int)r,2*(int)r);
			}

	//Methode pour dessiner le graph
	private void drawGraph(Graphics g) {
		// Parametres graphe

		// Distance entre axe et text
		int distanceX = 20;
		int distanceY = 8;

		// Pour le decoupage selon l'image
		double indentationY = (bufferedOriginal.getHeight() / 11);
		double indentationX = (bufferedOriginal.getWidth() / 11);

		// Taille des traits d'indentation
		int tailleInden = 5;

		// Point En haut a� gauche
		int yZeroX = getLabel().getLocation().x;
		int yZeroY = getLabel().getLocation().y;

		// En Bas a� gauche
		int yFinY = yZeroY + getLabel().getIcon().getIconHeight();

		// En Bas a� droite
		int xFinX = yZeroX + getLabel().getIcon().getIconWidth();

		// Longueur
		double xLength = (xFinX - yZeroX) / indentationX;
		double yLength = (yFinY - yZeroY) / indentationY;

		// Dessin

		// ParamÃ¨tres
		Graphics2D g2 = (Graphics2D) g;
		// Epaisseur
		g2.setStroke(new BasicStroke(1));
		// Couleur
		g2.setColor(Color.BLACK);

		// Y Axis
		// Ligne
		g2.drawLine(yZeroX -1, yZeroY, yZeroX -1, yFinY);
		// Texte
		g2.drawString("Y", yZeroX - distanceX - 2, yZeroY - distanceY - 4);

		// X Axis
		// Ligne
		g2.drawLine(yZeroX, yZeroY-1,xFinX, yZeroY-1);
		// Texte
		g2.drawString("X", yZeroX - 2, yZeroY - distanceY - 12);


	    // Numerotation Y

		int longueurMot;
		for(int i = 0; i < indentationY +1; i=i+15) {
			g2.draw(new Line2D.Double(yZeroX - tailleInden, yZeroY + (i * yLength), yZeroX + tailleInden, yZeroY + (i * yLength)));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*10));
			g2.drawString(Integer.toString(i*10), (yZeroX - distanceX) - longueurMot /2, yZeroY + (int) (i * (yLength)));

		}

		// Numerotation X

		for(int i = 0; i < indentationX +1; i=i+15) {
			g2.draw(new Line2D.Double(yZeroX + (i * xLength), yZeroY - tailleInden, yZeroX + (i * xLength), yZeroY + tailleInden));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*10));
			g2.drawString(Integer.toString(i*10), yZeroX + (int) (i * xLength) - longueurMot /2, yZeroY - distanceY);
		}

	}
	
	/**
	 * Dessine un point sur l'image
	 * @param g2d Variable pour dessiner
	 * @param e Le point en question
	 */
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
	
	/**
	 * Change la luminosite
	 * @param scaleFactor Facteur de luminositer
	 */
	public void setBrightness(float scaleFactor){
		brightFactor = scaleFactor;
        RescaleOp op = new RescaleOp(scaleFactor/15, 0, null);
        bufferedScaled = op.filter(bufferedScaled2, bufferedScaled);
        toGray(bufferedScaled);
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

	public void setBright(float e){
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

	public void setZoneZoom(Point zonezoom) {
		this.zoneZoom = zonezoom;
	}

	public String getFileName() {
		return fileName;
	}

	public Raster getRaster() {
		return raster;
	}

}																																							//Morteum and Heywir 2017
 