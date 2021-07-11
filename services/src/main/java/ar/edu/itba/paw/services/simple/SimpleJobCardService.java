package ar.edu.itba.paw.services.simple;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.interfaces.dao.JobCardDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.JobCard;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.models.exceptions.JobPackageNotFoundException;
import ar.edu.itba.paw.models.exceptions.JobPostNotFoundException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    public List<JobCard> search(String query, int zone, int jobType, int order, int page, Locale locale) {
        if (query == null || zone < 0 || zone > JobPost.Zone.values().length || jobType < SEARCH_WITHOUT_CATEGORIES ||
                jobType > JobPost.JobType.values().length || order < 0
                || order > JobCard.OrderBy.values().length || page < 0) {
            return new ArrayList<>();
        }
        List<JobPost.JobType> similarTypes = getSimilarTypes(query, locale);
        JobPost.Zone parsedZone = JobPost.Zone.values()[zone];
        JobCard.OrderBy orderBy = JobCard.OrderBy.values()[order];
        if (jobType == SEARCH_WITHOUT_CATEGORIES)
            return jobCardDao.search(query, parsedZone, similarTypes, orderBy, page);
        else
            return jobCardDao.searchWithCategory(query, parsedZone, JobPost.JobType.values()[jobType], similarTypes, orderBy, page);
    }

    @Override
    public JobCard findByPostId(long id) {
        return jobCardDao.findByPostId(id).orElseThrow(JobPostNotFoundException::new);
    }

    @Override
    public JobCard findByPackageIdWithPackageInfoWithInactive(long id) {
        return jobCardDao.findByPackageIdWithPackageInfoWithInactive(id).orElseThrow(JobPackageNotFoundException::new);
    }

    @Override
    public JobCard findByPostIdWithInactive(long id) {
        return jobCardDao.findByPostIdWithInactive(id).orElseThrow(JobPostNotFoundException::new);
    }

    @Override
    public List<JobCard> findRelatedJobCards(long professional_id, int page) {
        return jobCardDao.findRelatedJobCards(professional_id, page);
    }

    @Override
    public int findByUserIdSize(long id) {
        return jobPostService.findSizeByUserId(id);
    }

    @Override
    public int findAllMaxPage() {
        return jobCardDao.findAllMaxPage();
    }

    @Override
    public int findByUserIdMaxPage(long id) {
        return jobCardDao.findByUserIdMaxPage(id);
    }

    @Override
    public int searchMaxPage(String query, JobPost.Zone value, Locale locale) {
        return jobCardDao.searchMaxPage(query, value, getSimilarTypes(query, locale));
    }

    @Override
    public int searchWithCategoryMaxPage(String query, JobPost.Zone value, JobPost.JobType jobType, Locale locale) {
        return jobCardDao.searchWithCategoryMaxPage(query, value, jobType, getSimilarTypes(query, locale));
    }

    @Override
    public List<JobPost.JobType> getSimilarTypes(String query, Locale locale) {
        final double THRESHOLD = 0.5;
        List<JobPost.JobType> types = new ArrayList<>();

        Arrays.stream(JobPost.JobType.values()).forEach(jobType -> {
            String typeName = messageSource.getMessage(jobType.getStringCode(), null, locale);
            //FIXME: levenshteinDistance es null?
            int distance = levenshteinDistance.apply(query.toLowerCase(), typeName.toLowerCase());
            double similarity = 1.0 - ((double) distance / Math.max(query.length(), typeName.length()));

            if (similarity > THRESHOLD)
                types.add(jobType);
        });

        return types;
    }

    @Override
    public int findRelatedJobCardsMaxPage(long professional_id) {
        return jobCardDao.findRelatedJobCardsMaxPage(professional_id);
    }

}
