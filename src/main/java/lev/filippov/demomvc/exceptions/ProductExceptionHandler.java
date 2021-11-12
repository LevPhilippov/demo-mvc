package lev.filippov.demomvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

        //экспериментально
//        @ExceptionHandler(BindException.class)
//        public ResponseEntity<ErrorResponce> handleError(BindException ex){
//            List<ObjectError> allErrors = ex.getAllErrors();
//            for (ObjectError e : allErrors) {
//                System.out.println(e.getObjectName() + " " + Arrays.toString(e.getArguments()) + " " + e.getDefaultMessage() + "\n");
//            }
//
//            ErrorResponce e = new ErrorResponce();
//            e.setDate(new Date(System.currentTimeMillis()));
//            e.setMessage(ex.getMessage());
//            e.setStatus(HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
//        }


}
