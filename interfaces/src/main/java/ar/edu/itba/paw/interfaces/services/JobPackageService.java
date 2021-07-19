package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobCard;
import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;

import java.util.List;

public interface JobPackageService {

    JobPackage create(long postId, String title, String description, String price, long rateType);

    JobPackage findById(long packageId, long postId);

    List<JobPackage> findByPostId(long id);

    List<JobPackage> findByPostId(long id, int page);

    boolean updateJobPackage(long packageId, long postId, String title, String description, String price, Integer rateType, Boolean isActive);

    int findByPostIdMaxPage(long id);

    List<JobPackage> findByPostIdOnlyActive(long postId, int page);

    int findByPostIdOnlyActiveMaxPage(long postId);

    JobPackage findByOnlyId(long packageId);
}
