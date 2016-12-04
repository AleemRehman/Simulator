package uk.reading.ac.uk.Aleem;

public class Food extends AnEntity
{
	
	//-----------------------------Food Constructors--------------------------------
	//Default Constructor
	public Food() 
	{
		super();
	}
	
	//Overloaded Constructor - This is which will be mainly used
	public Food(int xpos, int ypos, String image, AWorld world)
	{
		super("Food", xpos, ypos, 500, "Food.jpg", world);
		
	}

}
