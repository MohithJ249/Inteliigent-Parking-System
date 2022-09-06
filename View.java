import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View 
{
	private Stage myStage;
	private Scene myMainScene;
	private Scene myRatesScene;
	private Scene myPlanScene;
	private ParkingSystem parker;
	
	public View()
	{
		parker = new ParkingSystem();
		myMainScene = new MainScene(new BorderPane(), this);
		myRatesScene = new RatesScene(new BorderPane(), this);
		myPlanScene = new PlanScene(new BorderPane(), this);
		
	}
	public Scene getMainScene()
	{
		return myMainScene;
	}
	public Scene getRatesScene()
	{
		return myRatesScene;
	}
	public Scene getPlanScene()
	{
		return myPlanScene;
	}
	public ParkingSystem getParkingSystem()
	{
		return parker;
	}
	public Stage getStage() { return myStage; }
	
	public void setStage(Stage stage)
	{
		myStage = stage;
		myStage.setTitle("Intelligent Parking System");
		myStage.setWidth(700);
		myStage.setHeight(670);
		myStage.setScene(myMainScene);
		
		stage.show();
	}
}
