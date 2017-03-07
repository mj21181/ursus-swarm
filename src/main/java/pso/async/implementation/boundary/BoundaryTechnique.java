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
package pso.async.implementation.boundary;

/**
 * 
 * @author Mike Johnson
 * 
 */
public enum BoundaryTechnique
{
	NONE("None"),
	INF("Infinity"),
	PULIDO_COELLO("PulidoCoello"),
	P_BEST("PBest"),
	RANDOM("Random"),
	REFLECT_UNMODIFIED("ReflectUnmodified"),
	REFLECT_ABSORB("ReflectAbsorb"),
	REFLECT_ADJUST("ReflectAdjust"),
	NEAREST_UNMODIFIED("NearestUnmodified"),
	NEAREST_ABSORB("NearestAbsorb"),
	NEAREST_DETERMINISTIC_BACK("NearestDeterministicBack"),
	NEAREST_RANDOM_BACK("NearestRandomBack"),
	NEAREST_ADJUST("NearestAdjust"),
	HYPERBOLIC("Hyperbolic"),
	RANDOM_FORTH("RandomForth"),
	BOUNDED_MIRROR("BoundedMirror"),
	PERIODIC("Periodic");
	
	private String	name	= null;
	
	private BoundaryTechnique (String name)
	{
		this.name = name;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public static BoundaryTechnique getBoundaryTechnique (String btech)
	{
		if (btech.equalsIgnoreCase(NONE.getName()))
		{
			return BoundaryTechnique.NONE;
		}
		else if (btech.equalsIgnoreCase(INF.getName()))
		{
			return BoundaryTechnique.INF;
		}
		else if (btech.equalsIgnoreCase(PULIDO_COELLO.getName()))
		{
			return BoundaryTechnique.PULIDO_COELLO;
		}
		else if (btech.equalsIgnoreCase(P_BEST.getName()))
		{
			return BoundaryTechnique.P_BEST;
		}
		else if (btech.equalsIgnoreCase(RANDOM.getName()))
		{
			return BoundaryTechnique.RANDOM;
		}
		else if (btech.equalsIgnoreCase(REFLECT_UNMODIFIED.getName()))
		{
			return BoundaryTechnique.REFLECT_UNMODIFIED;
		}
		else if (btech.equalsIgnoreCase(REFLECT_ABSORB.getName()))
		{
			return BoundaryTechnique.REFLECT_ABSORB;
		}
		else if (btech.equalsIgnoreCase(REFLECT_ADJUST.getName()))
		{
			return BoundaryTechnique.REFLECT_ADJUST;
		}
		else if (btech.equalsIgnoreCase(NEAREST_UNMODIFIED.getName()))
		{
			return BoundaryTechnique.NEAREST_UNMODIFIED;
		}
		else if (btech.equalsIgnoreCase(NEAREST_ABSORB.getName()))
		{
			return BoundaryTechnique.NEAREST_ABSORB;
		}
		else if (btech.equalsIgnoreCase(NEAREST_DETERMINISTIC_BACK.getName()))
		{
			return BoundaryTechnique.NEAREST_DETERMINISTIC_BACK;
		}
		else if (btech.equalsIgnoreCase(NEAREST_RANDOM_BACK.getName()))
		{
			return BoundaryTechnique.NEAREST_RANDOM_BACK;
		}
		else if (btech.equalsIgnoreCase(NEAREST_ADJUST.getName()))
		{
			return BoundaryTechnique.NEAREST_ADJUST;
		}
		else if (btech.equalsIgnoreCase(HYPERBOLIC.getName()))
		{
			return BoundaryTechnique.HYPERBOLIC;
		}
		else if (btech.equalsIgnoreCase(RANDOM_FORTH.getName()))
		{
			return BoundaryTechnique.RANDOM_FORTH;
		}
		else if (btech.equalsIgnoreCase(BOUNDED_MIRROR.getName()))
		{
			return BoundaryTechnique.BOUNDED_MIRROR;
		}
		else if (btech.equalsIgnoreCase(PERIODIC.getName()))
		{
			return BoundaryTechnique.PERIODIC;
		}
		else
		{
			return BoundaryTechnique.NONE;
		}
	}
}
