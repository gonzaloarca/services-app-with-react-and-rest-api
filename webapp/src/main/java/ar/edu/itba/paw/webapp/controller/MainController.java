package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.JobCardService;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    private JobCardService jobCardService;

    @RequestMapping(value = "/profile/services")
    public ModelAndView profileWithServices(){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("withServices", true);
        return mav;
    }

    @RequestMapping(value = "/profile/reviews")
    public ModelAndView profileWithReviews(){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("withServices", false);
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(@ModelAttribute("searchForm") SearchForm form) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("jobCards", jobCardService.findAll());
        mav.addObject("zones", JobPost.Zone.values());
        return mav;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView search(@Valid @ModelAttribute("searchForm") SearchForm form, final BindingResult errors) {
        final ModelAndView mav = new ModelAndView("search");
        mav.addObject("zones", JobPost.Zone.values());
        mav.addObject("categories", JobPost.JobType.values());
        if (errors.hasErrors())
            return home(form);
        mav.addObject("query", form.getQuery());
        mav.addObject("pickedZone", JobPost.Zone.values()[Integer.parseInt(form.getZone())]);
        mav.addObject("pickedCategory", form.getCategory());
        mav.addObject("jobCards", jobCardService.search(form.getQuery(),
                JobPost.Zone.values()[Integer.parseInt(form.getZone())],
                (form.getCategory() == -1) ? null : JobPost.JobType.values()[form.getCategory()])
        );
        return mav;
    }
}
