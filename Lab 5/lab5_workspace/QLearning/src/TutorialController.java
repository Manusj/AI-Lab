public class TutorialController extends Controller {

    public SpringObject object;

    ComposedSpringObject cso;

    /* These are the agents senses (inputs) */
	DoubleFeature x; /* Positions */
	DoubleFeature y;
	DoubleFeature vx; /* Velocities */
	DoubleFeature vy;
	DoubleFeature angle; /* Angle */

    /* Example:
     * x.getValue() returns the vertical position of the rocket
     */

	/* These are the agents actuators (outputs)*/
	RocketEngine leftRocket;
	RocketEngine middleRocket;
	RocketEngine rightRocket;

    /* Example:
     * leftRocket.setBursting(true) turns on the left rocket
     */

	double count = 0;

	public void init() {
		cso = (ComposedSpringObject) object;
		x = (DoubleFeature) cso.getObjectById("x");
		y = (DoubleFeature) cso.getObjectById("y");
		vx = (DoubleFeature) cso.getObjectById("vx");
		vy = (DoubleFeature) cso.getObjectById("vy");
		angle = (DoubleFeature) cso.getObjectById("angle");

		leftRocket = (RocketEngine) cso.getObjectById("rocket_engine_left");
		rightRocket = (RocketEngine) cso.getObjectById("rocket_engine_right");
		middleRocket = (RocketEngine) cso.getObjectById("rocket_engine_middle");
	}

    public void tick(int currentTime) {

    	/* TODO: Insert your code here */

    	count++;
    	if(count == 1000)
    	{
    		System.out.println("Current Data - X = "+x.getValue()+",Y = "+y.getValue()+",vx = "+vx.getValue()+",vy = "+vy.getValue()+",angle = "+angle.getValue());
    		count = 0;
    	}
   
    	if(angle.getValue()>0)
    	{
    		rightRocket.setBursting(true);
    		leftRocket.setBursting(false);
    	}
    	else
    	{
    		leftRocket.setBursting(true);
    		rightRocket.setBursting(false);
    	}

    	if(vy.getValue()>0)
    	{
    		middleRocket.setBursting(true);
    	}
    	else
    	{
    		middleRocket.setBursting(false);
    		rightRocket.setBursting(false);
    		leftRocket.setBursting(false);
    	}

    }

}
