package uk.reading.ac.uk.Aleem;

public class Obstacle extends AnEntity 
{

	//-----------------------------Food Constructors--------------------------------
	//Default Constructor
	public Obstacle() 
	{
		super();
	}
	
	//Overloaded Constructor - This is which will be mainly used
	public Obstacle(int xpos, int ypos, String image, AWorld world)
	{
		super("Obstacle", xpos, ypos, 1000, image, world);
	}

}
