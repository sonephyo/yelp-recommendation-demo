//

package classes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Business implements Serializable {

    private String business_id;
    private String name;
    private String categories;

    private String[] categoriesArr;

    private String Rv_text;

    private Double latitude;

    private Double longitude;

    private List<Business> closestNeighbors;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRv_text() {
        return Rv_text;
    }

    public void setRv_text(String rv_text) {
        Rv_text = rv_text;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String[] getCategoriesArr() {
        return categoriesArr;
    }

    public void setCategoriesArr(String[] categoriesArr) {
        this.categoriesArr = categoriesArr;
    }

    public List<Business> getClosestNeighbors() {
        return closestNeighbors;
    }
    public void setClosestNeighbors(List<Business> closestNeighbors) {
        this.closestNeighbors = closestNeighbors;
    }
    @Override
    public String toString() {
        return "Business{" +
                "business_id='" + business_id + '\'' +
                ", name='" + name + '\'' +
                ", categories='" + categories + '\'' +
                ", categoriesArr=" + Arrays.toString(categoriesArr) +
                ", Rv_text='" + Rv_text + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}