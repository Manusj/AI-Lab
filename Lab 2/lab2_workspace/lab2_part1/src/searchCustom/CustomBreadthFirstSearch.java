package searchCustom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import searchShared.NodeQueue;
import searchShared.Problem;
import searchShared.SearchNode;
import world.GridPos;

import world.World;

public class CustomBreadthFirstSearch  extends CustomGraphSearch{

	public   CustomBreadthFirstSearch(int maxDepth){
		super(false); // Setting to false as BFS uses a FIFO queue
	}
/*	
	public ArrayList<SearchNode> search(Problem p)
	{	
		path = super.search(p);
		
		System.out.println("Custom BFS Search");
		
		SearchNode currentNode = new SearchNode(p.getInitialState());
		System.out.println("StartNode"+currentNode.getState().toString());
		
		if(currentNode.getState() == p.getGoalState())
		{
			createPath(currentNode);
			return path;
		}
		
		while(!IsEmptyFrontier())
		{	
			currentNode = PopFrontier();
			ArrayList<GridPos> reachableState = p.getReachableStatesFrom(currentNode.getState());
		
			for(GridPos next : reachableState)
			{
				SearchNode nextNode = new SearchNode(next,currentNode);
				
				//System.out.println("Next possible node to CurrentNode(" +currentNode.getState().toString() + ") - " +next.toString());
				if(currentNode.getState() == p.getGoalState())
				{
					createPath(currentNode);
					return path;
				}
				addToExploredNodes(nextNode);
			}
		}		
		return path;
	}
	*/
};
