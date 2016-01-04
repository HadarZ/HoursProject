package hours.viewmodel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MonthData implements Iterable<DayData> {
	private DataFileHandler dataFileHandler;

	private Map<LocalDate, DayData> daysData;

	private LocalDate startDate;
	private LocalDate endDate;

	public MonthData() {
		daysData = new TreeMap<>();
		dataFileHandler = new DataFileHandler(this);
		initStartAndEndDates();
	}

	private void initStartAndEndDates() {
		startDate = LocalDate.now();
		while (startDate.getDayOfMonth() != 16) {
			startDate = startDate.minusDays(1);
		}

		endDate = LocalDate.now();
		while (endDate.getDayOfMonth() != 15) {
			endDate = endDate.plusDays(1);
		}
	}

	public void addTimeStamp(Time time) {
		getOrCreateCurrentDayData().addTime(time);
		setDayAs(LocalDate.now(), DayType.Work);

		dataFileHandler.flush();
	}

	private DayData getOrCreateCurrentDayData() {
		return getOrCreateDayDataFor(LocalDate.now());
	}

	public String getFileName() {
		String fileName = String.format("%02d_%d-%02d_%d", startDate.getMonthValue(), startDate.getYear(),
				endDate.getMonthValue(), endDate.getYear());
		return fileName;
	}

	@Override
	public String toString() {
		return "start: " + startDate + ", end: " + endDate;
	}

	public void load() {
		dataFileHandler.read();
	}

	public String getFullFilePath() {
		return dataFileHandler.getFullFilePath();
	}

	public void setDayAs(LocalDate date, DayType type) {
		getOrCreateDayDataFor(date).setDayType(type);
	}

	private DayData getOrCreateDayDataFor(LocalDate date) {
		DayData dayData = daysData.get(date);
		if (dayData == null) {
			dayData = new DayData(date);
			daysData.put(date, dayData);
		}
		return dayData;
	}

	public int getNumberOfDays(DayType type) {
		long count = daysData.values().stream().filter(d -> d.getDayType() == type).count();
		return (int) count;
	}

	@Override
	public Iterator<DayData> iterator() {
		return daysData.values().iterator();
	}

	public void addDay(DayData dayData) {
		daysData.put(dayData.getDate(), dayData);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public DayType getDayType(LocalDate current) {
		return getOrCreateDayDataFor(current).getDayType();
	}

	public int getNumberOfNonWorkDays(LocalDate dateLimit) {
		return (int) daysData.values().stream().filter(d -> !d.getDayType().equals(DayType.Work))
				.filter(d -> !d.getDate().isAfter(dateLimit)).count();
	}

	public void updateDays(LocalDate start, LocalDate end, DayType daysType) {
		LocalDate current = start;
		while (!current.isAfter(end)) {
			if (!current.getDayOfWeek().equals(DayOfWeek.FRIDAY)
					&& !current.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				setDayAs(current, daysType);
			}
			current = current.plusDays(1);
		}
		dataFileHandler.flush();
	}

	public boolean hasWorkedToday() {
		DayData todayData = daysData.get(LocalDate.now());
		if (todayData == null) {
			return false;
		}
		return !todayData.isEmpty();
	}
}
