package simple;

import ar.edu.itba.paw.interfaces.HirenetUtils;
import ar.edu.itba.paw.interfaces.services.JobCardService;
import ar.edu.itba.paw.models.JobPost;
import ar.edu.itba.paw.services.utils.SimplePaginationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimplePaginationServiceTest {

    private static final JobPost.Zone ZONE = JobPost.Zone.BELGRANO;
    private static final String QUERY = "queryqueryquery";

    @InjectMocks
    private final SimplePaginationService simplePaginationService = new SimplePaginationService();

    @Mock
    private JobCardService jobCardService;

    @Test
    public void testFindMaxPageJobPostsSearchWithoutCategory() {
        SimplePaginationService spy = Mockito.spy(simplePaginationService);

        spy.findMaxPageJobPostsSearch(QUERY, ZONE.ordinal(), HirenetUtils.SEARCH_WITHOUT_CATEGORIES);

        Mockito.verify(jobCardService).findMaxPageSearch(Mockito.eq(QUERY), Mockito.eq(ZONE));
    }

    @Test
    public void testFindMaxPageJobPostsSearchWithCategory() {
        SimplePaginationService spy = Mockito.spy(simplePaginationService);

        spy.findMaxPageJobPostsSearch(QUERY, ZONE.ordinal(), JobPost.JobType.BABYSITTING.ordinal());

        Mockito.verify(jobCardService).findMaxPageSearchWithCategory(Mockito.eq(QUERY), Mockito.eq(ZONE),
                Mockito.eq(JobPost.JobType.BABYSITTING));
    }
}
