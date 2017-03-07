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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class RenderImage
{
	private Image	image	= null;
	
	public RenderImage (Image image)
	{
		this.image = image;
	}
	
	public int getWidth ()
	{
		return image.getWidth(null);
	}
	
	public int getHeight ()
	{
		return image.getHeight(null);
	}
	
	public void draw (Graphics g, int x, int y)
	{
		g.drawImage(image, x, y, null);
	}
	
	public void scale(double scaleX, double scaleY)
	{
		BufferedImage tmp = new BufferedImage((int) (scaleX * getWidth()),
				(int) (scaleY * getHeight()), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = tmp.createGraphics();
		graphics.scale(scaleX, scaleY);
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		
		image = tmp;
	}
}
