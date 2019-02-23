package dam.javazquez.inmoapp.responses;

public class PhotoResponse {

    private String propertyId;
    private String imgurlink;
    private String deletehash;

    public PhotoResponse() {

    }

    public PhotoResponse(String propertyId, String imgurlink, String deletehash) {
        this.propertyId = propertyId;
        this.imgurlink = imgurlink;
        this.deletehash = deletehash;
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
