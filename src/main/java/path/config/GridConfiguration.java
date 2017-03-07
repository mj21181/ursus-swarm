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
package path.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridConfiguration implements Serializable
{
	/**
	 * Generated
	 */
	private static final long		serialVersionUID	= -792263076277085076L;
	
	private int						rows				= 0;
	private int						cols				= 0;
	private double					gridDeclination		= 0.0;
	
	private String					pathToObstacleFile	= null;
	
	private LocationConfig			bottomLeft			= new LocationConfig();
	private LocationConfig			topRight			= new LocationConfig();
	
	private List<BlockedLocation>	blockedList			= new ArrayList<BlockedLocation>();
	
	public GridConfiguration ()
	{
		
	}
	
	/**
	 * @return the rows
	 */
	public int getRows ()
	{
		return rows;
	}
	
	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows (int rows)
	{
		this.rows = rows;
	}
	
	/**
	 * @return the cols
	 */
	public int getCols ()
	{
		return cols;
	}
	
	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols (int cols)
	{
		this.cols = cols;
	}
	
	public void addBlockedLocation (BlockedLocation b)
	{
		blockedList.add(b);
	}
	
	public List<BlockedLocation> getBlockedLocations ()
	{
		return blockedList;
	}
	
	/**
	 * @return the gridDeclination
	 */
	public double getGridDeclination ()
	{
		return gridDeclination;
	}
	
	/**
	 * @param gridDeclination
	 *            the gridDeclination to set
	 */
	public void setGridDeclination (double gridDeclination)
	{
		this.gridDeclination = gridDeclination;
	}
	
	/**
	 * @return the bottomLeft
	 */
	public LocationConfig getBottomLeft ()
	{
		return bottomLeft;
	}
	
	/**
	 * @param bottomLeft
	 *            the bottomLeft to set
	 */
	public void setBottomLeft (LocationConfig bottomLeft)
	{
		this.bottomLeft = bottomLeft;
	}
	
	/**
	 * @return the topRight
	 */
	public LocationConfig getTopRight ()
	{
		return topRight;
	}
	
	/**
	 * @param topRight
	 *            the topRight to set
	 */
	public void setTopRight (LocationConfig topRight)
	{
		this.topRight = topRight;
	}
	
	public void setPathToObstacleFile (String path)
	{
		pathToObstacleFile = path;
	}
	
	public String getPathToObstacleFile ()
	{
		return pathToObstacleFile;
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
		result = prime * result
				+ ((blockedList == null) ? 0 : blockedList.hashCode());
		result = prime * result
				+ ((bottomLeft == null) ? 0 : bottomLeft.hashCode());
		result = prime * result + cols;
		long temp;
		temp = Double.doubleToLongBits(gridDeclination);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((pathToObstacleFile == null) ? 0 : pathToObstacleFile
						.hashCode());
		result = prime * result + rows;
		result = prime * result
				+ ((topRight == null) ? 0 : topRight.hashCode());
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
		GridConfiguration other = (GridConfiguration) obj;
		if (blockedList == null)
		{
			if (other.blockedList != null) return false;
		}
		else if (!blockedList.equals(other.blockedList)) return false;
		if (bottomLeft == null)
		{
			if (other.bottomLeft != null) return false;
		}
		else if (!bottomLeft.equals(other.bottomLeft)) return false;
		if (cols != other.cols) return false;
		if (Double.doubleToLongBits(gridDeclination) != Double
				.doubleToLongBits(other.gridDeclination)) return false;
		if (pathToObstacleFile == null)
		{
			if (other.pathToObstacleFile != null) return false;
		}
		else if (!pathToObstacleFile.equals(other.pathToObstacleFile)) return false;
		if (rows != other.rows) return false;
		if (topRight == null)
		{
			if (other.topRight != null) return false;
		}
		else if (!topRight.equals(other.topRight)) return false;
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
		return "GridConfiguration [rows=" + rows + ", cols=" + cols
				+ ", gridDeclination=" + gridDeclination + ", bottomLeft="
				+ bottomLeft.toString() + ", topRight=" + topRight.toString()
				+ ", blockedList=" + Arrays.toString(blockedList.toArray())
				+ "]";
	}
	
}
