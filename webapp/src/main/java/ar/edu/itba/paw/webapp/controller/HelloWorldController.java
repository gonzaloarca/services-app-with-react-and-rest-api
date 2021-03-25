package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Supplier;

@Controller
public class HelloWorldController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView helloWorld(@RequestParam("userId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", userService.findById(id).orElseThrow(UserNotFoundException::new));
        //mav.addObject("password",userService.list().get(2).getPassword());
        return mav;
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView getUser(@PathVariable("userId") final long id) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", userService.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(path = { "/create" })//, method = RequestMethod.POST)
    public ModelAndView registerUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        final User user = userService.register(username, password);
        return new ModelAndView("redirect:/user/" + user.getId());
    }

}
