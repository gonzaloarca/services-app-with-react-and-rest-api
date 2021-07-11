package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.JobCard;
import ar.edu.itba.paw.models.JobPost;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class JobCardDto {
    private JobPostDto jobPost;
    private String title;
    private JobTypeDto jobType;
    private List<JobPostZoneDto> zones;
    private Integer reviewsCount;
    private Double avgRate;
    private Integer contractsCompleted;
    private Double price;
    private JobPackageRateTypeDto rateType;
    private URI imageUrl;

    public static JobCardDto fromJobCard(JobCard jobCard, UriInfo uriInfo){
        JobCardDto jobCardDto = new JobCardDto();
        jobCardDto.jobPost = JobPostDto.linkDataFromJobPost(jobCard.getJobPost(),uriInfo);
        jobCardDto.title = jobCard.getJobPost().getTitle();
        jobCardDto.jobType = JobTypeDto.fromJobType(jobCard.getJobPost().getJobType());
        jobCardDto.zones = jobCard.getJobPost().getZones().stream().map(JobPostZoneDto::fromJobPostZone).collect(Collectors.toList());
        jobCardDto.reviewsCount = jobCard.getReviewsCount();
        jobCardDto.avgRate = jobCard.getRating();
        jobCardDto.contractsCompleted = jobCard.getContractsCompleted();
        jobCardDto.price = jobCard.getPrice();
        jobCardDto.rateType = JobPackageRateTypeDto.fromJobPackageRateType(jobCard.getRateType());
        jobCardDto.imageUrl =  uriInfo.getBaseUriBuilder().path("/job-posts")
                .path(String.valueOf(jobCardDto.jobPost.getId()))
                .path("/images")
                .path(String.valueOf(jobCard.getPostImageId()))
                .build();
        return jobCardDto;
    }

    public JobPostDto getJobPost() {
        return jobPost;
    }

    public void setJobPost(JobPostDto jobPost) {
        this.jobPost = jobPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JobTypeDto getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeDto jobType) {
        this.jobType = jobType;
    }

    public List<JobPostZoneDto> getZones() {
        return zones;
    }

    public void setZones(List<JobPostZoneDto> zones) {
        this.zones = zones;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Double getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Double avgRate) {
        this.avgRate = avgRate;
    }

    public Integer getContractsCompleted() {
        return contractsCompleted;
    }

    public void setContractsCompleted(Integer contractsCompleted) {
        this.contractsCompleted = contractsCompleted;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public JobPackageRateTypeDto getRateType() {
        return rateType;
    }

    public void setRateType(JobPackageRateTypeDto rateType) {
        this.rateType = rateType;
    }

    public URI getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }
}
