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

/**
 * This interface allows a search algorithm to trivially swap out different
 * types of heuristics for use in search.
 * 
 * @author Mike Johnson
 *
 */
public interface GraphHeuristic
{
	/**
	 * Calculates the H Score between two given vertices.
	 * 
	 * Throws a {@link VertexTypeException} if the {@link GraphVertex} type is
	 * the wrong kind for that heuristic.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 * @throws VertexTypeException
	 */
	public double calculateCost (GraphVertex v1, GraphVertex v2)
			throws VertexTypeException;
}
