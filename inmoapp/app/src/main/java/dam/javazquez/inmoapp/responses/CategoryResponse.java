package dam.javazquez.inmoapp.responses;

public class CategoryResponse {

    private String name;

    public CategoryResponse() {

    }

    public CategoryResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
