package by.dytni.gp_tech.dto;

import lombok.Data;

@Data
public class HotelShortInfoDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}