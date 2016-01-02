package hours.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hours.ui.DaysSelectorPane;
import hours.ui.HoursView;
import hours.viewmodel.DayType;
import hours.viewmodel.HoursResult;
import hours.viewmodel.IHoursModel;
import hours.viewmodel.Time;

public class HoursController implements HoursViewHandler {
	private IHoursModel hoursModel;
	private HoursView hoursView;

	public HoursController(HoursView view, IHoursModel model) {
		hoursView = view;
		hoursModel = model;
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
		HoursResult query = hoursModel.query();

		List<String> lines = new ArrayList<>();

		String totalHours = String.format("Total required work hours: %d", query.getTotalRequiredHours());
		lines.add(totalHours);

		String currentRequired = String.format("Current required work hours: %d", query.getCurrentRequiredHours());
		lines.add(currentRequired);

		String workHours = String.format("Work hours: %02d:%02d", query.getWorkHours(), query.getWorkMinutes());
		lines.add(workHours);

		Time required = new Time(query.getCurrentRequiredHours(), 0);
		Time worked = new Time(query.getWorkHours(), query.getWorkMinutes());
		Time diff = worked.getTimeDiff(required);
		String hoursDiff;
		if (diff.isNegative()) {
			hoursDiff = String.format("You have minus %02d:%02d hours", diff.getHours(), diff.getMinutes());
		} else {
			hoursDiff = String.format("You have extra %02d:%02d hours", diff.getHours(), diff.getMinutes());
		}
		lines.add(hoursDiff);

		String sickDays = String.format("You were sick for %d days", hoursModel.getNumberOfDays(DayType.Sick));
		lines.add(sickDays);

		String vacationDays = String.format("You were on vacation for %d days",
				hoursModel.getNumberOfDays(DayType.Vacation));
		lines.add(vacationDays);

		hoursView.setStatusLines(lines);
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
