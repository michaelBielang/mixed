package elastic_file_indexer.document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Document {

	public Document(String path, LocalDateTime creationTime, LocalDateTime lastAccessTime,
			LocalDateTime lastModifiedTime, Long size) {
		this.path = path;
		this.creationTime = creationTime;
		this.lastAccessTime = lastAccessTime;
		this.lastModifiedTime = lastModifiedTime;
		this.size = size;
	}

	private String path;
	private LocalDateTime creationTime;
	private LocalDateTime lastAccessTime;
	private LocalDateTime lastModifiedTime;
	private long size;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getLasAccessTime() {
		return lastAccessTime;
	}

	public void setLasAccessTime(LocalDateTime lasAccessTime) {
		this.lastAccessTime = lasAccessTime;
	}

	public LocalDateTime getLasModifiedTime() {
		return lastModifiedTime;
	}

	public void setLasModifiedTime(LocalDateTime lasModifiedTime) {
		this.lastModifiedTime = lasModifiedTime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("path: ").append(this.path).append("\t\tcreationTime: ")
				.append(this.parseDate(this.creationTime)).append("\tlastAccessTime: ")
				.append(this.parseDate(this.lastAccessTime)).append("\tlastModifiedTime: ")
				.append(this.parseDate(this.lastModifiedTime)).append("\tsize: ").append(this.size / 1000).append(" KB")
				.toString();

	}

	private String parseDate(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		return date.format(formatter);
	}
}
