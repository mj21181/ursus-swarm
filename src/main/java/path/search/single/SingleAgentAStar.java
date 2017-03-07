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
package path.search.single;

import java.util.LinkedList;
import java.util.List;
// import org.jgraph.graph.DefaultEdge;
// import org.jgrapht.graph.ListenableUndirectedGraph;

import path.config.SingleAgentConfig;
import path.elements.AgentPath;
import path.elements.vertices.LocationVertex;

/**
 * This class is an implementation of the Single Agent A Star path planning
 * algorithm.
 * 
 * This class assumes a uniform cost 8 connect grid.
 * 
 * @author Mike Johnson
 *
 */
public class SingleAgentAStar extends AbstractSingleAgentSearch
{
	/**
	 * Constructs a {@link SingleAgentAStar} object from the
	 * {@link SingleAgentConfig}
	 * 
	 * @param cfg
	 */
	public SingleAgentAStar (SingleAgentConfig cfg)
	{
		super(cfg);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.single.AbstractSingleAgentSearch#calculateGScore(LocationVertex
	 * current, LocationVertex successor);
	 */
	@Override
	protected double calculateGScore (LocationVertex current,
			LocationVertex successor)
	{
		boolean diag = checkIfDiagonal(current, successor);
		
		if (diag) { return current.getG() + Math.sqrt(2); }
		
		return current.getG() + 1;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.single.AbstractSingleAgentSearch#findSuccessors(LocationVertex
	 * current);
	 */
	@Override
	protected List<LocationVertex> findSuccessors (LocationVertex current)
	{
		LinkedList<LocationVertex> successors =
				new LinkedList<LocationVertex>();
		
		int x = current.getX() + 1;
		int y = current.getY();
		int t = current.getT() + 1;
		
		LocationVertex s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX() + 1;
		y = current.getY() + 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX() + 1;
		y = current.getY() - 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX() - 1;
		y = current.getY();
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX() - 1;
		y = current.getY() + 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX() - 1;
		y = current.getY() - 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX();
		y = current.getY() - 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX();
		y = current.getY() + 1;
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		x = current.getX();
		y = current.getY();
		t = current.getT() + 1;
		
		s = new LocationVertex(x, y, t);
		
		if (!checkIfBlocked(x, y, t))
		{
			successors.add(s);
		}
		
		return successors;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.single.AbstractSingleAgentSearch#optionallyPadPath(AgentPath
	 * p);
	 */
	@Override
	protected void optionallyPadPath (AgentPath p)
	{
		return;
	}
	
}
