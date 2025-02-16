package by.dytni.gp_tech.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private int houseNumber;
    private String street;
    private String city;
    private String county;
    private String postCode;
}