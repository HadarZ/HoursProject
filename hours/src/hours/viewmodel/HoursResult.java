package hours.viewmodel;

public class HoursResult {
	private int totalRequiredHours;
	private Time workHours;
	private int currentRequiredHours;

	public HoursResult(int totalRequiredHours, int currentRequiredHours, Time workHours) {
		super();
		this.totalRequiredHours = totalRequiredHours;
		this.currentRequiredHours = currentRequiredHours;
		this.workHours = workHours;
	}

	public int getTotalRequiredHours() {
		return totalRequiredHours;
	}

	public int getWorkHours() {
		return workHours.getHours();
	}

	public int getWorkMinutes() {
		return workHours.getMinutes();
	}

	public int getCurrentRequiredHours() {
		return currentRequiredHours;
	}
}
