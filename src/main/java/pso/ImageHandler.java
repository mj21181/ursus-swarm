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
/**
 * 
 */
package pso;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import log.ApplicationLogger;

/**
 * The ImageHandler is a wrapper class used to import Greyscale images as
 * heightmaps to use a fitness functions in simulations
 * 
 * @author mike
 * 
 */
public class ImageHandler
{
	private BufferedImage	img;
	
	/**
	 * Default Constructor
	 */
	public ImageHandler ()
	{
		img = null;
	}
	
	/**
	 * Explicit Constructor
	 * 
	 * @param path
	 *            filepath to image
	 */
	public ImageHandler (String path)
	{
		try
		{
			img = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			ApplicationLogger.getInstance().logException(e);
		}
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param ih
	 *            second ImageHandler
	 */
	public ImageHandler (ImageHandler ih)
	{
		this.img = ih.img;
	}
	
	/**
	 * Takes the RGB values of the BufferedImage and converts them into a Matrix
	 * of values representing the heights of a landscape
	 * 
	 * @return
	 */
	public RealMatrix extractHeights ()
	{
		RealMatrix m = new Array2DRowRealMatrix(img.getWidth(), img.getHeight());
		
		for (int i = 0; i < m.getRowDimension(); i++)
		{
			for (int j = 0; j < m.getColumnDimension(); j++)
			{
				// averages values in case not greyscale
				Color col = new Color(img.getRGB(i, j));
				
				double val = (col.getRed() + col.getBlue() + col.getGreen()) / 3.0;
				
				m.setEntry(i, j, val);
			}
		}
		
		return m;
	}
	
	/**
	 * Gets the currently loaded Image
	 * 
	 * @return the img
	 */
	public BufferedImage getImg ()
	{
		return img;
	}
	
	/**
	 * Sets the currently loaded Image
	 * 
	 * @param img
	 *            the img to set
	 */
	public void setImg (BufferedImage img)
	{
		this.img = img;
	}
}
