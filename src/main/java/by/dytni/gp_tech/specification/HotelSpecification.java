package by.dytni.gp_tech.specification;

import by.dytni.gp_tech.model.Hotel;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase());
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Hotel> hasCounty(String county) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("county")), county.toLowerCase());
    }

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, criteriaBuilder) -> {
            Root<Hotel> hotelRoot = query.from(Hotel.class);
            Predicate predicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            for (String amenity : amenities) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isMember(amenity, hotelRoot.get("amenities")));
            }
            return predicate;
        };
    }

    public static Specification<Hotel> buildSpecification(String name, String brand, String city, String county, List<String> amenities) {
        List<Specification<Hotel>> specifications = new ArrayList<>();

        if (name != null) {
            specifications.add(hasName(name));
        }

        if (brand != null) {
            specifications.add(hasBrand(brand));
        }

        if (city != null) {
            specifications.add(hasCity(city));
        }

        if (county != null) {
            specifications.add(hasCounty(county));
        }

        if (amenities != null && !amenities.isEmpty()) {
            specifications.add(hasAmenities(amenities));
        }

        return specifications.stream().reduce(Specification.where(null), Specification::and);
    }
}