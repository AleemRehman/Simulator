package uk.reading.ac.uk.Aleem;

import java.util.Random;

public enum Directions 
{
	NORTH, 
	EAST, 
	SOUTH, 
	WEST; //Constants
	
	
	public static Directions getRandomDirection()
	{
	//Random random = new Random();
		return values()[(int) (Math.random() * values().length)];
		//return values()[random.nextInt(values().length)];
	}

}
