package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.JobCard;
import ar.edu.itba.paw.models.JobPost;

import java.util.List;
import java.util.Optional;

public interface JobCardDao {

    List<JobCard> findAll(int page);

    List<JobCard> findByUserId(long id, int page);

    List<JobCard> search(String query, JobPost.Zone zone, int page);

    List<JobCard> searchWithCategory(String query, JobPost.Zone zone, JobPost.JobType jobType, int page);

    List<JobCard> findByUserIdWithReview(long id, int page);

    Optional<JobCard> findByPostId(long id);

    List<JobCard> findRelatedJobPosts(long professional_id, int page);

    int findAllMaxPage();

    int findMaxPageByUserId(long id);

    int findMaxPageSearch(String query, JobPost.Zone zone);

    int findMaxPageSearchWithCategory(String query, JobPost.Zone zone, JobPost.JobType jobType);

    int findMaxPageRelatedJobPosts(long professional_id);
}
