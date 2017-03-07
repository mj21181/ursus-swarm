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
package path.search.non;

import java.util.Queue;

// import org.jgraph.graph.DefaultEdge;
// import org.jgrapht.graph.ListenableUndirectedGraph;

import path.GraphSearch;
import path.GraphSearchEngine;
import path.config.EngineConfig;
import path.elements.VertexTypeException;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

/**
 * This class is what the {@link GraphSearchEngine} is configured to use when
 * the {@link EngineConfig} is not known and no search c
 * 
 * @author Mike Johnson
 * 
 */
public class MisconfiguredSearch implements GraphSearch
{
	
	/**
	 * 
	 */
	public MisconfiguredSearch ()
	{
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#initialize()
	 */
	@Override
	public GraphVertex initialize ()
	{
		return new LocationVertex(-1, -1, -1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#expandSuccessorNodes(edu.unh.acl
	 * .app.model.path.elements.GraphVertex, java.util.Queue)
	 */
	@Override
	public boolean expandSuccessorNodes (GraphVertex current,
			Queue<GraphVertex> openList) throws VertexTypeException
	{
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#getSolution()
	 */
	@Override
	public Object getSolution ()
	{
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#needsToRun()
	 */
	@Override
	public boolean needsToRun ()
	{
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.GraphSearch#getClosedListSize()
	 */
	@Override
	public int getClosedListSize ()
	{
		return 0;
	}
	
}
