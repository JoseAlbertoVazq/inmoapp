package dam.javazquez.inmoapp.responses;

import java.io.Serializable;

public class CategoryResponse implements Serializable {

    private String id;
    private String name;

    public CategoryResponse() {

    }

    public CategoryResponse(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
