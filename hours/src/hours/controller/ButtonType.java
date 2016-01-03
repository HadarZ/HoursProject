package hours.controller;

public enum ButtonType {
	OPEN_IN_EXCEL("Open in\nExcel"), OPEN_IN_NOTEPAD("Open in\nNotepad++"), START("Start\nWork"), END(
			"End\nWork"), RELOAD("Reload\nData"), CHECK("Check\nBalance"), SET_DAY_TYPE("Set Days\nAs...");

	private String text;

	private ButtonType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String asText() {
		return text;
	}
}
