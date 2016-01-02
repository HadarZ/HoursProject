package hours.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hours.controller.ButtonType;
import hours.controller.HoursViewHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HoursView extends FlowPane {

	private HBox buttonsPane = new HBox();
	private VBox statusPane = new VBox();
	private HoursViewHandler viewHandler;
	private DaysSelectorPane daysSelectorPane;
	private StackPane bottomPane = new StackPane();

	private Map<ButtonType, Button> buttons = new HashMap<>();

	public HoursView() {
		initPanes();
	}

	private void initPanes() {
		buttonsPane.setPadding(new Insets(30));
		buttonsPane.setSpacing(20);
		buttonsPane.setStyle("-fx-background-color: #336699;");
		buttonsPane.setPrefHeight(100);
		// buttonsPane.setPrefWidth(Integer.MAX_VALUE);

		statusPane.setPrefHeight(200);
		// statusPane.setPrefWidth(Integer.MAX_VALUE);
		statusPane.setPadding(new Insets(30));

		bottomPane.getChildren().add(statusPane);
		bottomPane.setPrefHeight(200);
		// bottomPane.setPrefWidth(Integer.MAX_VALUE);

		getChildren().addAll(buttonsPane, bottomPane);
	}

	public void addButton(ButtonType buttonType) {
		Button button = new Button(buttonType.getText());
		button.setFocusTraversable(false);
		button.setTextAlignment(TextAlignment.CENTER);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> viewHandler.buttonClicked(buttonType));
		buttonsPane.getChildren().add(button);
		buttons.put(buttonType, button);
	}

	public void setStatusLines(List<String> lines) {
		statusPane.getChildren().clear();
		for (String string : lines) {
			Text text = new Text(string);
			statusPane.getChildren().add(text);
			VBox.setMargin(text, new Insets(0, 0, 10, 0));
		}
	}

	public void setViewHandler(HoursViewHandler viewHandler) {
		this.viewHandler = viewHandler;
	}

	public void showDaysSelector(LocalDate rangeStart, LocalDate rangeEnd) {
		daysSelectorPane = new DaysSelectorPane(rangeStart, rangeEnd, viewHandler);
		daysSelectorPane.setPrefWidth(buttonsPane.getWidth());
		bottomPane.getChildren().add(daysSelectorPane);
	}

	public void hideDaysSelector() {
		bottomPane.getChildren().remove(daysSelectorPane);
		daysSelectorPane = null;
	}

	public void enableButton(ButtonType buttonType) {
		buttons.get(buttonType).setDisable(false);
	}

	public void disableButton(ButtonType buttonType) {
		buttons.get(buttonType).setDisable(true);
	}

	public DaysSelectorPane getDaysSelectorPane() {
		return daysSelectorPane;
	}
}
