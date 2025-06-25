package in.ac.iitj.instiapp.payload.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private String internalCode;
    private String message;
    private T data;
    private List<String> errors;
    private String timestamp;

    // Constructors
    public ApiResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public ApiResponse(int status, String internalCode, String message, T data, List<String> errors) {
        this.status = status;
        this.internalCode = internalCode;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    // Getters and setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp;
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                message,
                data,
                null
        );
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                null,
                message,
                null,
                null
        );
    }

    public static <T> ApiResponse<T> error(Exception ex, String internalCode, HttpStatus status, T data, List<String> errors) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        List<String> errorList = (errors != null) ? errors : Collections.singletonList(errorMessage);
        return new ApiResponse<>(
                status.value(),
                internalCode,
                errorMessage,
                data,
                errorList
        );
    }

    public static <T> ApiResponse<T> error(String message, String internalCode, HttpStatus status, T data) {
        return new ApiResponse<>(
                status.value(),
                internalCode,
                message,
                data,
                Collections.singletonList(message)
        );
    }
}