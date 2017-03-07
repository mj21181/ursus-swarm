package path.config;

import java.util.ArrayList;

import path.elements.Agent;
import path.elements.map.ObstacleMap;

public class CBSConfig implements EngineConfig
{
	private ArrayList<Agent>	agents	= null;
	private ObstacleMap			obs		= null;
	private boolean				debug 	= false;
	
	public CBSConfig (ArrayList<Agent> a, ObstacleMap obstacles)
	{
		agents = a;
		obs = obstacles;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Agent> getAgents ()
	{
		return agents;
	}
	
	/**
	 * @param agents the agents to set
	 */
	public void setAgents (ArrayList<Agent> agents)
	{
		this.agents = agents;
	}
	
	/**
	 * @param obs the obs to set
	 */
	public void setObstacles (ObstacleMap obs)
	{
		this.obs = obs;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.config.EngineConfig#getObstacles()
	 */
	@Override
	public ObstacleMap getObstacles ()
	{
		return obs;
	}
	
	public void setDebug (boolean b)
	{
		debug = b;
	}
	
	public boolean getDebug ()
	{
		return debug;
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
		result = prime * result + ( (agents == null) ? 0 : agents.hashCode());
		result = prime * result + ( (obs == null) ? 0 : obs.hashCode());
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
		CBSConfig other = (CBSConfig) obj;
		if (agents == null)
		{
			if (other.agents != null) return false;
		}
		else if (!agents.equals(other.agents)) return false;
		if (obs == null)
		{
			if (other.obs != null) return false;
		}
		else if (!obs.equals(other.obs)) return false;
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
		return String.format("CBSConfig [agents=%s]", agents);
	}
}
