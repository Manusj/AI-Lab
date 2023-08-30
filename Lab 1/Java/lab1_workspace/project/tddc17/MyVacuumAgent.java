package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.Random;

class MyAgentState
{
	public int[][] world = new int[22][22];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;

	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;

	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
	    {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
	    }
	}

	public void updateDirection(DynamicPercept p, int turn)
	{
		if(turn==ACTION_TURN_LEFT) {
			agent_direction = ((agent_direction-1) % 4);
			if (agent_direction<0)
				agent_direction +=4;
		}
		else
			agent_direction = ((agent_direction+1) % 4);
	}

	public Action doActions(DynamicPercept p, int act)
	{
		if(act==ACTION_NONE) {
			agent_last_action=ACTION_NONE;
			return NoOpAction.NO_OP;
		}
		else if(act==ACTION_TURN_LEFT) {
			agent_last_action = ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		}
		else if(act==ACTION_TURN_RIGHT) {
			agent_last_action = ACTION_TURN_RIGHT;
			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		}
		else if(act==ACTION_MOVE_FORWARD) {
			agent_last_action = ACTION_MOVE_FORWARD;
			return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
		}
		else {
			agent_last_action=ACTION_SUCK;
			return LIUVacuumEnvironment.ACTION_SUCK;
		}
	}

	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}

	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();
	private int returnToHome = 100;
	private int after=0;

	// Here you can define your variables!
	public int iterationCounter = 10000;
	public MyAgentState state = new MyAgentState();
	public int flag=-1;
	public int nextrow;
	public int nextcol;
	public int prev=0;
	public int count=0;
	public int c=1;
	public int norm=0;
	public int d;
	public int suckcount=0;

	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
			state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
			return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
		} else if (action==1) {
			state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
		}
		return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
	}
	public Action returnToHomeAfterRandom(DynamicPercept percept) {
		returnToHome--;
		state.updatePosition(percept);
		if(state.agent_x_position>1) {
			if(state.agent_direction==3)
				return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
			else {
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
			}
		}
		if(state.agent_y_position>1) {
			if(state.agent_direction==0)
				return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
			else {
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			}
		}
		if(state.agent_x_position==1 && state.agent_y_position==1) {
			if(state.agent_direction==3) {
				returnToHome=0;
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			}
			else if(state.agent_direction==0) {
				returnToHome=-1;
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			}
			else if(state.agent_direction==2) {
				returnToHome=-1;
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
			}
			else {
				returnToHome=-1;
				return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
			}
		}
		return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
}
	//public Action moveTheVacuumAgent(DynamicPercept percept) {

//	}

	@Override
	public Action execute(Percept percept) {

		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initnialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initnialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
				System.out.println("Returning to HOME after the last execution of moveToRandomStartPosition()");
				return state.doActions((DynamicPercept) percept,state.ACTION_SUCK);
			}
			if(after==0) {
				if(returnToHome>0) {
					return returnToHomeAfterRandom((DynamicPercept) percept);
				} else if (returnToHome==0) {
					returnToHome--;
					state.updatePosition((DynamicPercept) percept);
					System.out.println("Processing percepts from HOMEPosition");
					state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
					return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
    		}


    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
 			state.updatePosition((DynamicPercept)percept);
    	System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);


	    iterationCounter--;

	    if (iterationCounter==0)
	    	return state.doActions((DynamicPercept) percept,state.ACTION_NONE);

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);

	    // State update based on the percept value and the last action
	    if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
	    }
	    if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);

	    state.printWorldDebug();

	    // Next action selection based on the percept value
			if(state.agent_direction==0)
			{
				nextrow=state.agent_y_position-1;
				nextcol=state.agent_x_position;
			}
			else if(state.agent_direction==1)
			{
				nextrow=state.agent_y_position;
				nextcol=state.agent_x_position+1;
			}
			else if(state.agent_direction==2)
			{
				nextrow=state.agent_y_position+1;
				nextcol=state.agent_x_position;
			}
			else if(state.agent_direction==3)
			{
				nextrow=state.agent_y_position;
				nextcol=state.agent_x_position-1;
			}
			if(suckcount==20)
			return state.doActions((DynamicPercept) percept,state.ACTION_NONE);
			if(count==4)
			{
				if(c==1)
				{
					c=2;
					d = random_generator.nextInt(2);
				}
				if(c==2)
				{
					c=3;
					if(d==0)
					{
						state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
						return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
					}
					else if(d==1)
					{
						state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
						return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
					}
				}
				if(c==3)
				{
					if(state.world[nextcol][nextrow]==state.CLEAR)
					{
						flag=flag+1;
						count=0;
						prev=0;
						c=1;
						return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
					}
					else if(state.world[nextcol][nextrow]==state.WALL)
					{
						state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
						return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
					}
				}
			}
			System.out.println("nextcol = " + nextcol + ", nextrow = "+nextrow);
			if((state.world[nextcol][nextrow]==state.WALL)||(state.world[nextcol][nextrow]==state.CLEAR))
			{
				System.out.println("Pass");
				if(state.agent_direction==0&&prev!=1)
				{
					prev=1;
					count=count+1;
				}
				if(state.agent_direction==1&&prev!=2)
				{
					prev=2;
					count=count+1;
				}
				if(state.agent_direction==2&&prev!=3)
				{
					prev=3;
					count=count+1;
				}
				if(state.agent_direction==3&&prev!=4)
				{
					prev=4;
					count=count+1;
				}
			}
	    if(dirt)
	    {
				suckcount++;
	    	System.out.println("DIRT -> choosing SUCK action!");
				return state.doActions((DynamicPercept) percept,state.ACTION_SUCK);
	    }
	    else
	    {
	    	if(bump)
	    	{
					flag=0;
				}
				if(flag==0)
				{
					flag=flag+1;
					state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
					return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				}
				else if(flag==1||flag==3||flag==4||flag==6||flag==7||flag==9)
				{
					if((state.world[nextcol][nextrow]!=state.WALL)&&(state.world[nextcol][nextrow]!=state.CLEAR)){
						flag=flag+1;
						return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
					}
				}
				else if(flag==2||flag==5||flag==8)
				{
					flag=flag+1;
					prev=0;
					count=0;
					state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
					return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
				}
			}
			if((state.world[nextcol][nextrow]!=state.WALL)&&(state.world[nextcol][nextrow]!=state.CLEAR))
			{
				if(norm==0){
				norm=1;
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_LEFT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_LEFT);
			}
			if(norm==1){
				norm=0;
				return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
			}
			}
			else
			{
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			}
	}
			if(after==1) {
				if(state.agent_x_position==1 && state.agent_y_position==1)
				return state.doActions((DynamicPercept) percept,state.ACTION_NONE);
				if(returnToHome>0) {
				return returnToHomeAfterRandom((DynamicPercept) percept);
		 } else if (returnToHome==0) {
				returnToHome--;
				state.updatePosition((DynamicPercept) percept);
				System.out.println("Processing percepts from HOMEPosition");
				state.updateDirection((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
				return state.doActions((DynamicPercept) percept,state.ACTION_TURN_RIGHT);
			}
		}
		 return state.doActions((DynamicPercept) percept,state.ACTION_MOVE_FORWARD);
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());



	}
}
