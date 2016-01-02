package hours.viewmodel;

import java.time.LocalDate;

public interface IHoursModel {

	void startWork();

	String getFullFilePath();

	void endWork();

	HoursResult query();

	void reload();

	int getNumberOfDays(DayType dayType);

	LocalDate getMonthStart();

	LocalDate getMonthEnd();

	void updateDaysInRange(LocalDate start, LocalDate end, DayType daysType);
}