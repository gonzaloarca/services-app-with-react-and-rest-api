package ar.edu.itba.paw.webapp.restcontrollers;

import ar.edu.itba.paw.interfaces.services.JobContractService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.dto.input.EditJobContractDto;
import ar.edu.itba.paw.webapp.dto.input.NewJobContractDto;
import ar.edu.itba.paw.webapp.dto.output.JobContractCardDto;
import ar.edu.itba.paw.webapp.dto.output.JobContractStateDto;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.ImagesUtil;
import ar.edu.itba.paw.webapp.utils.LocaleResolverUtil;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import ar.edu.itba.paw.webapp.validation.ValidImage;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Path("/contracts")
@Component
public class JobContractController {
    private final Logger JobContractsControllerLogger = LoggerFactory.getLogger(ar.edu.itba.paw.webapp.restcontrollers.JobContractController.class);

    @Autowired
    private JobContractService jobContractService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findContracts(@QueryParam("userId") final Long userId,
                                  @QueryParam("state") final String contractState,
                                  @QueryParam("type") final String type,
                                  @QueryParam("page") @DefaultValue("1") final int page) {
        Locale locale = LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages());
        JobContractsControllerLogger.debug("Finding contracts Max page {}", userId);
        int maxPage = jobContractService.findContractsMaxPage(userId, contractState, type);

        List<JobContractCardDto> jobContractCardDtoList = jobContractService.findContracts(userId, contractState, type, page - 1)
                .stream().map(jobContractCard -> JobContractCardDto
                        .fromJobContractCardWithLocalizedMessage(jobContractCard, uriInfo, messageSource
                                .getMessage(jobContractCard.getJobCard().getJobPost().getJobType().getDescription(),
                                        null, locale)
                        )).collect(Collectors.toList());

        return PageResponseUtil.getGenericListResponse(page, maxPage, uriInfo,
                Response.ok(new GenericEntity<List<JobContractCardDto>>(jobContractCardDtoList) {
                }));
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createJobContract(final NewJobContractDto newContract) {
        Locale locale = LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages());
        String webpageUrl = uriInfo.getAbsolutePathBuilder().replacePath(null)
                .build().toString();
        User current = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        JobContractsControllerLogger.debug("Creating contract for package {} with data: client id:{}, description:{}",
                newContract.getJobPackageId(), current.getId(), newContract.getDescription());
        JobContractWithImage jobContract = jobContractService.create(
                current.getId(), newContract.getJobPackageId(),
                newContract.getDescription(), newContract.getScheduledDate(), locale, webpageUrl);

        final URI contractUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(jobContract.getId())).build();
        return Response.created(contractUri).build();
    }

    @GET
    @Path("/{contractId}/")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("contractId") final long contractId) {
        Locale locale = LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages());
        JobContractsControllerLogger.debug("Finding job contract by id: {}", contractId);
        JobContractCard jobContractCard = jobContractService.findContractCardById(contractId);
        JobContractCardDto jobContractCardDto = JobContractCardDto
                .fromJobContractCardWithLocalizedMessage(jobContractCard, uriInfo, messageSource
                        .getMessage(jobContractCard.getJobCard().getJobPost().getJobType().getDescription(),
                                null, locale));
        return Response.ok(new GenericEntity<JobContractCardDto>(jobContractCardDto) {
        }).build();
    }

    @PUT
    @Path("/{contractId}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response updateContract(@Valid final EditJobContractDto editJobContractDto,
                                   @PathParam("contractId") final long contractId) {
        Locale locale = LocaleResolverUtil.resolveLocale(headers.getAcceptableLanguages());
        String webPageUrl = uriInfo.getAbsolutePathBuilder().replacePath(null)
                .build().toString();

        if (editJobContractDto.getNewScheduledDate() != null) {
            JobContractsControllerLogger.debug("Updating job contract with id: {} scheduledDate to: {}",
                    contractId, editJobContractDto.getNewScheduledDate());
            jobContractService.changeContractScheduledDate(contractId,
                    editJobContractDto.getNewScheduledDate(),
                    locale);
        }
        JobContractsControllerLogger.debug("Updating job contract with id: {} state to: {}",
                contractId, editJobContractDto.getNewState());

        long userId = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new).getId();
        jobContractService.changeContractState(contractId,
                userId,
                JobContract.ContractState.values()[Math.toIntExact(editJobContractDto.getNewState())],
                locale, webPageUrl);

        final URI contractUri = uriInfo.getAbsolutePathBuilder().build();
        return Response.ok(contractUri).build();
    }

    @Path("/{contractId}/image")
    @GET
    @Produces(value = {"image/png", "image/jpg", "image/jpeg", MediaType.APPLICATION_JSON})
    public Response getContractImage(@PathParam("contractId") final long contractId, @Context Request request) {
        ByteImage byteImage = jobContractService.findImageByContractId(contractId);
        return ImagesUtil.sendCacheableImageResponse(byteImage, request);
    }

    @Path("/{contractId}/image")
    @PUT
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response uploadContractImage(@PathParam("contractId") final long contractId,
                                        @Valid @ValidImage @FormDataParam("file") final FormDataBodyPart body) {
        try {
            if (jobContractService.addContractImage(contractId, ImagesUtil.fromInputStream(body)) == -1)
                throw new RuntimeException("Couldn't save image");
        } catch (IOException e) {
            throw new RuntimeException("Upload failed");
        }
        return Response.created(uriInfo.getBaseUriBuilder()
                .path("/contracts").path(String.valueOf(contractId))
                .path("/image").build()).build();
    }

    @Path("/states")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getStates(@Context Request request) {
        List<JobContract.ContractState> contractStates = Arrays.asList(JobContract.ContractState.values());
        List<JobContractStateDto> contractStateDtos = contractStates.stream()
                .map(JobContractStateDto::fromJobContractState).collect(Collectors.toList());
        GenericEntity<List<JobContractStateDto>> entity = new GenericEntity<List<JobContractStateDto>>(contractStateDtos) {};
        return CacheUtils.sendConditionalCacheResponse(request, entity, new EntityTag(Integer.toString(contractStates.hashCode())));
    }

    @Path("/states/{id}")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getStateById(@PathParam("id") int id,@Context Request request) {
        JobContract.ContractState contractState = JobContract.ContractState.values()[id];
        JobContractStateDto contractStateDto = JobContractStateDto.fromJobContractState(JobContract.ContractState.values()[id]);
        GenericEntity<JobContractStateDto> entity = new GenericEntity<JobContractStateDto>(contractStateDto) {};
        return CacheUtils.sendConditionalCacheResponse(request, entity, new EntityTag(Integer.toString(contractState.hashCode())));
    }
}
