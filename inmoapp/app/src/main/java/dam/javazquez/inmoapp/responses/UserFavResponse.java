package dam.javazquez.inmoapp.responses;

import java.util.List;

public class UserFavResponse {

    private String role;
    private List<String> favs;
    private List<String> keywords;
    private String _id;
    private String picture;
    private String name;
    private String password;
    private String createdAt;
    private String updatedAt;

    public UserFavResponse() {

    }

    public UserFavResponse(String role, List<String> favs, List<String> keywords, String _id, String picture, String name, String password, String createdAt, String updatedAt) {
        this.role = role;
        this.favs = favs;
        this.keywords = keywords;
        this._id = _id;
        this.picture = picture;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getFavs() {
        return favs;
    }

    public void setFavs(List<String> favs) {
        this.favs = favs;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
