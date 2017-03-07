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
package path.elements.heuristic;

import java.util.Map;

import log.ApplicationLogger;
import path.elements.Agent;
import path.elements.AgentPath;
import path.elements.VertexTypeException;
import path.elements.vertices.ConstraintTreeNode;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This heuristic is used to calculate the cost of different
 * {@link ConstraintTreeNode}s
 * 
 * @author Mike Johnson
 * 
 */
public class SumInCostsHeuristic implements GraphHeuristic
{
	protected boolean debug = false;
	
	public SumInCostsHeuristic (boolean debug)
	{
		this.debug = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.elements.GraphHeuristic#calculateCost(edu.
	 * unh.acl.app.model.path.elements.GraphVertex, path.elements.GraphVertex)
	 */
	@Override
	public double calculateCost (GraphVertex v1, GraphVertex v2)
			throws VertexTypeException
	{
		ConstraintTreeNode n = (ConstraintTreeNode) v1;
		
		int sum = 0;
		
		Map<Agent, AgentPath> sol = n.getSolution();
		
		for (Agent a : sol.keySet())
		{
			int pathLen = 0;
			
			AgentPath pth = sol.get(a);
			
			boolean add = true;
			
			int lastCount = 0;
			
			// LocationVertex lastV = null;
			
			for (GraphVertex v : pth.getAsList())
			{
				LocationVertex l = (LocationVertex) v;
				
				if (debug)
				{
					ApplicationLogger.getInstance().log(l.toString());
				}
				
				if (l.equals(a.getTarget()))
				{
					add = false;
					lastCount = pathLen;
					
					if (debug)
					{
						ApplicationLogger.getInstance()
								.log("Found Goal, last count: " + lastCount);
					}
				}
				else
				{
					if (!add)
					{
						double diff = pathLen - lastCount;
						pathLen += diff;
						
						if (debug)
						{
							ApplicationLogger.getInstance()
									.log("Moved Off Goal: diff: " + diff
											+ " pl: " + pathLen);
						}
					}
					
					add = true;
				}
				
				if (add)
				{
					pathLen++ ;
				}
			}
			
			if (debug)
			{
				ApplicationLogger.getInstance().log("Agent %s Cost is: %d",
						a.toString(), pathLen);
			}
			
			sum += pathLen;
		}
		
		if (debug)
		{
			ApplicationLogger.getInstance().log("Total Cost: %d", sum);
		}
		
		return sum;
	}
	
	protected boolean isDiagonal (LocationVertex c, LocationVertex l)
	{
		int sX = c.getX();
		int sY = c.getY();
		int cX = l.getX();
		int cY = l.getY();
		
		if ( (sX == (cX - 1) && sY == (cY - 1))
				|| (sX == (cX + 1) && sY == (cY - 1))
				|| (sX == (cX + 1) && sY == (cY + 1))
				|| (sX == (cX - 1) && sY == (cY + 1))) { return true; }
		return false;
	}
}
