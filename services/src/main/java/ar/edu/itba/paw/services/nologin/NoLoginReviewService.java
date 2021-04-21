package ar.edu.itba.paw.services.nologin;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.services.ReviewService;
import ar.edu.itba.paw.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoLoginReviewService implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public Review create(long contractId, int rate, String title, String description) {
        return reviewDao.create(contractId, rate, title, description);
    }

    @Override
    public List<Review> findProfessionalReviews(long id) {
        return reviewDao.findProfessionalReviews(id, HirenetUtils.ALL_PAGES);
    }

    public List<Review> findProfessionalReviews(long id,int page) {
        return reviewDao.findProfessionalReviews(id,page);
    }


    @Override
    public Double findProfessionalAvgRate(long id) {
        return reviewDao.findProfessionalAvgRate(id);
    }

    @Override
    public List<Integer> findProfessionalReviewsByPoints(long id) {
        List<Integer> answer = new ArrayList<>();
        while (answer.size() < 5)
            answer.add(0);
        findProfessionalReviews(id).forEach((review) -> {
            answer.set(review.getRate() - 1, answer.get(review.getRate() - 1) + 1);
        });

        return answer;
    }

    @Override
    public int findJobPostReviewsSize(long id) {
        return reviewDao.findJobPostReviewsSize(id);
    }

    @Override
    public Optional<Review> findContractReview(long id) {
        return reviewDao.findReviewByContractId(id);
    }

    @Override
    public int findMaxPageReviewsByUserId(long id) {
        return reviewDao.findMaxPageReviewsByUserId(id);
    }

    @Override
    public int findProfessionalReviewsSize(long id) {
        return reviewDao.findProfessionalReviewsSize(id);
    }


}
