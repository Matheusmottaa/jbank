package app.matheus.motta.jbank.exception;

import org.springframework.http.ProblemDetail;

public abstract class JBankException extends RuntimeException{

    public JBankException(String message) {
        super(message);
    }

    public JBankException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(500);
        pd.setTitle("Internal Server Error");
        pd.setDetail("Contact JBank support");
        return pd;
    }
}
