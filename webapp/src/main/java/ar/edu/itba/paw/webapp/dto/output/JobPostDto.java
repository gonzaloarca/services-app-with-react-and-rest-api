package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.JobPost;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class JobPostDto {
    private Long id;
    private LinkDto professional;
    private String title;
    private String availableHours;
    private JobTypeDto jobType;
    private Boolean isActive;
    private List<JobPostZoneDto> zones;
    private LocalDateTime creationDate;
    private URI packages;
    private List<LinkDto> images;
    private URI reviews;

    public static JobPostDto fromJobPostWithLocalizedMessage(JobPost jobPost, List<Long> imagesId, UriInfo uriInfo, String message) {
        final JobPostDto jobPostDto = new JobPostDto();
        jobPostDto.id = jobPost.getId();
        long professionalId = jobPost.getUser().getId();
        jobPostDto.professional = LinkDto.fromUriAndId(uriInfo.getBaseUriBuilder().path("/users")
                .path(String.valueOf(professionalId)).build(), professionalId);
        jobPostDto.title = jobPost.getTitle();
        jobPostDto.availableHours = jobPost.getAvailableHours();
        jobPostDto.jobType = JobTypeDto.fromJobTypeWithLocalizedMessage(jobPost.getJobType(), message);
        jobPostDto.isActive = jobPost.isActive();
        jobPostDto.zones = jobPost.getZones().stream().map(JobPostZoneDto::fromJobPostZone).collect(Collectors.toList());
        jobPostDto.creationDate = jobPost.getCreationDate();
        jobPostDto.packages = uriInfo.getBaseUriBuilder().path("/job-posts/")
                .path(String.valueOf(jobPost.getId())).path("/packages").build();
        jobPostDto.images = imagesId.stream().map(imageId -> LinkDto.fromUriAndId(
                uriInfo.getBaseUriBuilder().path("/job-posts")
                        .path(String.valueOf(jobPostDto.id)).path("/images").path(String.valueOf(imageId)).build(), imageId))
                .collect(Collectors.toList());
        jobPostDto.reviews = uriInfo.getAbsolutePathBuilder().path("/reviews?postId=" + jobPostDto.id).build();
        return jobPostDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkDto getProfessional() {
        return professional;
    }

    public void setProfessional(LinkDto professional) {
        this.professional = professional;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(String availableHours) {
        this.availableHours = availableHours;
    }

    public JobTypeDto getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeDto jobType) {
        this.jobType = jobType;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    public List<JobPostZoneDto> getZones() {
        return zones;
    }

    public void setZones(List<JobPostZoneDto> zones) {
        this.zones = zones;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public URI getPackages() {
        return packages;
    }

    public void setPackages(URI packages) {
        this.packages = packages;
    }

    public List<LinkDto> getImages() {
        return images;
    }

    public void setImages(List<LinkDto> images) {
        this.images = images;
    }

    public URI getReviews() {
        return reviews;
    }

    public void setReviews(URI reviews) {
        this.reviews = reviews;
    }
}
