package main;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

import javax.swing.JFrame;

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

import javafx.scene.chart.NumberAxis;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class Graph extends JFrame implements ChartMouseListener{
	public XYSeries XY;
	private XYSeriesCollection dataset;
	private ChartPanel chartPanel;
    
	public Graph(String title, String chartTitle, ArrayList<Double> Intensity, ArrayList<Double> ListeRayon, ArrayList<Double> ListeD, ArrayList<Double> ListeS) {
        super(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Rayon" ,
                "intensité" ,
                createDataset(ListeRayon, Intensity) ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);        
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesOutlineStroke(0, new BasicStroke(0.1f));
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        renderer.setBaseShapesVisible(false);
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
        chartPanel.addChartMouseListener(this);
    }

    private XYDataset createDataset(ArrayList<Double> x, ArrayList<Double> y ) {

        XY = new XYSeries( "Moyenne et IntensitÃ©" );
        System.out.println(x.size());
        for (int i=0; i<x.size();i++){
	        XY.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(XY);
        
        return dataset;
    }

	public XYSeriesCollection getDataset() {
		return dataset;
	}

	public void setDataset(XYSeriesCollection dataset) {
		this.dataset = dataset;
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent e) {
		report(e);
		
	}
	
	private void report(ChartMouseEvent e) {
        ChartEntity ce = e.getEntity();
        if (ce instanceof XYItemEntity) {
            XYItemEntity e1 = (XYItemEntity) ce;
            XYDataset d = e1.getDataset();
            int s = e1.getSeriesIndex();
            int i = e1.getItem();
            System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
        }
    }


}