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
package pso.config;

public enum NeighborhoodTopology
{
	PERSONAL_BEST("PersonalBest"), GLOBAL_BEST("GlobalBest"), CIRCULAR(
			"Circular"), WHEEL("Wheel"), VON_NEUMANN("VonNeumann");
	
	private String	name	= null;
	
	private NeighborhoodTopology (String name)
	{
		this.name = name;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public static NeighborhoodTopology getNeighborhoodTopology (String nt)
	{
		if (nt.equalsIgnoreCase(PERSONAL_BEST.getName()))
		{
			return NeighborhoodTopology.PERSONAL_BEST;
		}
		else if (nt.equalsIgnoreCase(GLOBAL_BEST.getName()))
		{
			return NeighborhoodTopology.GLOBAL_BEST;
		}
		else if (nt.equalsIgnoreCase(CIRCULAR.getName()))
		{
			return NeighborhoodTopology.CIRCULAR;
		}
		else if (nt.equalsIgnoreCase(WHEEL.getName()))
		{
			return NeighborhoodTopology.WHEEL;
		}
		else if (nt.equalsIgnoreCase(VON_NEUMANN.getName()))
		{
			return NeighborhoodTopology.VON_NEUMANN;
		}
		else
		{
			return NeighborhoodTopology.GLOBAL_BEST;
		}
	}
};
