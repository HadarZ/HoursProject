package hours.viewmodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DayData {
	private DayType dayType;
	private List<Time> dayTimes = new ArrayList<>();
	private LocalDate date;

	public DayData(LocalDate date, DayType dayType) {
		super();
		this.date = date;
		this.dayType = dayType;
	}

	public DayData(LocalDate date) {
		this(date, null);
	}

	public void setDayTimes(List<Time> dayTimes) {
		this.dayTimes = dayTimes;
	}

	public void setDayType(DayType dayType) {
		this.dayType = dayType;
	}

	public DayType getDayType() {
		return dayType;
	}

	public List<Time> getDayTimes() {
		return dayTimes;
	}

	public void addTime(Time time) {
		dayTimes.add(time);
	}

	public boolean hasTimes() {
		return !dayTimes.isEmpty();
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isEmpty() {
		return dayTimes.size() < 2;
	}
}
