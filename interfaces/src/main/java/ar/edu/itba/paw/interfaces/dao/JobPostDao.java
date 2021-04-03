package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface JobPostDao {

    JobPost create(long userId, String title, String availableHours, JobPost.JobType jobType, List<JobPost.Zone> zones);

    Optional<JobPost> findById(long id);

    Optional<List<JobPost>> findByUserId(long id);

    Optional<List<JobPost>> findByJobType(JobPost.JobType jobType);

    Optional<List<JobPost>> findByZone(JobPost.Zone zone);

    Optional<List<JobPost>> findAll();
}
