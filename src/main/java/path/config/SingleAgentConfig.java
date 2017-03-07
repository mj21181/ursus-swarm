package path.config;

import java.util.List;

import path.elements.Agent;
import path.elements.Constraint;
import path.elements.JumpPointConstraint;
import path.elements.map.ObstacleMap;
import path.elements.vertices.LocationVertex;

public class SingleAgentConfig implements EngineConfig
{
	private Agent						a				= null;
	private ObstacleMap					obs				= null;
	private List<Constraint>			constraints		= null;
	private List<JumpPointConstraint>	jpConstraints	= null;
	private boolean						jps				= false;
	private boolean						debug			= false;
	
	public SingleAgentConfig (int id, int startX, int startY, int startT,
			int targetX, int targetY, int targetT, boolean jpsOn,
			ObstacleMap obstacles, List<Constraint> cons)
	{
		jps = jpsOn;
		
		this.a = new Agent(id, new LocationVertex(startX, startY, startT),
				new LocationVertex(targetX, targetY, targetT));
		
		obs = obstacles;
		constraints = cons;
	}
	
	/**
	 * @return the startX
	 */
	public Agent getAgent ()
	{
		return a;
	}
	
	/**
	 * @param startX the startX to set
	 */
	public void setAgent (Agent a)
	{
		this.a = a;
	}
	
	/**
	 * @return the jps
	 */
	public boolean isJps ()
	{
		return jps;
	}
	
	/**
	 * @param jps the jps to set
	 */
	public void setJps (boolean jps)
	{
		this.jps = jps;
	}
	
	public void setDebug (boolean b)
	{
		debug = b;
	}
	
	public boolean getDebug ()
	{
		return debug;
	}
	
	/**
	 * @return the obs
	 */
	@Override
	public ObstacleMap getObstacles ()
	{
		return obs;
	}
	
	/**
	 * @param obs the obs to set
	 */
	public void setObstacles (ObstacleMap obs)
	{
		this.obs = obs;
	}
	
	/**
	 * @return the constraints
	 */
	public List<Constraint> getConstraints ()
	{
		return constraints;
	}
	
	public List<JumpPointConstraint> getJumpPointConstraints ()
	{
		return jpConstraints;
	}
	
	/**
	 * @param constraints the constraints to set
	 */
	public void setConstraints (List<Constraint> constraints)
	{
		this.constraints = constraints;
	}
	
	public void setJumpPointConstraints (List<JumpPointConstraint> cons)
	{
		this.jpConstraints = cons;
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
		result = prime * result + ( (a == null) ? 0 : a.hashCode());
		result = prime * result
				+ ( (constraints == null) ? 0 : constraints.hashCode());
		result = prime * result + (jps ? 1231 : 1237);
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
		SingleAgentConfig other = (SingleAgentConfig) obj;
		if (a == null)
		{
			if (other.a != null) return false;
		}
		else if (!a.equals(other.a)) return false;
		if (constraints == null)
		{
			if (other.constraints != null) return false;
		}
		else if (!constraints.equals(other.constraints)) return false;
		if (jps != other.jps) return false;
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
		StringBuilder sb = new StringBuilder();
		
		sb.append("SingleAgentConfig [a=");
		sb.append(a.toString());
		sb.append(", jps=");
		sb.append(jps);
		sb.append("] ");
		sb.append(System.lineSeparator());
		// sb.append(obs.toString());
		// sb.append(System.lineSeparator());
		sb.append("Constraints: ");
		sb.append(System.lineSeparator());
		
		for (Constraint c : constraints)
		{
			sb.append(c.toString());
			sb.append(System.lineSeparator());
		}
		
		return sb.toString();
	}
	
}
