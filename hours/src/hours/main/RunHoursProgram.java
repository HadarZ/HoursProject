package hours.main;

import hours.controller.HoursController;
import hours.ui.HoursView;
import hours.viewmodel.HoursModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunHoursProgram extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Monthly Hours (\u00a9 Hadar Hillel)");
		HoursView view = new HoursView();
		primaryStage.setScene(new Scene(view, 580, 300));

		HoursController controller = new HoursController(view, new HoursModel());
		view.setViewHandler(controller);
		controller.start();
		primaryStage.show();
	}
}
