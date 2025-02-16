package by.dytni.gp_tech.service;

import by.dytni.gp_tech.dto.HotelDTO;
import by.dytni.gp_tech.model.Hotel;
import by.dytni.gp_tech.model.Address;
import by.dytni.gp_tech.repository.HotelRepository;
import by.dytni.gp_tech.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::toHotelShortInfoDTO)
                .collect(Collectors.toList());
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> searchHotels(String name, String brand, String city, String county, List<String> amenities) {
        Specification<Hotel> spec = HotelSpecification.buildSpecification(name, brand, city, county, amenities);
        return hotelRepository.findAll(spec);
    }

    public Hotel createHotel(Hotel hotel) {
        hotel.setId(null);
        return hotelRepository.save(hotel);
    }

    @Transactional
    public Hotel addAmenities(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.getAmenities().addAll(amenities);
        return hotelRepository.save(hotel);
    }

    public Map<String, Long> getHistogram(String param) {
        return switch (param) {
            case "brand" -> hotelRepository.findAll().stream()
                    .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            case "city" -> hotelRepository.findAll().stream()
                    .map(Hotel::getAddress)
                    .collect(Collectors.groupingBy(Address::getCity, Collectors.counting()));
            case "county" -> hotelRepository.findAll().stream()
                    .map(Hotel::getAddress)
                    .collect(Collectors.groupingBy(Address::getCounty, Collectors.counting()));
            case "amenities" -> hotelRepository.findAll().stream()
                    .flatMap(hotel -> hotel.getAmenities().stream())
                    .collect(Collectors.groupingBy(amenity -> amenity, Collectors.counting()));
            default -> throw new IllegalArgumentException("Invalid parameter for histogram");
        };
    }

    public HotelDTO toHotelShortInfoDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setAddress(formatAddress(hotel.getAddress()));
        dto.setPhone(hotel.getPhone());
        return dto;
    }

    private String formatAddress(Address address) {
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCounty());
    }
}