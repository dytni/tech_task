package by.dytni.gp_tech.controller;

import by.dytni.gp_tech.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/property-view/histogram")
@RequiredArgsConstructor
@Tag(name = "Histogram", description = "API for histogram data")
public class HistogramController {

    private final HotelService hotelService;

    @GetMapping("/{param}")
    @Operation(summary = "Get histogram data")
    @Parameter(in = ParameterIn.PATH, name = "param", description = "Parameter for histogram (brand, city, county, amenities)", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the histogram data",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(type = "integer")) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        try {
            Map<String, Long> histogram = hotelService.getHistogram(param);
            return ResponseEntity.ok(histogram);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}