package hours.viewmodel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HoursModel implements IHoursModel {

	private MonthData monthData;

	private boolean isLoaded() {
		return monthData != null;
	}

	private void loadCurrentData() {
		if (!isLoaded()) {
			monthData = new MonthData();
			monthData.load();
		}
	}

	@Override
	public void startWork() {
		addTimeStampToFile();
	}

	private void addTimeStampToFile() {
		loadCurrentData();
		monthData.addTimeStamp(getCurrentTime());
	}

	@Override
	public String getFullFilePath() {
		loadCurrentData();
		return monthData.getFullFilePath();
	}

	private Time getCurrentTime() {
		LocalTime time = LocalTime.now();
		return new Time(time.getHour(), time.getMinute());
	}

	@Override
	public void endWork() {
		addTimeStampToFile();
	}

	@Override
	public HoursResult query() {
		loadCurrentData();

		Time time = new Time(0, 0);
		for (DayData dayData : monthData) {
			calculateTimeForDay(time, dayData);
		}

		/*-		Time time2 = new Time(0, 0);
				for (DayData dayData : monthData) {
					List<Time> times = dayData.getDayTimes();
					IntStream.range(0, times.size()).filter(i -> i % 2 == 0).limit(times.size() / 2)
							.mapToObj(i -> times.get(i + 1).getTimeDiff(times.get(i))).forEach(t -> time2.addTime(t));
				}
				System.out.println(time2);
		*/

		int totalRequiredHours = getRequiredHoursInMonth();
		int currentReqiuredHours = getRequiredHoursUntilToday();

		return new HoursResult(totalRequiredHours, currentReqiuredHours, time);
	}

	private void calculateTimeForDay(Time time, DayData dayData) {
		List<Time> times = dayData.getDayTimes();
		for (int i = 0; i < times.size() && i + 1 < times.size(); i += 2) {
			time.addTimeDiff(times.get(i), times.get(i + 1));
		}
	}

	private int getRequiredHoursInMonth() {
		return getWorkHoursInPeriod(monthData.getStartDate(), monthData.getEndDate());
	}

	private int getRequiredHoursUntilToday() {
		LocalDate today = LocalDate.now();
		if (today.equals(monthData.getStartDate())) {
			return 0;
		}
		if (!monthData.hasWorkedToday()) {
			today = today.minusDays(1);
		}
		return getWorkHoursInPeriod(monthData.getStartDate(), today);
	}

	private int getWorkHoursInPeriod(LocalDate start, LocalDate end) {
		int workDays = 0;
		LocalDate current = start;
		while (!current.isAfter(end)) {
			if (!current.getDayOfWeek().equals(DayOfWeek.FRIDAY)
					&& !current.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				workDays++;
			}
			current = current.plusDays(1);
		}
		workDays -= monthData.getNumberOfNonWorkDays(end);
		return workDays * 9;
	}

	@Override
	public void reload() {
		clean();
		loadCurrentData();
	}

	private void clean() {
		monthData = null;
	}

	@Override
	public int getNumberOfDays(DayType dayType) {
		return monthData.getNumberOfDays(dayType);
	}

	@Override
	public LocalDate getMonthEnd() {
		loadCurrentData();
		return monthData.getEndDate();
	}

	@Override
	public LocalDate getMonthStart() {
		loadCurrentData();
		return monthData.getStartDate();
	}

	@Override
	public void updateDaysInRange(LocalDate start, LocalDate end, DayType daysType) {
		monthData.updateDays(start, end, daysType);
	}
}
