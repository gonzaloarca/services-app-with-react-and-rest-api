package ar.edu.itba.paw.webapp.restcontrollers;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.dto.input.EditUserDto;
import ar.edu.itba.paw.webapp.dto.input.VerifyEmailDto;
import ar.edu.itba.paw.webapp.dto.output.*;
import ar.edu.itba.paw.webapp.dto.input.NewUserDto;
import ar.edu.itba.paw.webapp.utils.ImageUploadUtil;
import ar.edu.itba.paw.webapp.utils.LocaleResolverUtil;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import ar.edu.itba.paw.webapp.validation.ValidImage;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/users")
public class UserController {
    private final Logger accountControllerLogger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JobContractService jobContractService;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll(@QueryParam("email") final String email,
                            @QueryParam("page") int page) {
        if (email != null) {
            accountControllerLogger.debug("Finding user with email {}", email);
            UserWithImage user = userService.findUserWithImageByEmail(email).orElseThrow(UserNotFoundException::new);
            return Response.ok(getUserDto(user)).build();
        }
        if (page < 1)
            page = 1;

        accountControllerLogger.debug("Finding all users for page {}", page);
        List<UserDto> usersDto = userService.findAllWithImage(page).stream()
                .map(this::getUserDto).collect(Collectors.toList());
        int maxPage = userService.findAllWithImageMaxPage();
        return PageResponseUtil.getGenericListResponse(page, maxPage, uriInfo,
                Response.ok(new GenericEntity<List<UserDto>>(usersDto) {
                }));
    }

    private UserDto getUserDto(UserWithImage userWithImage) {
        accountControllerLogger.debug("Finding auth info for user with email {}", userWithImage.getEmail());
        UserAuth auth = userService.getAuthInfo(userWithImage.getEmail()).orElseThrow(UserNotFoundException::new);

        return UserDto.fromUserAndRoles(userWithImage, auth.getRoles(), uriInfo);
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response register(@Valid final NewUserDto newUserDto) {
        User currentUser;
        try {
            accountControllerLogger.debug("Registering user with data: email: {}, password: {}, name: {}, phone: {}",
                    newUserDto.getEmail(), newUserDto.getPassword(), newUserDto.getUsername(), newUserDto.getPhone());
            currentUser = userService
                    .register(newUserDto.getEmail(), newUserDto.getPassword(), newUserDto.getUsername(), newUserDto.getPhone(),
                            null, LocaleResolverUtil
                                    .resolveLocale(headers.getAcceptableLanguages()), uriInfo.getBaseUri().toString());
        } catch (UserAlreadyExistsException e) {
            accountControllerLogger.error("Register error: email already exists");
            return Response.status(Response.Status.CONFLICT).build();
        }
        final URI userUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(currentUser.getId()))
                .build();
        return Response.created(userUri).build();
    }

