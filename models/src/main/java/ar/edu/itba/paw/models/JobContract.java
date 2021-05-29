package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "contract")
public class JobContract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_contract_id_seq")
    @SequenceGenerator(sequenceName = "contract_contract_id_seq", name = "contract_contract_id_seq", allocationSize = 1)
    @Column(name = "contract_id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "contract_client_id_fkey"))
    private User client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", foreignKey = @ForeignKey(name = "contract_package_id_fkey"))
    private JobPackage jobPackage;

    @Column(length = 100, name = "contract_creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(length = 100, name = "contract_description", nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "contract_state", nullable = false, columnDefinition = "INT default 6")
    private ContractState state;

    @AttributeOverrides({
            @AttributeOverride(name = "data", column = @Column(name = "image_data")),
            @AttributeOverride(name = "type", column = @Column(name = "contract_image_type", length = 100))
    })
    private ByteImage image;

    /*Default*/ JobContract() {
    }

    public JobContract(long id, User client, JobPackage jobPackage, LocalDateTime creationDate, String description,
                       ByteImage image) {
        this.id = id;
        this.client = client;
        this.jobPackage = jobPackage;
        this.creationDate = creationDate;
        this.description = description;
        this.image = image;
        this.state = ContractState.PENDING_APPROVAL;
    }

    public JobContract(User client, JobPackage jobPackage, LocalDateTime creationDate, String description,
                       ByteImage image) {
        this.client = client;
        this.jobPackage = jobPackage;
        this.creationDate = creationDate;
        this.description = description;
        this.image = image;
        this.state = ContractState.PENDING_APPROVAL;
    }

    public JobPackage getJobPackage() {
        return jobPackage;
    }

    public void setJobPackage(JobPackage jobPackage) {
        this.jobPackage = jobPackage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProfessional() {
        return jobPackage.getJobPost().getUser();
    }

    public ByteImage getImage() {
        return image;
    }

    public void setImage(ByteImage image) {
        this.image = image;
    }

    public ContractState getState() {
        return state;
    }

    public void setState(ContractState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "JobContract{" +
                "id=" + id +
                ", client=" + client +
                ", jobPackage=" + jobPackage +
                ", professional=" + getProfessional() +
                ", creationDate=" + creationDate +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobContract that = (JobContract) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum ContractState {
        PENDING_APPROVAL(1),
        APPROVED(0),
        CLIENT_REJECTED(2),
        PRO_REJECTED(2),
        CLIENT_CANCELLED(2),
        PRO_CANCELLED(2),
        COMPLETED(2),
        CLIENT_MODIFIED(1),
        PRO_MODIFIED(1);

        final int category;

        ContractState(int category) {
            this.category = category;
        }

        public int getCategory() {
            return category;
        }
    }
}
