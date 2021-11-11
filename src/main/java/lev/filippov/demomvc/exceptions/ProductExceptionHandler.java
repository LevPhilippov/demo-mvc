package lev.filippov.demomvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@ControllerAdvice
public class ProductExceptionHandler {

        @ExceptionHandler
        public ResponseEntity<ErrorResponce> handleError(ServerException ex){
            ErrorResponce e = new ErrorResponce();
            e.setDate(new Date(System.currentTimeMillis()));
            e.setMessage(ex.getMessage());
            e.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
        }


}
