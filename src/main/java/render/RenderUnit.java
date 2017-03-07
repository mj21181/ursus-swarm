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

import java.awt.Graphics2D;
import java.io.File;
import path.elements.map.TileBasedMap;

public class RenderUnit
{
	protected final double	HEADING	= Math.PI / 2.0;
	
	// image to paint
	protected RenderImage	image	= ImageLoader.getInstance()
			.getImage("render" + File.separator + "unit.png");
	
	protected int			x		= -1;
	
	protected int			y		= -1;
	
	protected TileBasedMap	map		= null;
	
	public RenderUnit (TileBasedMap map)
	{
		this.map = map;
	}
	
	/**
	 * Paints the unit on the specified {@link Graphics2D} object
	 * 
	 * @param g
	 */
	public void paint (Graphics2D g)
	{
		int xPixels = (int) (map.getCellWidthInPixels() * x);
		int yPixels = (int) (map.getCellHeightInPixels() * y);
		
		//xPixels -= image.getWidth() / 2;
		//yPixels -= image.getHeight() / 2;
		
		g.rotate(HEADING, (xPixels + image.getWidth() / 2),
				(yPixels + image.getHeight() / 2));
		image.draw(g, xPixels, yPixels);
		g.rotate(-HEADING, (xPixels + image.getWidth() / 2),
				(yPixels + image.getHeight() / 2));
	}
	
	/**
	 * Returns the {@link RenderImage} to draw the unit
	 * 
	 * @return
	 */
	public RenderImage getImage ()
	{
		return image;
	}
	
	/**
	 * Loads the render image associated with the specified id number
	 * 
	 * @param id
	 */
	public void setUnit (int id)
	{
		switch (id)
		{
			case 0:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit0.png");
				break;
			case 1:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit1.png");
				break;
			case 2:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit2.png");
				break;
			case 3:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit3.png");
				break;
			case 4:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit4.png");
				break;
			default:
				image = ImageLoader.getInstance()
						.getImage("render" + File.separator + "unit.png");
				break;
		}
		
		// scale the render image by the needed value
		double scaleX = map.getCellWidthInPixels() / (double) image.getWidth();
		double scaleY =
				map.getCellHeightInPixels() / (double) image.getHeight();
		
		image.scale(scaleX, scaleY);
	}
	
	/**
	 * @return the x
	 */
	public int getX ()
	{
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX (int x)
	{
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public int getY ()
	{
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY (int y)
	{
		this.y = y;
	}
}
