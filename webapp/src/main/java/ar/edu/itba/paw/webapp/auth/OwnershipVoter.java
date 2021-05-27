package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.JobContractService;
import ar.edu.itba.paw.interfaces.services.JobPackageService;
import ar.edu.itba.paw.interfaces.services.JobPostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import exceptions.JobContractNotFoundException;
import exceptions.JobPackageNotFoundException;
import exceptions.JobPostNotFoundException;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class OwnershipVoter implements AccessDecisionVoter {

    @Autowired
    private JobContractService jobContractService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobPackageService jobPackageService;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }


    @Override
    public int vote(Authentication authentication, Object o, Collection collection) {
        if (o instanceof FilterInvocation) {
            FilterInvocation filterInvocation = (FilterInvocation) o;
            String url = filterInvocation.getRequestUrl();

            if (url.equals("/"))
                return ACCESS_ABSTAIN;

            String[] paths = url.substring(1).split("/");
            int id;
            boolean isOwner;
            switch (paths[0]) {
                case "rate-contract":
                    if (paths.length == 1)
                        return ACCESS_ABSTAIN;
                    try {
                        id = Integer.parseInt(paths[1]);
                    } catch (NumberFormatException e) {
                        return ACCESS_ABSTAIN;
                    }
                    try {
                        isOwner = jobContractService.findById(id).getClient().getEmail().equals(authentication.getName());
                    } catch (NoSuchElementException e) {
                        return ACCESS_ABSTAIN;
                    }
                    if (isOwner)
                        return ACCESS_GRANTED;
                    else
                        return ACCESS_DENIED;
                case "job":
                    if (paths.length == 1)
                        return ACCESS_ABSTAIN;
                    try {
                        id = Integer.parseInt(paths[1]);
                    } catch (NumberFormatException e) {
                        return ACCESS_ABSTAIN;
                    }
                    try {
                        isOwner = jobPostService.findByIdWithInactive(id).getUser().getEmail().equals(authentication.getName());
                    } catch (NoSuchElementException e) {
                        return ACCESS_ABSTAIN;
                    }
                    if (paths.length > 2) {
                        if (paths[2].equals("edit") || paths[2].equals("packages") || paths[2].equals("delete")) {
                            if (isOwner)
                                return ACCESS_GRANTED;
                            else
                                return ACCESS_DENIED;
                        }
                    }
                    break;
                case "create-job-post":
                    if (paths.length == 1)
                        return ACCESS_ABSTAIN;
                    if (paths[1].matches("success\\?.*")) {
                        List<String> params = Arrays.asList(paths[1].split("[?|&]"));
                        if (params.stream().anyMatch(param -> param.matches("postId=[0-9]+"))) {
                            String postParam = null;
                            for (String param : params) {
                                if (param.matches("postId=[0-9]+"))
                                    postParam = param;
                            }
                            if (postParam != null) {
                                String postId = postParam.split("=")[1];
                                try {
                                    id = Integer.parseInt(postId);
                                } catch (NumberFormatException e) {
                                    return ACCESS_ABSTAIN;
                                }
                                try {
                                    isOwner = jobPostService.findById(id).getUser().getEmail().equals(authentication.getName());
                                } catch (NoSuchElementException e) {
                                    return ACCESS_ABSTAIN;
                                }
                                if (isOwner)
                                    return ACCESS_GRANTED;
                                else
                                    return ACCESS_DENIED;
                            }
                        }
                    }
                    break;
                case "contract":
                    if (paths.length <= 2)
                        return ACCESS_ABSTAIN;
                    if (paths[1].equals("package")) {
                        try {
                            id = Integer.parseInt(paths[2]);
                        } catch (NumberFormatException e) {
                            return ACCESS_ABSTAIN;
                        }
                        try {
                            long postId = jobPackageService.findByIdWithJobPost(id).getJobPost().getId();
                            isOwner = jobPostService.findById(postId).getUser().getEmail().equals(authentication.getName());
                        } catch (NoSuchElementException e) {
                            return ACCESS_ABSTAIN;
                        }
                        if (isOwner) {
                            return ACCESS_DENIED;
                        } else {
                            return ACCESS_GRANTED;
                        }
                    }
                    break;
                case "profile":
                    if (paths.length <= 3)
                        return ACCESS_ABSTAIN;
                    try {
                        id = Integer.parseInt(paths[1]);
                    } catch (NumberFormatException e) {
                        return ACCESS_ABSTAIN;
                    }
                    try {
                        isOwner = userService.findById(id).getEmail().equals(authentication.getName());
                    } catch (NoSuchElementException e) {
                        return ACCESS_ABSTAIN;
                    }
                    if (paths[2].equals("services") && paths[3].equals("delete")) {
                        if (isOwner)
                            return ACCESS_GRANTED;
                        else
                            return ACCESS_DENIED;
                    }
                    break;
            }
        }
        return ACCESS_ABSTAIN;
    }

    @Override
    public boolean supports(Class aClass) {
        return true;
    }
}
