package main;

import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

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


public class Graph extends ApplicationFrame {

    public Graph(String title, String chartTitle, ArrayList<Double> x, ArrayList<Double> y) {
        super(title);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "X" ,
                "Y" ,
                createDataset(x, y) ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }

    private XYDataset createDataset(ArrayList<Double> x, ArrayList<Double> y ) {

        final XYSeries XY = new XYSeries( "Moyenne et Intensité" );
        System.out.println(x.get(1) + "  " + y.get(1));
        System.out.println(x.size());
        XY.add(x.get(1), y.get(1));
        XY.add(x.get(2), y.get(2));

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(XY);

        return dataset;
    }



}