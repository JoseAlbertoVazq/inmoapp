package dam.javazquez.inmoapp.responses;

public class PhotoUploadResponse {

    private String id;
    private String propertyId;
    private String imgurLink;
    private String deletehash;

    public PhotoUploadResponse(String id, String propertyId, String imgurLink, String deletehash) {
        this.id = id;
        this.propertyId = propertyId;
        this.imgurLink = imgurLink;
        this.deletehash = deletehash;
    }

    public PhotoUploadResponse() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getImgurLink() {
        return imgurLink;
    }

    public void setImgurLink(String imgurLink) {
        this.imgurLink = imgurLink;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }
}
