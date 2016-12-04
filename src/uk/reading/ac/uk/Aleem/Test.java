package uk.reading.ac.uk.Aleem;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Test extends Application {
/**
 * Demonstrates animation by showing image of earth orbiting the sun
 * RJM
 */
	int canvasSize = 512;				// constants for relevant sizes
	int orbitSize = canvasSize / 3;
	int sunSize = 80;
	int earthSize = 30;
	@Override
	public void start(Stage theStage) throws Exception {
		// main code which defines what happens in program
		
	    theStage.setTitle( "Animation Example" );
	    
	    Group root = new Group();					// for group of what is shown
	    Scene theScene = new Scene( root );			// put it in a scene
	    theStage.setScene( theScene );				// apply the scene to the stage
	 
	    Canvas canvas = new Canvas( canvasSize, canvasSize );
	    							// create canvas onto which animation shown
	    root.getChildren().add( canvas );			// add to root and hence stage
	 
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    								// get context on canvas onto which images put

	    								//now load images of earth and sun
	    								// note these should be in package
	    Image earth = new Image(getClass().getResourceAsStream("Tiger.jpg"));
	 
	    final long startNanoTime = System.nanoTime();
	    								// for animation, note start time
	 
	    new AnimationTimer()			// create timer
	    {
	        public void handle(long currentNanoTime) 
	        {
	        				// define handle for what do at this time
	            double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
	            			// calculate time
	            double x = canvasSize/2 + orbitSize * Math.cos(t);
	            double y = canvasSize/2 + orbitSize * Math.sin(t);
	            			// calculate coordinates of earth
	            			// now clear canvas and draw sun and moon
	            gc.clearRect(0,  0,  canvasSize,  canvasSize);
	            gc.drawImage( earth, x-earthSize/2, y-earthSize/2, earthSize, earthSize );
	      
	        }
	    }.start();					// start it
	 
	    theStage.show();			// show the stage
	}

	public static void main(String[] args) {
		// Just launch the application
        Application.launch(args);
	}

}


