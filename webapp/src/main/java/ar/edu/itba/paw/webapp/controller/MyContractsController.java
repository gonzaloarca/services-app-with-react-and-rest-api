package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.ChangeContractStateForm;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/my-contracts")
@Controller
public class MyContractsController {

    private final Logger myContractsControllerLogger = LoggerFactory.getLogger(MyContractsController.class);

    @Autowired
    private JobContractService jobContractService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private JobPackageService jobPackageService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(path = "/{contractType}/{contractState}", method = RequestMethod.GET)
    public ModelAndView getMyContracts(Principal principal,
                                       @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
                                       @PathVariable final String contractType, @PathVariable final String contractState,
                                       @ModelAttribute("changeContractStateForm") ChangeContractStateForm form) {
        if (page < 1)
            throw new IllegalArgumentException();

        long id = userService.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new).getId();
        int maxPage;
        List<JobContractCard> jobContractCards;

        List<JobContract.ContractState> states = jobContractService.getContractStates(contractState);

        if (contractType.equals("professional")) {
            maxPage = paginationService.findMaxPageContractsByProId(id, states);
            jobContractCards = jobContractService
                    .findJobContractCardsByProId(id, states, page - 1);
        } else if (contractType.equals("client")) {
            maxPage = paginationService.findMaxPageContractsByClientId(id, states);
            jobContractCards = jobContractService
                    .findJobContractCardsByClientId(id, states, page - 1);
        } else
            throw new IllegalArgumentException();

        if (!contractState.equals("active") && !contractState.equals("pending") && !contractState.equals("finalized"))
            throw new IllegalArgumentException();

        UserAuth userAuth = userService.getAuthInfo(principal.getName()).orElseThrow(UserNotFoundException::new);

        myContractsControllerLogger.debug("Finding contract cards for professional {}", id);

        return new ModelAndView("myContracts")
                .addObject("currentPages", paginationService.findCurrentPages(page, maxPage))
                .addObject("maxPage", maxPage)
                .addObject("contractCards", jobContractCards)
                .addObject("isPro", userAuth.getRoles().contains(UserAuth.Role.PROFESSIONAL))
                .addObject("contractType", contractType)
                .addObject("contractStateEndpoint", contractState);
    }

    @RequestMapping(path = "/{id}/update", method = RequestMethod.POST)
    public ModelAndView updateContractState(@ModelAttribute("changeContractStateForm") ChangeContractStateForm form, HttpServletRequest servletRequest, @PathVariable("id") long id) {
        myContractsControllerLogger.debug("Updating state in contract {} to {}", id, form.getNewState());

        jobContractService.changeContractState(id, JobContract.ContractState.values()[form.getNewState()]);

        JobContractWithImage jobContract = jobContractService.findJobContractWithImage(id);
        JobPackage jobPackage = jobPackageService.findById(jobContract.getJobPackage().getId());
        JobPost jobPost = jobPostService.findById(jobPackage.getPostId());

        //TODO: Indicar a quien se manda? (ojo que depende del estado, ver el metodo de email para entenderlo)
        myContractsControllerLogger.debug("Sending email updating contract state for package {}, post {} and contract {}", jobPackage.getId(), jobPost.getId(), jobContract.getId());
        mailingService.sendUpdateContractStatusEmail(jobContract, jobPackage, jobPost, localeResolver.resolveLocale(servletRequest));

        return new ModelAndView("redirect:" + form.getReturnURL());
    }

}
