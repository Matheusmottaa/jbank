package app.matheus.motta.jbank.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuditInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AuditInterceptor.class);

    @Override
    // Before the data sent to the controller
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        logger.info("pre-handle");
        return true;
    }

    @Override
    // After the controller execution
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.info("post-handle");
    }


    @Override
    // After the complete controller execution
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        logger.info("Audit - Metodo: {}, Url: {}, StatusCode: {}, IpAddress: {}",
                request.getMethod(), request.getRequestURI(), response.getStatus(), request
                        .getAttribute("x-user-ip"));
    }
}
