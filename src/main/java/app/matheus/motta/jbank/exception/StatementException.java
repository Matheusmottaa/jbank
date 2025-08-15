package app.matheus.motta.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class StatementException extends  JBankException{

    private final String detail;

    public StatementException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Invalid statement scenario");
        pd.setDetail(detail);

        return pd;
    }
}
