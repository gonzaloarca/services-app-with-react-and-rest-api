package ar.edu.itba.paw.services.simple;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.interfaces.dao.JobCardDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.JobCard;
import ar.edu.itba.paw.models.JobPost;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ar.edu.itba.paw.interfaces.HirenetUtils.SEARCH_WITHOUT_CATEGORIES;

@Transactional
@Service
public class SimpleJobCardService implements JobCardService {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobCardDao jobCardDao;

    @Autowired
    private LevenshteinDistance levenshteinDistance;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<JobCard> findAll() {
        return jobCardDao.findAll(HirenetUtils.ALL_PAGES);
    }

    @Override
    public List<JobCard> findAll(int page) {
        return jobCardDao.findAll(page);
    }

    @Override
    public List<JobCard> findByUserId(long id) {
        return jobCardDao.findByUserId(id, HirenetUtils.ALL_PAGES);
    }

    @Override
    public List<JobCard> findByUserId(long id, int page) {
        return jobCardDao.findByUserId(id, page);
    }

    @Override
    public List<JobCard> search(String title, int zone, int jobType, int page) {
        List<JobPost.JobType> similarTypes = getSimilarTypes(title);
        JobPost.Zone parsedZone = JobPost.Zone.values()[zone];
        if (jobType == SEARCH_WITHOUT_CATEGORIES)
            return jobCardDao.search(title, parsedZone, similarTypes, page);
        else
            return jobCardDao.searchWithCategory(title, parsedZone, JobPost.JobType.values()[jobType], similarTypes, page);
    }

    @Override
    public JobCard findByPostId(long id) {
        return jobCardDao.findByPostId(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public JobCard findByPostIdWithInactive(long id) {
        return jobCardDao.findByPostIdWithInactive(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<JobCard> findRelatedJobCards(long professional_id, int page) {
        return jobCardDao.findRelatedJobCards(professional_id, page);
    }

    @Override
    public int findSizeByUserId(long id) {
        return jobPostService.findSizeByUserId(id);
    }

    @Override
    public int findMaxPage() {
        return jobCardDao.findAllMaxPage();
    }

    @Override
    public int findMaxPageByUserId(long id) {
        return jobCardDao.findMaxPageByUserId(id);
    }

    @Override
    public int findMaxPageSearch(String query, JobPost.Zone value) {
        return jobCardDao.findMaxPageSearch(query, value);
    }

    @Override
    public int findMaxPageSearchWithCategory(String query, JobPost.Zone value, JobPost.JobType jobType) {
        return jobCardDao.findMaxPageSearchWithCategory(query, value, jobType);
    }

    @Override
    public List<JobPost.JobType> getSimilarTypes(String query) {
        final double THRESHOLD = 0.5;
        List<JobPost.JobType> types = new ArrayList<>();

        Arrays.stream(JobPost.JobType.values()).forEach(jobType -> {
            String typeName = messageSource.getMessage(jobType.getStringCode(), null, LocaleContextHolder.getLocale());
            int distance = levenshteinDistance.apply(query.toLowerCase(), typeName.toLowerCase());
            double similarity = 1.0 - ((double) distance / Math.max(query.length(), typeName.length()));

            if (similarity > THRESHOLD)
                types.add(jobType);
        });

        return types;
    }

    @Override
    public int findMaxPageRelatedJobCards(long professional_id) {
        return jobCardDao.findMaxPageRelatedJobCards(professional_id);
    }

}
