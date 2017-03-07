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
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import log.ApplicationLogger;

public class LinePlot
{
	private JFreeChart			chart	= null;
	private XYSeriesCollection	xyds	= new XYSeriesCollection();
	private ChartPanel			cp		= null;
	private String				title	= "";
	private String				xLabel	= "";
	private String				yLabel	= "";
	
	/**
	 * Explicit constructor of a Line plot.
	 * 
	 * @param title The title of the plot
	 * @param xLabel The X Label
	 * @param yLabel The Y Label
	 */
	public LinePlot (String title, String xLabel, String yLabel,
			String series1Legend, String series2Legend, String series3Legend,
			long initialCapacity)
	{
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		
		XYSeriesExtended series =
				new XYSeriesExtended(series1Legend, false, false,
						initialCapacity);
		
		xyds.addSeries(series);
		
		if (series2Legend != null)
		{
			XYSeriesExtended series2 =
					new XYSeriesExtended(series2Legend, false, false,
							initialCapacity);
			
			xyds.addSeries(series2);
		}
		
		if (series3Legend != null)
		{
			XYSeriesExtended series3 =
					new XYSeriesExtended(series3Legend, false, false,
							initialCapacity);
			
			xyds.addSeries(series3);
		}
		
		chart =
				ChartFactory.createXYLineChart(this.title, this.xLabel,
						this.yLabel, xyds, PlotOrientation.VERTICAL, true,
						false, false);
		
		chart.setNotify(false);
		
		// LegendTitle lt = new LegendTitle(chart.getPlot());
		// lt.setItemFont(new Font("Dialog", Font.PLAIN, 9));
		// lt.setBackgroundPaint(new Color(200, 200, 255, 100));
		// lt.setFrame(new BlockBorder(Color.white));
		// lt.setPosition(RectangleEdge.BOTTOM);
		// XYTitleAnnotation ta = new XYTitleAnnotation(0.98, 0.02,
		// lt,RectangleAnchor.BOTTOM_RIGHT);
		//
		// ta.setMaxWidth(0.48);
		
		cp = new ChartPanel(chart);
		cp.setPreferredSize(new Dimension(800, 640));
	}
	
	public void addValuesFast (int lineIndex, List<XYDataItem> xy)
	{
		chart.setNotify(false);
		
		XYSeriesExtended xyse = (XYSeriesExtended) xyds.getSeries(lineIndex);
		
		xyse.setData(xy);
		
		chart.setNotify(true);
		
		xyse.fireSeriesChanged();
	}
	
	public void addValues (int lineIndex, List<Double> d)
	{
		chart.setNotify(false);
		
		for (Double doub : d)
		{
			// if(series.getItemCount() % (d.size()/ 10) == 0)
			// {
			// System.out.println("Adding: " + series.getItemCount());
			// }
			
			xyds.getSeries(lineIndex).add(
					xyds.getSeries(lineIndex).getItemCount(), doub);
		}
		
		chart.setNotify(true);
	}
	
	public void addValues (int lineIndex, List<Double> t, List<Double> d)
	{
		if (t.size() == d.size())
		{
			chart.setNotify(false);
			
			for (Double doub : d)
			{
				
				xyds.getSeries(lineIndex).add(
						t.get(xyds.getSeries(lineIndex).getItemCount()), doub);
			}
			
			chart.setNotify(true);
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
	
	private class XYSeriesExtended extends XYSeries
	{
		
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 2024089490940643117L;
		
	    /** The lowest x-value in the series, excluding Double.NaN values. */
	    private double minX;

	    /** The highest x-value in the series, excluding Double.NaN values. */
	    private double maxX;

	    /** The lowest y-value in the series, excluding Double.NaN values. */
	    private double minY;

	    /** The highest y-value in the series, excluding Double.NaN values. */
	    private double maxY;
		
		public XYSeriesExtended (Comparable<String> key, boolean autoSort,
				boolean allowDuplicateXValues, long initialCapacity)
		{
			super(key, autoSort, allowDuplicateXValues);
			
			data = new ArrayList<Number>((int) initialCapacity);
		}
		
		public void setData (List<XYDataItem> xy)
		{
			this.data = xy;
			verifyData();
		}
		
		protected void verifyData ()
		{
			for(Object o : data)
			{
				XYDataItem item = (XYDataItem) o;
		        
		        double x = item.getXValue();
		        this.minX = minIgnoreNaN(this.minX, x);
		        this.maxX = maxIgnoreNaN(this.maxX, x);
		        if (item.getY() != null) {
		            double y = item.getYValue();
		            this.minY = minIgnoreNaN(this.minY, y);
		            this.maxY = maxIgnoreNaN(this.maxY, y);
		        }
			}
		}
		
	    /**
	     * A function to find the minimum of two values, but ignoring any
	     * Double.NaN values.
	     *
	     * @param a  the first value.
	     * @param b  the second value.
	     *
	     * @return The minimum of the two values.
	     */
	    protected double minIgnoreNaN(double a, double b) {
	        if (Double.isNaN(a)) {
	            return b;
	        }
	        if (Double.isNaN(b)) {
	            return a;
	        }
	        return Math.min(a, b);
	    }

	    /**
	     * A function to find the maximum of two values, but ignoring any
	     * Double.NaN values.
	     *
	     * @param a  the first value.
	     * @param b  the second value.
	     *
	     * @return The maximum of the two values.
	     */
	    protected double maxIgnoreNaN(double a, double b) {
	        if (Double.isNaN(a)) {
	            return b;
	        }
	        if (Double.isNaN(b)) {
	            return a;
	        }
	        return Math.max(a, b);
	    }
	    
	    /**
	     * Returns the smallest x-value in the series, ignoring any Double.NaN
	     * values.  This method returns Double.NaN if there is no smallest x-value
	     * (for example, when the series is empty).
	     *
	     * @return The smallest x-value.
	     *
	     * @see #getMaxX()
	     *
	     * @since 1.0.13
	     */
	    @Override
	    public double getMinX() {
	        return this.minX;
	    }

	    /**
	     * Returns the largest x-value in the series, ignoring any Double.NaN
	     * values.  This method returns Double.NaN if there is no largest x-value
	     * (for example, when the series is empty).
	     *
	     * @return The largest x-value.
	     *
	     * @see #getMinX()
	     *
	     * @since 1.0.13
	     */
	    @Override
	    public double getMaxX() {
	        return this.maxX;
	    }

	    /**
	     * Returns the smallest y-value in the series, ignoring any null and
	     * Double.NaN values.  This method returns Double.NaN if there is no
	     * smallest y-value (for example, when the series is empty).
	     *
	     * @return The smallest y-value.
	     *
	     * @see #getMaxY()
	     *
	     * @since 1.0.13
	     */
	    @Override
	    public double getMinY() {
	        return this.minY;
	    }

	    /**
	     * Returns the largest y-value in the series, ignoring any Double.NaN
	     * values.  This method returns Double.NaN if there is no largest y-value
	     * (for example, when the series is empty).
	     *
	     * @return The largest y-value.
	     *
	     * @see #getMinY()
	     *
	     * @since 1.0.13
	     */
	    @Override
	    public double getMaxY() {
	        return this.maxY;
	    }
	}
}
