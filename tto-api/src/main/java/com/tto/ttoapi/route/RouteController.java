package com.tto.ttoapi.route;


import com.tto.ttoapi.global.ApiResponse;
import com.tto.ttodomain.location.LocationCoordinate;
import com.tto.ttodomain.route.RouteFindResponse;
import com.tto.ttodomain.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;


    @GetMapping("/route")
    public ResponseEntity<ApiResponse<RouteFindResponse>> findRoute(
            @RequestParam String sx,
            @RequestParam String sy,
            @RequestParam String ex,
            @RequestParam String ey
    ) {
        RouteFindResponse response = routeService.findRoutes(createLocationCoordinate(sx, sy, ex, ey));
        return ResponseEntity.ok(ApiResponse.ok(response));
    }


    @GetMapping("/test")
    public ResponseEntity<ApiResponse<RouteFindResponse>> testFindRoute() {
        //한양1차 -> 송도현대아울렛
        //localhost:8080/api/v1/route?sx=126.6700005&sy=37.4055207&ex=126.636651&ey=37.3768374
        RouteFindResponse response = routeService.findRoutes(createLocationCoordinate(
                "126.6700005", "37.4055207",
                "126.636651", "37.3768374"
        ));
        return ResponseEntity.ok(ApiResponse.ok(response));
    }


    private LocationCoordinate createLocationCoordinate(String sx, String sy, String ex, String ey) {
        return new LocationCoordinate(sx, sy, ex, ey);
    }
}

