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
package path.elements;

import log.ApplicationLogger;
import path.elements.vertices.LocationVertex;

/**
 * @author Mike Johnson
 *
 */
public class JumpPointConstraint
{
	protected LocationVertex	jumpPoint	= null;
	
	protected JumpPointType		type		= null;
	
	protected int				DX			= -1;
	
	protected int				DY			= -1;
	
	protected int				DT			= -1;
	
	/**
	 * 
	 */
	public JumpPointConstraint (LocationVertex one, LocationVertex two)
	{
		DX = two.getX() - one.getX();
		DY = two.getY() - one.getY();
		DT = two.getT() - one.getT();
		
		jumpPoint = one;
		
		if (DT < 0) { throw new IllegalArgumentException(
				"Jump Point Constraint cannot have DT negative one="
						+ one.toString() + " two=" + two.toString()); }
		
		if (DX == 0)
		{
			if (DY == 0)
			{
				type = JumpPointType.TIME;
			}
			else
			{
				type = JumpPointType.VERTICAL;
			}
		}
		else
		{
			if (DY == 0)
			{
				type = JumpPointType.HORIZONTAL;
			}
			else
			{
				type = JumpPointType.DIAGONAL;
			}
		}
	}
	
	protected JumpPointConstraint (JumpPointConstraint c)
	{
		this.jumpPoint = new LocationVertex(c.jumpPoint);
		this.type = c.getType();
		this.DX = c.getDX();
		this.DY = c.getDY();
		this.DT = c.getDT();
	}
	
	public JumpPointConstraint copy ()
	{
		return new JumpPointConstraint(this);
	}
	
	/**
	 * @return the jumpPoint
	 */
	public LocationVertex getJumpPoint ()
	{
		return jumpPoint;
	}
	
	/**
	 * @return the type
	 */
	public JumpPointType getType ()
	{
		return type;
	}
	
	/**
	 * @return the dX
	 */
	public int getDX ()
	{
		return DX;
	}
	
	/**
	 * @return the dY
	 */
	public int getDY ()
	{
		return DY;
	}
	
	/**
	 * @return the dT
	 */
	public int getDT ()
	{
		return DT;
	}
	
	public boolean applies (LocationVertex test, int agentDirX, int agentDirY,
			boolean debug)
	{
		if (agentDirX > 1
				|| agentDirX < -1) { throw new IllegalArgumentException(
						"Agent Direction in X must be -1, 0, or 1"); }
		
		if (agentDirY > 1
				|| agentDirY < -1) { throw new IllegalArgumentException(
						"Agent Direction in Y must be -1, 0, or 1"); }
		
		int testDX = test.getX() - jumpPoint.getX();
		int testDY = test.getY() - jumpPoint.getY();
		int testT = test.getT();
		
		if (debug)
		{
			ApplicationLogger.getInstance().log("TestDX: " + testDX);
			ApplicationLogger.getInstance().log("TestDT: " + testDY);
			ApplicationLogger.getInstance().log("TestT: " + testT);
		}
		
		boolean constraintApplies = false;
		
		switch (type)
		{
			case DIAGONAL:
				constraintApplies = appliesDiagonal(testDX, testDY, testT,
						agentDirX, agentDirY);
				break;
			case HORIZONTAL:
				constraintApplies =
						appliesHorizontal(testDX, testDY, testT, agentDirX);
				break;
			case TIME:
				constraintApplies = appliesTime(testDX, testDY, testT);
				break;
			case VERTICAL:
				constraintApplies =
						appliesVertical(testDX, testDY, testT, agentDirY);
				break;
			default:
				throw new IllegalStateException(
						"Jump Point Constraint did not have defined type: "
								+ this.toString());
				
		}
		
		return constraintApplies;
	}
	
