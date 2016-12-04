package uk.reading.ac.uk.Aleem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.Time;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.css.Style;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class GUIInterface extends Application
{

	//Global Variables--Will need adjusting
	int canvasSize = 550;
	int cSize = 250;
	private long lastUpdate = 0 ;
	int worldSize = 50;
	double scale, scaleSetting = 525;
	
	//File variables
	File currentFile;
	String autoConfigFile; //Previous config used
	String Configuration; //Current configuration -> should default to previous
	String newConfiguration;
	boolean newFileSelected = false;
	Text currFile;
	Text currConfig;
	
	//Simulation variables
	int clickCount = 0;
	long click1Time;
	long click2Time;
	long clickSim;
	boolean simulationYN = false;
	
	//Entity IN world Variables
	String imagepath;
	
	
	BorderPane root = new BorderPane(); //Scene layout
	Pane canvasPane = new Pane();
	
	HBox hbox = new HBox(); //top
	HBox hbox2 = new HBox(); //top
  	HBox hboxB = new HBox(); //bottom
  	VBox vboxL = new VBox(); //left
  	VBox vboxR = new VBox(); //right
  	ScrollPane scrollpaneR = new ScrollPane();
  	
  	
    AWorld world;
    Stage stage;
    GraphicsContext gc;
    Canvas canvas;
    AnimationTimer timer;
    boolean toggle = false;
    
    public void start(Stage stage) throws Exception
	{
		  	stage.setTitle( "Animation Example" );
		  	stage.setWidth(1175);
		  	stage.setHeight(637);
		  	
		  	Label label1 = new Label("Name:");
		  	TextField textField = new TextField ();
		  	HBox hb = new HBox();
		  	root.setTop(hb);
		  	hb.getChildren().addAll(label1, textField);
		  	hb.setSpacing(10);
		  	
		    //==================================BORDERPANE SETTINGS
		  
		  	root.setTop(hbox);//Assign HBox to top pane for root
		  	root.setLeft(vboxL);
		  	root.setRight(vboxR);
		  	root.setCenter(canvas);
		  	Image backgroundImgV = new Image(getClass().getResourceAsStream("VBOX.png"));
			BackgroundImage bgIMGV = new BackgroundImage(backgroundImgV, null, null, null, null);
			Background bgV = new Background(bgIMGV);
			vboxL.setBackground(bgV);
			vboxR.setBackground(bgV);
			
			//========HBox Settings
			hbox.setPadding(new Insets(15, 0, 15, 0));
			hbox2.setSpacing(10);
			Image backgroundImg = new Image(getClass().getResourceAsStream("HBOXMAIN.png"));
			BackgroundImage bgIMG = new BackgroundImage(backgroundImg, null, null, null, null);
			Background bg = new Background(bgIMG);
			hbox.setBackground(bg);

			
			//========VBox Settings
			scrollpaneR.setPadding(new Insets(0, 0, 0, 3));
			vboxR.setPadding(new Insets(0, 0, 0, 3));
			vboxL.setPadding(new Insets(0, 0, 0, 3));
			
			scrollpaneR.setPrefWidth(300);
			vboxR.setPrefWidth(300);
			vboxL.setPrefWidth(300);
			
			vboxR.setSpacing(5);
			vboxL.setSpacing(5);
			
			vboxL.setStyle("-fx-border-color: #0087fa;");
			scrollpaneR.setStyle("-fx-border-color: #0087fa;");
			vboxR.setStyle("-fx-border-color: #0087fa;");
		  
		  	//=======================BUTTONS
			//File Button
		  	Button fileBtn = new Button();
		  	fileBtn.setText("File");
		  	fileBtn.setPrefSize(100, 25);
		  	fileBtn.setOnAction(e -> fileHandle());
		  	fileBtn.setStyle("-fx-background-color:" 
		        + "#0b0b0b,"
		        + "linear-gradient(#0b0b0b 0%, #0b0b0b 20%, #0b0b0b 100%),"
		        + "linear-gradient(#0b0b0b, #0b0b0b),"
		        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
		        + "-fx-background-radius: 5,4,3,5;"
		        + "-fx-background-insets: 0,1,2,0;"
		    + "-fx-text-fill: white;"
		    + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
		    + "-fx-text-fill: linear-gradient(white, #d0d0d0);"
		    + "-fx-font-size: 12px;");

		  	
		  	
		  	
		  	//View Button
		  	Button viewBtn = new Button();
		  	viewBtn.setText("View");
		  	viewBtn.setPrefSize(100, 25);
		  	viewBtn.setOnAction(e -> viewHandle());
		  	viewBtn.setStyle("-fx-background-color:" 
			        + "#0b0b0b,"
			        + "linear-gradient(#0b0b0b 0%, #0b0b0b 20%, #0b0b0b 100%),"
			        + "linear-gradient(#0b0b0b, #0b0b0b),"
			        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
			        + "-fx-background-radius: 5,4,3,5;"
			        + "-fx-background-insets: 0,1,2,0;"
			    + "-fx-text-fill: white;"
			   + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
			    + "-fx-text-fill: linear-gradient(white, #d0d0d0);"
			    + "-fx-font-size: 12px;");
		  	
		  	 
		  	Button editBtn = new Button();
		  	editBtn.setText("Edit");
		  	editBtn.setPrefSize(100, 25);
		  	editBtn.setOnAction(e -> editHandle());
		  	editBtn.setStyle("-fx-background-color:" 
			        + "#848484,"
			        + "linear-gradient(#0b0b0b 0%, #0b0b0b 20%, #0b0b0b 100%),"
			        + "linear-gradient(#0b0b0b, #0b0b0b),"
			        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
			        + "-fx-background-radius: 5,4,3,5;"
			        + "-fx-background-insets: 0,1,2,0;"
			    + "-fx-text-fill: white;"
			   + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
			    + "-fx-text-fill: linear-gradient(white, #d0d0d0);"
			    + "-fx-font-size: 12px;");
		  	
		  	
			Button simBtn = new Button();
			simBtn.setText("Simulation");
			simBtn.setPrefSize(100, 25);//Add all these buttons 
			simBtn.setOnAction(e -> simulationHandle());
			simBtn.setStyle("-fx-background-color:" 
			        + "#0b0b0b,"
			        + "linear-gradient(#0b0b0b 0%, #0b0b0b 20%, #0b0b0b 100%),"
			        + "linear-gradient(#0b0b0b, #0b0b0b),"
			        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
			        + "-fx-background-radius: 5,4,3,5;"
			        + "-fx-background-insets: 0,1,2,0;"
			    + "-fx-text-fill: white;"
			   + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
			    + "-fx-text-fill: linear-gradient(white, #d0d0d0);"
			    + "-fx-font-size: 12px;");
			
			
			hbox.getChildren().add(hbox2);
			hbox.setAlignment(Pos.TOP_CENTER); //Buttons will always stay in center when window is resized
			hbox2.getChildren().addAll(fileBtn, viewBtn, editBtn, simBtn);
			
			
			
		  	//=====================================SCENE SETTINGS
			 
		  	Scene scene = new Scene(root); //Scene
		  	stage.setScene(scene); //Add scene to stage
		  	animation();
		    stage.show();			
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

	//===============================================================METHODS
	 private void SaveFile(String content, File file)
	    {
		 		
	            FileWriter fileWriter = null; //Create fileWriter
	            try 
	            {
	          
					fileWriter = new FileWriter(file); //Creates new file
					fileWriter.write(content); //Adds text to file
		            fileWriter.close(); //close stream
				} catch (IOException e) 
	            {
					e.printStackTrace();
				}
	            
	    }
	
	 public double scaleAlgo()
	{
		String[] configNew = Configuration.split("\\s+");
		int xval;
		
		xval = Integer.parseInt(configNew[0]);
		
		
		
		return scaleSetting/xval;
		
	
	}
	 
	 public void animation()
	 {
		 Canvas canvas = new Canvas(561, 543);// create canvas onto which animation shown
		 canvasPane.getChildren().add(canvas);
		 Image backgroundImg = new Image(getClass().getResourceAsStream("Background.png"));
		 BackgroundImage bgIMG = new BackgroundImage(backgroundImg, null, null, null, null);
		 Background bg = new Background(bgIMG);
		 canvasPane.setBackground(bg);
		 root.centerShapeProperty();
		 root.setCenter(canvasPane);// add the canvas to the center of the scene
		 canvas.setStyle("-fx-border-color: black");
		 gc = canvas.getGraphicsContext2D();
		 
		 vboxL.getChildren().clear();
		 autoUpdate();
		 PreviousFile();
		
		 scale = scaleAlgo(); //Scale algorithm to approrpiatley scale size of world to size of stage
		 gc.scale(scale,scale); //NEED TO CREATE SCALE FUNCTION 
		 
		   
		    
		
		 timer =  new AnimationTimer()			// create timer
		    {
		        public void handle(long now) //Handle for what to do at this time, which is all the time
		        {
		        	
		        	if(now - lastUpdate >= 100000000.00000)
		        	{
		            	gc.clearRect(0,  0,  canvasSize,  canvasSize); //Needs to be in this positi
		            	world.simulation();
				    	world.drawMap();
				    	lastUpdate = now;
				    	
		        	}
		        }
		    };		
	 }

	 public void selectImage() 
	 {
		 	FileChooser chooser = new FileChooser();
		 	chooser.getExtensionFilters().addAll(
	        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")); // limit chooser options to image files
		    chooser.setTitle("Open File");
		    File file = chooser.showOpenDialog(new Stage());
		    
		    if(file != null) 
		    {
		    	try 
		    	{
		    	
				imagepath = file.toURI().toURL().toString(); //TeamTreeHouse explain
		        
		    	} 
		    	catch (MalformedURLException e) 
		    	
		    	{
					e.printStackTrace();
				}
		    }
		    else
		    {
		        Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("Information Dialog");
		        alert.setHeaderText("No File Selected");
		        alert.showAndWait();
		    }
	 }
	 
	 public void resetWorld()
	 {
		timer.stop();		
		canvasPane.getChildren().clear();
		canvas = new Canvas(561, 543);
		canvasPane.getChildren().add(canvas);
		root.setCenter(canvasPane);
		gc = canvas.getGraphicsContext2D();
		world = new AWorld(worldSize, worldSize, gc);
		animation();
		timer.start();
			
	 }
	
	 public void listLife()
	 {
		 root.setRight(scrollpaneR);
			Text t = new Text(world.worldStats());
			scrollpaneR.setContent(t);
	 }
	 
	 //==========================================================HANDLERS
	public void viewHandle()
	{
		HBox viewHbox = new HBox();
		HBox viewHbox2 = new HBox();
		root.setTop(viewHbox);
		viewHbox.setPadding(new Insets(100));
		
		Button DisplayConfig = new Button();
		DisplayConfig.setText("Display Configuration");
		DisplayConfig.setPrefSize(100, 25);
		DisplayConfig.setOnAction(new EventHandler<ActionEvent>()
			{

					@Override
					public void handle(ActionEvent E) 
					{
						root.setRight(vboxR);
						vboxR.getChildren().clear();
						Text t = new Text("Map Configuration: \n" + Configuration);
						vboxR.getChildren().add(t);
						
					}
		
			});
		
		Button EditConfig = new Button();
		EditConfig.setText("Edit Config");
		EditConfig.setPrefSize(100, 25);
		EditConfig.setOnAction(new EventHandler<ActionEvent>()
			{
					//Need to implement actual editing of configuration
					@Override
					public void handle(ActionEvent E) 
					{
						vboxR.getChildren().clear();
						vboxR.setSpacing(5);
						root.setRight(vboxR);
						Label textLabel = new Label("Edit configuration below:");
						textLabel.setStyle("-fx-text-fill: white;");
						TextField textf = new TextField();
						textf.setMaxWidth(200); //Set max pref width
						textf.setMaxHeight(20);
						textf.setPrefSize(200, 20);
						textf.appendText(Configuration);
											
						Button saveBtn = new Button();
						saveBtn.setPrefSize(100, 25);
						saveBtn.setText("Save Configuraition");
						saveBtn.setOnAction(new EventHandler<ActionEvent>()
						{

							@Override
							public void handle(ActionEvent E) 
							{
								String reg4 = ".*[a-zA-Z]+ [0-9]+.*"; //Format for repeating Entity Number Entity Number
						 		String reg3 = "[0-9]+ [0-9]+ [0-9]+ [0-9]+ " + reg4 + "+"; //Used to check if input format is valid (num) (num) (num) (num) REPEAT [(Entity) (num)] N TIMES
						         
						 		
						 		if(textf.getText().matches(reg3))
						        {	
								 		Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
							        	saveAlert.setTitle("Information Dialog");
							        	saveAlert.setHeaderText("File Successfully saved to " + currentFile);
							        	saveAlert.setContentText(currentFile.getAbsolutePath());
							        	saveAlert.showAndWait();
										SaveFile(textf.getText(), currentFile);
										
										 try 
							             { 
								            //If no file selected, error output
								            if (currentFile != null)
								            {
							     			FileReader fr = new FileReader(currentFile); //Read from txt file
							     			BufferedReader br = new BufferedReader(fr); //Put text into stream
							     			
							     			Configuration = br.readLine(); //initialise variable from stream

							     			br.close(); //close stream
							     				
							     		
							     			FileWriter fw = new FileWriter("Previous Configuration.txt", false);
							     			PrintWriter pw = new PrintWriter(fw); //open stream to write
								     		pw.println(currentFile.getName());
								     		pw.close();
								     		
								     		 Text t1 = new Text("Current File: " + currentFile.getName());
								             Text t2 = new Text("Configuration: " + Configuration);
								             vboxL.getChildren().clear();
								             vboxL.getChildren().addAll(t1, t2);
								             
								             animation();
								             newFileSelected = true;
								     		
								            }
								            
								            else
								     		{
								     			System.err.println("No File Selected");
								     			Alert noFileAlert = new Alert(Alert.AlertType.WARNING);
								     			noFileAlert.setTitle("File Warning");
								     			noFileAlert.setHeaderText("No File Selected");
								     			noFileAlert.setContentText("No file has been selected. The simulation configuration has not been changed!");
								     			noFileAlert.showAndWait();
								     		}
								     		
							     		} 
							             catch (IOException e1) 
							     		{
							     			System.out.println("Error");
							     			
							     		}
										
						        	 		
						         }
						         else
						         {
						        	 	Alert saveAlert = new Alert(Alert.AlertType.WARNING);
						        	 	saveAlert.setTitle("Warning Dialog");
						        	 	saveAlert.setHeaderText("Incorrect Format Entered");
						        	 	saveAlert.setContentText("The text you have entered if of the incorrect format. Please type again in the following format : \n "
						        	 							+ "[x] [y] [food %] [Obstacle %] [Entity] [Entity Number] \n\n Example: \n 100 100 10 20 Tiger 5 Ant 3" );
						        	 	saveAlert.showAndWait();
						         }
								
								
							}
							
						});
						
						
						vboxR.getChildren().addAll(textLabel, textf, saveBtn);
					}
			
				});
				
		Button DisplayLifeForm = new Button();
		DisplayLifeForm.setText("Life Form info");
		DisplayLifeForm.setPrefSize(100, 25);
		DisplayLifeForm.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				root.setRight(scrollpaneR);
				Text t = new Text(world.worldStats());
				scrollpaneR.setContent(t);
				
			}
			
		});
		
		Button DisplayMapInfo = new Button();
		DisplayMapInfo.setText("Mpa Info");
		DisplayMapInfo.setPrefSize(100, 25);
		DisplayMapInfo.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				root.setRight(vboxR);
				vboxR.getChildren().clear();
				Text t = new Text(world.mapStats());
				vboxR.getChildren().add(t); //Prints table/text to right of root
				
			}
			
		});
		
		Button Exit = new Button();
		Exit.setText("Exit");
		Exit.setPrefSize(100, 25);
		Exit.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{	
				root.setRight(vboxR);
				root.getChildren().remove(viewHbox);
				root.setTop(hbox);

			}
			
		});

		//Sub button settings
		viewHbox.setPadding(new Insets(15, 0, 15, 0));
		viewHbox.setAlignment(Pos.CENTER);
		viewHbox.getChildren().add(viewHbox2);
		viewHbox2.setSpacing(10);
		viewHbox2.getChildren().addAll(DisplayConfig, EditConfig, DisplayLifeForm, DisplayMapInfo, Exit);
		
		
	}

	public void fileHandle()
	{
		
		
		HBox fileHbox = new HBox();
		HBox fileHbox2 = new HBox();
		root.setTop(fileHbox);
		TextField textf = new TextField();
		
		Button newConfig = new Button();
		newConfig.setText("New Configuration");
		newConfig.setPrefSize(120, 25);
		newConfig.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent E) 
					{
						root.setRight(vboxR);
						vboxR.getChildren().clear();
						Label textLabel = new Label("Add configuration below:");
						textf.setMaxWidth(200); //Set max pref width
						textf.setMaxHeight(20);
						textf.setPrefSize(200, 20);
						
						Button save = new Button();
						save.setText("Save");
						save.setPrefSize(120, 25);
						save.setOnAction(new EventHandler<ActionEvent>()
						{

							@Override
							public void handle(ActionEvent E) 
							{
							 FileChooser fileChooser = new FileChooser();
					         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
					         fileChooser.getExtensionFilters().add(extFilter);
					         
					        String reg1 = ".*[a-zA-Z]+ [0-1000]+.*"; //Format for repeating Entity Number Entity Number
						 	String regTyp1 = "[0-9]+ [0-9]+ [0-9]+ [0-9]+ " + reg1 + "?"; //Used to check if input format is valid (num) (num) (num) (num) REPEAT [(Entity) (num)] N TIMES
						 	String regTyp2 = "[0-9]+ [0-9]+ [0-9]+ [0-9]+"; //Incase just world size
					 		if(textf.getText().matches(regTyp1))
				        	{
				 					
				        	 		SaveFile(textf.getText(), currentFile); //Saves the string entered in TextField to the file
				        	}
				 		
				   else if(textf.getText().matches(regTyp2))
				         	{
									
				        	 		SaveFile(textf.getText(), currentFile);
				         	}
				 		
				   else
				         	{	  	
					        	    Alert saveAlert = new Alert(Alert.AlertType.WARNING);
					        	 	saveAlert.setTitle("Information Dialog");
					        	 	saveAlert.setHeaderText("Incorrect Format Entered");
					        	 	saveAlert.setContentText("The text you have entered if of the incorrect format. Please type again in the following format : \n "
					        	 							+ "[x] [y] [food %] [Obstacle %] [Entity] [Entity Number] \n\n Example: \n 100 100 10 20 Tiger 5 Ant 3" );
					        	 	saveAlert.showAndWait();
				         	}
					         
							}
							
						});
						Button saveAs = new Button();
						saveAs.setText("Save as");
						saveAs.setPrefSize(120, 25);
						saveAs.setOnAction(new EventHandler<ActionEvent>()
								{
						
									@Override
									public void handle(ActionEvent E) 
									{
									 FileChooser fileChooser = new FileChooser();
							         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
							         fileChooser.getExtensionFilters().add(extFilter);
							         File file = fileChooser.showSaveDialog(stage);
							         if(file != null)
							            {
							              SaveFile(textf.getText(), file); //Saves the string entered in TextField to the file
							            }
										
									}
									
								});
						
						
						vboxR.getChildren().addAll(textLabel, textf, save, saveAs);
						
					}
			
				});
		
		Button openConfig = new Button();
		openConfig.setText("Open Configuration");
		openConfig.setPrefSize(120, 25);
		openConfig.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent E) 
					{
						 
			             
			             try 
			             {
			            	vboxR.getChildren().clear();
			            	FileChooser fileChooser = new FileChooser();
				            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				            fileChooser.getExtensionFilters().add(extFilter);
				            currentFile = fileChooser.showOpenDialog(stage);
				            
				            //If no file selected, error output
				            if (currentFile != null)
				            {
			     			FileReader fr = new FileReader(currentFile); //Read from txt file
			     			BufferedReader br = new BufferedReader(fr); //Put text into stream
			     			
			     			Configuration = br.readLine(); //initialise variable from stream
			     			br.close(); //close stream
			     				
			     		
			     			FileWriter fw = new FileWriter("Previous Configuration.txt", false);
			     			PrintWriter pw = new PrintWriter(fw); //open stream to write
				     		pw.println(currentFile.getName());
				     		pw.close();
				     		
				     		 Text t1 = new Text("Current File: " + currentFile.getName());
				             Text t2 = new Text("Configuration: " + Configuration);
				             vboxL.getChildren().clear();
				             vboxL.getChildren().addAll(t1, t2);
				             
				             animation();
				             newFileSelected = true;
				     		
				            }
				            
				            else
				     		{
				     			System.err.println("No File Selected");
				     			Alert noFileAlert = new Alert(Alert.AlertType.WARNING);
				     			noFileAlert.setTitle("File Warning");
				     			noFileAlert.setHeaderText("No File Selected");
				     			noFileAlert.setContentText("No file has been selected. The simulation configuration has not been changed!");
				     			noFileAlert.showAndWait();
				     		}
				     		
			     		} 
			             catch (IOException e1) 
			     		{
			     			System.out.println("Error");
			     			
			     		}
			            
			             
			     		
			            
					}
					
				});
		
		Button save = new Button();
		save.setText("Save");
		save.setPrefSize(120, 25);
		save.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
			 FileChooser fileChooser = new FileChooser();
	         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	         fileChooser.getExtensionFilters().add(extFilter);
	         
	        String reg1 = ".*[a-zA-Z]+ [0-1000]+.*"; //Format for repeating Entity Number Entity Number
	 		String regTyp1 = "[0-9]+ [0-9]+ [0-9]+ [0-9]+ " + reg1 + "?"; //Used to check if input format is valid (num) (num) (num) (num) REPEAT [(Entity) (num)] N TIMES
	 		String regTyp2 = "[0-9]+ [0-9]+ [0-9]+ [0-9]+"; //Incase just world size
	 		if(textf.getText().matches(regTyp1))
	        	{
	 					
	        	 		SaveFile(textf.getText(), currentFile); //Saves the string entered in TextField to the file
	        	}
	 		
	   else if(textf.getText().matches(regTyp2))
	         	{
						
	        	 		SaveFile(textf.getText(), currentFile);
	         	}
	 		
	   else
	         	{	  	
		        	    Alert saveAlert = new Alert(Alert.AlertType.WARNING);
		        	 	saveAlert.setTitle("Information Dialog");
		        	 	saveAlert.setHeaderText("Incorrect Format Entered");
		        	 	saveAlert.setContentText("The text you have entered if of the incorrect format. Please type again in the following format : \n "
		        	 							+ "[x] [y] [food %] [Obstacle %] [Entity] [Entity Number] \n\n Example: \n 100 100 10 20 Tiger 5 Ant 3" );
		        	 	saveAlert.showAndWait();
	         	}
	         
		}
			
	});
		
		Button saveAs = new Button();
		saveAs.setText("Save as");
		saveAs.setPrefSize(120, 25);
		saveAs.setOnAction(new EventHandler<ActionEvent>()
				{
		
					@Override
					public void handle(ActionEvent E) 
					{
					 FileChooser fileChooser = new FileChooser();
			         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			         fileChooser.getExtensionFilters().add(extFilter);
			         File file = fileChooser.showSaveDialog(stage);
			         if(file != null)
			            {
			              SaveFile(textf.getText(), file); //Saves the string entered in TextField to the file
			            }
						
					}
					
				});
		
		Button Exit = new Button();
		Exit.setText("Exit");
		Exit.setPrefSize(120, 25);
		Exit.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{	
				vboxR.getChildren().clear();
				root.getChildren().remove(fileHbox);
				root.setTop(hbox);

			}
			
		});
		
		fileHbox.setPadding(new Insets(15, 0, 15, 0));
		fileHbox.setAlignment(Pos.CENTER);
		fileHbox.getChildren().add(fileHbox2);
		fileHbox2.setSpacing(10);
		fileHbox2.getChildren().addAll(newConfig, openConfig, save, saveAs, Exit);
		
	}

	public void simulationHandle()
	{
		
		HBox simHbox = new HBox();
		HBox simHbox2 = new HBox();
		root.setTop(simHbox);
		
		Button startBtn = new Button();
		startBtn.setText("Start");
		startBtn.setPrefSize(120, 25);
		startBtn.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				newFileSelected = true;
				timer.start();
				
			}
			
		});
		
		Button pauseBtn = new Button();
		pauseBtn.setText("Pause");
		pauseBtn.setPrefSize(120, 25);
		pauseBtn.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent E) 
					{
						timer.stop();
					}
			
				});
	
		Button resetBtn = new Button();
		resetBtn.setText("Reset");
		resetBtn.setPrefSize(120, 25);
		resetBtn.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				resetWorld();
			}
	
		});
	
		Button Toggle = new Button();
		Toggle.setText("Toggle Animation");
		Toggle.setPrefSize(120, 25);
		Toggle.setOnAction(new EventHandler<ActionEvent>()
				{
			
					@Override
					public void handle(ActionEvent E) 
					{
						clickCount++;
						
						//Toggle OFF
						if((clickCount % 2) == 0)
						{
							
							click2Time = System.currentTimeMillis();
							clickSim = click2Time - click1Time;
							clickSim = clickSim / 120; //120 refresh rate to canvas
							
							for(int i = 0; i < (clickSim); i++)
							{
								world.simulation();
							}
							timer.start();
							toggle = true;
						}
						
						//Toggle ON
						if((clickCount % 2) == 1)
						{
							
							timer.stop();
							click1Time = System.currentTimeMillis();
							
						}
							
						
						
					}
			
			
			
		});

		Button exit = new Button();
		exit.setText("Exit");
		exit.setPrefSize(120, 25);
		exit.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				vboxR.getChildren().clear();
				root.getChildren().remove(simHbox);
				root.setTop(hbox);
			}
			
		});
		
		
		simHbox.setPadding(new Insets(15, 0, 15, 0));
		simHbox.setAlignment(Pos.CENTER);
		simHbox.getChildren().add(simHbox2);
		simHbox2.setSpacing(10);
		simHbox2.getChildren().addAll(startBtn, pauseBtn, resetBtn, Toggle, exit);
		
		
	}
	
	public void editHandle()
	{
		HBox editHbox = new HBox();
		HBox editHbox2 = new HBox();
		root.setTop(editHbox);
		listLife();
		
		Button modLife = new Button();
		modLife.setText("Modify Life Forms");
		modLife.setPrefSize(120, 25);
		modLife.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				
				vboxL.getChildren().clear();
				timer.stop();
				root.setLeft(vboxL);
				
				
				
				Label idLbl = new Label();
				idLbl.setText("Enter Life Form ID:");
				
				TextField tfEnterId = new TextField();
				tfEnterId.setMaxSize(130, 25);
				String id = tfEnterId.getText();
				
				
				Button selectEnt = new Button();
				selectEnt.setText("Select Life Form");
				selectEnt.setPrefSize(130, 25);
				selectEnt.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent E) 
					{
						
						int entId;
						String entN, entX, entY, entE, entImage;
						int entXi, entYi, entEi;
						entId = Integer.parseInt(tfEnterId.getText());
						vboxL.getChildren().clear();
						
						//Assign entered Life Form values int variables
						//=======NAME
						entN = world.getEntityName(entId); 
						Label nameLbl = new Label();
						nameLbl.setText("Life Form:");
						TextField nameTf = new TextField();
						nameTf.setMaxSize(130, 25);
						nameTf.appendText(entN);
						
						//========XPOSITION
						entX = world.getEntityX(entId);
						Label xLbl = new Label();
						xLbl.setText("X Position: ");
						TextField xTf = new TextField();
						xTf.setMaxSize(130, 25);
						xTf.appendText(entX);
						
						//=========YPOSITION
						entY = world.getEntityY(entId);
						Label yLbl = new Label();
						yLbl.setText("Y Position: ");
						TextField yTf = new TextField();
						yTf.setMaxSize(130, 25);
						yTf.appendText(entY);
						
						
						entE = world.getEntityEnergy(entId);
						Label energyLbl = new Label();
						energyLbl.setText("Energy Level: ");
						TextField energyTf = new TextField();
						energyTf.setMaxSize(130, 25);
						energyTf.appendText(entE);
						
						
						//entImage = world.getEntityImage(entId);
						
						Button saveChanges = new Button();
						Label saveCh = new Label();
						saveCh.setText("Save Changes");
						saveChanges.setText("Save Changes: ");
						saveChanges.setMinSize(130, 25);
						saveChanges.setOnAction(new EventHandler<ActionEvent>()
								{

									@Override
									public void handle(ActionEvent e) 
									{
										if(newFileSelected == true)
										{
										String entN = nameTf.getText();
										int entXi = Integer.parseInt(xTf.getText());
										int entYi = Integer.parseInt(yTf.getText());
										int entEi = Integer.parseInt(energyTf.getText());
										
										world.setEntityEnergy(entId, entEi);
										world.setEntityName(entId, entN);
										world.setEntityX(entId, entXi);
										world.setEntityY(entId, entYi);
										world.simulation();
										timer.start();
										vboxL.getChildren().clear();
										vboxR.getChildren().clear();
				
										}
										
										else
										{
											String entN = nameTf.getText();
											int entXi = Integer.parseInt(xTf.getText());
											int entYi = Integer.parseInt(yTf.getText());
											int entEi = Integer.parseInt(energyTf.getText());
											
											world.setEntityEnergy(entId, entEi);
											world.setEntityName(entId, entN);
											world.setEntityX(entId, entXi);
											world.setEntityY(entId, entYi);
											timer.start();
											vboxL.getChildren().clear();
											vboxR.getChildren().clear();
											
										}
										
									}
							
								});
						
						
						
						vboxL.getChildren().addAll(nameLbl, nameTf, xLbl, xTf, yLbl, yTf, energyLbl, energyTf, saveCh, saveChanges);
						
						
					}
					
				});
				listLife();
				vboxL.getChildren().addAll(idLbl, tfEnterId, selectEnt);
				
			}
			
		});
		
		Button delLife = new Button();
		delLife.setText("Delete Life Form");
		delLife.setPrefSize(120, 25);
		delLife.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
					vboxL.getChildren().clear();
					vboxR.getChildren().clear();
					Label delLbl = new Label();
					delLbl.setText("Please Enter ID:");
					TextField tf = new TextField();
					tf.setMinSize(130, 25);
					tf.setMaxSize(130, 25);
					tf.setPrefSize(130, 25);
					
					
					Button delBtn = new Button();
					delBtn.setMinSize(130, 25);
					delBtn.setText("Delete Entity");
					delBtn.setOnAction(e -> 
					{
						//timer.stop();
						int id = Integer.parseInt(tf.getText());
						world.removeEntity(id);
						world.simulation();
						listLife();
						
						}); //Lambda expression
					
					
					vboxL.getChildren().clear();
					vboxL.getChildren().addAll(delLbl, tf, delBtn);
					
					
			}
			
		});
		
		Button addLife = new Button();
		addLife.setText("Add Life Form");
		addLife.setPrefSize(120, 25);
		addLife.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent arg0) 
			{
				
			
				//Just a place holder -> EDIT/COPY THIS FOR ADDING MORE NEW LIFE FORMS
				vboxL.getChildren().clear();
				vboxR.getChildren().clear();
				Image img1 = new Image(getClass().getResourceAsStream("Tiger.gif"));
				ImageView iv = new ImageView(img1);
				iv.setFitHeight(50);
				iv.setFitWidth(50);
				Button btn1 = new Button("", iv);
				btn1.setMinSize(50, 50);
				btn1.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent e) 
					{
						world.addEntityName("Tiger");
						listLife();
					}
					
				});
				
				Button btn2 = new Button();
				
				Button btn3 = new Button();
				
				vboxL.getChildren().clear();
				vboxL.getChildren().add(btn1);
				
		
			}
			
		});
		
		Button exit = new Button();
		exit.setText("Exit");
		exit.setPrefSize(120, 25);
		exit.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent E) 
			{
				root.setRight(vboxR);
				vboxL.getChildren().clear();
				vboxR.getChildren().clear();
				root.setTop(hbox);
			}
			
		});

		
		editHbox.setPadding(new Insets(15, 0, 15, 0));
		editHbox.setAlignment(Pos.CENTER);
		editHbox.getChildren().add(editHbox2);
		editHbox2.setSpacing(10);
		editHbox2.getChildren().addAll(modLife, delLife, addLife, exit);
		
	}
	//==============================================================IMPORTED METHODS
	
	public void autoUpdate()
	{
		try 
		{
			FileReader fr = new FileReader("Previous Configuration.txt");
			BufferedReader br = new BufferedReader(fr);
			autoConfigFile = br.readLine(); //Name of file
			currentFile = new File(autoConfigFile);
			br.close();
			
		} catch (IOException e) 
			{
				e.printStackTrace();
			}
	}

	public void PreviousFile()
	{
		try {
			FileReader fr = new FileReader(currentFile); //Read from txt file
			BufferedReader br = new BufferedReader(fr); //Put text into stream

			
			Configuration = br.readLine(); //initialise variable from stream
			br.close(); //close stream
				
			} catch (IOException e) 
			{
			}
		
		
			world = new AWorld(gc); //Need to create a new world for new config, otherwise old world will just fill up with more and more entities
			world.configureFile(Configuration); //New world configuration loaded
			
			Label currFile = new Label("Current File: " + autoConfigFile);
			currFile.setStyle("-fx-text-fill: white;");
			Label currConfig = new Label("Current Configuration: " + Configuration);
			currConfig.setStyle("-fx-text-fill: white;");
			vboxL.getChildren().addAll(currFile, currConfig);
	}
}
