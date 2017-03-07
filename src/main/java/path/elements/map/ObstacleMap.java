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

import java.util.Arrays;

/**
 * This class is used to represent the locations of obstacles in the uniform cost grid
 * 
 * @author Mike Johnson
 *
 */
public class ObstacleMap
{
	/**
	 * The 2D array of obstacle locations
	 */
	protected boolean[][]	map	= null;
	
	/**
	 * Constructs an {@link ObstacleMap} of the specified number of rows and columns
	 * 
	 * @param rows
	 * @param cols
	 */
	public ObstacleMap (int rows, int cols)
	{
		map = new boolean[rows][cols];
	}
	
	/**
	 * Gets the number of columns
	 * 
	 * @return
	 */
	public int getCols ()
	{
		return map[0].length;
	}
	
	/**
	 * Gets the number of rows
	 * 
	 * @return
	 */
	public int getRows ()
	{
		return map.length;
	}
	
	/**
	 * Marks a location in the map as blocked
	 * 
	 * @param row
	 * @param col
	 */
	public void block (int row, int col)
	{
		map[row][col] = true;
	}
	
	/**
	 * Marks a location in the map as blocked using x, y coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public void blockXY (int x, int y)
	{
		map[y][x] = true;
	}
	
	/**
	 * Marks a location in the map as not blocked
	 * 
	 * @param row
	 * @param col
	 */
	public void unblock (int row, int col)
	{
		map[row][col] = false;
	}
	
	/**
	 * Marks a location in the map as not blocked using x, y coordinates
	 * 
	 * @param x
	 * @param y
	 */
	public void unblockXY (int x, int y)
	{
		map[y][x] = false;
	}
	
	/**
	 * Checks if a location in the map is blocked. True if it is blocked
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isBlocked (int row, int col)
	{
		if(vertexExists(row, col))
		{
			return map[row][col];			
		}
		
		return true;
	}
	
	/**
	 * Checks if a location in the map is blocked using x, y coordinates. True if it is blocked
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isBlockedXY (int x, int y)
	{
		if(vertexExists(y, x))
		{
			return map[y][x];			
		}
		
		return true;
	}
	
	/**
	 * Checks whether a given node exists within the bounds of the search space
	 * 
	 * @param row
	 * @param col
	 * @return true if the vertex is within bounds
	 */
	public boolean vertexExists (int row, int col)
	{
		if (col < 0) { return false; }
		
		if (row < 0) { return false; }
		
		if (col >= getCols()) { return false; }
		
		if (row >= getRows()) { return false; }
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode ()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(map);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ObstacleMap other = (ObstacleMap) obj;
		if (!Arrays.deepEquals(map, other.map)) return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString ()
	{
		String msg =
				"Obstacle Map (0, 0) bottom left:" + System.lineSeparator();
		
		for (int i = getRows() - 1; i >= 0; i-- )
		{
			for (int j = 0; j < getCols(); j++ )
			{
				if (map[i][j])
				{
					msg += " true, ";
				}
				else
				{
					msg += "false, ";
				}
			}
			
			msg += System.lineSeparator();
		}
		
		return msg;
	}
}
