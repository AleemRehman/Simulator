package uk.reading.ac.uk.Aleem;

import java.util.Comparator;
import java.util.Scanner;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public abstract class AnEntity 
{
	//-----------------------------Entity Attributes--------------------------------
	
	
	protected String species;
	protected String entityName;
	protected char symbol;
	protected int xpos, ypos, energy; 
	protected static int uniqueID = 0; 
	protected int personalID;
	protected String image;
	protected double dy;
	protected double dx;
	protected Image imageIcon;
	protected int iconsize = 4;
	protected int canvasSize = 512;				// constants for relevant sizes
	protected int orbitSize = canvasSize / 3;
	protected int worldSize;
	
	
	//-----------------------------CONSTRUCTORS--------------------------------
		AnEntity()
			{
				xpos = 0;
				ypos = 0;
				energy = 100;
				species = "human";
				symbol = 'H';
				uniqueID++; //ID is static, it will never be the same
				personalID = uniqueID;
			}
		
	
		//Overloaded constructor
		AnEntity(String speciesType, int verticalPos, int horizontalPos, int energyNo, String img, AWorld world)
			{
				xpos = verticalPos;
				ypos = horizontalPos;
				species = speciesType;
				energy = energyNo;
				symbol = speciesType.charAt(0);
				symbol = Character.toUpperCase(symbol);
				uniqueID++;
				personalID = uniqueID;
				image = img;
				imageIcon = new Image(getClass().getResourceAsStream(img));
				worldSize = world.getXSize();
				iconAlgo();
				
				
			}
		
		AnEntity(String speciesType, int verticalPos, int horizontalPos, int energyNo, String img, AWorld world, boolean defaultImg)
		{
			xpos = verticalPos;
			ypos = horizontalPos;
			species = speciesType;
			energy = energyNo;
			symbol = speciesType.charAt(0);
			symbol = Character.toUpperCase(symbol);
			uniqueID++;
			personalID = uniqueID;
			imageIcon = new Image(img);
			worldSize = world.getXSize();
			iconAlgo();
			
			
			
			
		}
		
		
		
		//-----------------------------METHODS--------------------------------
		//GET ID
		
		public int getID()
		{
			return personalID;
		}
		
		public void setID(int ID)
		{
			personalID = ID;
		}
		
		
		//----IMAGE METHODS
		public void setImage(String imgpath)
		{
			imageIcon = new Image(imgpath);
		}
		
		public String getImage()
		{
			return image;
		}
		
		
		//----XPOSITION SET GET
		public int getXPosition()
		{
			return xpos;
		}
		
		public void setXPosition(int xVal)
		{
			xpos = xVal;
		}
			
		//----YPOSITION SET GET
		public int getYPosition()
		{
			return ypos;
		}
		
		public void setYPosition(int yVal)
		{
			ypos = yVal;
		}
		
		//----IS HERE
		
		public boolean isHere(int x, int y)
		{
			if (x == xpos && y == ypos)
				{
					return true;
				}
			else
				{
					return false;
					
				}
		}
		
		//----ENERGY SET GET
		public int getEnergy()
		{
			return energy;
		}
		
		public void setEnergy(int newEnergy)
		{
			energy = newEnergy;
		}
		
		
		
		
		//----SPECIES SET GET
		public String getSpecies()
		{
			return species;
		}
		
		public void setSpecies(String newSpecies)
		{
			species = newSpecies;
			symbol = species.charAt(0); //Symbol is the first char of spec name
			symbol = Character.toUpperCase(symbol); //Upercase symbol
		}
		
		//----SYMBOL GET
		public char getSymbol() 
		{
			return symbol;
		}
		
		public void setSymbol(char symb)
		{
			symbol = symb;
			
		}
		
		
		
		//-----TO STRING/TO TEXT
		public String toString()
		{
			String specInfo;
			String intx;
			String inty;
			
			intx = Integer.toString(xpos);
			inty = Integer.toString(ypos);
			specInfo = species + " at postion (" + intx + "," + inty + ") "; //Return species basic infomation [name (x,y)]
			return specInfo;
			
			
		}
		
		public String toText()
		{
			
			String text;
			text = species + " (" + symbol + ") " + "at postion (" + xpos + "," + ypos + "). \n" + "(" + symbol + ") " + "Energy Level: " + energy;
			return text; //Return all info about entity as single string
		}
		
		//Animation Stuff
		
		public void iconAlgo()
		{
			if(worldSize <= 15)
			{
				if(image == "Tree.png")
				{
					iconsize = 2;
				}
				
				else if(image == "Rock.png")
				{
					iconsize = 2;
				}
				
				else
				{
					iconsize = 1;
				}
			}
			
			if(worldSize <= 35 && worldSize > 15)
			{
				if(image == "Tree.png")
				{
					iconsize = 3;
				}
				
				else if(image == "Rock.png")
				{
					iconsize = 3;
				}
				
				else
				{
					iconsize = 2;
				}
			}
		
			
			if(worldSize <= 50 && worldSize > 35)
			{
				if(image == "Tree.png")
				{
					iconsize = 5;
				}
				
				else if(image == "Rock.png")
				{
					iconsize = 5;
				}
				
				else
				{
					iconsize = 3;
				}
			}
			
			
			
			
			
		}
		
		public void draw(GraphicsContext gc, double xmov, double ymov)
		{
			
			gc.drawImage(imageIcon, xpos, ypos, iconsize, iconsize);
			
			
		}
		
	
		
		
	
		
		
		
	public static void main(String[] args) 
	{
	
		
	}

}