	protected boolean appliesVertical (int testDX, int testDY, int testT,
			int agentDirY)
	{
		// check the test location is also vertical up or down from the jump
		// point
		if (testDX == 0)
		{
			// if the second jump point's location distance up or down is
			// greater than or equal to the test point
			if (Math.abs(DY) >= Math.abs(testDY))
			{
				// the time it takes the other agent to reach this point from
				// the jump point
				int agentT = jumpPoint.getT() + Math.abs(testDY);
				
				// if the time the other agent reaches this test point is the
				// same as the time of the test point, return true
				if (testT == agentT) { return true; }
				
				// check for edge conflict
				int dirY = getDirection(DY);
				
				// moving opposite direction
				if (dirY == -agentDirY)
				{
					// if the time it takes the other agent to get to the test
					// point is one more than the test point's time, and they
					// were moving opposite directions it is an edge conflict
					if (agentT == testT + 1) { return true; }
				}
			}
		}
		
		return false;
	}
	
	protected boolean appliesHorizontal (int testDX, int testDY, int testT,
			int agentDirX)
	{
		// check the test location is also horizontal up or down from the jump
		// point
		if (testDY == 0)
		{
			// if the second jump point's location distance left or right is
			// greater than or equal to the test point
			if (Math.abs(DX) >= Math.abs(testDX))
			{
				// the time it takes the other agent to reach this point from
				// the jump point
				int agentT = jumpPoint.getT() + Math.abs(testDX);
				
				// if the time the other agent reaches this test point is the
				// same as the time of the test point, return true
				if (testT == agentT) { return true; }
				
				// check for edge conflict
				int dirX = getDirection(DX);
				
				// moving opposite direction
				if (dirX == -agentDirX)
				{
					// if the time it takes the other agent to get to the test
					// point is one more than the test point's time, and they
					// were moving opposite directions it is an edge conflict
					if (agentT == testT + 1) { return true; }
				}
			}
		}
		
		return false;
	}
	
	protected boolean appliesTime (int testDX, int testDY, int testT)
	{
		// check it's the same X location as the jump point
		if (testDX == 0)
		{
			// check it's the same Y location as the jump point
			if (testDY == 0)
			{
				int testDT = testT - jumpPoint.getT();
				
				// if the next jump point is the same DT or greater than the
				// test point, the constraint applies
				if (DT >= testDT) { return true; }
			}
		}
		
		return false;
	}
	
	protected boolean appliesDiagonal (int testDX, int testDY, int testT,
			int agentDirX, int agentDirY)
	{
		// check if the test point is diagonal from the jump point
		if (Math.abs(testDX) == Math.abs(testDY))
		{
			// get the direction to the next jump point
			int dirX = getDirection(DX);
			int dirY = getDirection(DY);
			
			// get the direction to the test point
			int dirTX = getDirection(testDX);
			int dirTY = getDirection(testDY);
			
			// check they are in the same direction
			if (dirX == dirTX)
			{
				if (dirY == dirTY)
				{
					// the time it takes the other agent to reach this point
					// from the jump point. note it does not matter that magX is
					// being added when it is Y because we checked they are
					// equal above
					int agentT = jumpPoint.getT() + Math.abs(testDX);
					
					if (testT == agentT) { return true; }
					
					// moving opposite direction
					if (dirX == -agentDirX)
					{
						if (dirY == -agentDirY)
						{
							// if the time it takes the other agent to get to
							// the test point is one more than the test point's
							// time, and they were moving opposite directions it
							// is an edge conflict
							if (agentT == testT + 1) { return true; }
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks the delta value and returns the direction it is headed
	 * 
	 * @param delta
	 * @return
	 */
	protected int getDirection (int delta)
	{
		return ( (delta < 0) ? -1 : (delta > 0) ? 1 : 0);
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
		result = prime * result + DT;
		result = prime * result + DX;
		result = prime * result + DY;
		result = prime * result
				+ ( (jumpPoint == null) ? 0 : jumpPoint.hashCode());
		result = prime * result + ( (type == null) ? 0 : type.hashCode());
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
		JumpPointConstraint other = (JumpPointConstraint) obj;
		if (DT != other.DT) return false;
		if (DX != other.DX) return false;
		if (DY != other.DY) return false;
		if (jumpPoint == null)
		{
			if (other.jumpPoint != null) return false;
		}
		else if (!jumpPoint.equalsWithTime(other.jumpPoint)) return false;
		if (type != other.type) return false;
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
		return String.format(
				"JumpPointConstraint [jumpPoint=%s, type=%s, DX=%s, DY=%s, DT=%s]",
				jumpPoint, type.getTypeAsString(), DX, DY, DT);
	}
	
}
