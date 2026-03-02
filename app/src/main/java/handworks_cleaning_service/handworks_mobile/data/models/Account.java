package handworks_cleaning_service.handworks_mobile.data.models;

public class Account {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String provider;
    private String role;
    private String clerk_id;
    private String created_at;
    private String updated_at;

    public Account(String id, String clerk_id, String email, String first_name, String last_name, String provider, String role, String created_at, String updated_at) {
        this.id = id;
        this.clerk_id = clerk_id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.provider = provider;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public String getClerk_id() {
        return clerk_id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProvider() {
        return provider;
    }

    public String getRole() {
        return role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClerk_id(String clerk_id) {
        this.clerk_id = clerk_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
