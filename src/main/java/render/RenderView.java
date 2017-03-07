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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import render.Renderer;

/**
 * This class extends a {@link JFrame} window for rendering simulations
 * 
 * @author Mike Johnson
 *
 */
public class RenderView extends JFrame
{
	/**
	 * Generated
	 */
	private static final long	serialVersionUID	= 8487724781467688325L;
	
	/**
	 * The {@link Renderer} object doing the work behind this JFrame
	 */
	protected Renderer			render				= null;
	
	/**
	 * The {@link Thread} which repaints the frame periodically
	 */
	protected Thread			renderThread		= null;
	
	/**
	 * Constructs a new {@link RenderView} with the specified {@link Renderer}
	 * and with the top left corner at the specified x and y pixel on the
	 * screen.
	 * 
	 * @param r
	 * @param x
	 * @param y
	 */
	public RenderView (Renderer r, int x, int y, int rate)
	{
		render = r;
		
		// sets the bounds a little larger to hold the renderer
		setBounds(x, y, render.getPixelWidth() + 20,
				render.getPixelHeight() + 35);
		
		// we only want to close the window by default, not the program
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// stops the render thread if we close the window
		WindowListener exitListener = new WindowListener()
		{
			
			@Override
			public void windowOpened (WindowEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void windowIconified (WindowEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void windowDeiconified (WindowEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void windowDeactivated (WindowEvent e)
			{
				// intentionally blank
			}
			
			@Override
			public void windowClosing (WindowEvent e)
			{
				render.stopRendering();
			}
			
			@Override
			public void windowClosed (WindowEvent e)
			{
				render.stopRendering();
			}
			
			@Override
			public void windowActivated (WindowEvent e)
			{
				// intentionally blank
			}
		};
		addWindowListener(exitListener);
		
		// adds the renderer to the content pane
		getContentPane().add(render);
		setVisible(true); // must be true for buffer strategy
		
		// ignore repaint commands from the OS because we plan on double
		// buffering
		setIgnoreRepaint(true);
		
		// initialize the renderer
		render.setParentFrame(this);
		render.initialize(rate);
		
		renderThread = new Thread(render, "Rendering_Thread");
	}
	
	/**
	 * Called when the renderer should start, causes the thread to begin
	 * rendering, makes visible
	 */
	public void startRendering ()
	{
		setVisible(true);
		renderThread.start();
	}
}
