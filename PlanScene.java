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
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;

public class PlanScene extends Scene
{
	private BorderPane myRoot;
	private Text[] floorNums;
	private TextField[] fields;
	private HBox[] rows;
	private VBox textPlusFields;
	private int maxFloors;
	private int[][] plan;
	private static boolean checkPlanNums;
	
	public PlanScene(Parent root, View view) {
		super(root);
		
		myRoot = (BorderPane) root;
		View myView = view;
		checkPlanNums = true;
		
		VBox topPart = new VBox(50);
		
		Text title = new Text("\t\tBuilding Plan");
		title.setFont(new Font(50));
		
		Text note = new Text("   Note: Ensure that all of the cars left,"
				+ " as the previous data will be removed.");
		note.setFont(new Font(20));
		
		Text note2 = new Text("   Note: If nothing is entered, the program"
							+ " will default to 40.");
		note2.setFont(new Font(20));
		
		HBox floorRow = new HBox(20);
		Text floorNum = new Text("Number of Floors: ");
		floorNum.setFont(new Font(20));
		TextField floorField = new TextField();
		Button submitButton = new Button("Submit");
		floorRow.getChildren().
			addAll(floorNum, floorField, submitButton);
		
		topPart.getChildren().addAll(title, note, floorRow, note2);
		
		myRoot.setTop(topPart);
		
		
		textPlusFields = new VBox();
		Text forP1 = new Text("\t\t\t\t\tEnter the total number of cars each "
		+ "floor can hold\n");
		Pane p = new Pane();
		ScrollPane p1 = new ScrollPane();
		p1.setPrefSize(680, 150);
		p.getChildren().add(p1);
		
		p1.setContent(textPlusFields);
		textPlusFields.getChildren().add(forP1);

		myRoot.setCenter(p);
		
		
		submitButton.setOnAction(e -> {
			
			textPlusFields.getChildren().clear();
			textPlusFields.getChildren().add(forP1);
			
			String numChecker = "[0-9]+";
			
			
			if(floorField.getText().trim().matches(numChecker))
			{	
				int floorInput = Integer.parseInt(floorField.getText());
				maxFloors = floorInput;
				initialize(floorInput);
				
				for(int i = 0; i < floorInput; i++)
				{
					floorNums[i] = new Text("Floor: " + i);
					fields[i] = new TextField();
					rows[i] = new HBox(50);
					rows[i].getChildren().addAll(floorNums[i], fields[i]);
					fields[i].setPrefWidth(400);
					
					textPlusFields.getChildren().add(rows[i]);
				}
				
			}
			//if the user didn't input his/her coordinates and the maximum
			//distance he/she will travel to locations with ratings.
			else
			{
				floorField.setText("Enter the total number of floors");
			}
			
		});
		
		VBox bottomSection = new VBox(10);
		Text note3 = new Text(" Note: You must either enter the number of parking"
		 + " spaces each floor can hold or\n leave each one blank to go back to"
		 + " save the new plan. Also, the back button won't\n save any edits made here."
		 + " The save button will take you back, if the input is valid.");
		note3.setFont(new Font(18));
		
		HBox bottomButtons = new HBox(200);
		
		Button saveButton = new Button("Save");
		saveButton.setMinWidth(50);
		saveButton.setMinHeight(50);
		saveButton.setFont(new Font(20));

		saveButton.setOnAction(e -> {
			for(int i = 0; i < maxFloors; i++)
			{
				plan[0][i] = 0;
				
				String spacePerFloor = fields[i].getText();
				
				String numChecker = "[0-9]+";
			
				if(spacePerFloor.trim().matches(numChecker))
					plan[1][i] = Integer.parseInt(spacePerFloor);
				else if(spacePerFloor.equals(""))
					plan[1][i] = 40;
				else
				{
					fields[i].setText("Enter the number of parking spaces");
					checkPlanNums = false;
				}
				
			}
			if(checkPlanNums)
			{
				textPlusFields.getChildren().clear();
				floorField.setText("");
				myView.getParkingSystem().setPlan(maxFloors, plan);
				myView.getStage().setScene(myView.getMainScene());
			}
			else
				checkPlanNums = true;
		});
		

		Button backButton = new Button("Back");
		backButton.setMinWidth(50);
		backButton.setMinHeight(50);
		backButton.setFont(new Font(20));

		backButton.setOnAction(e -> {
			myView.getStage().setScene(myView.getMainScene());
		});
		
		bottomButtons.getChildren().addAll(saveButton, backButton);
		bottomButtons.setAlignment(Pos.CENTER);
		bottomSection.getChildren().addAll(note3, bottomButtons);
		myRoot.setBottom(bottomSection);
		
		
		
	}
	public static boolean hasNewPlan()
	{
		return checkPlanNums;
	}
	public int[][] getBuildingPlan()
	{
		return plan;
	}
	public int getTotalFloors()
	{
		return maxFloors;
	}
	public void initialize(int floorInput)
	{
		plan = new int[2][floorInput];
		floorNums = new Text[floorInput];
		fields = new TextField[floorInput];
		rows = new HBox[floorInput];
	}
}
