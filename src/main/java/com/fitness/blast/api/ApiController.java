package com.fitness.blast.api;

import com.fitness.blast.UserNotFoundException;
import com.fitness.blast.dao.Game;
import com.fitness.blast.dao.Point;
import com.fitness.blast.dao.User;
import com.fitness.blast.integrations.CopernicusApiService;
import com.fitness.blast.integrations.WikipediaApi;
import com.fitness.blast.integrations.wiki.dto.WikiResponseDto;
import com.fitness.blast.service.GameService;
import com.fitness.blast.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "api", tags = {"api"}, description = "User Api", produces = "application/json", consumes = "application/json")
@Slf4j
public class ApiController {
	
    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    private WikipediaApi wikipediaApi;

    @Autowired
    private CopernicusApiService copernicusApiService;


    @RequestMapping(value = "/user/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User register(
            @ApiParam(value = "Name", required = true) @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "Latitude", required = true) @RequestParam(value = "latitude", defaultValue = "") String latitude,
            @ApiParam(value = "Longitude", required = true) @RequestParam(value = "longitude", defaultValue = "") String longitude,
            @ApiParam(value = "Location As a String", required = true) @RequestParam(value = "location", defaultValue = "") String location,
            HttpServletRequest request) {

        UUID id = UUID.randomUUID();

        User newUser = new User(id, name, latitude, longitude, location, 0, true,false);
        userService.register(newUser);
        User opponent = userService.getOpponent(newUser.getId().toString());
        if (opponent == null) {
            return newUser;
        }

        gameService.registerGame(newUser, opponent);

        return newUser;
    }


    @RequestMapping(value = "/find/opponent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User addPoint(
            @ApiParam(value = "Id", required = true) @RequestParam(value = "id", defaultValue = "") String id,
            HttpServletRequest request) {
        return userService.getOpponent(id);
    }

    @RequestMapping(value = "/add/point", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addPoint(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", defaultValue = "") String userId,
            @ApiParam(value = "Latitude", required = true) @RequestParam(value = "latitude", defaultValue = "") double latitude,
            @ApiParam(value = "Longitude", required = true) @RequestParam(value = "longitude", defaultValue = "") double longitude,
            HttpServletRequest request) {

        User user = userService.findUser(userId);
        
        Point point = new Point(user, latitude, longitude);
        if (user == null){
            throw new UserNotFoundException("User not found: " + userId);
        }

        point.setMessage("Urban area");
        gameService.addPointToOpponentMap(user, point);
        
        // add info to point
		point.setMessage(this.gameService.getPointMessage(point.getLatitude(), point.getLongitude()));

        return point.getMessage();
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Game ping(
            @ApiParam(value = "User Id", required = true) @RequestParam(value = "userId", defaultValue = "") String userId,
            @ApiParam(value = "Latitude", required = true) @RequestParam(value = "latitude", defaultValue = "") Double latitude,
            @ApiParam(value = "Longitude", required = true) @RequestParam(value = "longitude", defaultValue = "") Double longitude,

            HttpServletRequest request) {
    	
        log.info("Ping: "+userId+ " coordinates("+latitude+ ", "+ longitude+")");
        User user = userService.findUser(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found for ID:" + userId);
        }

        Game game = gameService.ping(user, latitude, longitude);
        return game.setSortingOwner(user);
    }


    @RequestMapping(value = "/wikipedia", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<WikiResponseDto> wikipedia(
            @ApiParam(value = "Latitude", required = true) @RequestParam(value = "latitude", defaultValue = "") Double latitude,
            @ApiParam(value = "Longitude", required = true) @RequestParam(value = "longitude", defaultValue = "") Double longitude,
            HttpServletRequest request) {

		return this.wikipediaApi.getNearbyPOIs(latitude, longitude);

	}



    @RequestMapping(value = "/copernicus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String copernicus(
            @ApiParam(value = "Latitude", required = true) @RequestParam(value = "latitude", defaultValue = "") Double latitude,
            @ApiParam(value = "Longitude", required = true) @RequestParam(value = "longitude", defaultValue = "") Double longitude,
            HttpServletRequest request) {

        return this.copernicusApiService.getUrbanizationLayer(latitude, longitude);
    }

}
