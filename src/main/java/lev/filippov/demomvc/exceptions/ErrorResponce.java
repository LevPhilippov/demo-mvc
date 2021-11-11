package lev.filippov.demomvc.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponce {
    HttpStatus status;
    String message;
    Date date;
    Object object;

    public ErrorResponce(HttpStatus status, String message, Date date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public ErrorResponce() {
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
