package app.matheus.motta.jbank.exception;

import app.matheus.motta.jbank.exception.dto.InvalidParamDto;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JBankException.class)
    public ResponseEntity<ProblemDetail>  handleJBankException(JBankException e) {
        var detail = e.toProblemDetail();
        return ResponseEntity.status(detail.getStatus()).body(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var invalidParams = e.getFieldErrors().stream()
                                .map(f -> new InvalidParamDto(f.getField(), f.getDefaultMessage()))
                                .toList();

        var pd = ProblemDetail.forStatus(400);
        pd.setTitle("Invalid request parameters");
        pd.setDetail("There is invalid fields on the request");
        pd.setProperty("invalid-params", invalidParams);

        return ResponseEntity.badRequest().body(pd);
    }

}
