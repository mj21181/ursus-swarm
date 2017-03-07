/*******************************************************************************
 * Copyright (C) 2016 Michael Johnson
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 *******************************************************************************/
package gui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import log.ApplicationLogger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

/**
 * 
 * @author Mike Johnson
 *
 */
public class Histogram
{
	
	private JFreeChart				chart	= null;
	private SimpleHistogramDataset	hds		= null;
	private ChartPanel				cp		= null;
	private int						bins	= 20;
	private double					lb		= -100;
	private double					ub		= 100;
	private String					title	= "";
	private String					xLabel	= "";
	private String					yLabel	= "";
	
	/**
	 * Constructs a histogram
	 * 
	 * @param binNumber the number of bins for the histogram
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param disp whether to display the plot in a JFrame
	 * @param title the title of the plot
	 */
	public Histogram (int binNumber, double lowerBound, double upperBound,
			String title, String xLabel, String yLabel)
	{
		
		bins = binNumber;
		lb = lowerBound;
		ub = upperBound;
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		
		hds = new SimpleHistogramDataset("x");
		hds.setAdjustForBinSize(false);
		
		chart =
				ChartFactory.createHistogram(this.title, this.xLabel,
						this.yLabel, hds, PlotOrientation.VERTICAL, false,
						false, false);
		
		cp = new ChartPanel(chart);
		cp.setPreferredSize(new Dimension(800, 640));
		
		setupHistogram();
	}
	
	/**
	 * Adds a list of Samples to the Histogram.
	 * 
	 * @param d
	 */
	public void addSamples (LinkedList<Integer> samps)
	{
		
		// prevent numbers outside the range from being added
		Iterator<Integer> it = samps.iterator();
		while (it.hasNext())
		{
			double next = it.next();
			if (next > ub || next < lb)
			{
				it.remove();
			}
		}
		
		double[] sampls = new double[samps.size()];
		
		int count = 0;
		it = samps.iterator();
		while (it.hasNext())
		{
			double next = it.next();
			sampls[count] = next;
			count++ ;
		}
		
		// add the data to the bins
		hds.setNotify(false);
		hds.addObservations(sampls);
		hds.setNotify(true);
	}
	
	/**
	 * Adds a list of Samples to the Histogram.
	 * 
	 * @param d
	 */
	public void addSamples (List<Double> d)
	{
		
		// prevent numbers outside the range from being added
		
		Iterator<Double> it = d.iterator();
		while (it.hasNext())
		{
			double next = it.next();
			if (next > ub || next < lb)
			{
				// System.out.println("Removing "+next);
				it.remove();
			}
		}
		
		double[] samps = new double[d.size()];
		
		int count = 0;
		it = d.iterator();
		while (it.hasNext())
		{
			double next = it.next();
			samps[count] = next;
			count++ ;
		}
		
		// add the data to the bins
		hds.setNotify(false);
		hds.addObservations(samps);
		
		hds.setNotify(true);
	}
	
	private void setupHistogram ()
	{
		// add the bins
		double delta = (ub - lb) / bins;
		double start = lb;
		// System.out.println("Delta: "+delta);
		// System.out.println("LB: "+start);
		
		hds.removeAllBins();
		
		for (int i = 0; i < bins; i++ )
		{
			// System.out.println("Adding bin: "+i);
			// System.out.println("Start: "+start);
			// System.out.println("End: "+(start+delta));
			
			SimpleHistogramBin bin;
			if (i == (bins - 1))
			{
				bin = new SimpleHistogramBin(start, start + delta, true, true);
			}
			else
			{
				bin = new SimpleHistogramBin(start, start + delta, true, false);
			}
			
			hds.addBin(bin);
			start = start + delta;
		}
	}
	
	/**
	 * Saves the chart as a PNG File
	 * 
	 * @param f the File to save to
	 * @param width the width of the image
	 * @param height the height of the image
	 */
	public void saveAsPNG (File f, int width, int height)
	{
		try
		{
			ApplicationLogger.getInstance().log(
					"Saving Image: " + f.getAbsolutePath());
			ChartUtilities.saveChartAsPNG(f, chart, width, height);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public JFreeChart getChart ()
	{
		return chart;
	}
	
	/**
	 * @return the cp
	 */
	public ChartPanel getChartPanel ()
	{
		return cp;
	}
	
	/**
	 * @param cp the cp to set
	 */
	public void setChartPanel (ChartPanel cp)
	{
		this.cp = cp;
	}
	
	/**
	 * Returns the number of histogram bins
	 * 
	 * @return the number of bins
	 */
	public int getHistogramBinNumber ()
	{
		return bins;
	}
	
	/**
	 * Returns the lower bound of the histogram
	 * 
	 * @return the lower bound
	 */
	public double getLowerBound ()
	{
		return lb;
	}
	
	/**
	 * Returns the upper bound of the histogram
	 * 
	 * @return the ub
	 */
	public double getUpperBound ()
	{
		return ub;
	}
	
	/**
	 * Returns the title of the Chart
	 * 
	 * @return the title
	 */
	public String getTitle ()
	{
		return title;
	}
	
	/**
	 * Returns the X Axis Label of the Histogram
	 * 
	 * @return the xLabel
	 */
	public String getxLabel ()
	{
		return xLabel;
	}
	
	/**
	 * Returns the Y Axis Label of the Histogram
	 * 
	 * @return the yLabel
	 */
	public String getyLabel ()
	{
		return yLabel;
	}
	
	/**
	 * Sets the number of bins in the Histogram
	 * 
	 * @param bins the bins to set
	 */
	public void setBins (int bins)
	{
		this.bins = bins;
	}
	
	/**
	 * Sets the Lower Bound of the Histogram
	 * 
	 * @param lb the lower bound to set
	 */
	public void setLowerBound (double lb)
	{
		this.lb = lb;
	}
	
	/**
	 * Sets the Upper Bound of the Histogram
	 * 
	 * @param ub the upper bound to set
	 */
	public void setUpperBound (double ub)
	{
		this.ub = ub;
	}
	
	/**
	 * Sets the title of the Chart
	 * 
	 * @param title the title to set
	 */
	public void setTitle (String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the X Axis Label
	 * 
	 * @param xLabel the xLabel to set
	 */
	public void setxLabel (String xLabel)
	{
		this.xLabel = xLabel;
	}
	
	/**
	 * Sets the Y Axis Label
	 * 
	 * @param yLabel the yLabel to set
	 */
	public void setyLabel (String yLabel)
	{
		this.yLabel = yLabel;
	}
}
