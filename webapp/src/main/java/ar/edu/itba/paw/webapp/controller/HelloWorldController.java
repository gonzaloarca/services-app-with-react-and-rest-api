package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserServiceOriginal;
import ar.edu.itba.paw.interfaces.services.JobPackageService;
import ar.edu.itba.paw.interfaces.services.JobPostService;
import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.UserOriginal;
import ar.edu.itba.paw.webapp.exceptions.JobCardsNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.JobPackageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.JobPostNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {
    @Autowired
    private UserServiceOriginal userService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobPackageService jobPackageService;

    @RequestMapping("/")
    public ModelAndView home() {
        //TODO: REFACTOR??
        final ModelAndView mav = new ModelAndView("index");
        List<JobPost> jobPosts = jobPostService.findAll().orElseThrow(JobCardsNotFoundException::new);
        mav.addObject("jobCards", jobPosts);

        List<JobPackage> jobPackages = new ArrayList<>();
        jobPosts.forEach(jobPost -> jobPackages.add(jobPackageService.findByPostId(
                jobPost.getId()).orElseThrow(JobPackageNotFoundException::new).get(0)));
        mav.addObject("packages", jobPackages);
        return mav;
    }

    @RequestMapping("/create-job-post")
    public ModelAndView createJobPost() {
        final ModelAndView mav = new ModelAndView("createJobPost");
        return mav;
    }

    @RequestMapping("/job/{postId}")
    public ModelAndView jobPostDetails(@PathVariable("postId") final long id) {
        final ModelAndView mav = new ModelAndView("jobPostDetails");
        mav.addObject("jobPost", jobPostService.findById(id).orElseThrow(JobPostNotFoundException::new));
        mav.addObject("packages", jobPackageService.findByPostId(id).orElseThrow(JobPostNotFoundException::new));
        return mav;
    }

//    @RequestMapping("/user/{userId}")
//    public ModelAndView getUser(@PathVariable("userId") final long id) {
//        final ModelAndView mav = new ModelAndView("index");
//        UserOriginal aux = userService.findById(id).orElseThrow(UserNotFoundException::new);
//        mav.addObject("greeting", aux.getName());
//        mav.addObject("password", aux.getPassword());
//        return mav;
//    }
//
//    @RequestMapping(path = {"/create"})//, method = RequestMethod.POST)
//    public ModelAndView registerUser(@RequestParam("username") String username, @RequestParam("password") String password) {
//        final UserOriginal user = userService.register(username, password);
//        return new ModelAndView("redirect:/user/" + user.getId());
//    }

}
