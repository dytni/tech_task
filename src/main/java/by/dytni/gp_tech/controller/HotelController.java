package by.dytni.gp_tech.controller;

import by.dytni.gp_tech.dto.HotelDTO;
import by.dytni.gp_tech.dto.HotelShortDTO;
import by.dytni.gp_tech.model.Hotel;
import by.dytni.gp_tech.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotel Management", description = "API for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of hotels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotels not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public List<HotelShortDTO> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the hotel",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create a new hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hotel.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<HotelShortDTO> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.createHotel(hotel);
        HotelShortDTO hotelDTO = hotelService.toHotelShortInfoDTO(createdHotel);
        return ResponseEntity.ok(hotelDTO);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to a hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities added successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<HotelDTO> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        Hotel updatedHotel = hotelService.addAmenities(id, amenities);
        HotelDTO hotelDTO = hotelService.toHotelDTO(updatedHotel);
        return ResponseEntity.ok(hotelDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "name", description = "Hotel name", required = false),
            @Parameter(in = ParameterIn.QUERY, name = "brand", description = "Hotel brand", required = false),
            @Parameter(in = ParameterIn.QUERY, name = "city", description = "City where the hotel is located", required = false),
            @Parameter(in = ParameterIn.QUERY, name = "county", description = "County where the hotel is located", required = false),
            @Parameter(in = ParameterIn.QUERY, name = "amenity", description = "Hotel amenities", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of hotels",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Hotels not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public List<HotelShortDTO> searchHotels(
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