/**
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
 */
package path.elements.map;

import org.apache.commons.math3.linear.RealMatrix;

import path.config.BlockedLocation;
import path.config.GridConfiguration;
import pso.ImageHandler;

/**
 * The {@link TileBasedMap} class is a wrapper class for the {@link ObstacleMap}
 * for rendering purposes. It keeps track of the size of each of the locations
 * in the map pixel-wise. It can also be used to import an obstacle map from a
 * file.
 * 
 * @author Mike Johnson
 *
 */
public class TileBasedMap
{
	/**
	 * The {@link ObstacleMap} which this class is responsible for rendering
	 */
	protected ObstacleMap	obstacles			= null;
	
	/**
	 * The width of one of the cells in the {@link ObstacleMap}
	 */
	protected int			cellWidthInPixels	= -1;
	
	/**
	 * The height of one of the cells in the {@link ObstacleMap}
	 */
	protected int			cellHeightInPixels	= -1;
	
	/**
	 * Constructs a new {@link TileBasedMap} of specified rows and columns
	 * 
	 * @param rows
	 * @param cols
	 */
	public TileBasedMap (int rows, int cols)
	{
		obstacles = new ObstacleMap(rows, cols);
	}
	
	/**
	 * Constructs a {@link TileBasedMap} from a {@link GridConfiguration}
	 * object. If the path to an obstacle file is not null, the other settings
	 * in the {@link GridConfiguration} will be overridden and the map contained
	 * in that file will be imported.
	 * 
	 * @param cfg
	 */
	public TileBasedMap (GridConfiguration cfg)
	{
		if (cfg.getPathToObstacleFile() != null)
		{
			ImageHandler handler =
					new ImageHandler(cfg.getPathToObstacleFile());
			
			RealMatrix heights = handler.extractHeights();
			
			int rows = heights.getRowDimension();
			int cols = heights.getColumnDimension();
			
			obstacles = new ObstacleMap(rows, cols);
			
			for (BlockedLocation b : cfg.getBlockedLocations())
			{
				obstacles.blockXY(b.getX(), b.getY());
			}
			
			for (int i = 0; i < cols; i++ )
			{
				for (int j = 0; j < rows; j++ )
				{
					if (heights.getEntry(j, i) < 200)
					{
						obstacles.blockXY(i, j);
					}
				}
			}
		}
		else
		{
			int rows = cfg.getRows();
			int cols = cfg.getCols();
			
			obstacles = new ObstacleMap(rows, cols);
			
			for (BlockedLocation b : cfg.getBlockedLocations())
			{
				obstacles.blockXY(b.getX(), b.getY());
			}
		}
	}
	
	/**
	 * Gets the current {@link ObstacleMap} in use
	 * 
	 * @return
	 */
	public ObstacleMap getObstacleMap ()
	{
		return obstacles;
	}
	
	/**
	 * Sets the current {@link ObstacleMap} in use
	 * 
	 * @param map
	 */
	public void setObstacleMap (ObstacleMap map)
	{
		obstacles = map;
	}
	
	/**
	 * Gets the width of each cell in pixels
	 * 
	 * @return the cellWidthInPixels
	 */
	public int getCellWidthInPixels ()
	{
		return cellWidthInPixels;
	}
	
	/**
	 * Sets the width of each cell in pixels
	 * 
	 * @param cellWidthInPixels the cellWidthInPixels to set
	 */
	public void setCellWidthInPixels (int cellWidthInPixels)
	{
		this.cellWidthInPixels = cellWidthInPixels;
	}
	
	/**
	 * Gets the height of each cell in pixels
	 * 
	 * @return the cellHeightInPixels
	 */
	public int getCellHeightInPixels ()
	{
		return cellHeightInPixels;
	}
	
	/**
	 * Sets the height of each cell in pixels
	 * 
	 * @param cellHeightInPixels the cellHeightInPixels to set
	 */
	public void setCellHeightInPixels (int cellHeightInPixels)
	{
		this.cellHeightInPixels = cellHeightInPixels;
	}
}
