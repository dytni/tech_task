package by.dytni.gp_tech.controller;

import by.dytni.gp_tech.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/property-view/histogram")
@AllArgsConstructor
@Tag(name = "Histogram", description = "histogram controller")
public class HistogramController {

    private HotelService hotelService;

    @GetMapping("/{param}")
    @Operation(summary = "Get histogram data")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}