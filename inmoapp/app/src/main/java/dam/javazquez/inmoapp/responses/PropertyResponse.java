package dam.javazquez.inmoapp.responses;

public class PropertyResponse {

    private String id;
    private UserResponse ownerId;
    private String title;
    private String description;
    private float price;
    private Long number;
    private float size;
    private CategoryResponse categoryId;
    private String address;
    private String zipcode;
    private String city;
    private String province;
    private String loc;

    public PropertyResponse() {

    }

    public PropertyResponse(UserResponse ownerId, String title, String description, float price, Long number, float size, CategoryResponse categoryId, String address, String zipcode, String city, String province, String loc) {
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.number = number;
        this.size = size;
        this.categoryId = categoryId;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.province = province;
        this.loc = loc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponse getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserResponse ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public CategoryResponse getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryResponse categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
