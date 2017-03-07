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
package path;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import path.config.CBSConfig;
import path.config.EngineConfig;
import path.config.SingleAgentConfig;
import path.elements.Agent;
import path.elements.AgentPath;
import path.elements.VertexTypeException;
import path.elements.vertices.ConstraintTreeNode;
import path.elements.vertices.GraphVertex;
import path.search.multi.CBS;
import path.search.non.MisconfiguredSearch;
import path.search.single.SingleAgentAStar;
import path.search.single.SingleAgentJPS;
import log.ApplicationLogger;

/**
 * This class is responsible for determining which search needs to be run based
 * on the {@link EngineConfig}. After being configured the search can be run
 * using the {@link GraphSearchEngine#plan()} method.
 * 
 * The type of object returned by the plan method depends on which search type
 * was configured.
 * 
 * @author Mike Johnson
 *
 */
public class GraphSearchEngine
{
	/**
	 * A reference to the {@link GraphSearch} being performed by this engine
	 */
	protected GraphSearch					search			= null;
	
	/**
	 * The initial size of the Open List
	 */
	protected final int						listBufferSize	= 10;
	
	/**
	 * The Open List of {@link GraphVertex} objects which will be expanded
	 */
	protected PriorityQueue<GraphVertex>	openList		=
			new PriorityQueue<GraphVertex>(listBufferSize);
	
	/**
	 * Data structure for measuring search performance
	 */
	protected SearchPerformance				stats			=
			new SearchPerformance();
	
	/**
	 * Shortcut reference to the logger
	 */
	protected ApplicationLogger				log				=
			ApplicationLogger.getInstance();
	
	/**
	 * Constructs a new {@link GraphSearchEngine} based on the specified
	 * {@link EngineConfig} which will determine the type of search being
	 * performed.
	 * 
	 * @param cfg
	 */
	public GraphSearchEngine (EngineConfig cfg)
	{
		// multiple agent conflict based search
		if (cfg instanceof CBSConfig)
		{
			CBSConfig cbsConf = (CBSConfig) cfg;
			
			search = new CBS(cbsConf);
		}
		// single agent search
		else if (cfg instanceof SingleAgentConfig)
		{
			SingleAgentConfig saCfg = (SingleAgentConfig) cfg;
			
			// check if we are doing jump point search or A*
			if (saCfg.isJps())
			{
				search = new SingleAgentJPS(saCfg);
			}
			else
			{
				search = new SingleAgentAStar(saCfg);
			}
		}
		// we don't recognize the type of EngineConfig passed in
		else
		{
			search = new MisconfiguredSearch();
		}
	}
	
	/**
	 * Performs the {@link GraphSearch} that was configured and returns the
	 * solution object. The solution differs depending on the search type which
	 * was configured. Returns null if no solution exists.
	 * 
	 * @return
	 */
	public Object plan ()
	{
		log.logDebug("Initializing search");
		
		long startTime = System.currentTimeMillis();
		
		GraphVertex root = search.initialize();
		
		log.logDebug("Root Node: %s", root.toString());
		
		openList.add(root);
		
		if (!search.needsToRun()) { return search.getSolution(); }
		
		while (!openList.isEmpty())
		{
			GraphVertex n = getBestNode();
			
			// log.logDebug("Next Node: %s", n.toString());
			
			if(Thread.currentThread().isInterrupted())
			{
				return null;
			}
			
			try
			{
				if (search.expandSuccessorNodes(n, openList))
				{
					//log.logDebug("Found Goal! %s",
					//		search.getSolution().toString());
					
					Object sol = search.getSolution();
					
					populatePerformanceStats(startTime, sol);
					
					return sol;
				}
			}
			catch (VertexTypeException e)
			{
				log.logException(e);
				
				break;
			}
		}
		
		populatePerformanceStats(startTime, null);
		
		return null;
	}
	
	/**
	 * Gets the stats measuring search performance
	 * 
	 * @return
	 */
	public SearchPerformance getPerformanceStats ()
	{
		return stats;
	}
	
	/**
	 * Retrieves and removes the head vertex in the {@link Queue} and returns
	 * null if no more elements are present in the {@link Queue}
	 * 
	 * @return
	 */
	protected GraphVertex getBestNode ()
	{
		return openList.poll();
	}
	
	public Queue<GraphVertex> getOpenList ()
	{
		return openList;
	}
	
	public GraphSearch getSearch ()
	{
		return search;
	}
	
	protected void populatePerformanceStats (long startTime, Object sol)
	{
		long endTime = System.currentTimeMillis();
		
		stats.setTimeToRunInMilliseconds(endTime - startTime);
		
		if (search instanceof CBS)
		{
			stats.setHighLevel(true);
			
			if (sol == null) { return; }
			
			ConstraintTreeNode goalNode = (ConstraintTreeNode) sol;
			
			Map<Agent, AgentPath> solution = goalNode.getSolution();
			
			int len = 0;
			
			for (AgentPath ap : solution.values())
			{
				len += ap.getLength();
			}
			
			stats.setSolutionLength(len);
		}
		else
		{
			stats.setHighLevel(false);
			
			if (sol == null) { return; }
			
			AgentPath ap = (AgentPath) sol;
			
			stats.setSolutionLength(ap.getLength());
		}
		
		stats.setNumberOfNodesExpanded(
				openList.size() + search.getClosedListSize());
	}
}
