package by.dytni.gp_tech.service;

import by.dytni.gp_tech.dto.HotelDTO;
import by.dytni.gp_tech.dto.HotelShortInfoDTO;
import by.dytni.gp_tech.model.Hotel;
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

    public List<HotelShortInfoDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::toHotelShortInfoDTO)
                .collect(Collectors.toList());
    }

    public Optional<HotelDTO> getHotelById(Long id) {
        return hotelRepository.findById(id).map(this::toHotelDTO);
    }

    public List<Hotel> searchHotels(String name, String brand, String city, String county, List<String> amenities) {
        Specification<Hotel> spec = HotelSpecification.buildSpecification(name, brand, city, county, amenities);
        return hotelRepository.findAll(spec);
    }

    public Hotel createHotel(Hotel hotel) {
        hotel.setId(null); // Убедитесь, что id не установлен
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
        switch (param) {
            case "brand":
                return hotelRepository.findAll().stream()
                        .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            case "city":
                return hotelRepository.findAll().stream()
                        .collect(Collectors.groupingBy(Hotel::getCity, Collectors.counting()));
            case "county":
                return hotelRepository.findAll().stream()
                        .collect(Collectors.groupingBy(Hotel::getCounty, Collectors.counting()));
            case "amenities":
                return hotelRepository.findAll().stream()
                        .flatMap(hotel -> hotel.getAmenities().stream())
                        .collect(Collectors.groupingBy(amenity -> amenity, Collectors.counting()));
            default:
                throw new IllegalArgumentException("Invalid parameter for histogram");
        }
    }

    public HotelShortInfoDTO toHotelShortInfoDTO(Hotel hotel) {
        HotelShortInfoDTO dto = new HotelShortInfoDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setAddress(formatAddress(hotel));
        dto.setPhone(hotel.getPhone());
        return dto;
    }

    public HotelDTO toHotelDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());
        dto.setBrand(hotel.getBrand());
        dto.setContacts(toContactsDTO(hotel));
        dto.setArrivalTime(toArrivalTimeDTO(hotel));
        dto.setAddress(toAddressDTO(hotel));
        dto.setAmenities(hotel.getAmenities());
        return dto;
    }

    private String formatAddress(Hotel hotel) {
        return String.format("%d %s, %s, %s, %s",
                hotel.getHouseNumber(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getPostCode(),
                hotel.getCounty());
    }

    private HotelDTO.AddressDTO toAddressDTO(Hotel hotel) {
        HotelDTO.AddressDTO dto = new HotelDTO.AddressDTO();
        dto.setHouseNumber(hotel.getHouseNumber());
        dto.setStreet(hotel.getStreet());
        dto.setCity(hotel.getCity());
        dto.setCounty(hotel.getCounty());
        dto.setPostCode(hotel.getPostCode());
        return dto;
    }

    private HotelDTO.ContactsDTO toContactsDTO(Hotel hotel) {
        HotelDTO.ContactsDTO dto = new HotelDTO.ContactsDTO();
        dto.setPhone(hotel.getPhone());
        dto.setEmail(hotel.getEmail());
        return dto;
    }

    private HotelDTO.ArrivalTimeDTO toArrivalTimeDTO(Hotel hotel) {
        HotelDTO.ArrivalTimeDTO dto = new HotelDTO.ArrivalTimeDTO();
        dto.setCheckIn(hotel.getCheckIn());
        dto.setCheckOut(hotel.getCheckOut());
        return dto;
    }
}