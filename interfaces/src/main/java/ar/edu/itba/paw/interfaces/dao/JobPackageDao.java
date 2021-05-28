package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.JobPackage;
import ar.edu.itba.paw.models.JobPost;

import java.util.List;
import java.util.Optional;

public interface JobPackageDao {

    JobPackage create(long postId, String title, String description, Double price, JobPackage.RateType rateType);

    Optional<JobPackage> findById(long id);

    List<JobPackage> findByPostId(long id, int page);

    Optional<JobPost> findPostByPackageId(long id);

    boolean deletePackage(long id);

    boolean updatePackage(long id, String title, String description, Double price, JobPackage.RateType rateType);
}
