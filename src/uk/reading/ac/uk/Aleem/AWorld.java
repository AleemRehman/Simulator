package uk.reading.ac.uk.Aleem;

import java.util.Scanner;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class AWorld 
	{
	
	//-----------------------------Entity Attributes--------------------------------
	protected int xSize;
	protected int ySize;
	protected double dx, dy;
	//public AnEntity allEntities[];
	ArrayList<AnEntity> allEntities = new ArrayList<AnEntity>();
	private Random randomGenerator;
	float foodPercentage, obsPercentage;
	AnEntity mainCharacter;
	GraphicsContext gc;
	int gridSize;
	int worldSize;
	int treeCount = 0, rockCount = 0, breakCount = 0;;


	
	
	//-----------------------------CONSTRUCTORS--------------------------------
	public AWorld(GraphicsContext gcon) //Default constructor
	{
		xSize = 10;
		ySize = 10;
		//allEntities = new AnEntity[maxEntities]; //Array of entities
		ArrayList<AnEntity> allEntities = new ArrayList<AnEntity>();
		randomGenerator = new Random();
		gc = gcon;
		
		
	}
	
	public AWorld (int vertSize, int horSize, GraphicsContext gcon) //Overloaded constructor
	{
		ySize = vertSize;
		xSize = horSize;
		ArrayList<AnEntity> allEntities = new ArrayList<AnEntity>();
		randomGenerator = new Random();
		gc = gcon;
		
	}
	
	
	//-----------------------------METHODS--------------------------------
	
	
	//XY Set get
	public void setXSize(int horSize) //Set horizontal size of world
	{
		xSize = horSize;
	}
	
	public void setYSize(int vertSize) //Set verticle size of the world
	{
		ySize = vertSize;
	}
	
	
	public int getXSize() //Get horizontal size
	{
		return xSize;
	}
	
	public int getYSize() //get verticle size of the world
	{
		return ySize;
	}
	
	//Add Entity
	public void addEntityName(String name)
	{ //Same as entity set except name and energy is input
		
			String species;
			int xpos1, ypos1, energy1;
			Scanner inputInt = new Scanner(System.in);
			Scanner inputString = new Scanner(System.in);
			species = name;
			
			int tryX, tryY;
			do {
				tryX = randomGenerator.nextInt(xSize-1);			// get random X value
				tryY = randomGenerator.nextInt(ySize-1);	
				energy1 = randomGenerator.nextInt(50);// and Y
			}
			while (getEntityAt(tryX, tryY) >= 0);				// try again if already an entity at tryX,tryY
			allEntities.add(new LifeForm(species, tryX, tryY, energy1, name + ".Gif", this));

	}
	
	public void addFood()
	{
		
		
		int tryX, tryY;
		do {
			tryX = randomGenerator.nextInt(xSize-1);			// get random X value within value range
			tryY = randomGenerator.nextInt(ySize-1);			// and Y
		}
		while (getEntityAt(tryX, tryY) >= 0);				// try again if already an Entity at tryX,tryY

		allEntities.add(new Food(tryX, tryY,"Food.jpg", this));
		
	}
	
	public void addRock()
	{
		int tryX, tryY, randomRockNumY,randomRockNumX, randomRockCount;
		do {
			tryX = randomGenerator.nextInt(xSize-1);			// get random X value within value range
			tryY = randomGenerator.nextInt(ySize-1);			// and Y
		}
		while (getEntityAt(tryX, tryY) >= 0);				// try again if already an Entity at tryX,tryY
		//Add entity to array
		AnEntity tempHold = new Obstacle(tryX, tryY, "Rock.png", this);
		allEntities.add(tempHold);
		randomRockCount = randomGenerator.nextInt(rockCount-1);

		
		if(tryX > 10 && tryY > 10)
		{
			for(int i = 0; i < randomRockCount; i++)
			{
				do{
				randomRockNumY = randomGenerator.nextInt(tempHold.getYPosition()-10) + 10;
				randomRockNumX = randomGenerator.nextInt(tempHold.getXPosition()-10) + 10;
				breakCount++;
				
				if(breakCount == 25)
				{
					breakCount = 0;
					break;
				}
				
				}while(getEntityAt(randomRockNumX,randomRockNumY) >= 0);
				
				allEntities.add(new Obstacle(randomRockNumX, randomRockNumY, "Rock.png", this));
				rockCount--;
			}
			
		}
		

	}
	
	public void addTree()
	{
		
		
		int tryX, tryY;
		do {
			tryX = randomGenerator.nextInt(xSize-1);			// get random X value within value range
			tryY = randomGenerator.nextInt(ySize-1);			// and Y
		}
		while (getEntityAt(tryX, tryY) >= 0);				// try again if already an Entity at tryX,tryY
		//Add entity to array
		allEntities.add(new Obstacle(tryX, tryY, "Tree.png", this));

		
	}
	
	
	public void getEntities()
	{
	
		
		for (int i = 0; i < allEntities.size() ; i++)
		{
			
			if (allEntities.get(i) != null) //Cannot list empty array without null exception, 
				{ //list all entities and their attributes
				System.out.println("\t \t \t SPECIES " + (i+1));
				System.out.println("Species " + (i+1) + " name: " + allEntities.get(i).getSpecies());
				System.out.println("Species " + (i+1) + " Symbol: " + allEntities.get(i).getSymbol());
				System.out.println("Species " + (i+1) + " X position: " + allEntities.get(i).getXPosition());
				System.out.println("Species " + (i+1) + " Y position: " + allEntities.get(i).getYPosition());
				System.out.println("Species " + (i+1) + " Energy Level: " + allEntities.get(i).getEnergy() + "\n");
				}
			
			else
				{
					break; //If null position encountered, exit listing
				}
			
		}
		
		
	}

	
	public int getEntityAt(int x, int y)
	{
		int ans = -1;
		
		for (int i = 0; i < allEntities.size(); i++)
		{
			if (allEntities.get(i) != null)
			{
				if (allEntities.get(i).isHere(x, y))
				{
					ans = 1; //Will be used to see if entity at position, used in conditional statements
				}
		
			}
			
			if(x < 0 || y < 0 ||( x < 0 && y < 0))
			{
				ans = 1;
			}
		}
				
				
		return ans;
	}
	
	
	
	//------------------------CONFIGURATION METHODS
	public void configureFile(String input)
	{
		String[] configNew = input.split("\\s+");
		
		
		int xval, yval;
		float food1Perc, obs1Perc, food1, obs1;
		xval = Integer.parseInt(configNew[0]);
		yval = Integer.parseInt(configNew[1]);
		food1Perc = Integer.parseInt(configNew[2]);
		obs1Perc = Integer.parseInt(configNew[3]);
		
		this.setXSize(xval);
		this.setYSize(yval);
		
		float  worldsize = xval * yval;
		obs1 = (worldsize*(obs1Perc/(xval*10)));
		food1 = (worldsize*(food1Perc/(xval*10)));
		
		for (int k = 0; k < food1; k++)
		{
			addFood();
		}
		
		
		for (int l = 0; l < obs1; l++)
		{
			
			if(l % 2 == 0)
			{
				treeCount++;
			}
			
			else
			{
				rockCount++;
			}
			
		}
		
		for(int y = 0; y < rockCount; y++)
		{
			addRock();
		}
		
		for(int u = 0; u < treeCount; u++)
		{
			addTree();
		}
		
		for (int i = 4; i < configNew.length; i++)
		{
			for (int j = 0; j < Integer.parseInt(configNew[i+1]); j++)
			{
				addEntityName(configNew[i]);
				
			}
			i++;
		}
		
	}

	
	public void foodReport(int x, int y)
	{
		int ans = 0; //Used for conditional statements
		for (int i = 0; i < allEntities.size(); i++)
		{
			
			if (allEntities.get(i).getSymbol() == 'f' || allEntities.get(i).getSymbol() == 'F')
			{
				if (allEntities.get(i).isHere((x-1), (y-1))) //-1 for user friendly. (1,1) in user eyes is actually (0,0)
				{
				System.out.println("Food at: (" + (allEntities.get(i).getXPosition()+1) + "," + (allEntities.get(i).getYPosition()+1) + ")");
				ans = 1;
				
				}
			}
			
		}
		if (ans != 1)
		{
			System.out.println("No food");
			
		}
	}
	
	public boolean movePossible(int x, int y)
	{
		boolean result = true;
		for (int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getSpecies() == "Obstacle")
			{
				
				if(allEntities.get(i).isHere(x, y))
				{
					result = false;
					break;
				}
				
				
				
			}
			
		}
		
		if(x < 0 || y < 0) //Out of map condition
		{
			result = false;
		}
		
		if(x >= this.getXSize() || y >= this.getYSize()) //Out of map condition
		{
			result = false;
		}
		
			
		
		return result;
		
	}
	
	
	//==========================SIMULATION METHODS
	public boolean smellFood(Directions direction, int range, AnEntity smellEnt) //Find food in direction (N S E W)
	{
		
		boolean result = false;
		switch (direction)
		{
			
		//-------------------CASE FOR NORTH FACING
			case NORTH:
				for (int i = 0; i < allEntities.size() ; i++)
				{
					int tempX = smellEnt.getXPosition();
					int tempY = smellEnt.getYPosition();
					int reach;
					
					reach = tempY-range;
					//Check if in same row, check if food, then check if found within range
					if ((allEntities.get(i).getXPosition() == tempX && allEntities.get(i).getSymbol() == '5') && (allEntities.get(i).getYPosition() >= reach && allEntities.get(i).getYPosition() < tempY))
							{
								
								for(int k = 0; k < allEntities.size(); k++)
								{//This checks if obstacle is in the way of entity searching, if so the food cannot be reached by going in that direction, hence result is false
									if(allEntities.get(k).getSymbol() == 'O' && (allEntities.get(k).getXPosition() == tempX) && (allEntities.get(k).getYPosition() > allEntities.get(k).getYPosition() && (allEntities.get(k).getYPosition() < tempY)))
									{//Same for all other switch
										result = false;
										break;
									}
									
									else if ((k == allEntities.size()-1)) //If loop reaches end (array filled with Null)
									{
										result = true;
										break;
									}
								}
								break;
							}
					else if (i == allEntities.size())
							{
							result = false;
							break;
							}
				}
				break;
				
				
				//-------------------CASE FOR EAST FACING
			case EAST:
				for (int i = 0; i < allEntities.size() ; i++)
				{
					int tempX = smellEnt.getXPosition();
					int tempY = smellEnt.getYPosition();
					int reach = tempX+range;
					if ((allEntities.get(i).getYPosition() == tempY && allEntities.get(i).getSymbol() == 'F') && (allEntities.get(i).getXPosition() <= reach && allEntities.get(i).getXPosition() > tempX))
						{
						for(int k = 0; k < allEntities.size(); k++)
						{
							if(allEntities.get(k).getSymbol() == 'O' && (allEntities.get(k).getYPosition() == tempY) && (allEntities.get(k).getXPosition() < allEntities.get(i).getXPosition() && (allEntities.get(k).getXPosition() > tempX)))
							{
								result = false;
								break;
							}
							
							else if ((k == allEntities.size()-1))
							{
								result = true;
								break;
							}
						}
						break;
						}
					else if (i == allEntities.size())
						{
						result = false;
						break;
						}
				}
				break;
				
				//-------------------CASE FOR SOUTH FACING
			case SOUTH:
				for (int i = 0; i < allEntities.size() ; i++)
				{
					int tempX = smellEnt.getXPosition();
					int tempY = smellEnt.getYPosition();
					int reach = tempY+range;
					
					if ((allEntities.get(i).getXPosition() == tempX && allEntities.get(i).getSymbol() == 'F') && (allEntities.get(i).getYPosition() <= reach && allEntities.get(i).getYPosition() > tempY))
					{
						for(int k = 0; k < allEntities.size(); k++)
						{
							if(allEntities.get(k).getSymbol() == 'O' && (allEntities.get(k).getXPosition() == tempX) && (allEntities.get(k).getYPosition() < allEntities.get(i).getYPosition() && (allEntities.get(k).getYPosition() > tempY)))
							{
								result = false;
								break;
							}
							
							else if ((k == allEntities.size()-1))
							{
								result = true;
								break;
							}
						}
						break;
					}
				else if (i == allEntities.size())
					{
					result = false;
					break;
					}
				}
				break;
				
				
				//-------------------CASE FOR WEST FACING
			case WEST:
				for (int i = 0; i < allEntities.size() ; i++)
				{
					int tempX = smellEnt.getXPosition();
					int tempY = smellEnt.getYPosition();
					int reach = tempX-range;
					
					if ((allEntities.get(i).getYPosition() == tempY && allEntities.get(i).getSymbol() == 'F') && (allEntities.get(i).getXPosition() >= reach && allEntities.get(i).getXPosition() < tempX ))
					{
						for(int k = 0; k < allEntities.size(); k++)
						{
							if(allEntities.get(k).getSymbol() == 'O' && (allEntities.get(k).getYPosition() == tempY) && (allEntities.get(k).getXPosition() > allEntities.get(i).getXPosition() && (allEntities.get(k).getXPosition() < tempX)))
							{
								result = false;
								break;
							}
							
							else if ((k == allEntities.size()-1))
							{
								result = true;
								break;
							}
						}
						break;
					}
				else if (i == allEntities.size())
					{
					result = false;
					break;
					}
				}
				break;
		
			
				
				
		} //Switch end
		
		return result;
	}
	
	public void move(Directions direction, AnEntity movEntity)
	{
		int entX, entY;
		entX = movEntity.getXPosition();
		entY = movEntity.getYPosition();
		
		switch (direction)
		{
				
			case NORTH:
				if (movePossible(entX, (entY-1))) //Checks if it can move one position north
						{
							movEntity.setYPosition((entY-1)); //if so, entity y position changes (decreasing meaning it goes north)
							//dy = 0;
							//dx = 0;
							break;
						}
				else
						{
							move(Directions.getRandomDirection(), movEntity); //If move is not possible, recursive function to move in random direction
							break;
						}
				
				//Same for all other case
				
				
				
			case SOUTH:
				if (movePossible(entX, (entY+1)))
					{
						movEntity.setYPosition((entY+1));
						//dy = -0.001;
						//dx = 0;
						break;
					}
				else
					{
						move(Directions.getRandomDirection(), movEntity);
						break;
					}
				
				
				
			case EAST:
				if (movePossible((entX+1), entY))
					{
						
						movEntity.setXPosition((entX+1));
						//dy = 0;
						//dx = 0.001;
						break;
					}
				else
					{
						move(Directions.getRandomDirection(), movEntity);
						break;
					}
				
				
				
			case WEST:
				if (movePossible((entX-1), entY))
					{
						movEntity.setXPosition((entX-1));
					    //dy = 0;
						//dx = -0.001;
						break;
					}
				else
					{
						move(Directions.getRandomDirection(), movEntity);
						break;
					}
				
		
		
		
		
		}
		
	}
	
	public void checkFood(AnEntity entity)
	{
		for (int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getSpecies() == "Food") //Finds entities with symbol 'F' a.k.a food
			{
				if(allEntities.get(i).getXPosition() == entity.getXPosition() && allEntities.get(i).getYPosition() == entity.getYPosition()) //If entity and food pos is the same
				{
					 //Replace food entity symbol with N/A 
					entity.setEnergy((entity.getEnergy() + allEntities.get(i).getEnergy())); //Entity eat's food, thus gaining energy
					allEntities.remove(i);
					
				}
			}
		}
		
	}
	
	public void simulation() //Amalgamation of methods and loops
	{
		for (int i = 0; i < allEntities.size(); i++) //All Entities that are not FOOD or OBTACLE into Seperate Array
		{
			if ((allEntities.get(i).getSpecies() != "Food") && (allEntities.get(i).getSpecies() != "Obstacle"))
			{
				
					
					if (allEntities.size() == 0)
					{
						System.out.println("No Life Forms found!");
						break;
					}
					
					if (smellFood(Directions.NORTH, this.getYSize()*this.getXSize(), allEntities.get(i)))
					{
						move(Directions.NORTH, allEntities.get(i));
						checkFood(allEntities.get(i));
						
					}
					
					else if (smellFood(Directions.EAST, this.getYSize()*this.getXSize(), allEntities.get(i)))
					{
						move(Directions.EAST, allEntities.get(i));
						checkFood(allEntities.get(i));
					}
					
					else if (smellFood(Directions.SOUTH, this.getYSize()*this.getXSize(), allEntities.get(i)))
					{
						move(Directions.SOUTH, allEntities.get(i));
						checkFood(allEntities.get(i));
					}
					
					else if (smellFood(Directions.WEST, this.getYSize()*this.getXSize(), allEntities.get(i)))
					{
						move(Directions.WEST, allEntities.get(i));
						checkFood(allEntities.get(i));
					}
					
					else 
					{
						move(Directions.getRandomDirection(), allEntities.get(i));
						checkFood(allEntities.get(i));
					}
					
				
			}
				
			}

		
		
	}
		

	//-------ENTITY EDIT METHODS
	
	//NEED TO FIX
	//Not using ID to match arraylist index, but rather to find ID itself. Due to entity being eaten etc
	public void removeEntity(int id)
	{
		int tempID = id;
		AnEntity entityToPass = null;
		
		for(int i = 0; i < id; i++)
		{
			if(allEntities.get(i).getID() == id)
			{
				entityToPass = allEntities.get(i);
				allEntities.size();
				System.out.println("id: " + id);
				System.out.println("I: " + i);
				System.out.println(entityToPass.getEnergy());
				break;
			}
			
		}
		
		allEntities.remove(entityToPass);
		allEntities.size();
		
	}
	
	public String getEntityName(int id)
	{
		int Entid = id;
		String entName = null;
		
		for(int i = 0; i < allEntities.size(); i++)
		{
			if (allEntities.get(i).getID() == id)
			{
				entName = allEntities.get(i).getSpecies();
			}
		}
		
		return entName;
	}
	
	public String getEntityX(int id)
	{
		int Entid = id;
		String strX = null;
		
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == Entid)
			{
				int entXpos = allEntities.get(i).getXPosition();
				strX = Integer.toString(entXpos);
				
			}
			
			
		}
		
		return strX;

	}
	
	public String getEntityY(int id)
	{
		int Entid = id;
		String strY = null;
		
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == Entid)
			{
				int entYpos = allEntities.get(i).getYPosition();
				strY = Integer.toString(entYpos);
				
			}
			
			
		}
		
		return strY;
	}
	
	public String getEntityEnergy(int id)
	{
		int Entid = id;
		String energy = null;
		
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == Entid)
			{
				int entXpos = allEntities.get(i).getEnergy();
				energy = Integer.toString(entXpos);
				
			}
			
			
		}
		
		return energy;
	}
	
	
	public void setEntityName(int id, String spec)
	{
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == (id))
			{
				allEntities.get(i).setSpecies(spec);
			}
		}
		
	}
	
	public void setEntityX(int id, int x)
	{
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == (id))
			{
				allEntities.get(i).setXPosition(x);
			}
		}
	}
	
	public void setEntityY(int id, int y)
	{
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == (id))
			{
				allEntities.get(i).setYPosition(y);
			}
		}
	}
	
	public void setEntityEnergy(int id, int energy)
	{
		for(int i = 0; i < allEntities.size(); i++)
		{
			if(allEntities.get(i).getID() == (id))
			{
				allEntities.get(i).setEnergy(energy);
			}
		}
	}
	
	//-------STATISTIC METHODS & MAP FEEDBACK
	
	public int numFood()
	{
		int foodCount = 0;
		for (int i = 0; i < allEntities.size(); i++)
		{
			if (allEntities.get(i).getSpecies() == "Food")
			{
				foodCount++;
			}
		}
		return foodCount;
	}
	
	public int numObs()
	{
		int obsCount = 0;
		for (int i = 0; i < allEntities.size(); i++)
		{
			if (allEntities.get(i).getSpecies() == "Obstacle")
			{
				obsCount++;
			}
		}
		return obsCount;
	}
	
	public String worldStats()
	{
		String leftAlignFormat = "| %-4s | %-8s | %-15s | %-14d |%n";
		String returnVal;
		returnVal = "---ENTITY STATS---\n";
		returnVal = returnVal + "\t ID \t (x,y) \t Species \t Energy \n\n";
		for (int i = 0; i < allEntities.size(); i++) 
		{
			if (allEntities.get(i)==null)
			{
				
			}
			else
			{
				returnVal = returnVal + "\t" + allEntities.get(i).getID() + "\t" + "("+ allEntities.get(i).getXPosition() + "," + allEntities.get(i).getYPosition() + ")" + "\t" + allEntities.get(i).getSpecies() + "\t" + allEntities.get(i).getEnergy() + "\n";
		
			}
		}
		return returnVal;
	}
	
	public String mapStats()
	{
		String mapReturn;
		mapReturn = "---MAP STATS--- \n";
		mapReturn = mapReturn + "Verticle Size: " + this.getYSize() + "\nHorizontal size: " + this.getXSize() + "\nMap size: " + this.getXSize() * this.getYSize() + "\n";
		mapReturn = mapReturn + "Food Available: " + this.numFood() + "\n";
		mapReturn = mapReturn + "Obstacles: " + this.numObs();
		
		return mapReturn;
	}
	
	//---------GUI Stuff
	public void drawMap()
	{
		
//The order is really important in this. The lower "Technically higher y value", the image should be in front. 
		
			for(int j = 0; j < allEntities.size(); j++)
			{
				if(allEntities.get(j).getSpecies() == "Food")
				{
					allEntities.get(j).draw(gc, dx, dy);
				}
			}
		
		
			for(int i = 0; i < allEntities.size(); i++)
			{
				if (allEntities.get(i).getSpecies() != "Obstacle" && allEntities.get(i).getSpecies() != "Food")
				{
					int tempEnergy = allEntities.get(i).getEnergy();
					if(tempEnergy == 0)
					{
						allEntities.remove(i);
					}
					else
					{
						allEntities.get(i).draw(gc, dx, dy);
						allEntities.get(i).setEnergy(tempEnergy - 1);
					}
				}
			}
			
			
			//Draw Tree 
			ArrayList<AnEntity> objects = new ArrayList<AnEntity>();
			objects = sortObjects();
			for(int k = objects.size()-1; k > 0; k--)
			{	
				objects.get(k).draw(gc, dx, dy);
			}
			
		
	}
	
	
	public ArrayList<AnEntity> sortObjects()
	{
		
		ArrayList<AnEntity> returnList = new ArrayList<AnEntity>();
		ArrayList<AnEntity> tempList = new ArrayList<AnEntity>();
		int position = 0;
		int maxY = Integer.MIN_VALUE;
		
		for(int j = 0; j < allEntities.size(); j++)
		{
			if(allEntities.get(j).getSpecies() == "Obstacle")
			{
			tempList.add(allEntities.get(j));
			}
		}
		
		int size = tempList.size();
		
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < tempList.size(); j++)
			{
				if(tempList.get(j).getYPosition() > maxY)
				{
					maxY = tempList.get(j).getYPosition();
					position = j;
				}
			}
			
			if(tempList.size() != 0)
			{
				returnList.add(tempList.get(position));
				tempList.remove(position);
				position = 0;
				maxY = Integer.MIN_VALUE;
			}

			
		}
		
		
	            
		return returnList;
		
	}

	

	
	//-----------------------------MAIN--------------------------------
	public static void main(String[] args) 
	{
	
	}

}
