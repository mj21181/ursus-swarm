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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import path.elements.map.ObstacleMap;
import path.elements.map.TileBasedMap;

/**
 * This class is the parent of any which is used for rendering simulations. It
 * implements all of the common methods needed for the renderer.
 * 
 * @author Mike Johnson
 *
 */
public abstract class Renderer extends Canvas implements Runnable
{
	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= -6164507174496249304L;
	
	/**
	 * The {@link BufferStrategy} used to double buffer the render image for
	 * leet rendering
	 */
	protected BufferStrategy	bs					= null;
	
	/**
	 * A {@link KeyHandler} class for handling input from the user if you're
	 * into that kind of thing.
	 */
	protected KeyHandler		keyHandler			= new KeyHandler();
	
	/**
	 * Flag used to indicate/control whether the {@link Renderer} is still
	 * rendering
	 */
	protected boolean			rendering			= true;
	
	/**
	 * Flag indicating whether the renderer has stopped rendering after being
	 * told to stop.
	 */
	protected boolean			isStopped			= false;
	
	/**
	 * This flag is used by other threads to signal the rendering thread to
	 * redraw
	 */
	protected boolean			redraw				= true;
	
	/**
	 * The total width in pixels of the area being rendered
	 */
	protected int				pixelWidth			= 800;
	
	/**
	 * The total height in pixels of the area being rendered
	 */
	protected int				pixelHeight			= 800;
	
	/**
	 * The {@link TileBasedMap} being rendered which the units are exploring.
	 */
	protected TileBasedMap		map					= null;
	
	/**
	 * The {@link Timer} thread periodically redrawing the frame
	 */
	protected Timer				timer				=
			new Timer("Renderer_PeriodicRedrawTimer");
	
	/**
	 * When we are calculating how many Frames Per Second we are rendering at,
	 * we need to do an average because the instantaneous measurement changes
	 * quite rapidly. This is the size of the window for said average.
	 */
	protected final int			MAX_FPS_SAMPLES		= 200;
	
	/**
	 * The index into the window for the FPS average
	 */
	protected int				fpsIndex			= 0;
	
	/**
	 * The sum for calculating the FPS average
	 */
	protected double			fpsSum				= 0.0;
	
	/**
	 * The window of samples for FPS average
	 */
	protected double[]			fpsSamples			=
			new double[MAX_FPS_SAMPLES];
	
	/**
	 * The parent {@link JFrame} being used to render this
	 */
	protected JFrame			parent				= null;
	
	/**
	 * Parent constructor at a given pixel width and height
	 * 
	 * @param pixelWidth
	 * @param pixelHeight
	 */
	public Renderer (int pixelWidth, int pixelHeight)
	{
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		
		this.addKeyListener(keyHandler);
	}
	
	/**
	 * Initializes the renderer for double buffering and starts the periodic
	 * task for repainting the canvas
	 */
	public void initialize (int rate)
	{
		createBufferStrategy(2);
		bs = getBufferStrategy();
		
		setIgnoreRepaint(true);
		
		// every half second
		TimerTask periodicRedrawTask = new TimerTask()
		{
			@Override
			public void run ()
			{
				callForRedraw();
			}
			
		};
		
		timer.scheduleAtFixedRate(periodicRedrawTask, 0, rate);
	}
	
