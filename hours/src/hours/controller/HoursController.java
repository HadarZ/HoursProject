package hours.controller;

import java.io.IOException;

import hours.ui.DaysSelectorPane;
import hours.ui.HoursView;
import hours.viewmodel.IHoursModel;

public class HoursController implements HoursViewHandler {
	private IHoursModel hoursModel;
	private HoursView hoursView;
	private HoursStatusController statusController;

	public HoursController(HoursView view, IHoursModel model) {
		hoursView = view;
		hoursModel = model;
		statusController = new HoursStatusController(hoursModel, hoursView);
	}

	public void start() {
		hoursView.addButton(ButtonType.START);
		hoursView.addButton(ButtonType.END);
		hoursView.addButton(ButtonType.RELOAD);
		hoursView.addButton(ButtonType.CHECK);
		hoursView.addButton(ButtonType.OPEN_IN_EXCEL);
		hoursView.addButton(ButtonType.OPEN_IN_NOTEPAD);
		hoursView.addButton(ButtonType.SET_DAY_TYPE);
	}

	private void query() {
		statusController.showStatus();
	}

	private void openExcel() {
		openEditor("C:\\Program Files (x86)\\Microsoft Office\\Office14\\EXCEL.EXE");
	}

	private void openEditor(String programPath) {
		try {
			Runtime.getRuntime().exec(programPath + " " + hoursModel.getFullFilePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openNotepad() {
		openEditor("C:\\Program Files (x86)\\Notepad++\\notepad++.exe");
	}

	@Override
	public void buttonClicked(ButtonType type) {
		switch (type) {
		case CHECK:
			query();
			break;
		case OPEN_IN_EXCEL:
			openExcel();
			break;
		case OPEN_IN_NOTEPAD:
			openNotepad();
			break;
		case START:
			hoursModel.startWork();
			break;
		case END:
			hoursModel.endWork();
			break;
		case RELOAD:
			hoursModel.reload();
			break;
		case SET_DAY_TYPE:
			hoursView.showDaysSelector(hoursModel.getMonthStart(), hoursModel.getMonthEnd());
			hoursView.disableButton(ButtonType.SET_DAY_TYPE);
		default:
			break;
		}
	}

	@Override
	public void doneSelectingDaysClicked() {
		DaysSelectorPane selectorPane = hoursView.getDaysSelectorPane();
		hoursModel.updateDaysInRange(selectorPane.getStart(), selectorPane.getEnd(), selectorPane.getDaysType());
		query();
		closeSelectionPanel();
	}

	@Override
	public void cancelSelectingDaysClicked() {
		closeSelectionPanel();
	}

	private void closeSelectionPanel() {
		hoursView.hideDaysSelector();
		hoursView.enableButton(ButtonType.SET_DAY_TYPE);
	}
}
