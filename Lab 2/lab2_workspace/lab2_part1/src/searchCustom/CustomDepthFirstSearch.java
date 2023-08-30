package searchCustom;

import java.util.ArrayList;
import java.util.Random;

import searchShared.Problem;
import searchShared.SearchNode;
import world.GridPos;


public class CustomDepthFirstSearch extends CustomGraphSearch{
	public CustomDepthFirstSearch(int maxDepth){
		super(true); // Setting to true as Depth first search uses a stack or a LIFO queue
		System.out.println("Change line above in \"CustomDepthFirstSearch.java\"!");}
/*	
	public ArrayList<SearchNode> search(Problem p)
	{	
		path = super.search(p);
		
		System.out.println("Custom DFS Search");
		
		SearchNode currentNode = new SearchNode(p.getInitialState());
		System.out.println("StartNode"+currentNode.getState().toString());
		
		while(!IsEmptyFrontier())
		{
			currentNode = PopFrontier();
			
			// Checking if goal is reached by poping the element from the stack
			if(currentNode.getState() == p.getGoalState())
			{
				createPath(currentNode);
				return path;
			}
			
			ArrayList<GridPos> reachableState = p.getReachableStatesFrom(currentNode.getState());
			
			for(GridPos next : reachableState)
			{
				SearchNode nextNode = new SearchNode(next,currentNode);
				
				addToExploredNodes(nextNode);
			}
		}
		return path;
	}
	*/
};
