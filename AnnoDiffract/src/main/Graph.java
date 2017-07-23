package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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


class Graph extends JFrame implements ChartMouseListener, ActionListener{
	private XYSeries XY;
	private final String chartTitle;
	private final ChartPanel chartPanel;
	private final ArrayList<Double> Intensity;
	private final ArrayList<Double> IntensityBeam;
	private final ArrayList<Double> ListeRayon;
	private final ArrayList<Double> ListeD;
	private final ArrayList<Double> ListeS;
	private final ArrayList<Double> Liste2theta;
	private JMenuItem setRayon = null;
	private JMenuItem setS = null;
	private JMenuItem set2theta = null;
	private JMenuItem setBeam = null;
	private JMenuItem setNoBeam = null;
	private JFreeChart xylineChartS = null;
	private JFreeChart xylineChartRayon = null;
	private JFreeChart xylineChart2theta = null;
	
	public Graph(ArrayList<Double> Intensity,
				 ArrayList<Double> ListeRayon, ArrayList<Double> ListeD,
				 ArrayList<Double> ListeS, ArrayList<Double> Liste2theta,
				 ArrayList<Double> IntensityBeam) {
		//Construction du Graphique
		super("Graphique");
		this.chartTitle = "Profile IntensitÃ©";
        
		// Layout Fenetre
     	BorderLayout layout = new BorderLayout();
     	this.setLayout(layout);
        
		
		this.Intensity = Intensity;
		this.IntensityBeam = IntensityBeam;
        this.ListeD = ListeD;
        this.ListeRayon = ListeRayon;
        this.ListeS = ListeS;
        this.Liste2theta = Liste2theta;
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        withoutBeam();
        chartPanel = new ChartPanel(xylineChartRayon);
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        XYPlot plot = xylineChartRayon.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);        
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesOutlineStroke(0, new BasicStroke(0.1f));
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        renderer.setBaseShapesVisible(false);
        plot.setRenderer( renderer );

