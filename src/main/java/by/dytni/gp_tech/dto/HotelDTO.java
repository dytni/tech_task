package by.dytni.gp_tech.dto;

import lombok.Data;
import java.util.List;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private ContactsDTO contacts;
    private ArrivalTimeDTO arrivalTime;
    private AddressDTO address;
    private List<String> amenities;

    @Data
    public static class ContactsDTO {
        private String phone;
        private String email;
    }

    @Data
    public static class ArrivalTimeDTO {
        private String checkIn;
        private String checkOut;
    }

    @Data
    public static class AddressDTO {
        private int houseNumber;
        private String street;
        private String city;
        private String county;
        private String postCode;
    }
}