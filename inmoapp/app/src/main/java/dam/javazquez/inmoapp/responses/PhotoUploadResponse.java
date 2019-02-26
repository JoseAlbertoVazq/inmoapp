package dam.javazquez.inmoapp.responses;

public class PhotoUploadResponse {

    private String id;
    private String propertyId;
    private String imgurlink;
    private String deletehash;

    public PhotoUploadResponse(String id, String propertyId, String imgurlink, String deletehash) {
        this.id = id;
        this.propertyId = propertyId;
        this.imgurlink = imgurlink;
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

    public String getImgurlink() {
        return imgurlink;
    }

    public void setImgurlink(String imgurlink) {
        this.imgurlink = imgurlink;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }
}