    @Path("/{id}")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final long id) {
        accountControllerLogger.debug("Finding user with id {}", id);
        UserWithImage user = userService.findUserWithImage(id);
        accountControllerLogger.debug("Finding auth info for user with email {}", user.getEmail());
        UserAuth auth = userService.getAuthInfo(user.getEmail()).orElseThrow(UserNotFoundException::new);

        UserDto userAnswer = UserDto.fromUserAndRoles(user, auth.getRoles(), uriInfo);
        return Response.ok(userAnswer).build();
    }

    @Path("/{id}/image")
    @GET
    @Produces(value = {"image/png", "image/jpg","image/jpeg", MediaType.APPLICATION_JSON})
    public Response getUserImage(@PathParam("id") final long id) {
        ByteImage byteImage = userService.findImageByUserId(id);
        return Response.ok(new ByteArrayInputStream(byteImage.getData())).build();
    }

    @Path("/{id}/image")
    @PUT
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response uploadUserImage(@Valid @NotNull @ValidImage @FormDataParam("file") final FormDataBodyPart body,
                                    @PathParam("id") long id) {

        long result;
        try {
            result = userService.updateUserImage(id, ImageUploadUtil.fromInputStream(body));
        } catch (IOException e) {
            throw new RuntimeException("Upload failed");
        }
        if (result == -1)
            throw new RuntimeException("Couldn't save image");
        return Response.created(uriInfo.getBaseUriBuilder().path("/users").path(String.valueOf(id)).path("/image").build()).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response changeData(@Valid final EditUserDto editUserDto,
                               @PathParam("id") final long id) {

        User user = userService.findById(id);
        accountControllerLogger.debug("Finding auth info for user with email {}", user.getEmail());
        UserAuth auth = userService.getAuthInfo(user.getEmail()).orElseThrow(UserNotFoundException::new);
        String username = editUserDto.getUsername() != null ? editUserDto.getUsername() : user.getUsername();
        String phone = editUserDto.getPhone() != null ? editUserDto.getPhone() : user.getPhone();
        accountControllerLogger.debug("Updating user {} with data: name: {}, phone: {}", id,
                username, phone);
        UserWithImage updatedUser = userService.updateUserById(id, editUserDto.getUsername(), editUserDto.getPhone());

        UserDto userAnswer = UserDto.fromUserAndRoles(updatedUser, auth.getRoles(), uriInfo);
        return Response.ok(userAnswer).build();
    }

    @Path("/{id}/security")
    @PUT
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response changePassword(@Valid @NotBlank @Size(min = 8, max = 100) String password,
                                   @PathParam("id") final long id) {
        accountControllerLogger.debug("Updating user password with id {}", id);
        userService.changeUserPassword(id, password);
        return Response.ok(userService.findById(id)).build();
    }


    @Path("/{id}/verify")
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response verifyEmail(@Valid VerifyEmailDto token,
                                @PathParam("id") final long id) {
        accountControllerLogger.debug("Finding user with id: {}", id);
        User user = userService.findById(id);

        if (user.isVerified()) {
            throw new IllegalArgumentException("User is already verified");
        }

        accountControllerLogger.debug("verifying verification token: {} for user:{}", token, user.getId());

        if (!tokenService.verifyVerificationToken(user, token.getToken())) {
            accountControllerLogger.debug("Verification token expired");
            throw new IllegalArgumentException("Token expired");
        }
        return Response.ok(new GenericEntity<String>("User verified") {
        }).build();

    }

    @Path("/recover-account")
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response recoverAccount(@Valid @NotBlank @Email @FormParam("email") final String email) {

        accountControllerLogger.debug("Recovering password for email: {}", email);

        userService.recoverUserAccount(email, LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages()), uriInfo.getBaseUri().toString());

        return Response.ok(new GenericEntity<String>("Recovery mail sent") {
        }).build();

    }

    @Path("/recover-account/change-password")
    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response recoverAccount(@Valid @NotBlank @FormParam("token") final String token,
                                   @Valid @NotBlank @Email @FormParam("email") final String email,
                                   @Valid @NotBlank @FormParam("password") final String newPassword) {

        User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);

        accountControllerLogger.debug("verifying recovery token: {} for user:{}", token, user.getId());

        if (!tokenService.verifyRecoveryToken(user.getId(), token)) {
            accountControllerLogger.debug("Recovery token expired");
            throw new IllegalArgumentException("Token expired");
        }

        userService.recoverUserPassword(user.getId(), newPassword);
        return Response.ok(new GenericEntity<String>("Recovery mail sent") {
        }).build();
    }

    @GET
    @Path("/{id}/rates")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response rates(@PathParam("id") final long id) {
        accountControllerLogger.debug("Finding rates for pro with id: {}", id);
        return Response.ok(ReviewsByExactRateDto.fromListWithRates(reviewService.findProfessionalReviewsByPoints(id)))
                .build();
    }

    @Path("/{id}/rankings")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getCategoryRankings(@PathParam("id") final long id) {
        accountControllerLogger.debug("Finding analytics rankings for user {}", id);
        List<AnalyticRankingDto> rankingDtoList = userService.findUserAnalyticRankings(id).stream()
                .map(ranking -> AnalyticRankingDto.fromAnalyticRanking(ranking,
                        messageSource.getMessage(ranking.getJobType().getDescription(), null,
                                LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages()))))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<AnalyticRankingDto>>(rankingDtoList) {
        }).build();
    }

    @Path("/{id}/professional-info")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getProfessionalDetails(@PathParam("id") final long id) {
        accountControllerLogger.debug("Finding average rate for user {}", id);
        Double avgRate = reviewService.findProfessionalAvgRate(id);

        accountControllerLogger.debug("Finding rates quantity for user {}", id);
        long rateQty = reviewService.findReviewsByProIdSize(id);

        accountControllerLogger.debug("Finding contracts completed for user {}", id);
        long contractsQty = jobContractService.findCompletedContractsByProIdQuantity(id);

        return Response.ok(ProfessionalDto.fromUserAndRoles(avgRate, rateQty, contractsQty)).build();
    }

}

