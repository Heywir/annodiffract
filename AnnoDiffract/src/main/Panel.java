package main;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
	public final ArrayList<Point> listePoint = new ArrayList<>();
	public final ArrayList<Point> resPoint = new ArrayList<>();
	

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
			Image img = bufferedScaled.getScaledInstance((this.getWidth()/100)*ratioX, ((this.getHeight()/100)*ratioY),  Image.SCALE_SMOOTH);
			getLabel().setIcon(new ImageIcon(img));
			if(!listePoint.isEmpty()){
				for(int i= 0 ; i < listePoint.size(); i++ ){
					System.out.println(getLabel().getWidth()+"/"+resPoint.get(i).getX()+"*"+listePoint.get(i).getX()+","+ getLabel().getHeight()+"/"+resPoint.get(i).getY()+"*"+listePoint.get(i).getY());
					listePoint.get(i).setLocation(((getLabel().getWidth()/resPoint.get(i).getX())*listePoint.get(i).getX()), ((getLabel().getHeight()/resPoint.get(i).getY())*listePoint.get(i).getY()));
					System.out.println(listePoint.get(i).getX()+" "+listePoint.get(i).getY());
					resPoint.get(i).setLocation(getLabel().getWidth(), getLabel().getHeight());
				}
			}
			repaint();
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

	public void paintComponent(Graphics g) {
		int x1,y1,x2,y2;
		super.paintComponent(g);
		if (isLoaded()) {
			if(!listePoint.isEmpty()){
				for (Point aListePoint : listePoint) {
					//System.out.println(listePoint.get(i).getX()+" "+listePoint.get(i).getY());
					Graphics2D g2d = bufferedScaled.createGraphics();
					x1 = (int) Math.round(aListePoint.getX() - 3);
					y1 = (int) Math.round(aListePoint.getY());
					x2 = (int) Math.round(aListePoint.getX());
					y2 = (int) Math.round(aListePoint.getY());
					g2d.drawLine(x1, y1, x2, y2);
					x1 = (int) Math.round(aListePoint.getX());
					y1 = (int) Math.round(aListePoint.getY() - 3);
					x2 = (int) Math.round(aListePoint.getX());
					y2 = (int) Math.round(aListePoint.getY());
					g2d.drawLine(x1, y1, x2, y2);
					x1 = (int) Math.round(aListePoint.getX());
					y1 = (int) Math.round(aListePoint.getY());
					x2 = (int) Math.round(aListePoint.getX() + 3);
					y2 = (int) Math.round(aListePoint.getY());
					g2d.drawLine(x1, y1, x2, y2);
					x1 = (int) Math.round(aListePoint.getX());
					y1 = (int) Math.round(aListePoint.getY());
					x2 = (int) Math.round(aListePoint.getX());
					y2 = (int) Math.round(aListePoint.getY() + 3);
					g2d.drawLine(x1, y1, x2, y2);
					g2d.dispose();
				}
			}
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
		int indentationY = (getLabel().getHeight() / 100);
		int indentationX = (getLabel().getWidth() / 100);
		int tailleInden = 5;

		// Point En haut Ã  gauche
		int yZeroX = getLabel().getLocation().x;
		int yZeroY = getLabel().getLocation().y;

		// En Bas Ã  gauche
		int yFinY = yZeroY + getLabel().getIcon().getIconHeight();

		// En Bas Ã  droite
		int xFinX = yZeroX + getLabel().getIcon().getIconWidth();

		// Longueur

		int xLength = (xFinX - yZeroX) / indentationX;
		int yLength = (yFinY - yZeroY) / indentationY;

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
			g2.drawLine(yZeroX - tailleInden, yZeroY + (i * yLength), yZeroX + tailleInden, yZeroY + (i * yLength));
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), (yZeroX - distance) - longueurMot /2, yZeroY + (i * yLength));

		}

		// Numerotation X

		for(int i = 0; i < indentationX +1; i++) {
			g2.drawLine(yZeroX + (i * xLength), yFinY - tailleInden, yZeroX + (i * xLength), yFinY + tailleInden);
			FontMetrics fm = getFontMetrics(getFont());
			longueurMot = fm.stringWidth(Integer.toString(i*100));
			g2.drawString(Integer.toString(i*100), yZeroX + (i * xLength) - longueurMot /2, yFinY + distance);
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

	public void setCurrentTool() {
		this.currentTool = TypeOutil.POINT;
	}

	public ArrayList<Point> getListePoint() {
		return listePoint;
	}
	
}
