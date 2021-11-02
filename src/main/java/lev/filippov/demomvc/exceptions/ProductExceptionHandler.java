package lev.filippov.demomvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ProductExceptionHandler {

        @ExceptionHandler
        public ResponseEntity<ErrorResponce> handleError(RuntimeException ex){
            ErrorResponce e = new ErrorResponce();
            e.setDate(new Date(System.currentTimeMillis()));
            e.setMessage(ex.getMessage());
            e.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
        }

    static class ErrorResponce {
            HttpStatus status;
            String message;
            Date date;

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
}
