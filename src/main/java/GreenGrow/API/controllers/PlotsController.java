package GreenGrow.API.controllers;

import GreenGrow.API.entities.Plot;
import GreenGrow.API.entities.User;
import GreenGrow.API.services.PlotService;
import GreenGrow.API.services.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/plots")
@Api(tags = "Plots", value = "Web Service RESTful - Plots")
public class PlotsController {
    private PlotService plotService;
    private UserService userService;

    public PlotsController(PlotService plotService, UserService userService) {
        this.plotService = plotService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Plot>> findAllPlots() {
        try {
            List<Plot> plots = plotService.getAll();
            if (plots.size() > 0)
                return new ResponseEntity<>(plots , HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plot>findPlotById(@PathVariable("id") Long id) {
        try {
            Optional<Plot> plot = plotService.getById(id);
            if (!plot.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(plot.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{userId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plot> insertPlot(@PathVariable("userId") Long userId, @Valid @RequestBody Plot plot){
        try{
            Optional<User> user = userService.getById(userId);
            if(user.isPresent()) {
                plot.setUser(user.get());
                Plot newPlot = plotService.save(plot);
                return ResponseEntity.status(HttpStatus.CREATED).body(newPlot);
            }
            else
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plot> deletePlot (@PathVariable("id") Long id){
        try{
            Optional<Plot> plotDelete = plotService.getById(id);
            if(!plotDelete.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            plotService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
