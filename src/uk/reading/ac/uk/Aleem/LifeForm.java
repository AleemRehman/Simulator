package uk.reading.ac.uk.Aleem;

public class LifeForm extends AnEntity
{
	//-----------------------------Food Constructors--------------------------------
		//Default Constructor
		public LifeForm() 
		{
			super();
		}
		
		//Overloaded Constructor - This is which will be mainly used
		public LifeForm(String name, int xpos, int ypos, int ene, String image, AWorld world)
		{
			super(name, xpos, ypos, ene, image, world);
			
		}
		
		public LifeForm(String name, int xpos, int ypos, int ene, String image, AWorld world, boolean result)
		{
			super(name, xpos, ypos, ene, image, world, result);
			
		}
	

}
