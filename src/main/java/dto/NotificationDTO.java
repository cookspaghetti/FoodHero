package dto;

import java.time.LocalDateTime;

public class NotificationDTO {
	
	private String id;
    private String userId;
    private String title;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;
    
    // Constructor
    public NotificationDTO(String id, String userId, String title, String message, LocalDateTime timestamp,
			boolean isRead) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.message = message;
		this.timestamp = timestamp;
		this.isRead = isRead;
	}
    
	// Getters and Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
    
}
