package hours.viewmodel;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DataFileHandler {

	private static final String FILE_EXT = ".csv";
	private static final String COMMA_DELIMETER = ",";
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US);

	private MonthData monthData;
	private BackupManager backupManager;

	public DataFileHandler(MonthData monthData) {
		this.monthData = monthData;
	}

	public void read() {
		try {
			String filePath = getFullFilePath();
			File file = new File(filePath);
			if (!file.exists()) {
				File folder = new File(getFilePath());
				folder.mkdirs();
				file.createNewFile();
			}
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				parseLine(scanner.nextLine());
			}
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseLine(String nextLine) {
		String[] split = nextLine.split(COMMA_DELIMETER);
		final List<String> asList = new ArrayList<>(Arrays.asList(split));
		String key = asList.remove(0);
		DayType dayType = DayType.valueOf(asList.remove(0));
		DayData dayData = new DayData(LocalDate.parse(key, dateTimeFormatter), dayType);
		if (!asList.isEmpty()) {
			List<Time> times = asList.stream().map(Time::new).collect(Collectors.toList());
			dayData.setDayTimes(times);
		}
		monthData.addDay(dayData);
	}

	public void flush() {
		getBackupManager().backup();
		writeToFile();
	}

	private void writeToFile() {
		try {
			PrintWriter pw = new PrintWriter(getFullFilePath());

			for (DayData dayData : monthData) {
				StringBuilder stringBuilder = new StringBuilder(dayData.getDate().format(dateTimeFormatter));
				stringBuilder.append(COMMA_DELIMETER);
				stringBuilder.append(dayData.getDayType());
				stringBuilder.append(COMMA_DELIMETER);
				if (dayData.hasTimes()) {
					for (Time time : dayData.getDayTimes()) {
						stringBuilder.append(time);
						stringBuilder.append(COMMA_DELIMETER);
					}
				}
				pw.write(stringBuilder.toString());
				pw.println();
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BackupManager getBackupManager() {
		if (backupManager == null) {
			backupManager = new BackupManager(getFilePath(), getFileName());
		}
		return backupManager;
	}

	public String getFullFilePath() {
		return getFilePath() + getFileName();
	}

	private String getFileName() {
		return monthData.getFileName() + FILE_EXT;
	}

	private String getFilePath() {
		return System.getProperty("user.home") + "\\hours\\";
	}

}
