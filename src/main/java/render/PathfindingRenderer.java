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
package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import org.apache.commons.math3.linear.RealVector;

import path.elements.AgentPath;
import path.elements.map.ObstacleMap;
import path.elements.map.TileBasedMap;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;
import pso.async.implementation.fitness.FitnessState;

public class PathfindingRenderer extends Renderer
{
	protected Vector<RenderUnit>			units				=
			new Vector<RenderUnit>();
	
	protected Map<Integer, LocationVertex>	startLocations		=
			new HashMap<Integer, LocationVertex>();
	protected Map<Integer, LocationVertex>	targetLocations		=
			new HashMap<Integer, LocationVertex>();
	
	protected Map<Integer, AgentPath>		paths				=
			new HashMap<Integer, AgentPath>();
	
	/**
	 * 
	 */
	protected static final long				serialVersionUID	=
			-5198238338406779625L;
	
	protected boolean						paintUnits			= true;
	
	protected FitnessState					fState				= null;
	
	protected RealVector					point				= null;
	
	protected double						maxElevation		=
			Double.MIN_VALUE;
	protected double						minElevation		=
			Double.MAX_VALUE;
	
	protected ObstacleMap					onPath				= null;
	
	public PathfindingRenderer (int pixelWidth, int pixelHeight,
			TileBasedMap tbm)
	{
		super(pixelWidth, pixelHeight);
		
		map = tbm;
		
		onPath = new ObstacleMap(map.getObstacleMap().getRows(),
				map.getObstacleMap().getCols());
		
		map.setCellWidthInPixels(
				(int) Math.floor(pixelWidth / map.getObstacleMap().getCols()));
		map.setCellHeightInPixels(
				(int) Math.floor(pixelHeight / map.getObstacleMap().getRows()));
		
	}
	
	@Override
	public void updateMovement (long delta)
	{
		// not implemented for this Renderer type
	}
	
	private void paintTargetLocations (Graphics2D g)
	{
		for (Integer id : targetLocations.keySet())
		{
			LocationVertex target = targetLocations.get(id);
			
			// System.out.println("Drawing Circle");
			int xLoc = target.getX() * map.getCellWidthInPixels();
			int yLoc = target.getY() * map.getCellHeightInPixels();
			
			// System.out.println("Dot X Start: " + xLoc);
			// System.out.println("Dot Y Start: " + yLoc);
			
			Ellipse2D.Double circle = new Ellipse2D.Double(xLoc, yLoc,
					map.getCellWidthInPixels(), map.getCellHeightInPixels());
			
			g.setColor(Color.ORANGE);
			g.fill(circle);
			
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			g.setColor(Color.GREEN);
			
			g.scale(1, -1);
			g.translate(0, - (getHeight() - 1));
			
			g.drawString(String.valueOf(id),
					xLoc + (int) (map.getCellWidthInPixels() / 2.0),
					getHeight() - (yLoc
							+ (int) (map.getCellHeightInPixels() / 2.0)));
			
			g.translate(0, getHeight() - 1);
			g.scale(1, -1);
		}
	}
	
	private void paintStartLocations (Graphics2D g)
	{
		for (Integer id : startLocations.keySet())
		{
			LocationVertex start = startLocations.get(id);
			
			// System.out.println("Drawing Circle");
			int xLoc = start.getX() * map.getCellWidthInPixels();
			int yLoc = start.getY() * map.getCellHeightInPixels();
			
			// System.out.println("Dot X Start: " + xLoc);
			// System.out.println("Dot Y Start: " + yLoc);
			
			Ellipse2D.Double circle = new Ellipse2D.Double(xLoc, yLoc,
					map.getCellWidthInPixels(), map.getCellHeightInPixels());
			g.setColor(Color.YELLOW);
			g.fill(circle);
			
			g.setColor(Color.GREEN);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			// need to paint text in og coordinate system
			g.scale(1, -1);
			g.translate(0, - (getHeight() - 1));
			
			g.drawString(String.valueOf(id),
					xLoc + (int) (map.getCellWidthInPixels() / 2.0),
					getHeight() - (yLoc
							+ (int) (map.getCellHeightInPixels() / 2.0)));
			
			g.translate(0, getHeight() - 1);
			g.scale(1, -1);
		}
	}
	
	@Override
	public void startRenderer ()
	{
		// nothing additional needed for this implementation
	}
	
