package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;
import java.util.Optional;

@ControllerAdvice
public class ExceptionController {

    private final Logger exceptionLogger = LoggerFactory.getLogger(ExceptionController.class);

    @Autowired
    private UserService userService;

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler({UserNotFoundException.class,
            JobPostNotFoundException.class, JobPackageNotFoundException.class,
            ReviewNotFoundException.class, JobContractNotFoundException.class, NoSuchElementException.class,
            AccessDeniedException.class,
    })
    public ModelAndView notFoundError(RuntimeException e) {
        exceptionLogger.debug("Not found exception handled: {}", e.getMessage());
        return logUser(new ModelAndView("error/404"));
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalStateException.class})
    public ModelAndView conflictError(RuntimeException e) {
        exceptionLogger.debug("Not found exception handled: {}", e.getMessage());
        return logUser(new ModelAndView("error/404"));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception e) {
        exceptionLogger.debug("Exception handled: {}", e.getMessage());
        return logUser(new ModelAndView("error/default"));
    }

    private ModelAndView logUser(ModelAndView mav) {
        Optional<User> maybeUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (maybeUser.isPresent()) {
            User currentUser = maybeUser.get();
            mav.addObject("currentUser", currentUser);
            exceptionLogger.debug("Current user is: {}", currentUser);
        }
        return mav;
    }

}
