package com.tto.busapi.domain.bus;

import com.tto.busapi.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 정류장를 경유하는 특정 노선의 도착 예정 버스의 차량번호, 노선번호, 막차 구분 도착예정 시간 등의 정보를 조회한다.
 */
@FeignClient(
        name = "public-data-bus-api",
        url = "http://apis.data.go.kr/6280000/busArrivalService/getBusArrivalList",
        configuration = FeignConfig.class
)
public interface PublicDataApi {

    @GetMapping
    String callBusArrivalListApi(
            @RequestParam String serviceKey,
            @RequestParam String pageNo,
            @RequestParam String numOfRows,
            @RequestParam String bstopId,
            @RequestParam String routeId
    );
}
