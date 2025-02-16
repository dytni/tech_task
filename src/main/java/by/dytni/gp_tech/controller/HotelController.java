package by.dytni.gp_tech.controller;

import by.dytni.gp_tech.dto.HotelDTO;
import by.dytni.gp_tech.model.Hotel;
import by.dytni.gp_tech.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotel Management", description = "Controller for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels")
    public List<HotelDTO> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create a new hotel")
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to a hotel")
    public Hotel addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels")
    public List<HotelDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) List<String> amenities) {
        return hotelService.searchHotels(name, brand, city, county, amenities).stream()
                .map(hotelService::toHotelShortInfoDTO)
                .collect(Collectors.toList());
    }
}