	@Override
	public void paintUnits (Graphics2D g)
	{
		if (paintUnits)
		{
			for (RenderUnit u : units)
			{
				u.paint(g);
			}
		}
	}
	
	@Override
	public void paintEnvironment (Graphics2D g)
	{
		if (fState != null)
		{
			paintElevation(g);
		}
		else
		{
			paintMap(g, pixelWidth, pixelHeight);
		}
		
		paintPaths(g);
		
		paintStartLocations(g);
		paintTargetLocations(g);
	}
	
	public void clearPaths ()
	{
		onPath = new ObstacleMap(onPath.getRows(), onPath.getCols());
	}
	
	public void clear ()
	{
		startLocations.clear();
		targetLocations.clear();
		paths.clear();
		
		for (int j = 0; j < onPath.getRows(); j++ )
		{
			for (int i = 0; i < onPath.getCols(); i++ )
			{
				onPath.unblock(j, i);
			}
		}
	}
	
	public void paintPaths (Graphics2D g)
	{
		for (int x = 0; x < onPath.getCols(); x++ )
		{
			for (int y = 0; y < onPath.getRows(); y++ )
			{
				if (onPath.isBlockedXY(x, y))
				{
					g.setColor(Color.RED);
				}
				else
				{
					continue;
				}
				
				// draw the rectangle with a dark outline
				
				int xPixels = x * map.getCellWidthInPixels();
				int yPixels = y * map.getCellHeightInPixels();
				
				// System.out.println("xpix: " + xPixels);
				// System.out.println("ypix: " + yPixels);
				
				g.fillRect(xPixels, yPixels, map.getCellWidthInPixels(),
						map.getCellHeightInPixels());
				g.setColor(g.getColor().darker());
				g.drawRect(xPixels, yPixels, map.getCellWidthInPixels(),
						map.getCellHeightInPixels());
			}
		}
	}
	
	@Override
	public void logFPS (double fps, JFrame parent)
	{
		parent.setTitle(String.format("Frames Per Second: %6.2f", fps));
	}
	
	protected void paintElevation (Graphics2D g)
	{
		for (int x = 0; x < map.getObstacleMap().getCols(); x++ )
		{
			for (int y = 0; y < map.getObstacleMap().getRows(); y++ )
			{
				setColor(g, x, y);
				
				// draw the rectangle with a dark outline
				
				int xPixels = x * map.getCellWidthInPixels();
				int yPixels = y * map.getCellHeightInPixels();
				
				// System.out.println("xpix: " + xPixels);
				// System.out.println("ypix: " + yPixels);
				
				drawSquares(g, xPixels, yPixels);
			}
		}
	}
	
	protected void setColor (Graphics2D g, int x, int y)
	{
		boolean blked = map.getObstacleMap().isBlocked(y, x);
		
		if (blked)
		{
			g.setColor(Color.DARK_GRAY);
		}
		// else if (l.isOnPath())
		// {
		// g.setColor(Color.RED);
		// }
		// else if (l.isVisited())
		// {
		// g.setColor(Color.ORANGE);
		// }
		else
		{
			// get the elevation of this square
			
			// System.out.println("Calculating elevation for: " +
			// point.toString());
			// double elevation = calculateFitnessForLocation(point, x, y);
			//
			// double ratio =
			// (elevation - minElevation) / (maxElevation - minElevation);
			//
			// int color = (int) (ratio * 255.0);
			//
			// Color c = new Color(color, color, color);
			//
			// g.setColor(c);
		}
	}
	
	protected void drawSquares (Graphics2D g, int xPixels, int yPixels)
	{
		g.fillRect(xPixels, yPixels, map.getCellWidthInPixels(),
				map.getCellHeightInPixels());
		
		// draw the outline
		g.setColor(Color.LIGHT_GRAY);
		g.setColor(g.getColor().darker());
		g.drawRect(xPixels, yPixels, map.getCellWidthInPixels(),
				map.getCellHeightInPixels());
	}
	
	@Override
	public void resizeRendering (Dimension d)
	{
		if (d.width % map.getObstacleMap().getCols() == 0
				&& d.height % map.getObstacleMap().getRows() == 0)
		{
			// System.out.println("painting");
			
			// double scaleX = d.width / (double) pixelWidth;
			// double scaleY = d.height / (double) pixelHeight;
			//
			// for (RenderUnit u : units)
			// {
			// // u.getImage().scale(scaleX, scaleY);
			// }
			
			pixelWidth = d.width;
			pixelHeight = d.height;
			// System.out.println("Setting Width: " + pixelWidth);
			// System.out.println("Setting Height: " + pixelHeight);
		}
	}
	
