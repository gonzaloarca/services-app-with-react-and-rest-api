package ar.edu.itba.paw.models;

public class JobPost {
    private long id;
    private long userId;
    private String title;
    private String availableHours;
    private JobType jobType;

    public JobPost() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return id + ": " + title;
    }
}
