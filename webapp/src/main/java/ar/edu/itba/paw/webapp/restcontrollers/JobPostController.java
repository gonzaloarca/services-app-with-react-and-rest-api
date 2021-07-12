package ar.edu.itba.paw.webapp.restcontrollers;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.UpdateFailException;
import ar.edu.itba.paw.webapp.dto.JobContractDto;
import ar.edu.itba.paw.webapp.dto.JobPackageDto;
import ar.edu.itba.paw.webapp.dto.JobPostDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.utils.LocaleResolverUtil;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@Path("/job-posts")
public class JobPostController {
    private final Logger jobPostControllerLogger = LoggerFactory.getLogger(JobPostController.class);

    @Autowired
    PaginationService paginationService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JobPackageService jobPackageService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @POST
    @Path("/{postId}/packages")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createJobPackage(final JobPackageDto jobPackageDto, @PathParam("postId") final long postId) {
        String title = jobPackageDto.getTitle();
        String description = jobPackageDto.getDescription();
        String price = jobPackageDto.getPrice().toString();
        long rateType = jobPackageDto.getRateType().getId();
        jobPostControllerLogger.debug("Creating package for post {} with data: title: {}, description: {}, price: {}, rate type:{}", postId, title, description, price, rateType);
        JobPackage jobPackage = jobPackageService.create(postId, title, description, price, rateType);

        final URI packageUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(jobPackage.getId())).build();
        return Response.created(packageUri).build();
    }

    @PUT
    @Path("/{postId}/packages/{packageId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response editJobPackage(final JobPackageDto jobPackageDto,
                                   @PathParam("postId") final long postId,
                                   @PathParam("packageId") final long packageId) {
        String title = jobPackageDto.getTitle();
        String description = jobPackageDto.getDescription();
        String price = jobPackageDto.getPrice().toString();
        long rateType = jobPackageDto.getRateType().getId();
        boolean isActive = jobPackageDto.isActive();

        jobPostControllerLogger.debug("Updating package {} with data: title: {}, description: {}, price: {}, rate type: {}, isActive: {}",
                packageId, title, description, price, rateType, isActive);
        if (!jobPackageService.updateJobPackage(packageId, title, description, price, rateType, isActive)) {
            jobPostControllerLogger.debug("Error updating package {}", packageId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final URI packageUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(packageId)).build();
        return Response.ok(packageUri).build();
    }

    @GET
    @Path("/{id}/")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response jobPostDetails(@PathParam("id") final long id) {
        jobPostControllerLogger.debug("Finding job post by id: {}", id);
        JobPost jobPost = jobPostService.findByIdWithInactive(id);
        return Response.ok(JobPostDto.fromJobPostWithLocalizedMessage(
                jobPost, uriInfo,
                messageSource.getMessage(jobPost.getJobType().getDescription(), null, LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages()))))
                .build();
    }

    @GET
    @Path("/{id}/reviews")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reviewsByPostId(
            @PathParam("id") final long id,
            @QueryParam(value = "page") @DefaultValue("1") int page) {
        if (page < 1) {
            page = 1;
        }

        jobPostControllerLogger.debug("Finding reviews for post: {}", id);
        int maxPage = paginationService.findReviewsByPostIdMaxPage(id);

        final List<ReviewDto> reviewDtoList = reviewService.findReviewsByPostId(id, page - 1)
                .stream().map(review -> ReviewDto.fromReview(review, uriInfo)).collect(Collectors.toList());

        return PageResponseUtil.getGenericListResponse(page, maxPage, uriInfo,
                Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtoList) {
                }));
    }

    @Path("/{id}/packages")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response packagesByPostId(
            @PathParam("id") final long id,
            @QueryParam(value = "page") @DefaultValue("1") int page) {
        if (page < 1) {
            page = 1;
        }
        jobPostControllerLogger.debug("Finding packages for post: {}", id);
        int maxPage = paginationService.findJobPackageByPostIdMaxPage(id);
        final List<JobPackageDto> packageDtoList = jobPackageService.findByPostId(id, page - 1)
                .stream().map(pack -> JobPackageDto.fromJobPackage(pack, uriInfo))
                .collect(Collectors.toList());

        return PageResponseUtil.getGenericListResponse(page, maxPage, uriInfo,
                Response.ok(new GenericEntity<List<JobPackageDto>>(packageDtoList) {
                }));
    }

    @Path("/{postId}/packages/{packageId}")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response packagesById(
            @PathParam("postId") final long postId,
            @PathParam("packageId") final long packageId) {

        jobPostControllerLogger.debug("Finding packages by Id: {}", packageId);
        final JobPackage jobPackage = jobPackageService.findById(packageId);
        final JobPackageDto packageDto = JobPackageDto.fromJobPackage(jobPackage, uriInfo);

        return Response.ok(new GenericEntity<JobPackageDto>(packageDto) {
        }).build();
    }

}
