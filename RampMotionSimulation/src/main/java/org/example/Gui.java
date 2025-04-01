package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Gui {
    public Gui(ArrayList<Double> mass_x, ArrayList<Double> mass_y,
               ArrayList<Double> circlePoint_x, ArrayList<Double> circlePoint_y,
               ArrayList<Double> PotEnergy_x, ArrayList<Double> PotEnergy_y,
               ArrayList<Double> KinEnergy_x, ArrayList<Double> KinEnergy_y,
               ArrayList<Double> totalEnergy_x, ArrayList<Double> totalEnergy_y) {

        // CHART 1: RAMP SIMULATION

        //ramp line from (0,20) to (20,0)
        XYSeries rampSeries = new XYSeries("Ramp");
        rampSeries.add(0.0, 20.0);
        rampSeries.add(20.0, 0.0);

        XYSeriesCollection rampDataset = new XYSeriesCollection();
        rampDataset.addSeries(rampSeries);

        //center-of-mass (gray points)
        XYSeries centerSeries = new XYSeries("Center of Mass");
        for (int i = 0; i < mass_x.size(); i++) {
            centerSeries.add(mass_x.get(i), mass_y.get(i));
        }
        XYSeriesCollection centerDataset = new XYSeriesCollection();
        centerDataset.addSeries(centerSeries);

        //sphere marker (blue line)
        XYSeries markerSeries = new XYSeries("Sphere Marker");
        for (int i = 0; i < circlePoint_x.size(); i++) {
            markerSeries.add(circlePoint_x.get(i), circlePoint_y.get(i));
        }
        XYSeriesCollection markerDataset = new XYSeriesCollection();
        markerDataset.addSeries(markerSeries);

        //create the base chart with the ramp dataset
        JFreeChart simulationChart = ChartFactory.createXYLineChart(
                "Ramp Simulation",
                "X (m)", "Y (m)",
                rampDataset
        );
        XYPlot simPlot = simulationChart.getXYPlot();

        XYLineAndShapeRenderer rampRenderer = (XYLineAndShapeRenderer) simPlot.getRenderer();
        rampRenderer.setSeriesPaint(0, Color.ORANGE);
        rampRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        rampRenderer.setSeriesShapesVisible(0, false);

        simPlot.setDataset(1, centerDataset);
        XYLineAndShapeRenderer centerRenderer = new XYLineAndShapeRenderer();

        centerRenderer.setSeriesLinesVisible(0, false);
        centerRenderer.setSeriesShapesVisible(0, true);

        centerRenderer.setSeriesPaint(0, Color.GRAY);
        Shape circleShape = new Ellipse2D.Double(-3, -3, 6, 6);
        centerRenderer.setSeriesShape(0, circleShape);
        simPlot.setRenderer(1, centerRenderer);

        simPlot.setDataset(2, markerDataset);
        XYLineAndShapeRenderer markerRenderer = new XYLineAndShapeRenderer();
        //show line, hide shapes
        markerRenderer.setSeriesLinesVisible(0, true);
        markerRenderer.setSeriesShapesVisible(0, false);
        markerRenderer.setSeriesPaint(0, Color.BLUE);
        markerRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        simPlot.setRenderer(2, markerRenderer);

        //adjust axis range to have a bit of padding
        NumberAxis domainAxis = (NumberAxis) simPlot.getDomainAxis();
        domainAxis.setRange(-5, 25); // so the ramp is visible with some margin
        NumberAxis rangeAxis = (NumberAxis) simPlot.getRangeAxis();
        rangeAxis.setRange(-5, 25);

        // CHART 2: ENERGY VS TIME
        XYSeries peSeries = new XYSeries("Potential Energy");
        XYSeries keSeries = new XYSeries("Kinetic Energy");
        XYSeries teSeries = new XYSeries("Total Energy");
        for (int i = 0; i < PotEnergy_x.size(); i++) {
            peSeries.add(PotEnergy_x.get(i), PotEnergy_y.get(i));
            keSeries.add(KinEnergy_x.get(i), KinEnergy_y.get(i));
            teSeries.add(totalEnergy_x.get(i), totalEnergy_y.get(i));
        }
        XYSeriesCollection energyDataset = new XYSeriesCollection();
        energyDataset.addSeries(peSeries);
        energyDataset.addSeries(keSeries);
        energyDataset.addSeries(teSeries);

        JFreeChart energyChart = ChartFactory.createXYLineChart(
                "Energies vs Time",
                "Time (s)", "Energy (J)",
                energyDataset
        );

        //combine both charts
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(new ChartPanel(simulationChart));
        mainPanel.add(new ChartPanel(energyChart));

        JFrame frame = new JFrame("Ramp Motion Simulation & Energies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
