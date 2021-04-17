package ar.edu.itba.paw.services.nologin;

import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.services.JobPostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoLoginJobPostService implements JobPostService {

    @Autowired
    private JobPostDao jobPostDao;

    @Autowired
    private UserService userService;

    @Override
    public JobPost create(String email,String title, String availableHours, int jobType, int[] zones) {

        User user = userService.findByEmail(email).orElseThrow(RuntimeException::new);
        userService.assignRole(user.getId(),UserAuth.Role.PROFESSIONAL.ordinal());
        List<JobPost.Zone> parsedZones = Arrays.stream(zones).mapToObj(zone -> JobPost.Zone.values()[zone]).collect(Collectors.toList());
        JobPost.JobType parsedJobType = JobPost.JobType.values()[jobType];
        return jobPostDao.create(user.getId(), title, availableHours, parsedJobType, parsedZones);
    }

    @Override
    public Optional<JobPost> findById(long id) {
        return jobPostDao.findById(id);
    }

    @Override
    public Optional<List<JobPost>> findByUserId(long id) {
        return jobPostDao.findByUserId(id);
    }

    @Override
    public Optional<List<JobPost>> findByJobType(JobPost.JobType jobType) {
        return jobPostDao.findByJobType(jobType);
    }

    @Override
    public Optional<List<JobPost>> findByZone(JobPost.Zone zone) {
        return jobPostDao.findByZone(zone);
    }

    @Override
    public Optional<List<JobPost>> findAll() {
        return jobPostDao.findAll();
    }

    @Override
    public Optional<List<JobPost>> search(String title, JobPost.Zone zone, JobPost.JobType jobType) {
        if (jobType == null)
            return jobPostDao.search(title, zone);
        else return jobPostDao.searchWithCategory(title, zone, jobType);
    }
}
