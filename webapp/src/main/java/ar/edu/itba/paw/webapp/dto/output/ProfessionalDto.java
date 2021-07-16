package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;

public class ProfessionalDto {
    private Double reviewAvg;
    private long reviewsQuantity;
    private long contractsCompleted;
    private LinkDto user;

    public static ProfessionalDto fromUserAndRoles(User user,
                                                   Double reviewAvg,
                                                   long reviewsQuantity,
                                                   long contractsCompleted,
                                                   UriInfo uriInfo) {
        ProfessionalDto dto = new ProfessionalDto();
        dto.reviewAvg = reviewAvg;
        dto.reviewsQuantity = reviewsQuantity;
        dto.contractsCompleted = contractsCompleted;
        dto.user = LinkDto.fromUriAndId(uriInfo.getBaseUriBuilder().path("/users")
                .path(String.valueOf(user.getId())).build(), user.getId());
        return dto;
    }

    public Double getReviewAvg() {
        return reviewAvg;
    }

    public void setReviewAvg(Double reviewAvg) {
        this.reviewAvg = reviewAvg;
    }

    public long getReviewsQuantity() {
        return reviewsQuantity;
    }

    public void setReviewsQuantity(long reviewsQuantity) {
        this.reviewsQuantity = reviewsQuantity;
    }

    public long getContractsCompleted() {
        return contractsCompleted;
    }

    public void setContractsCompleted(long contractsCompleted) {
        this.contractsCompleted = contractsCompleted;
    }

    public LinkDto getUser() {
        return user;
    }

    public void setUser(LinkDto user) {
        this.user = user;
    }
}