	/**
	 * Calls for a repaint now instead of waiting for the periodic one.
	 */
	public void callForRedraw ()
	{
		redraw = true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run ()
	{
		long last = System.nanoTime();
		
		while (rendering)
		{
			Graphics2D g = (Graphics2D) bs.getDrawGraphics();
			AffineTransform old = g.getTransform();
			
			// flip the coordinate system so origin is in bottom-left of canvas
			g.translate(0, getHeight() - 1);
			g.scale(1, -1);
			
			if (redraw)
			{
				paintEnvironment(g);
				
				redraw = false;
			}
			
			// calculates the fps moving average
			long delta = (System.nanoTime() - last) / 1000000;
			last = System.nanoTime();
			
			double fps = 1000.0 / delta;
			
			calculateMovingAverageFPS(fps);
			
			if (parent != null)
			{
				logFPS(fps, parent);
			}
			
			// System.out.println("Delta (ms): " + delta);
			
			// if the subclass is such that it renders movement based on time
			// past, do so.
			for (int i = 0; i < delta / 5; i++ )
			{
				updateMovement(5);
			}
			
			// for any timesteps past 5
			if ( (delta % 5) != 0)
			{
				updateMovement(delta % 5);
			}
			
			// draws the units
			paintUnits(g);
			
			// restore the old coordinate system
			g.setTransform(old);
			
			// flip the double buffer
			g.dispose();
			bs.show();
			
			// sleep keeps you from hurting yourself, which is not recommended
			// by the prevailing medical doctrine of my time period
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
		}
		
		// shut it all down
		timer.cancel();
		
		parent.dispose();
		isStopped = true;
	}
	
	/**
	 * Calculates a moving average of the frames per second at which we are
	 * rendering
	 * 
	 * @param newFPS
	 * @return
	 */
	protected double calculateMovingAverageFPS (double newFPS)
	{
		// update window
		fpsSum -= fpsSamples[fpsIndex]; // subtract value falling off
		fpsSum += newFPS; // add new value
		fpsSamples[fpsIndex] = newFPS;
		
		if ( ++fpsIndex == MAX_FPS_SAMPLES)
		{
			fpsIndex = 0;
		}
		
		return ((double) fpsSum / MAX_FPS_SAMPLES);
	}
	
	/**
	 * For a renderer implementation that renders based on elapsed time, updates
	 * the movement based on how much time has past since this last happened in
	 * milliseconds.
	 * 
	 * @param delta
	 */
	public abstract void updateMovement (long delta);
	
	/**
	 * Starts the rendering
	 */
	public abstract void startRenderer ();
	
	/**
	 * Paints the units on the specified {@link Graphics2D} object.
	 * 
	 * @param g
	 */
	public abstract void paintUnits (Graphics2D g);
	
	/**
	 * Paints the environment on the specified {@link Graphics2D} object
	 * 
	 * @param g
	 */
	public abstract void paintEnvironment (Graphics2D g);
	
	/**
	 * Logs the FPS we are currently rendering at (paint in title bar of parent
	 * JFrame)
	 * 
	 * @param fps
	 * @param parent
	 */
	public abstract void logFPS (double fps, JFrame parent);
	
	/**
	 * Resizes the rendering to the new {@link Dimension}
	 * 
	 * @param d
	 */
	public abstract void resizeRendering (Dimension d);
	
	/**
	 * Sets the parent {@link JFrame} for this renderer, adds a resizing
	 * listener to change the tile size if the window is resized.
	 * 
	 * @param f
	 */
	public void setParentFrame (final JFrame f)
	{
		f.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentResized (ComponentEvent e)
			{
				// System.out.println("Frame Width: " + f.getSize().width);
				// System.out.println("Frame Height: " + f.getSize().height);
				
				Dimension newDimension =
						new Dimension(f.getSize().width, f.getSize().height);
				
				resizeRendering(newDimension);
				
				callForRedraw();
			}
			
			@Override
			public void componentMoved (ComponentEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void componentShown (ComponentEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void componentHidden (ComponentEvent e)
			{
				// intentionally blank
			}
		});
		
		parent = f;
	}
	
	/**
	 * Gets the pixel width being rendered
	 * 
	 * @return the pixelWidth
	 */
	public int getPixelWidth ()
	{
		return pixelWidth;
	}
	
	/**
	 * Sets the pixel width being rendered
	 * 
	 * @param pixelWidth the pixelWidth to set
	 */
	public void setPixelWidth (int pixelWidth)
	{
		this.pixelWidth = pixelWidth;
	}
	
	/**
	 * Gets the pixel height being renderd
	 * 
	 * @return the pixelHeight
	 */
	public int getPixelHeight ()
	{
		return pixelHeight;
	}
	
	/**
	 * Sets the pixel height being rendered
	 * 
	 * @param pixelHeight the pixelHeight to set
	 */
	public void setPixelHeight (int pixelHeight)
	{
		this.pixelHeight = pixelHeight;
	}
	
	/**
	 * Stops the rendering thread
	 */
	public void stopRendering ()
	{
		rendering = false;
	}
	
	/**
	 * Returns true once the rendering thread is finished shutting down
	 * 
	 * @return
	 */
	public boolean isStopped ()
	{
		return isStopped;
	}
	
	/**
	 * Gets the {@link TileBasedMap} being explored
	 * 
	 * @return
	 */
	public TileBasedMap getMap ()
	{
		return map;
	}
	
	/**
	 * Paints the {@link TileBasedMap} being used
	 * 
	 * @param g
	 * @param w
	 * @param h
	 */
	protected void paintMap (Graphics2D g, double w, double h)
	{
		ObstacleMap obs = map.getObstacleMap();
		
		map.setCellWidthInPixels((int) Math.floor(w / obs.getCols()));
		map.setCellHeightInPixels((int) Math.floor(h / obs.getRows()));
		
		// System.out.println("ch: " + map.getCellHeightInPixels());
		// System.out.println("cw: " + map.getCellWidthInPixels());
		
		for (int x = 0; x < obs.getCols(); x++ )
		{
			for (int y = 0; y < obs.getRows(); y++ )
			{
				if (obs.isBlocked(y, x))
				{
					g.setColor(Color.DARK_GRAY);
				}
				// else if (map.isOnPath(x, y))
				// {
				// g.setColor(Color.RED);
				// }
				// else if (map.isVisited(x, y))
				// {
				// g.setColor(Color.ORANGE);
				// }
				else
				{
					g.setColor(Color.LIGHT_GRAY);
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
}
