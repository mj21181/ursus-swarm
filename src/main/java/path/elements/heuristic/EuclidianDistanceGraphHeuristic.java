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

import path.elements.VertexTypeException;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This class implements the {@link GraphHeuristic} interface and is used for
 * calculating the euclidian distance between two {@link LocationVertex}
 * objects.
 * 
 * @author Mike Johnson
 *
 */
public class EuclidianDistanceGraphHeuristic implements GraphHeuristic
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphHeuristic#calculateCost(edu.unh.acl.app
	 * .model.path.GraphVertex, path.GraphVertex)
	 */
	@Override
	public double calculateCost (GraphVertex v1, GraphVertex v2)
			throws VertexTypeException
	{
		if (! (v1 instanceof LocationVertex)
				|| ! (v2 instanceof LocationVertex)) { throw new VertexTypeException(
						"Input vertex not of type LocationVertex"); }
		
		LocationVertex l1 = (LocationVertex) v1;
		LocationVertex l2 = (LocationVertex) v2;
		
		return Math.sqrt(Math.pow(l2.getX() - l1.getX(), 2)
				+ Math.pow(l2.getY() - l1.getY(), 2));
	}
	
}
