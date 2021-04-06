package ar.edu.itba.paw.services.nologin;

import ar.edu.itba.paw.interfaces.dao.JobPostDao;
import ar.edu.itba.paw.interfaces.services.JobContractService;
import ar.edu.itba.paw.interfaces.services.JobPackageService;
import ar.edu.itba.paw.interfaces.services.JobPostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoLoginJobPostService implements JobPostService {

    @Autowired
    private JobPostDao jobPostDao;

    @Autowired
    private UserService userService;

    @Autowired
    private JobPackageService jobPackageService;

    @Autowired
    private JobContractService jobContractService;

    @Override
    public JobPost create(String email, String username, String phone, String title, String availableHours, JobPost.JobType jobType, List<JobPost.Zone> zones) {

        //Chequeamos si el user esta registrado
        Optional<User> maybeUser = userService.findByEmail(email);
        User user = null;
        //Si existe vemos si es profesional, si no lo es lo hacemos
        if (maybeUser.isPresent()) {
            if (!maybeUser.get().isProfessional()) {
                Optional<User> newRole = userService.switchRole(maybeUser.get().getId());
                user = newRole.orElse(maybeUser.get());
            }
        } else {
            user = userService.register(email, username, phone, true);
        }
        if (user == null)
            //TODO: LANZAR EXCEPCION APROPIADA
            throw new RuntimeException();
        return jobPostDao.create(user.getId(), title, availableHours, jobType, zones);
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
    public Map<JobPost, List<String>> findAllJobCardsWithData() {
        Map<JobPost, List<String>> answer = new HashMap<>();
        Optional<List<JobPost>> jobPosts = findAll();
        if (!jobPosts.isPresent())
            return answer;

        mapPostWithData(answer, jobPosts);

        return answer;
    }



    @Override
    public Optional<List<JobPost>> search(String title, JobPost.Zone zone) {
        return jobPostDao.search(title, zone);
    }

    @Override
    public  Map<JobPost, List<String>> searchWithData(String title, int zoneId){
        JobPost.Zone zone = JobPost.Zone.values()[zoneId];
        Map<JobPost, List<String>> answer = new HashMap<>();
        Optional<List<JobPost>> jobPosts = jobPostDao.search(title,zone);
        if (!jobPosts.isPresent())
            return answer;
        mapPostWithData(answer,jobPosts);
        return answer;
    }

    private void mapPostWithData(Map<JobPost, List<String>> answer, Optional<List<JobPost>> jobPosts) {
        jobPosts.get().forEach(jobPost -> {
            Optional<List<JobPackage>> aux = jobPackageService.findByPostId(jobPost.getId());
            //TODO: SI NO ESTA PRESENTE ARROJAR EXCEPCION?
            aux.ifPresent(jobPackages -> {
                JobPackage jobPackage = jobPackages.stream().min(Comparator.comparingDouble(JobPackage::getPrice)).get();
                //Estoy seguro de que existe almenos un package en cada post
                List<String> strings = new ArrayList<>();
                strings.add(jobPackageService.getPriceFormat(jobPackage.getPrice(), jobPackage.getRateType()));
                strings.add(String.valueOf(
                        jobContractService.findContractsQuantityByProId(jobPost.getUser().getId())));
                answer.put(jobPost, strings);
            });
        });
    }
}
