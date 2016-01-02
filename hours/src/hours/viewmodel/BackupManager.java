package hours.viewmodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BackupManager {

	private static final int MAX_NUMBER_OF_BACKUPS = 5;
	private static final String BACKUPS_FOLDER = "backups\\";
	private static final String BACKUP_FILE_SUFFIX = ".backup";
	private static final String BACKUP_FOLDER_PREFIX = "backup_";

	private String filePath;
	private String fileName;

	public BackupManager(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	public void backup() {
		ensureBackupsFolder();
		doBackup();
		deleteOldBackups();
	}

	private void ensureBackupsFolder() {
		File dir = new File(getBackupFolder());
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private void doBackup() {
		Path from = Paths.get(filePath + fileName);
		Path to = Paths.get(getBackupFolder() + getBackupFileName());
		try {
			Files.copy(from, to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getBackupFileName() {
		String backupFileName = System.currentTimeMillis() + "_" + fileName + BACKUP_FILE_SUFFIX;
		return backupFileName;
	}

	private void deleteOldBackups() {
		File dir = new File(getBackupFolder());
		File[] matchingFiles = dir.listFiles(f -> f.getName().contains(fileName));

		if (matchingFiles != null && matchingFiles.length >= MAX_NUMBER_OF_BACKUPS) {
			Arrays.stream(matchingFiles).sorted((f1, f2) -> (int) (f1.lastModified() - f2.lastModified()))
					.limit(matchingFiles.length - MAX_NUMBER_OF_BACKUPS).forEach(File::delete);
		}
	}

	private String getBackupFolder() {
		return filePath + BACKUPS_FOLDER + BACKUP_FOLDER_PREFIX + fileName + "\\";
	}
}
