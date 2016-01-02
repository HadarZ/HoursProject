package hours.viewmodel;

public class Time {
	int hours;
	int minutes;
	boolean negative = false;

	public Time(int hours, int minutes) {
		super();
		this.hours = hours;
		this.minutes = minutes;
	}

	public Time(String hour) {
		String[] times = hour.split(":");
		hours = Integer.parseInt(times[0]);
		minutes = Integer.parseInt(times[1]);
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d", hours, minutes);
	}

	public Time getTimeDiff(Time another) {
		int totalMinutes = hours * 60 + minutes;
		int totalMinutesAnother = another.hours * 60 + another.minutes;
		int diff = totalMinutes - totalMinutesAnother;
		Time hourDiff = new Time(Math.abs(diff) / 60, Math.abs(diff) % 60);
		hourDiff.negative = diff < 0;
		return hourDiff;
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public boolean isNegative() {
		return negative;
	}

	public void addTimeDiff(Time time1, Time time2) {
		Time diff = time1.getTimeDiff(time2);
		hours += diff.getHours();
		minutes += diff.getMinutes();
		if (minutes >= 60) {
			hours++;
			minutes -= 60;
		}
	}

	// public void addTime(Time time) {
	// hours += time.getHours();
	// minutes += time.getMinutes();
	// if (minutes >= 60) {
	// hours++;
	// minutes -= 60;
	// }
	// }
}
