package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Locale;

public interface JobContractService {

    JobContractWithImage create(String client_email, long packageId, String description, Locale locale);

    JobContractWithImage create(String client_email, long packageId, String description, ByteImage image, Locale locale);

    JobContract findById(long id);

    List<JobContract> findByClientId(long id);

    List<JobContract> findByClientId(long id, int page);

    List<JobContract> findByClientId(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByProId(long id);

    List<JobContract> findByProId(long id, int page);

    List<JobContract> findByProId(long id, List<JobContract.ContractState> states, int page);

    List<JobContract> findByPostId(long id);

    List<JobContract> findByPostId(long id, int page);

    List<JobContract> findByPackageId(long id);

    List<JobContract> findByPackageId(long id, int page);

    User findClientByContractId(long id);

    int findContractsQuantityByProId(long id);

    int findContractsQuantityByPostId(long id);

    int findMaxPageContractsByClientId(long id, List<JobContract.ContractState> states);

    int findMaxPageContractsByProId(long id, List<JobContract.ContractState> states);

    List<JobContractCard> findJobContractCardsByClientId(long id, List<JobContract.ContractState> states, int page);

    List<JobContractCard> findJobContractCardsByProId(long id, List<JobContract.ContractState> states, int page);

    void changeContractState(long id, JobContract.ContractState state);

    JobContractWithImage findJobContractWithImage(long id);
}
