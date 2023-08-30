public class StateAndReward {

	
	/* State discretization function for the angle controller */
	public static String getStateAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		int MIN_ANGLE = -3;
		int MAX_ANGLE = 3;
		int number_of_states = 4;
		
		String state = "state_angle_" + discretize(angle, number_of_states, MIN_ANGLE, MAX_ANGLE);
		
		return state;
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		int MAX_ANGLE = 3;
		
		double reward = MAX_ANGLE - Math.abs(angle);

		return reward;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		int MAX_STATE = 6;
		float MIN_TRANSLATION = -3f;
		float MAX_TRANSLATION = 3f;
		int MIN_ANGLE = -3;
		int MAX_ANGLE = 3;
		
		String state_vx = "vx_" + discretize(vx, 4, MIN_TRANSLATION, MAX_TRANSLATION);
		String state_vy = "vy_" + discretize(vy, 10, MIN_TRANSLATION, MAX_TRANSLATION);
		String state_angle = "state_angle_" + discretize(angle, 6, MIN_ANGLE, MAX_ANGLE);

		
		String state = "state_"+state_vx+"_"+state_vy+"_"+state_angle;
		
		return state;
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		int MAX_ANGLE = 3;
		float MAX_VY = 1f;
		float MAX_VX = 3f;
		
		double reward_angle = (MAX_ANGLE - Math.abs(angle))/MAX_ANGLE;
		double reward_vx = (MAX_VX - Math.abs(vx))/MAX_VX;
		double reward_vy = (MAX_VY - Math.abs(vy))/MAX_VY;
		
		
		
		double reward = (reward_angle+reward_vy+reward_vx);

		return reward;
	}

	// ///////////////////////////////////////////////////////////
	// discretize() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 1 and nrValues-2 is returned.
	//
	// Use discretize2() if you want a discretization method that does
	// not handle values lower than min and higher than max.
	// ///////////////////////////////////////////////////////////
	public static int discretize(double value, int nrValues, double min,
			double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	// ///////////////////////////////////////////////////////////
	// discretize2() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 0 and nrValues-1 is returned.
	// ///////////////////////////////////////////////////////////
	public static int discretize2(double value, int nrValues, double min,
			double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}

}
