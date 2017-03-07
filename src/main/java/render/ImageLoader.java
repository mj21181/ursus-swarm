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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class ImageLoader
{
	private static ImageLoader				instance	= null;
	private Hashtable<String, RenderImage>	images		= new Hashtable<String, RenderImage>();
	
	private ImageLoader ()
	{
		// Exists only to defeat instantiation.
	}
	
	public synchronized static ImageLoader getInstance ()
	{
		if (instance == null)
		{
			instance = new ImageLoader();
		}
		
		return instance;
	}
	
	public RenderImage getImage (String urlOrSomething)
	{
		if (images.get(urlOrSomething) != null) { return images
				.get(urlOrSomething); }
		
		BufferedImage img = null;
		
		URL url = getClass().getClassLoader().getResource(urlOrSomething);
		
		try
		{
			img = ImageIO.read(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image acceleratedImage = gc.createCompatibleImage(img.getWidth(),
				img.getHeight(), Transparency.BITMASK);
		
		acceleratedImage.getGraphics().drawImage(img, 0, 0, null);
		
		RenderImage mappImg = new RenderImage(acceleratedImage);
		images.put(urlOrSomething, mappImg);
		
		return mappImg;
	}
}
