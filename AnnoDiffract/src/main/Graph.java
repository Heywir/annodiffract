package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
 
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * 
 * @author Morteum and Heywir 2017@
 *
 */
class Graph extends JFrame implements ChartMouseListener, ActionListener{

	private final Fenetre f;
	private XYSeries XY;
	private final String chartTitle;
	private final ChartPanel chartPanel;
	private JMenuItem save = null;
	private JMenuItem setRayon = null;
	private JMenuItem setMoy = null;
	private JMenuItem setSum = null;
	private JMenuItem setS = null;
	private JMenuItem set2theta = null;
	private JMenuItem setBeam = null;
	private JMenuItem setNoBeam = null;
	private JMenuItem clearAnnotation = null;
	private JFreeChart xylineChartS = null;
	private JFreeChart xylineChartRayon = null;
	private JFreeChart xylineChart2theta = null;
	private final JLabel statusLabel;
	private XYPlot plot;
	private Boolean yMoy=true;
	/**
	 * Creer l'object Graph
	 * @param f Necessite un objet fenetre pour fonctionner
	 */
	public Graph(Fenetre f) {
		//Construction du Graphique
		super("Graphic");
		this.chartTitle = "Intensity Profile";
		// Layout Fenetre
     	BorderLayout layout = new BorderLayout();
     	this.setLayout(layout);
        
     	this.f=f;
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        withoutBeam();
        chartPanel = new ChartPanel(xylineChartRayon);
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        
        //Construction fenetre
		JMenuBar menu = new JMenuBar();
      	menu.setBorder(null);
      	JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel();

      	// Layout
      	BorderLayout barLayout = new BorderLayout();
     	this.setLayout(barLayout);

     	// Menus
     	JMenu menuFile = new JMenu("File");
     	JMenu menuAbsc = new JMenu("X-Axis");
     	JMenu menuAxiY = new JMenu("Y-Axis");
     	JMenu menuBeam = new JMenu("BeamStop");
     	JMenu menuAnnotation = new JMenu("Annotaion");
     	save= new JMenuItem("Save");
     	setRayon = new JMenuItem("Radius in Meters");
     	setS = new JMenuItem("Distance Vector");
     	set2theta = new JMenuItem("Diffraction Angle 2 theta");
     	setMoy = new JMenuItem("Set Average Intensity");
     	setSum = new JMenuItem("Set Sum Intensity");
     	setBeam = new JMenuItem("Beamstop Correction");
     	setNoBeam = new JMenuItem("Without Beamstop Correction");
     	clearAnnotation = new JMenuItem("Clear Annotation");
     	
     	// Status Bar
     	statusPanel.add(statusLabel, BorderLayout.EAST);
     	
     	menuFile.add(save);
     	menuAbsc.add(setRayon);
     	menuAbsc.add(setS);
     	menuAbsc.add(set2theta);
     	menuAxiY.add(setMoy);
     	menuAxiY.add(setSum);
     	menuBeam.add(setBeam);
     	menuBeam.add(setNoBeam);
     	menuAnnotation.add(clearAnnotation);
     	
     	//Listeners
     	save.addActionListener(this);
        chartPanel.addChartMouseListener(this);
        setRayon.addActionListener(this);
        setS.addActionListener(this);
        setMoy.addActionListener(this);
        setSum.addActionListener(this);
        set2theta.addActionListener(this);
        setBeam.addActionListener(this);
        setNoBeam.addActionListener(this);
        clearAnnotation.addActionListener(this);
        
     	//Ajout dans la fenetre
        menu.add(menuFile);
        menu.add(menuAbsc);
     	menu.add(menuAxiY);
     	menu.add(menuBeam);
     	menu.add(menuAnnotation);
     	this.add(menu,BorderLayout.NORTH);
     	this.add(chartPanel, BorderLayout.CENTER);
     	this.add(statusPanel, BorderLayout.SOUTH);
     	
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    private XYDataset createDataset(ArrayList<Double> x, ArrayList<Double> y ) {

    	//Creer une serie pour le graphe
        XY = new XYSeries( "Intensity Profile" );
        for (int i=0; i<x.size();i++){
	        XY.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(XY);
        
        return dataset;
    }
    
    /**
     *  Regle les options graphique du graphique
     */
	private void graphicOption(){
		
		//Viseur, couleur et �paisseur
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesPaint( 1 , Color.RED );
        renderer.setSeriesOutlineStroke(0, new BasicStroke(0.1f));
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        renderer.setBaseShapesVisible(false);
        plot = xylineChartRayon.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true); 
        plot.setRenderer( renderer );
        plot = xylineChartS.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);      
        plot.setRenderer( renderer );
        plot = xylineChart2theta.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);      
        plot.setRenderer( renderer );
        plot = xylineChartRayon.getXYPlot();
	}

	@Override
	/**
	 * Reagit au clique de la souris
	 */
	public void chartMouseClicked(ChartMouseEvent e) {
		//Ajoute une annotation sur le graphe
		ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
            XYItemEntity e1 = (XYItemEntity) ce;
            XYDataset d = e1.getDataset();
            int s = e1.getSeriesIndex();
            int i = e1.getItem();
            XYPointerAnnotation h = new XYPointerAnnotation(((int)(100*f.getMainPanel2().listeD.get(i))/100.)+" A",(double)d.getX(s, i), (double)d.getY(s, i),4.75);
			plot.addAnnotation(h);
        }
		
	}

	@Override
	/**
	 * Reagit au mouvement de la souris
	 */
	public void chartMouseMoved(ChartMouseEvent e) {
		ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
        	//Affiche les X et Y du graphe lorsque la souris bouge
            XYItemEntity e1 = (XYItemEntity) ce;
            XYDataset d = e1.getDataset();
            int s = e1.getSeriesIndex();
            int i = e1.getItem();
            statusLabel.setText("X: " + d.getX(s, i) + "           Y: " + d.getY(s, i));
        }		
	}
	

	
	@Override
	/**
	 * Action sur les boutons
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == setS){
			chartPanel.setChart(xylineChartS);
			plot = xylineChartS.getXYPlot();
		}
		if(e.getSource() == setRayon){
			chartPanel.setChart(xylineChartRayon);
			plot = xylineChartRayon.getXYPlot();
		}
		if(e.getSource() == set2theta){
			chartPanel.setChart(xylineChart2theta);
			plot = xylineChart2theta.getXYPlot();
		}
		if(e.getSource() == setMoy){
			yMoy = true;
			withoutBeam();
			chartPanel.setChart(xylineChartRayon);
		}
		if(e.getSource() == setSum){
			yMoy = false;
			withoutBeam();
			chartPanel.setChart(xylineChartRayon);
		}
		
		if(e.getSource() == setBeam){
			if(f.getMaxBS()!=-1){
				withBeam();
				graphicOption();
				chartPanel.setChart(xylineChartRayon);
			}
		}
		if(e.getSource() == setNoBeam){
			withoutBeam();
			graphicOption();
			chartPanel.setChart(xylineChartRayon);
		}
		
		if(e.getSource() == clearAnnotation){
			plot.clearAnnotations();
		}
		if(e.getSource() == save){
		    String destinationFile ;
			JFileChooser chooser = new JFileChooser(); 
		    chooser.setDialogTitle("Save");
		    FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("Excel File(.csv)","csv");
			chooser.addChoosableFileFilter(filterCSV);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File(.txt)","txt");
			chooser.addChoosableFileFilter(filter);
			chooser.setSelectedFile(new File(f.getMainPanel2().getFileName()));
		    chooser.setFileFilter(filter);
		    chooser.setApproveButtonText("Save");
		    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
		        if(filter.equals(chooser.getFileFilter())){
			    	destinationFile = chooser.getSelectedFile().getAbsolutePath()+".txt";
			    	convertAndPrint(destinationFile);
			    }
			    if(filterCSV.equals(chooser.getFileFilter())){
			    	destinationFile = chooser.getSelectedFile().getAbsolutePath()+".csv";
			    	convertAndPrint(destinationFile);
			    }
		    }
		}
	}
	
	 private void convertAndPrint(String destinationCSVFile) {
	        StringBuilder commaSeparatedValues = new StringBuilder();
	        double som = 0 ;
	        for (int i =0;i<f.getMainPanel2().listeMoyenBeam.size();i++){
	        	som = som+f.getMainPanel2().listeMoyenBeam.get(i);
	        }
	        
	        if (f.getMainPanel2().listeMoyenBeam.isEmpty()) {
	            /** Sort the list if sortTheList was passed as true**/
				/**Iterate through the list and append comma after each values**/
	            Iterator<Double> iterD = f.getMainPanel2().listeD.iterator();
	            Iterator<Double> iterS = f.getMainPanel2().listeS.iterator();
	            Iterator<Double> iter2theta = f.getMainPanel2().liste2theta.iterator();
	            Iterator<Double> iterInt = f.getMainPanel2().listeMoyen.iterator();
	            Iterator<Double> iterIntSum = f.getMainPanel2().listeSomme.iterator();
	            commaSeparatedValues.append("Settings\r\nPPI; ").append(f.getP()).append("; Microscope Tension:; ").append(f.getV()).append("; Camera Lenght:; ").append(f.getL()).append(";\r\n\r\n");
	            int i = 0;
	            commaSeparatedValues.append("Interarticular Distance d; Scattering Vector S; Diffraction Angle 2 theta; Average Intensity Without BeamStop Correction; Sum Intensity Without BeamStop Correction;\r\n");
	            while (iterD.hasNext()) {
	                commaSeparatedValues.append(iterD.next()).append("; ").append(iterS.next()).append("; ").append(iter2theta.next()).append("; ").append(iterInt.next()).append(" ;").append(iterIntSum.next()).append(";\r\n");
	                i++;
	                if(i>5){
	                	commaSeparatedValues.append("Interarticular Distance d; Scattering Vector S; Diffraction Angle 2 theta; Average Intensity Without BeamStop Correction; Sum Intensity Without BeamStop Correction;\r\n");
	                	i=0;
	                }
	            }
	            /**Remove the last comma**/
	            if (commaSeparatedValues.toString().endsWith(",")) {
	                commaSeparatedValues = new StringBuilder(commaSeparatedValues.substring(0,
							commaSeparatedValues.lastIndexOf(",")));
	            }
	        }else{
				/**Iterate through the list and append comma after each values**/
	            Iterator<Double> iterD = f.getMainPanel2().listeD.iterator();
	            Iterator<Double> iterS = f.getMainPanel2().listeS.iterator();
	            Iterator<Double> iter2theta = f.getMainPanel2().liste2theta.iterator();
	            Iterator<Double> iterInt = f.getMainPanel2().listeMoyen.iterator();
	            Iterator<Double> iterIntBeam = f.getMainPanel2().listeMoyenBeam.iterator();
	            Iterator<Double> iterIntSum = f.getMainPanel2().listeSomme.iterator();
	            Iterator<Double> iterIntSumBeam = f.getMainPanel2().listeSommeBeam.iterator();
	            commaSeparatedValues.append("Settings\r\nPPI; ").append(f.getP()).append("; Microscope Tension:; ").append(f.getV()).append("; Camera Lenght:; ").append(f.getL()).append(";\r\n\r\n");
	            commaSeparatedValues.append("Interarticular Distance d; Scattering Vector S; Diffraction Angle 2 theta;Average Intensity  (u. a.) Without BeamStop Correction;Average Intensity  (u. a.) With BeamStop correction;Sum Intensity  (u. a.) Without BeamStop Correction;Sum Intensity  (u. a.) With BeamStop correction; \r\n");
	            int i =0;
	            while (iterD.hasNext()) {
	                commaSeparatedValues.append(iterD.next()).append("; ").append(iterS.next()).append("; ").append(iter2theta.next()).append("; ").append(iterInt.next()).append(";").append(iterIntBeam.next()).append(";").append(iterIntSum.next()).append(";").append(iterIntSumBeam.next()).append(";\r\n");
	                i++;
	                if(i>5){
	                	i=0;
	                	commaSeparatedValues.append("Interarticular Distance d; Scattering Vector S; Diffraction Angle 2 theta;Average Intensity  (u. a.) Without BeamStop Correction;Average Intensity  (u. a.) With BeamStop correction;Sum Intensity  (u. a.) Without BeamStop Correction;Sum Intensity  (u. a.) With BeamStop correction; \r\n");
	                }
	            }
	            /**Remove the last comma**/
	            if (commaSeparatedValues.toString().endsWith(",")) {
	                commaSeparatedValues = new StringBuilder(commaSeparatedValues.substring(0,
							commaSeparatedValues.lastIndexOf(",")));
	            }
	        }
	        /** If writeToConsole flag was passed as true, output to console**/
		 /** If writeToFile flag was passed as true, output to File**/
		 try {
             File f = new File(destinationCSVFile);
             FileWriter fstream = new FileWriter(f, false);
             BufferedWriter out = new BufferedWriter(fstream);
             out.write(commaSeparatedValues.toString());
             out.close();
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Error Encounter. "
                     + "Afile of the same name is used, If you want to save close the software which use the file or"
                     + " change the name of the file you want to save"
                     , "Error", JOptionPane.ERROR_MESSAGE);
         }

	 }
	 //Methode qui va mettre a jour les series avec la correction Beamstop
	 private void withBeam(){
		XYSeriesCollection dataset = new XYSeriesCollection();
		XY = new XYSeries( "Without BeamStop Correction" );
		if(yMoy){
			for (int i=0; i<f.getMainPanel2().listeRayon.size();i++){
		        XY.add(f.getMainPanel2().listeRayon.get(i), f.getMainPanel2().listeMoyen.get(i));
		    }
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeRayon.size();i++){
		        XY.add(f.getMainPanel2().listeRayon.get(i), f.getMainPanel2().listeMoyenBeam.get(i));
		    }
			dataset.addSeries(XY);
			xylineChartRayon = ChartFactory.createXYLineChart(
		             chartTitle ,
		             "Radius" ,
		             "Intensity  (u. a.)" ,
		             dataset,
		             PlotOrientation.VERTICAL ,
		             true , true , false);
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Without BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeS.size();i++){
		        XY.add(f.getMainPanel2().listeS.get(i), f.getMainPanel2().listeMoyen.get(i));
		    }
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeS.size();i++){
		        XY.add(f.getMainPanel2().listeS.get(i), f.getMainPanel2().listeMoyenBeam.get(i));
		    }
			dataset.addSeries(XY);
			xylineChartS = ChartFactory.createXYLineChart(
		           chartTitle ,
		           "Scattering Vector (1/A)" ,
		           "Intensity  (u. a.)" ,
		           dataset,
		           PlotOrientation.VERTICAL ,
		           true , true , false);
			
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Without BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().liste2theta.size();i++){
		        XY.add(f.getMainPanel2().liste2theta.get(i), f.getMainPanel2().listeMoyen.get(i));
		    }
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );			
			for (int i=0; i<f.getMainPanel2().liste2theta.size();i++){
		        XY.add(f.getMainPanel2().liste2theta.get(i), f.getMainPanel2().listeMoyenBeam.get(i));
		    }
			dataset.addSeries(XY);
			xylineChart2theta = ChartFactory.createXYLineChart(
		                chartTitle ,
		                "Diffraction Angle 2 theta" ,
		                "Intensity  (u. a.)" ,
		                dataset,
		                PlotOrientation.VERTICAL ,
		                true , true , false);
		}else{
			for (int i=0; i<f.getMainPanel2().listeRayon.size();i++){
		        XY.add(f.getMainPanel2().listeRayon.get(i), f.getMainPanel2().listeSomme.get(i));
		    }
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeRayon.size();i++){
		        XY.add(f.getMainPanel2().listeRayon.get(i), f.getMainPanel2().listeSommeBeam.get(i));
		    }
			dataset.addSeries(XY);
			xylineChartRayon = ChartFactory.createXYLineChart(
		                chartTitle ,
		                "Radius" ,
		                "Intensity  (u. a.)" ,
		                dataset,
		                PlotOrientation.VERTICAL ,
		                true , true , false);
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Without BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeS.size();i++){
		        XY.add(f.getMainPanel2().listeS.get(i), f.getMainPanel2().listeSomme.get(i));
		    }
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().listeS.size();i++){
		        XY.add(f.getMainPanel2().listeS.get(i), f.getMainPanel2().listeSommeBeam.get(i));
		    }			
			dataset.addSeries(XY);
			xylineChartS = ChartFactory.createXYLineChart(
		                chartTitle ,
		                "Diffusion Vector" ,
		                "Intensity  (u. a.)" ,
		                dataset,
		                PlotOrientation.VERTICAL ,
		                true , true , false);
				
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Without BeamStop Correction" );
			for (int i=0; i<f.getMainPanel2().liste2theta.size();i++){
			     XY.add(f.getMainPanel2().liste2theta.get(i), f.getMainPanel2().listeSomme.get(i));
		    }			
			dataset.addSeries(XY);
			XY = new XYSeries( "BeamStop Correction" );			
			for (int i=0; i<f.getMainPanel2().liste2theta.size();i++){
			      XY.add(f.getMainPanel2().liste2theta.get(i), f.getMainPanel2().listeSommeBeam.get(i));
		    }
			dataset.addSeries(XY);
			xylineChart2theta = ChartFactory.createXYLineChart(
		                chartTitle ,
		                "Diffraction Angle 2 theta" ,
		                "Intensity  (u. a.)" ,
		                dataset,
		                PlotOrientation.VERTICAL ,
		                true , true , false);
		}
	 }
	 	//Methode qui met a jour les chartes sans la correction Beamstop
	private void withoutBeam(){
        //On creer les Charts que l'utilisateur pourra afficher s'il le souhaite
	    if(yMoy){
			xylineChartRayon = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Radius" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().listeRayon, f.getMainPanel2().listeMoyen),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	        xylineChartS = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Scattering Vector S (1/A)" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().listeS, f.getMainPanel2().listeMoyen),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	        xylineChart2theta = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Diffraction Angle 2theta" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().liste2theta, f.getMainPanel2().listeMoyen),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	    }else{
	    	xylineChartRayon = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Radius" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().listeRayon, f.getMainPanel2().listeSomme),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	        xylineChartS = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Scattering Vector S (1/A)" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().listeS, f.getMainPanel2().listeSomme),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	        xylineChart2theta = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Diffraction Angle 2theta" ,
	                "Intensity  (u. a.)" ,
	                createDataset(f.getMainPanel2().liste2theta, f.getMainPanel2().listeSomme),
	                PlotOrientation.VERTICAL ,
	                true , true , false);
	   }
        graphicOption();
	}

}																																						////Morteum and Heywir 2017