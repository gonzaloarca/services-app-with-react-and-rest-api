package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface JobContractService {

    JobContractWithImage create(long clientId, long packageId, String description, LocalDateTime scheduledDate, Locale locale,String webpageUrl);

    JobContractWithImage create(long clientId, long packageId, String description, LocalDateTime scheduledDate, ByteImage image, Locale locale,String webpageUrl);

    JobContract findById(long id);

    List<JobContract> findByClientId(long id);

    List<JobContract> findByClientId(long id, int page);

    List<JobContract> findByClientId(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByClientIdAndSortedByModificationDate(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByProId(long id);

    List<JobContract> findByProId(long id, int page);

    List<JobContract> findByProId(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByProIdAndSortedByModificationDate(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByPostId(long id);

    List<JobContract> findByPostId(long id, int page);

    List<JobContract> findByPackageId(long id);

    List<JobContract> findByPackageId(long id, int page);

    User findClientByContractId(long id);

    int findCompletedContractsByProIdQuantity(long id);

    int findContractsByPostIdQuantity(long id);

    int findContractsByClientIdMaxPage(long id, List<JobContract.ContractState> states);

    int findContractsByProIdMaxPage(long id, List<JobContract.ContractState> states);

    List<JobContractCard> findJobContractCardsByProIdAndSorted(long id, List<JobContract.ContractState> states, int page, Locale locale);

    List<JobContractCard> findJobContractCardsByClientId(long id, List<JobContract.ContractState> states, int page, Locale locale);

    List<JobContractCard> findJobContractCardsByProId(long id, List<JobContract.ContractState> states, int page, Locale locale);

    List<JobContractCard> findJobContractCardsByClientIdAndSorted(long id, List<JobContract.ContractState> states, int page, Locale locale);

    void changeContractState(long id, JobContract.ContractState state, Locale locale, String webPageUrl);

    void changeContractScheduledDate(long id, LocalDateTime scheduledDate, boolean isServiceOwner, Locale locale);

    JobContractWithImage findJobContractWithImage(long id);

    ByteImage findImageByContractId(long id);

    List<JobContract.ContractState> getContractStates(String contractState);

    JobContract findByIdWithUser(long id);

    int findByPackageIdMaxPage(long id);
}
