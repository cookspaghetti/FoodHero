package enumeration;

public enum TaskStatus {
	
	PENDING,
    ASSIGNED,       // Task assigned to the runner
    ACCEPTED,       // Runner has accepted the task
    IN_PROGRESS,    // Runner is currently performing the task
    COMPLETED,      // Task successfully completed
    CANCELLED,      // Task has been cancelled
    FAILED          // Task failed due to an issue
}

