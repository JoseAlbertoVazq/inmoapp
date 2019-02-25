package dam.javazquez.inmoapp.responses;

public class PhotoResponse {

    private String id;
    private PropertyResponse propertyId;
    private String imgurlink;
    private String deletehash;

    public PhotoResponse() {

    }

    public PhotoResponse(PropertyResponse propertyId, String imgurlink, String deletehash) {
        this.propertyId = propertyId;
        this.imgurlink = imgurlink;
        this.deletehash = deletehash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PropertyResponse getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(PropertyResponse propertyId) {
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
