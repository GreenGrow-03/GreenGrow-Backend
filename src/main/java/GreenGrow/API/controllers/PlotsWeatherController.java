package GreenGrow.API.controllers;

import GreenGrow.API.entities.PlotsWeather;
import GreenGrow.API.services.PlotsWeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plotsweather")
public class PlotsWeatherController {
    private PlotsWeatherService plotsWeatherService;

    public PlotsWeatherController(PlotsWeatherService plotsWeatherService) {
        this.plotsWeatherService = plotsWeatherService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlotsWeather>> findAllPlotsWeather() {
        try {
            List<PlotsWeather> plotsWeathers = plotsWeatherService.getAll();
            if (plotsWeathers.size() > 0)
                return new ResponseEntity<>(plotsWeathers, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlotsWeather>findPlotsWeatherById(@PathVariable("id") Long id) {
        try {
            Optional<PlotsWeather> plotsWeather = plotsWeatherService.getById(id);
            if (!plotsWeather.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(plotsWeather.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
