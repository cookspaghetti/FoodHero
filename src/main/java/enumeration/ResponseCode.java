package enumeration;

public enum ResponseCode {
	
	// Success Codes
    SUCCESS(200, "Operation completed successfully. "),

    // File Errors
    RECORD_NOT_FOUND(404, "Record not found. "),
    FAILED_TO_CREATE_FILE(400, "Failed to create file. "),

    // Exception Errors
    IO_EXCEPTION(501, "Input/Output Exception Occurred. "),
    JSON_EXCEPTION(502, "JSON Processing Exception. "),
	UNKNOWN_EXCEPTION(503, "Unknown Exception Occured. ");

    private final int code;
    private final String message;

    // Constructor
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Utility Method to Get ResponseCode by Code
    public static ResponseCode fromCode(int code) {
        for (ResponseCode responseCode : ResponseCode.values()) {
            if (responseCode.getCode() == code) {
                return responseCode;
            }
        }
        return null;
    }
    
}