	public synchronized void addUnit (int numberOfUnits, int id,
			LocationVertex s, LocationVertex t)
	{
		RenderUnit ru = new RenderUnit(map);
		
		ru.setUnit(id);
		
		paintUnits = false;
		
		units.add(ru);
		
		startLocations.put(id, s);
		targetLocations.put(id, t);
		
		if (numberOfUnits == units.size())
		{
			paintUnits = true;
		}
	}
	
	public void putPath (int id, AgentPath p)
	{
		paths.put(id, p);
		
		for (GraphVertex v : p.getAsList())
		{
			LocationVertex l = (LocationVertex) v;
			
			onPath.blockXY(l.getX(), l.getY());
		}
	}
	
	public Map<Integer, LocationVertex> getStartLocations ()
	{
		return startLocations;
	}
	
	public Map<Integer, LocationVertex> getTargetLocations ()
	{
		return targetLocations;
	}
	
	public void putStartLocation (int id, LocationVertex s)
	{
		startLocations.put(id, s);
		
		units.get(id).setX(s.getX());
		units.get(id).setY(s.getY());
	}
	
	public void putTargetLocation (int id, LocationVertex t)
	{
		targetLocations.put(id, t);
	}
	
	public RenderUnit getUnit (int index)
	{
		return units.get(index);
	}
	
	public void clearUnits ()
	{
		units.clear();
	}
	
	public void setFitnessState (FitnessState f)
	{
		// this.fState = f;
		//
		// point = new ArrayRealVector(2);
		//
		// for (int x = 0; x < map.getObstacleMap().getCols(); x++ )
		// {
		// for (int y = 0; y < map.getObstacleMap().getRows(); y++ )
		// {
		// double elevation = calculateFitnessForLocation(point, x, y);
		//
		// if (elevation > maxElevation)
		// {
		// maxElevation = elevation;
		// }
		//
		// if (elevation < minElevation)
		// {
		// minElevation = elevation;
		// }
		// }
		// }
		//
		// System.out.println("Min elevation: " + minElevation);
		// System.out.println("Max elevation: " + maxElevation);
	}
	
	// protected double calculateFitnessForLocation (RealVector point, int x,
	// int y)
	// {
	// double xFit = convertToPsoCoordinates(fState.getLowerBound(),
	// fState.getUpperBound(), x);
	// double yFit = convertToPsoCoordinates(fState.getLowerBound(),
	// fState.getUpperBound(), y);
	//
	// FitnessCalculator calc = new FitnessCalculator();
	//
	// // //TODO: Comment me out when not running TOM optimization ***/
	// //
	// // double delta = (fState.getUpperBound() - fState.getLowerBound()) /
	// // fState.getMOLAData().getColumnDimension();
	// //
	// // xFit = (xFit - fState.getLowerBound()) / delta;
	// // yFit = (yFit - fState.getLowerBound()) / delta;
	// //
	// //// System.out.println("X: " + xFit);
	// //// System.out.println("Y: " + yFit);
	// //
	// // /*********************************************************/
	//
	// point.setEntry(0, xFit);
	// point.setEntry(1, yFit);
	//
	// return calc.calculateFitness(point, fState);
	// }
	
	public int convertToGridCoordinates (double lb, double ub, double val)
	{
		System.out.println("In: " + val);
		// y = (x - min) / (max - min);
		double ratio = (val - lb) / (ub - lb);
		
		System.out.println("LB: " + lb);
		System.out.println("UB: " + ub);
		System.out.println("Ratio: " + ratio);
		System.out.println("Cols: " + map.getObstacleMap().getCols());
		// z = y * (max_z - min_z) + min_z
		// max_z = cols
		// min_z = 0
		double gridVal = ratio * (map.getObstacleMap().getCols());
		
		System.out.println("GridVal: " + gridVal);
		
		return (int) gridVal;
	}
	
	protected double convertToPsoCoordinates (double lb, double ub, double val)
	{
		// y = (x - min) / (max - min);
		double ratio = val / map.getObstacleMap().getCols();
		
		// System.out.println("Ratio: " + ratio);
		// System.out.println("LB: " + lb);
		// System.out.println("UB: " + ub);
		
		double psoVal = ratio * (ub - lb) + lb;
		
		// System.out.println("In: " + val);
		// System.out.println("val: " + psoVal);
		
		return psoVal;
	}
}
