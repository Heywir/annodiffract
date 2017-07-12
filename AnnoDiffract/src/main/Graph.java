package main;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class Graph extends JFrame {
	public XYSeries XY;
	private XYSeriesCollection dataset;
	
    public Graph(String title, String chartTitle, ArrayList<Double> x, ArrayList<Double> y) {
        super(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Rayon" ,
                "intensité" ,
                createDataset(x, y) ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesOutlineStroke(0, new BasicStroke(0.1f));
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
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



}