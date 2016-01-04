package hours.ui;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import hours.controller.HoursViewHandler;
import hours.viewmodel.DayType;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DaysSelectorPane extends VBox {

	private LocalDate rangeStart;
	private LocalDate rangeEnd;

	private DatePicker startPicker = new DatePicker();
	private DatePicker endPicker = new DatePicker();
	private HoursViewHandler viewHandler;

	private ComboBox<DayType> daysType = new ComboBox<>();

	public DaysSelectorPane(LocalDate rangeStart, LocalDate rangeEnd, HoursViewHandler viewHandler) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.viewHandler = viewHandler;
		setStyle("-fx-background-color: #DADFE0;");
		setPadding(new Insets(30));
		setSpacing(30);
		initialise();
	}

	private void initialise() {
		List<DayType> list = Arrays.stream(DayType.values()).filter(t -> !t.equals(DayType.Work))
				.collect(Collectors.toList());
		daysType.getItems().setAll(list);
		Button cancel = createAndAddButton(e -> viewHandler.cancelSelectingDaysClicked(), "Cancel");
		Button done = createAndAddButton(e -> viewHandler.doneSelectingDaysClicked(), "Done");
		endPicker.setDisable(true);
		startPicker.setDayCellFactory(dcf -> new StartRangeDayCell());
		endPicker.setDayCellFactory(dcf -> new EndRangeDayCell());

		daysType.valueProperty().addListener((v, o, n) -> refreshDoneButton(done));
		endPicker.valueProperty().addListener((v, o, n) -> refreshDoneButton(done));
		startPicker.valueProperty().addListener((v, o, n) -> {
			if (n == null) {
				endPicker.setDisable(true);
				endPicker.setValue(null);
			} else {
				endPicker.setDisable(false);
				if (endPicker.getValue() == null) {
					endPicker.setValue(n);
				}
			}
			refreshDoneButton(done);
		});
		startPicker.requestFocus();

		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(20);
		gridPane.add(new Label("Select dates range"), 0, 0, 2, 1);
		gridPane.add(new Label("From:"), 0, 1);
		gridPane.add(startPicker, 0, 2);
		gridPane.add(new Label("To:"), 1, 1);
		gridPane.add(endPicker, 1, 2);
		// GridPane.setHgrow(daysType, Priority.ALWAYS);
		gridPane.add(daysType, 2, 2);
		// gridPane.setStyle("-fx-border-color: #2e8b57;-fx-border-width:
		// 1px;-fx-border-style:solid;");

		HBox hBox = new HBox(20);
		hBox.getChildren().addAll(cancel, done);
		hBox.setAlignment(Pos.TOP_RIGHT);
		// hBox.setStyle("-fx-border-color: red;-fx-border-width:
		// 1px;-fx-border-style:solid;");

		// hBox.setMaxWidth(520);
		// hBox.setPrefSize(getWidth(), getHeight());

		getChildren().addAll(gridPane, hBox);

		refreshDoneButton(done);
	}

	private void refreshDoneButton(Button doneButton) {
		doneButton.setDisable(
				startPicker.getValue() == null || endPicker.getValue() == null || daysType.getValue() == null);
	}

	private Button createAndAddButton(EventHandler<? super MouseEvent> eventHandler, String text) {
		Button cancel = new Button(text);
		cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
		cancel.setFocusTraversable(false);
		return cancel;
	}

	public LocalDate getStart() {
		return startPicker.getValue();
	}

	public LocalDate getEnd() {
		return endPicker.getValue();
	}

	public DayType getDaysType() {
		return daysType.getValue();
	}

	private class StartRangeDayCell extends DateCell {

		@Override
		public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);

			if (item.isBefore(rangeStart) || item.isAfter(rangeEnd)) {
				setDisable(true);
			}
		}
	}

	private class EndRangeDayCell extends StartRangeDayCell {

		@Override
		public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);

			if (startPicker.getValue() != null) {
				if (item.isBefore(startPicker.getValue())) {
					setDisable(true);
				}
				if (endPicker.getValue() != null) {
					if ((item.isEqual(startPicker.getValue()) || item.isAfter(startPicker.getValue()))
							&& (item.isBefore(endPicker.getValue()) || item.isEqual(endPicker.getValue()))) {
						setStyle("-fx-background-color: #ffc0cb;");
					}
				}
			}
		}
	}

}
