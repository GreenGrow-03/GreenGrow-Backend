package GreenGrow.API.controllers;

import GreenGrow.API.entities.Event;
import GreenGrow.API.entities.Plant;
import GreenGrow.API.entities.Plot;
import GreenGrow.API.entities.User;
import GreenGrow.API.services.PlantService;
import GreenGrow.API.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    private PlantService plantService;

    public UserController(UserService userService, PlantService plantService) {
        this.userService = userService;
        this.plantService = plantService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllUsers() {
        try {
            List<User> users = userService.getAll();
            if(users.size() > 0)
                return new ResponseEntity<>(users, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User>findUserById(@PathVariable("id") Long id) {
        try {
            Optional<User> user = userService.getById(id);
            if (!user.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}/plants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Plant>>findAllPlantsByUserId(@PathVariable("id") Long id) {
        try {
            List<Plant> plants = userService.getPlantsByUserId(id);
            Optional<User> user = userService.getById(id);
            if (user.isPresent()) {
                if (plants.size() > 0)
                    return new ResponseEntity<>(plants, HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/plants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Plant> insertPlantIntoUser(@PathVariable("id") Long id, @Valid @RequestBody Plant plant) {
        try {
            Optional<User> user = userService.getById(id);
            if(user.isPresent()) {
                user.get().getPlants().add(plant);
                Plant newPlant = plantService.save(plant);
                return ResponseEntity.status(HttpStatus.CREATED).body(newPlant);
            }
            else
                return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "{id}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> findAllEventsByUserId(@PathVariable("id") Long id) {
        try {
            List<Event> events = userService.getEventsByUserId(id);
            Optional<User> user = userService.getById(id);
            if (user.isPresent()) {
                if (events.size() > 0)
                    return new ResponseEntity<>(events, HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "{id}/plots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Plot>> findAllPlotsByUserId(@PathVariable("id") Long id) {
        try {
            List<Plot> plots = userService.getPlotsByUserId(id);
            Optional<User> user = userService.getById(id);
            if(user.isPresent()) {
                if (plots.size() > 0)
                    return new ResponseEntity<>(plots, HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findByEmail(@PathVariable("email") String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            if(!user.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/searchPremium", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>>findByPremium() {
        try {
            List<User> users = userService.findByPremium(true);
            if(users.size() > 0)
                return new ResponseEntity<>(users, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/noSearchPremium", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>>findByNoPremium() {
        try {
            List<User> users = userService.findByNoPremium(false);
            if(users.size() > 0)
                return new ResponseEntity<>(users, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        try {
            Optional<User> deleteUser = userService.getById(id);
            if(!deleteUser.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> insertUser(@Valid @RequestBody User user) {
        try {
            User newUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la excepción en la consola
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}