        plot = xylineChartS.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);      
        plot.setRenderer( renderer );
        
        plot = xylineChart2theta.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);      
        plot.setRenderer( renderer );
        
    	
        //Construction fenetre
		JMenuBar menu = new JMenuBar();
      	menu.setBorder(null);

      	// Layout
      	BorderLayout barLayout = new BorderLayout();
     	this.setLayout(barLayout);

     	// Menus
     	JMenu menuFile = new JMenu("Abscisse");
     	JMenu menuBeam = new JMenu("BeamStop");
     	setRayon = new JMenuItem("Rayon en Metre");
     	setS = new JMenuItem("Vecteur de Distance S");
     	set2theta = new JMenuItem("Angle de Diffraction 2 theta");
     	setBeam = new JMenuItem("Correction Beamstop");
     	setNoBeam = new JMenuItem("Sans correction Beamstop");
     	
     	
     	menuFile.add(setRayon);
     	menuFile.add(setS);
     	menuFile.add(set2theta);
     	menuBeam.add(setBeam);
     	menuBeam.add(setNoBeam);
     	
     	//Listeners
        chartPanel.addChartMouseListener(this);
        setRayon.addActionListener(this);
        setS.addActionListener(this);
        set2theta.addActionListener(this);
        setBeam.addActionListener(this);
        setNoBeam.addActionListener(this);
        
     	//Ajout ï¿½ la fenetre
     	menu.add(menuFile);
     	menu.add(menuBeam);
     	this.add(menu,BorderLayout.NORTH);
     	this.add(chartPanel, BorderLayout.CENTER);
     	
	}

    private XYDataset createDataset(ArrayList<Double> x, ArrayList<Double> y ) {

        XY = new XYSeries( "Moyenne et Intensitï¿½" );
        for (int i=0; i<x.size();i++){
	        XY.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(XY);
        
        return dataset;
    }

	private void report(ChartMouseEvent e) {
        ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
            XYItemEntity e1 = (XYItemEntity) ce;
            XYDataset d = e1.getDataset();
            //int s = e1.getSeriesIndex();
            int i = e1.getItem();
            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
            System.out.println(ListeD.get(i));
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == setS){
			chartPanel.setChart(xylineChartS);
		}
		if(e.getSource() == setRayon){
			chartPanel.setChart(xylineChartRayon);
		}
		if(e.getSource() == set2theta){
			chartPanel.setChart(xylineChart2theta);
		}
		if(e.getSource() == setBeam){
			XYSeriesCollection dataset = new XYSeriesCollection();
			XY = new XYSeries( "Moyenne et Intensitï¿½" );
			for (int i=0; i<ListeRayon.size();i++){
		        XY.add(ListeRayon.get(i), IntensityBeam.get(i));
	        }
			dataset.addSeries(XY);
			XY = new XYSeries( "Moyenne et Intensitï¿½" );
			for (int i=0; i<ListeRayon.size();i++){
		        XY.add(ListeRayon.get(i), Intensity.get(i));
	        }
			dataset.addSeries(XY);
			xylineChartRayon = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Rayon" ,
	                "Intensitï¿½" ,
	                dataset,
	                PlotOrientation.VERTICAL ,
	                true , true , false);
			chartPanel.setChart(xylineChartRayon);
			
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Avec Correction BeamStop" );
			for (int i=0; i<ListeS.size();i++){
		        XY.add(ListeS.get(i), IntensityBeam.get(i));
	        }
			dataset.addSeries(XY);
			XY = new XYSeries( "Sans Correction Beamstop" );
			for (int i=0; i<ListeRayon.size();i++){
		        XY.add(ListeS.get(i), Intensity.get(i));
	        }			
			dataset.addSeries(XY);
			xylineChartS = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Vecteur de Diffraction" ,
	                "Intensité" ,
	                dataset,
	                PlotOrientation.VERTICAL ,
	                true , true , false);
			
			dataset = new XYSeriesCollection();
			XY = new XYSeries( "Avec Correction BeamStop" );
			for (int i=0; i<ListeS.size();i++){
		        XY.add(Liste2theta.get(i), IntensityBeam.get(i));
	        }
			dataset.addSeries(XY);
			XY = new XYSeries( "Sans Correction Beamstop" );			
			for (int i=0; i<ListeRayon.size();i++){
		        XY.add(Liste2theta.get(i), Intensity.get(i));
	        }
			dataset.addSeries(XY);
			xylineChart2theta = ChartFactory.createXYLineChart(
	                chartTitle ,
	                "Angle de diffraction 2 theta" ,
	                "Intensitï¿½" ,
	                dataset,
	                PlotOrientation.VERTICAL ,
	                true , true , false);

		}
		if(e.getSource() == setNoBeam){
			withoutBeam();
			chartPanel.setChart(xylineChartRayon);
		}
	}

	private void withoutBeam(){
        //On creer les Charts que l'utilisateur pourra afficher s'il le souhaite
        xylineChartRayon = ChartFactory.createXYLineChart(
                chartTitle ,
                "Rayon" ,
                "intensitï¿½" ,
                createDataset(ListeRayon, Intensity),
                PlotOrientation.VERTICAL ,
                true , true , false);
        xylineChartS = ChartFactory.createXYLineChart(
                chartTitle ,
                "Vecteur de distance S" ,
                "intensitï¿½" ,
                createDataset(ListeS, Intensity),
                PlotOrientation.VERTICAL ,
                true , true , false);
        xylineChart2theta = ChartFactory.createXYLineChart(
                chartTitle ,
                "Angle de Diffraction 2theta" ,
                "intensitï¿½" ,
                createDataset(Liste2theta, Intensity),
                PlotOrientation.VERTICAL ,
                true , true , false);
	}
	

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent e) {
		report(e);
		
	}

}