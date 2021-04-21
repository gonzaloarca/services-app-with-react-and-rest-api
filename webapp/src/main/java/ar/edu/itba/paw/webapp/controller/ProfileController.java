package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserAuth;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/profile/{id}")
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    JobCardService jobCardService;

    @Autowired
    JobContractService jobContractService;

    @Autowired
    JobPostService jobPostService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    PaginationService paginationService;

    @ModelAttribute("user")
    private User getUser(@PathVariable("id") final long id) {
        return userService.getUserByRoleAndId(1, id);
    }

    @ModelAttribute("avgRate")
    private Double getAvgRate(@PathVariable("id") final long id) {
        return reviewService.findProfessionalAvgRate(id);
    }

    @ModelAttribute("totalContractsCompleted")
    private int getTotalContractsCompleted(@PathVariable("id") final long id) {
        return jobContractService.findContractsQuantityByProId(id);
    }

    @RequestMapping(value = "/services")
    public ModelAndView profileWithServices(@PathVariable("id") final long id,@RequestParam(value="page",required = false,defaultValue = "1") final int page, @ModelAttribute("user") User user) {
        if(page < 1)
            throw new IllegalArgumentException();
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("withServices", true);
        mav.addObject("reviews",reviewService.findProfessionalReviews(id));
        int maxPage = paginationService.findMaxPagesJobPostByUserId(id);
        mav.addObject("currentPages",paginationService.findCurrentPages(page,maxPage));
        mav.addObject("maxPage",maxPage);
        //TODO: preguntar si esta bien
        mav.addObject("jobCards", jobCardService.findByUserIdWithReview(id,page-1));
        UserAuth auth = userService.getAuthInfo(user.getEmail()).orElseThrow(UserNotFoundException::new);
        mav.addObject("isPro", auth.getRoles().contains(UserAuth.Role.PROFESSIONAL));
        return mav;
    }

    @RequestMapping(value = "/reviews")
    public ModelAndView profileWithReviews(@PathVariable("id") final long id, @RequestParam(value="page",required = false,defaultValue = "1") final int page, @ModelAttribute("user") User user) {
        if(page < 1)
            throw new IllegalArgumentException();
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("reviews",reviewService.findProfessionalReviews(id,page-1));
        mav.addObject("withServices", false);
        mav.addObject("reviewsByPoints", reviewService.findProfessionalReviewsByPoints(id));
        UserAuth auth = userService.getAuthInfo(user.getEmail()).orElseThrow(UserNotFoundException::new);
        mav.addObject("isPro", auth.getRoles().contains(UserAuth.Role.PROFESSIONAL));
        return mav;
    }
}
