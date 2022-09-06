import java.util.ArrayList;
import java.util.TreeMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class MainScene extends Scene
{
	private ParkingSystem parker;
	private BorderPane myRoot;
	private Text forP1;
	private static double hourCharge;
	private static double hour5Charge;
	private static double dayCharge;
	
	public MainScene(Parent root, View view) {
		super(root);
		
		myRoot = (BorderPane) root;
		View myView = view;
		parker = myView.getParkingSystem();
		forP1 = new Text("");
		
		hourCharge = parker.getRates()[0];
		hour5Charge = parker.getRates()[1];
		dayCharge = parker.getRates()[2];
		
		//top part of the scene
		VBox top = new VBox(10);
		
		//title
		Text title = new Text("    Intelligent Parking System");
		title.setFont(new Font(50));
		
		editForP1();
		Pane p = new Pane();
		ScrollPane p1 = new ScrollPane();
		p1.setPrefSize(680, 180);
		p.getChildren().add(p1);
		
		p1.setContent(forP1);
		top.getChildren().addAll(title, p);
		myRoot.setTop(top);
	
		
		//center part of the scene
		Text forParking = new Text("\tFor Parking:");
		Text forLeaving = new Text("\tFor Leaving:");
		Text forID = new Text(" ID: ");
		Text forID2 = new Text("ID:  ");
		Text forTimeParked = new Text(" Time Parked:\t\t\t\t       ");
		Text forCharges = new Text("Amount Charged: $\nProfits: $" 
								+ parker.calculateProfits());
		
		forParking.setFont(new Font(20));
		forLeaving.setFont(new Font(20));
		forID.setFont(new Font(20));
		forID2.setFont(new Font(20));
		forTimeParked.setFont(new Font(20));
		forCharges.setFont(new Font(20));

		HBox unparkID = new HBox(10);
		TextField idEntry = new TextField();
		unparkID.getChildren().addAll(forID2, idEntry);
		
		
		HBox forUnpark = new HBox();
		Button unParkButton = new Button("Unpark");
		unParkButton.setMinWidth(50);
		unParkButton.setMinHeight(50);
		unParkButton.setFont(new Font(20));
		forUnpark.setMargin(unParkButton, new Insets(0,0,0,50));
		forUnpark.getChildren().add(unParkButton);
		
		HBox forParkButton = new HBox();
		Button parkButton = new Button("Park");
		parkButton.setMinWidth(50);
		parkButton.setMinHeight(50);
		parkButton.setFont(new Font(20));
		forParkButton.setMargin(parkButton, new Insets(0,0,0,100));
		forParkButton.getChildren().add(parkButton);
		
		GridPane centerGrid = new GridPane();
		centerGrid.setHgap(90);
		centerGrid.setVgap(50);	
		
		centerGrid.add(forParking, 0, 0);
		centerGrid.add(forLeaving, 1, 0);
		centerGrid.add(forID, 0, 1);
		centerGrid.add(unparkID, 1, 1);
		centerGrid.add(forTimeParked, 0, 2);
		centerGrid.add(forCharges, 1, 2);
		centerGrid.add(forParkButton, 0, 3);
		centerGrid.add(forUnpark, 1, 3);
		
		myRoot.setCenter(centerGrid);
		
		parkButton.setOnAction(e -> {
			Car temp = parker.assignCarToFloor();
			
			if(temp != null)
			{
				forID.setText(" ID: " + temp.getID());
				forTimeParked.setText(" Time Parked: " + temp.getTime());
				editForP1();
			}
			else
			{
				forID.setText(" ID: Parking lot is full");
				forTimeParked.setText(" Time Parked: NA");
			}
		});
		
		unParkButton.setOnAction(e -> {
			String numChecker = "[0-9]+";
			
			if(!(idEntry.getText().trim().matches(numChecker)))
				idEntry.setText("Enter the ID of the car");
			else
			{
				int idOfCar = Integer.parseInt(idEntry.getText());
				
				double chargePerHour = RatesScene.getHourCharge();
				double chargePer5Hour = RatesScene.getHour5Charge();
				double chargePerDay = RatesScene.getDayCharge();

				if(chargePerHour != 0 && chargePer5Hour != 0  
					&& chargePerDay != 0)
					parker.setRates(chargePerHour, chargePer5Hour, chargePerDay);
					
				forCharges.setText("Amount Charged: $" + 
								parker.calculateCharge(idOfCar)
								+ "\nProfits: $" + 
								parker.calculateProfits());
				parker.unparkCar(idOfCar);
				
			}
			
			editForP1();
		});
			
		//bottom part of the scene		
		HBox bottomButtons = new HBox(10);
		
		Button setRatesButton = new Button("Set Rates?");
		setRatesButton.setMinWidth(50);
		setRatesButton.setMinHeight(50);
		setRatesButton.setFont(new Font(20));

		setRatesButton.setOnAction(e -> {
			
			myView.getStage().setScene(myView.getRatesScene());
		});
		
		Button setPlanButton = new Button("Set Building Plan?");
		setPlanButton.setMinWidth(50);
		setPlanButton.setMinHeight(50);
		setPlanButton.setFont(new Font(20));

		setPlanButton.setOnAction(e -> {
			forP1.setText("");
			myView.getStage().setScene(myView.getPlanScene());
		});
		
		Button doneButton = new Button("Done");
		doneButton.setMinWidth(50);
		doneButton.setMinHeight(50);
		doneButton.setFont(new Font(20));

		doneButton.setOnAction(e -> {
			parker.writeCarsToFile();
			System.exit(0);
		});
		
		bottomButtons.getChildren().addAll(setRatesButton
				, setPlanButton, doneButton);
		bottomButtons.setAlignment(Pos.CENTER);
	
		myRoot.setBottom(bottomButtons);
		
	}
	public static double getHourCharge()
	{
		return hourCharge;
	}
	public static double getHour5Charge()
	{
		return hour5Charge;
	}
	public static double getDayCharge()
	{
		return dayCharge;
	}
	public void editForP1()
	{
		int[][] carsPerFloor = parker.getCarsPerFloor();
		int maxFloors = parker.getFloors();
		
		String textToAdd = "";
		
		for(int i = 0; i < maxFloors; i++)
		{
			textToAdd += "Floor: " + i + "\nSpaces Taken: " + carsPerFloor[0][i]
			+ "\nTotal Number of Spaces: " + carsPerFloor[1][i] + "\n\n\n";
		}
		
		forP1.setText(textToAdd);
	}
}
