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

import java.util.ArrayList;
import java.util.List;

import path.config.SingleAgentConfig;
import path.elements.AgentPath;
import path.elements.JumpPointConstraint;
import path.elements.vertices.GraphVertex;
import path.elements.vertices.LocationVertex;

public class SingleAgentJPS extends AbstractSingleAgentSearch
{
	protected List<JumpPointConstraint> jmpCons = null;
	
	public SingleAgentJPS (SingleAgentConfig conf)
	{
		super(conf);
		
		if (conf.getJumpPointConstraints() == null)
		{
			throw new IllegalArgumentException(
					"Jump Point Constraints not set!");
		}
		else
		{
			jmpCons = conf.getJumpPointConstraints();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.search.AbstractSingleAgentSearch#calculateGScore
	 * (path.vertices.LocationVertex, path.vertices.LocationVertex)
	 */
	@Override
	protected double calculateGScore (LocationVertex current,
			LocationVertex successor)
	{
		double nextCost;
		// TODO: Fix for JPS
		// if it's a diagonal move, the added cost is the sqrt(2)
		if (checkIfDiagonal(current, successor))
		{
			nextCost = current.getG() + 1.41421356237;
		}
		// if it's the same node and goal, but waiting there is no cost because
		// no move
		else if ( (current.getX() == successor.getX())
				&& (current.getY() == successor.getY())
				&& (current.getX() == goal.getX()
						&& (current.getY() == goal.getY())))
		{
			nextCost = current.getG();
		}
		// otherwise the added cost is 1, including waiting at the same vertex
		else
		{
			nextCost = current.getG() + 1.0;
		}
		
		return nextCost;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.search.AbstractSingleAgentSearch#findSuccessors
	 * (path.vertices.LocationVertex)
	 */
	@Override
	protected List<LocationVertex> findSuccessors (LocationVertex current)
	{
		
		ArrayList<LocationVertex> successors = new ArrayList<LocationVertex>();
		
		List<LocationVertex> neighbors = current.getChildren();
		
		for (LocationVertex neighbor : neighbors)
		{
			// ApplicationLogger.getInstance()
			// .logDebug("Neighbor: " + neighbor.toString());
			
			// set the parent of the immediate neighbors
			// setParentIfApplicable(neighbor.getXIndex(), neighbor.getYIndex(),
			// current.getXIndex(), current.getXIndex(), map);
			
			// Direction from current node to neighbor:
			int dX = (int) clamp(neighbor.getX() - current.getX(), -1, 1);
			int dY = (int) clamp(neighbor.getY() - current.getY(), -1, 1);
			
			// ApplicationLogger.getInstance().logDebug(
			// "Direction x: %d y: %d", dX, dY);
			
			// Try to find a node to jump to:
			LocationVertex jumpPoint = jump(current, dX, dY);
			
			// If found add it to the list:
			if (jumpPoint != null)
			{
				if (debug)
				{
					log.log("Current Location: " + current.toString()
							+ " Jump Point: " + jumpPoint.toString());
				}
				
				boolean contains = false;
				
				for (LocationVertex v : successors)
				{
					if (v.equalsWithTime(jumpPoint))
					{
						contains = true;
						break;
					}
				}
				
				// for(LocationVertex v : closedList)
				// {
				// if(v.equalsWithTime(jumpPoint))
				// {
				// contains = true;
				// break;
				// }
				// }
				
				if (!contains)
				{
					if (debug)
					{
						log.log("Successors does not contain: "
								+ jumpPoint.toString());
					}
					
					successors.add(jumpPoint);
				}
			}
		}
		
		// ApplicationLogger.getInstance().logDebug("Found Successors: %s",
		// Arrays.toString(successors.toArray()));
		
		// check a corner case where we would have found some successor normally
		// but we got some high level constraint that we couldn't jump at the
		// next timestep. We just add our immediate neighbors and take the
		// momentary inefficiency in exchange for actually discovering the
		// solution
		
		if (successors.isEmpty())
		{
			// Collections.shuffle(neighbors);
			
			for (LocationVertex n : neighbors)
			{
				if (!checkIfBlocked(n.getX(), n.getY(), n.getT()))
				{
					int dirX = (int) clamp(n.getX() - current.getX(), -1, 1);
					int dirY = (int) clamp(n.getY() - current.getY(), -1, 1);
					
					if (!checkIfBlockedByJumpPointConstraint(n, dirX, dirY,
							false))
					{
						successors.add(n);
					}
				}
			}
		}
		
		if (debug)
		{
			log.log("Found successors");
		}
		
		return successors;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.search.single.AbstractSingleAgentSearch#
	 * optionallyPadPath(path.search.AgentPath)
	 */
	@Override
	protected void optionallyPadPath (AgentPath p)
	{
		ArrayList<GraphVertex> pathAsList = p.getAsList();
		ArrayList<GraphVertex> replacement = new ArrayList<GraphVertex>();
		
		for (int i = 0; i < pathAsList.size() - 1; i++ )
		{
			LocationVertex current = (LocationVertex) pathAsList.get(i);
			LocationVertex next = (LocationVertex) pathAsList.get(i + 1);
			
			// ApplicationLogger.getInstance().logDebug("Current: %s",
			// current.toString());
			// ApplicationLogger.getInstance().logDebug("Next: %s",
			// next.toString());
			
			int jX = current.getX();
			int jY = current.getY();
			int jT = current.getT();
			
			// Direction from current node to successor:
			int dX = (int) clamp(next.getX() - current.getX(), -1, 1);
			int dY = (int) clamp(next.getY() - current.getY(), -1, 1);
			int dT = (int) clamp(next.getT() - current.getT(), 1, 1);
			
			// ApplicationLogger.getInstance()
			// .logDebug();
			
			if (debug)
			{
				log.log("From: " + current.toString());
				log.log("To: " + next.toString());
				log.log("Adding until: JX: " + jX + " JY: " + jY + " JT: "
						+ jT);
				log.log("DX: " + dX + " DY: " + dY + " DT: " + dT);
			}
			
			while (! (jX == next.getX() && jY == next.getY()
					&& jT == next.getT()))
			{
				LocationVertex pad = new LocationVertex(jX, jY, jT);
				
				pad.setParent(new LocationVertex(jX + dX, jY + dY, jT + dT));
				
				replacement.add(pad);
				
				// if (debug)
				// {
				// log.log("Adding pad: " + pad.toString());
				// }
				
				if (jX != next.getX())
				{
					jX += dX;
				}
				
				if (jY != next.getY())
				{
					jY += dY;
				}
				
				if (jT != next.getT())
				{
					jT += dT;
				}
			}
		}
		
		// add in the last one
		replacement.add(pathAsList.get(pathAsList.size() - 1));
		
		pathAsList.clear();
		pathAsList.addAll(replacement);
	}
	
	/**
	 * Clamps a given value by a given max and given min. If the value is NaN,
	 * the result is NaN
	 * 
	 * @param number The input value
	 * @param min The minimum
	 * @param max The maximum
	 * @return The clamped value
	 */
	protected double clamp (double number, double min, double max)
	{
		return Math.max(Math.min(number, max), min);
	}
	
	/**
	 * if the location is the next location of another robot, it's just going to
	 * cause another conflict and another planned path, so we don't want to
	 * cross those
	 * 
	 * 
	 * @param x
	 * @param y
	 * @param t
	 * @return
	 */
	protected boolean checkIfBlockedByJumpPointConstraint (LocationVertex test,
			int dirX, int dirY, boolean dbg)
	{
		for (JumpPointConstraint c : jmpCons)
		{
			if (c.applies(test, dirX, dirY, dbg))
			{
				
				if (debug)
				{
					log.log("Constraint applies for " + test.toString());
					log.log("Constraint: " + c.toString());
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This method is at the heart of the Jump Point Search algorithm.
	 * 
	 * An excellent explanation can be found here:
	 * http://zerowidth.com/2013/05/05/jump-point-search-explained.html
	 * 
	 * @param current The the current node having its neighbors expanded
	 * @param directionX The X value of the neighbor of the current node in
	 *            relation to the current node
	 * @param directionY The Y value of the neighbor of the current node in
	 *            relation to the current node
	 * @return The {@link LocationVertex} of the jump point which is the
	 *         successor of the current node, or null if one does not exist in
	 *         this direction
	 */
	protected LocationVertex jump (LocationVertex current, int directionX,
			int directionY)
	{
		// get the indicies of the next neighbor to check
		int nextX = current.getX() + directionX;
		int nextY = current.getY() + directionY;
		int nextT = current.getT() + 1;
		
		// ApplicationLogger.getInstance().logDebug(
		// "Potential Jump Point: %d, %d, %d", nextX, nextY, nextT);
		
		if(Thread.currentThread().isInterrupted())
		{
			return null;
		}
		
		// if the next neighbor is an obstacle, return null
		if (checkIfBlocked(nextX, nextY, nextT)) { return null; }
		
		// ApplicationLogger.getInstance().logDebug("Point not blocked");
		
		LocationVertex next = new LocationVertex(nextX, nextY, nextT);
		
		next.setParent(current);
		
		// if a jump point constraint is blocking, return null
		if (checkIfBlockedByJumpPointConstraint(next, directionX, directionY,
				false)) { return null; }
		
		// if the next location is the target, return it! we definitely want to
		// search that one
		if (next.equals(goal))
		{
			if (goal.getT() == -1)
			{
				return next;
			}
			else
			{
				return checkTime(next, current);//checkGoal(next, current);
			}
		}
		
		// when we are staying on the same vertex
		if (directionX == 0 && directionY == 0)
		{
			// jumping forward in time
			return checkTime(next, current);
		}
		else if (directionX != 0 && directionY != 0)
		{
			// if(debug)
			// {
			// log.log("Diagonal Move");
			// }
			
			// when we are moving diagonally
			return checkDiagonal(next, directionX, directionY);
		}
		else
		{
			if (directionX != 0)
			{
				// if(debug)
				// {
				// log.log("Horizontal Move");
				// }
				
				// when we are moving horizontally
				return checkHorizontal(next, directionX);
			}
			else
			{
				// if(debug)
				// {
				// log.log("Vertical Move");
				// }
				
				// when we are moving vertically
				
				return checkVertical(next, directionY);
			}
		}
	}
	
	protected LocationVertex checkTime (LocationVertex next,
			LocationVertex current)
	{
		// initialize the offset values which will increment
		int offsetX = next.getX();
		int offsetY = next.getY();
		int offsetT = next.getT();
		
		LocationVertex lastOffsetVertex = current;
		LocationVertex offsetVertex =
				new LocationVertex(offsetX, offsetY, offsetT);
		
		offsetVertex.setParent(current);
		
		// if the goal becomes blocked it is because a constraint has appeared
		// there
		while (!checkIfBlocked(offsetVertex.getX(), offsetVertex.getY(),
				offsetVertex.getT()))
		{
			for (int xDir = -1; xDir < 2; xDir++ )
			{
				for (int yDir = -1; yDir < 2; yDir++ )
				{
					// check vertical
					if (xDir == 0 && yDir != 0)
					{
						if (jump(offsetVertex, 0, yDir) != null)
						{
							// return the current neighbor as the jump point
							return offsetVertex;
						}
					}
					// check horizontal
					else if (xDir != 0 && yDir == 0)
					{
						if (jump(offsetVertex, xDir, 0) != null)
						{
							// return the current neighbor as the jump point
							return offsetVertex;
						}
					}
					// check diagonal
					else if (xDir != 0 && yDir != 0)
					{
						if (jump(offsetVertex, xDir, yDir) != null)
						{
							// return the current neighbor as the jump point
							return offsetVertex;
						}
					}
				}
			}
			
			lastOffsetVertex = offsetVertex;
			
			// continue staying in the same place
			offsetT += 1;
			
			if (goal.getT() != -1)
			{
				if (offsetT > goal.getT())
				{
					break;
				}
			}
			
			if(Thread.currentThread().isInterrupted())
			{
				return null;
			}
			
			offsetVertex = new LocationVertex(offsetX, offsetY, offsetT);
			
			offsetVertex.setParent(lastOffsetVertex);
			
			if (checkIfBlockedByJumpPointConstraint(offsetVertex, 0, 0,
					false)) { return lastOffsetVertex; }
			
			if (offsetVertex.getX() == goal.getX()
					&& offsetVertex.getY() == goal.getY()
					&& offsetVertex.getT() == goal.getT())
			{
				// if we found the goal, return it! we want to search
				// that one
				return offsetVertex;
			}
		}
		
		return lastOffsetVertex;
	}
	
	protected LocationVertex checkDiagonal (LocationVertex next, int directionX,
			int directionY)
	{
		// initialize the offset values which will increment
		int offsetX = next.getX();
		int offsetY = next.getY();
		int offsetT = next.getT();
		
		LocationVertex lastOffsetVertex = next;
		LocationVertex offsetVertex =
				new LocationVertex(offsetX, offsetY, offsetT);
		
		offsetVertex.setParent(next);
		
		while (obstacles.vertexExists(offsetVertex.getY(), offsetVertex.getX()))
		{
			// perform the forced neighbor check
			
			// if the node above and to the left (or down and to the right)
			// of the current neighbor is free
			if (!checkIfBlocked(offsetX - directionX, offsetY + directionY,
					offsetT + 1))
			{
				// and the node to the left (or to the right) is blocked
				if (checkIfBlocked(offsetX - directionX, offsetY, offsetT))
				{
					// return the current neighbor as the jump point
					return offsetVertex;
				}
			}
			
			// LocationVertex testNeighbor = new LocationVertex(
			// offsetX - directionX, offsetY + directionY, offsetT + 1);
			// LocationVertex testForcedNeighbor =
			// new LocationVertex(offsetX - directionX, offsetY, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			// mirror of the other condition for the diagonal
			if (!checkIfBlocked(offsetX + directionX, offsetY - directionY,
					offsetT + 1))
			{
				// if the node below (or above) is blocked
				if (checkIfBlocked(offsetX, offsetY - directionY, offsetT))
				{
					// return the current neighbor as the jump point
					return offsetVertex;
				}
			}
			
			// testNeighbor = new LocationVertex(offsetX + directionX,
			// offsetY - directionY, offsetT + 1);
			// testForcedNeighbor =
			// new LocationVertex(offsetX, offsetY - directionY, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			// if the diagonal conditions have not been met, we need to
			// recursively call this jump method and check for jump nodes
			// horizontally and vertically
			
			// check horizontally
			if (jump(offsetVertex, directionX, 0) != null)
			{
				// return the current neighbor as the jump point
				return offsetVertex;
			}
			
			// check vertically
			if (jump(offsetVertex, 0, directionY) != null)
			{
				// return the current neighbor as the jump point
				return offsetVertex;
			}
			
			if(Thread.currentThread().isInterrupted())
			{
				return null;
			}
			
			// ApplicationLogger.getInstance().logDebug("OffX: " + offsetX);
			// ApplicationLogger.getInstance().logDebug("OffY: " + offsetY);
			
			// save a reference so that we can set the parent
			lastOffsetVertex = offsetVertex;
			
			// continue moving diagonally
			offsetX += directionX;
			offsetY += directionY;
			offsetT += 1;
			
			// create the new vertex
			offsetVertex = new LocationVertex(offsetX, offsetY, offsetT);
			
			// if(debug)
			// {
			// System.out.println("diag off: " + offsetVertex.toString());
			// }
			
			// set the parent
			offsetVertex.setParent(lastOffsetVertex);
			
			// if the current neighbor is out of bounds, return null - we will
			// not search it
			if (!obstacles.vertexExists(offsetY, offsetX)) { return null; }
			
			// if the current neighbor is blocked, return last
			if (checkIfBlocked(offsetX, offsetY,
					offsetT)) { return lastOffsetVertex; }
			
			if (checkIfBlockedByJumpPointConstraint(offsetVertex, directionX,
					directionY, false)) { return lastOffsetVertex; }
			
			if (offsetX == goal.getX() && offsetY == goal.getY())
			{
				// if we found the target, return it! we want to search
				// that one
				return offsetVertex;
			}
		}
		
		return null;
	}
	
	protected LocationVertex checkHorizontal (LocationVertex next,
			int directionX)
	{
		// initialize the offset values which will increment
		int offsetX = next.getX();
		int offsetY = next.getY();
		int offsetT = next.getT();
		
		LocationVertex lastOffsetVertex = next;
		LocationVertex offsetVertex =
				new LocationVertex(offsetX, offsetY, offsetT);
		
		offsetVertex.setParent(next);
		
		// if we increment outside the grid we need to break from the loop and
		// return null
		while (obstacles.vertexExists(offsetVertex.getY(), offsetVertex.getX()))
		{
			// perform the forced neighbor check
			
			// if the node above the next offset is not blocked,
			if (!checkIfBlocked(offsetX + directionX, next.getY() + 1,
					offsetT + 1))
			{
				// and the node above the current offset is blocked
				if (checkIfBlocked(offsetX, next.getY() + 1, offsetT))
				{
					// this represents the situation in this image
					// https://harablog.files.wordpress.com/2011/09/jps_recursestraight.png
					// we have jumped from x to y
					return offsetVertex;
				}
			}
			
			// LocationVertex testNeighbor = new LocationVertex(
			// offsetX + directionX, next.getY() + 1, offsetT + 1);
			// LocationVertex testForcedNeighbor =
			// new LocationVertex(offsetX, next.getY() + 1, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			// if the node below the next neighbor is not blocked
			if (!checkIfBlocked(offsetX + directionX, next.getY() - 1,
					offsetT + 1))
			{
				// and the node below the current neighbor is blocked
				if (checkIfBlocked(offsetX, next.getY() - 1, offsetT))
				{
					// this represents the situation in this image,
					// except with the obstacle below y
					// https://harablog.files.wordpress.com/2011/09/jps_recursestraight.png
					// we have jumped from x to y
					return offsetVertex;
					
				}
			}
			
			// testNeighbor = new LocationVertex(offsetX + directionX,
			// next.getY() - 1, offsetT + 1);
			// testForcedNeighbor =
			// new LocationVertex(offsetX, next.getY() - 1, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			// ApplicationLogger.getInstance().logDebug("ox: " + offsetX);
			// ApplicationLogger.getInstance().logDebug("ot: " + offsetT);
			// ApplicationLogger.getInstance().logDebug("ny: " + next.getY());
			
			if(Thread.currentThread().isInterrupted())
			{
				return null;
			}
			
			lastOffsetVertex = offsetVertex;
			
			// continue moving horizontally
			offsetX += directionX;
			offsetT += 1;
			
			offsetVertex = new LocationVertex(offsetX, next.getY(), offsetT);
			
			offsetVertex.setParent(lastOffsetVertex);
			
			// if the current neighbor is out of bounds, return null - we will
			// not search it
			if (!obstacles.vertexExists(next.getY(), offsetX)) { return null; }
			
			// if the current neighbor is blocked, return last
			if (checkIfBlocked(offsetX, next.getY(),
					offsetT)) { return lastOffsetVertex; }
			
			if (checkIfBlockedByJumpPointConstraint(offsetVertex, directionX, 0,
					false)) { return lastOffsetVertex; }
			
			if (offsetX == goal.getX() && next.getY() == goal.getY())
			{
				// if we found the target, return it! we want to search
				// that one
				return offsetVertex;
			}
		}
		
		return null;
	}
	
	protected LocationVertex checkVertical (LocationVertex next, int directionY)
	{
		// initialize the offset values which will increment
		int offsetX = next.getX();
		int offsetY = next.getY();
		int offsetT = next.getT();
		
		LocationVertex lastOffsetVertex = next;
		LocationVertex offsetVertex =
				new LocationVertex(offsetX, offsetY, offsetT);
		
		offsetVertex.setParent(next);
		
		while (obstacles.vertexExists(offsetVertex.getX(), offsetVertex.getY()))
		{
			
			// perform the forced neighbor check
			
			// if the node right of the next neighbor up/down is not blocked
			if (!checkIfBlocked(next.getX() + 1, offsetY + directionY,
					offsetT + 1))
			{
				// and the node right of the current neighbor up/down is blocked
				if (checkIfBlocked(next.getX() + 1, offsetY, offsetT))
				{
					// return the current neighbor as the jump point
					return offsetVertex;
				}
			}
			
			// LocationVertex testNeighbor = new LocationVertex(next.getX() + 1,
			// offsetY + directionY, offsetT + 1);
			// LocationVertex testForcedNeighbor =
			// new LocationVertex(next.getX() + 1, offsetY, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			// if the node left of the next neighbor is not blocked
			if (!checkIfBlocked(next.getX() - 1, offsetY + directionY,
					offsetT + 1))
			{
				// and the node left of the current neighbor is blocked
				if (checkIfBlocked(next.getX() - 1, offsetY, offsetT))
				{
					// return the current neighbor as the jump point
					return offsetVertex;
				}
			}
			
			// testNeighbor = new LocationVertex(next.getX() - 1,
			// offsetY + directionY, offsetT + 1);
			// testForcedNeighbor =
			// new LocationVertex(next.getX() - 1, offsetY, offsetT);
			//
			// perform the forced neighbor check for dynamic constraints.
			// directions are 0 because we are not actually moving to the node
			// if (!checkIfBlockedByJumpPointConstraint(testNeighbor, 0, 0,
			// false))
			// {
			// if (checkIfBlockedByJumpPointConstraint(testForcedNeighbor, 0,
			// 0, false)) { return offsetVertex; }
			// }
			
			if(Thread.currentThread().isInterrupted())
			{
				return null;
			}
			
			lastOffsetVertex = offsetVertex;
			
			// continue moving vertically
			offsetY += directionY;
			offsetT += 1;
			
			offsetVertex = new LocationVertex(next.getX(), offsetY, offsetT);
			
			offsetVertex.setParent(lastOffsetVertex);
			
			// if the current neighbor is out of bounds, return null - we will
			// not search it
			if (!obstacles.vertexExists(offsetY, next.getX())) { return null; }
			
			// if the current neighbor is blocked, return last
			if (checkIfBlocked(next.getX(), offsetY,
					offsetT)) { return lastOffsetVertex; }
			
			if (checkIfBlockedByJumpPointConstraint(offsetVertex, 0, directionY,
					false)) { return lastOffsetVertex; }
			
			if (next.getX() == goal.getX() && offsetY == goal.getY())
			{
				// if we found the target, return it! we want to search
				// that one
				return offsetVertex;
			}
		}
		
		return null;
	}
	
	protected LocationVertex checkGoal (LocationVertex next,
			LocationVertex current)
	{
		// initialize the offset values which will increment
		int offsetX = next.getX();
		int offsetY = next.getY();
		int offsetT = next.getT();
		
		LocationVertex lastOffsetVertex = current;
		LocationVertex offsetVertex =
				new LocationVertex(offsetX, offsetY, offsetT);
		
		offsetVertex.setParent(current);
		
		if (debug)
		{
			log.log("Jumping to goal time: " + goal.getT());
		}
		
		// if the goal becomes blocked it is because a constraint has appeared
		// there
		while (!checkIfBlocked(offsetVertex.getX(), offsetVertex.getY(),
				offsetVertex.getT()))
		{
			
			lastOffsetVertex = offsetVertex;
			
			// continue staying in the same place
			offsetT += 1;
			
			if (offsetT > goal.getT())
			{
				if (debug)
				{
					log.log("Jumped to time: " + (offsetT - 1));
				}
				
				break;
			}
			
			offsetVertex = new LocationVertex(offsetX, offsetY, offsetT);
			
			offsetVertex.setParent(lastOffsetVertex);
			
			if (checkIfBlockedByJumpPointConstraint(offsetVertex, 0, 0, false))
			{
				
				if (debug)
				{
					log.log("Jumped to: " + lastOffsetVertex.toString());
				}
				
				return lastOffsetVertex;
			}
			
			if (offsetVertex.getX() == goal.getX()
					&& offsetVertex.getY() == goal.getY()
					&& offsetVertex.getT() == goal.getT())
			{
				if (debug)
				{
					log.log("Jumped to goal: " + offsetVertex.toString());
				}
				
				// if we found the goal, return it! we want to search
				// that one
				return offsetVertex;
			}
		}
		
		if (debug)
		{
			log.log("Constraint blocked goal, exiting at "
					+ lastOffsetVertex.toString());
		}
		
		return lastOffsetVertex;
	}
}
