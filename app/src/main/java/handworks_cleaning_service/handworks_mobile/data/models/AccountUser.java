package handworks_cleaning_service.handworks_mobile.data.models;

public class AccountUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String provider;
    private String createdAt;
    private String updatedAt;
    private String clerkId;

    public AccountUser(String id, String firstName, String lastName, String email, String role, String provider, String createdAt, String updatedAt, String clerkId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.clerkId = clerkId;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getProvider() {
        return provider;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getClerkId() {
        return clerkId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setClerkId(String clerkId) {
        this.clerkId = clerkId;
    }
}
