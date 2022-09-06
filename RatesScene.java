import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.geometry.Insets;

public class RatesScene extends Scene
{
	private BorderPane myRoot;
	private static double hourCharge;
	private static double hour5Charge;
	private static double dayCharge;
	
	public RatesScene(Parent root, View view) {
		super(root);
		
		myRoot = (BorderPane) root;
		View myView = view;
		
		Text title = new Text("\t\t    Rates\n\t  Amount Charged($):");
		title.setFont(new Font(50));
		
		myRoot.setTop(title);
		
		VBox centerBox = new VBox(100);
		HBox centerBoxRow = new HBox(60);
		
		Text forSpacing = new Text(" ");
		Text perHour = new Text("Per Hour: ");
		Text perDay = new Text("Per Day: ");
		
		perHour.setFont(new Font(20));
		perDay.setFont(new Font(20));
		
		TextField hourField = new TextField("" + MainScene.getHourCharge());
		TextField dayField = new TextField("" + MainScene.getDayCharge());
		
		centerBoxRow.getChildren().
			addAll(perHour, hourField, perDay, dayField);
		
		HBox centerBoxRow2 = new HBox(36);
		Text per5Hours = new Text("Per 5 Hours: ");
		per5Hours.setFont(new Font(20));
		
		TextField hour5Field = new TextField("" + MainScene.getHour5Charge());
		
		centerBoxRow2.getChildren().addAll(per5Hours, hour5Field);
		centerBoxRow2.setMargin(per5Hours, new Insets(0,0,0,200));
		
		centerBox.getChildren().addAll(forSpacing, 
					centerBoxRow, centerBoxRow2);
		
		
		myRoot.setCenter(centerBox);
		
		VBox bottomButtons = new VBox(20);
		
		HBox forSave = new HBox();
		Button saveButton = new Button("Save");
		saveButton.setMinWidth(50);
		saveButton.setMinHeight(50);
		saveButton.setFont(new Font(20));
		forSave.setMargin(saveButton, new Insets(0,0,0,300));
		forSave.getChildren().add(saveButton);
		
		saveButton.setOnAction(e -> {
			String numChecker = "[0-9.]+";
			
			if(hourField.getText().trim().matches(numChecker)
				&& hour5Field.getText().trim().matches(numChecker)
				&& dayField.getText().trim().matches(numChecker))
			{
				hourCharge = Double.parseDouble(hourField.getText());
				hour5Charge = Double.parseDouble(hour5Field.getText());
				dayCharge = Double.parseDouble(dayField.getText());
			}
			//if the user didn't input his/her coordinates and the maximum
			//distance he/she will travel to locations with ratings.
			else
			{
				if(!(hourField.getText().trim().matches(numChecker)))
					hourField.setText("Enter charge rate per hour");
					
				if(!(hour5Field.getText().trim().matches(numChecker)))
					hour5Field.setText("Enter charge rate per 5 hours");
					
				if(!(dayField.getText().trim().matches(numChecker)))
					dayField.setText("Enter charge rate per day");
			}
			
		});
		
		
		HBox forBack = new HBox();
		Button backButton = new Button("Back");
		backButton.setMinWidth(50);
		backButton.setMinHeight(50);
		backButton.setFont(new Font(20));
		forBack.setMargin(backButton, new Insets(0,0,0,300));
		forBack.getChildren().add(backButton);
		
		backButton.setOnAction(e -> {
			
			myView.getStage().setScene(myView.getMainScene());
		});
		
		bottomButtons.getChildren().addAll(forSave, forBack);
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
}
