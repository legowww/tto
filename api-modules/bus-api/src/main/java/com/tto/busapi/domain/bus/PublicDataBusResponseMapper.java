package com.tto.busapi.domain.bus;

import com.tto.ttodomain.transportation.bus.BusArrivalTimeResponse;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class PublicDataBusResponseMapper {

    public Optional<BusArrivalTimeResponse> execute(String response) {
        JSONObject json = XML.toJSONObject(response);
        JSONObject serviceResult = (JSONObject) json.get("ServiceResult");
        JSONObject msgHeader = (JSONObject) serviceResult.get("msgHeader");

        int resultCode = Integer.parseInt(msgHeader.get("resultCode").toString());
        int totalCount = Integer.parseInt(msgHeader.get("totalCount").toString());

        if (resultCode == 0) {
            JSONObject msgBody = (JSONObject) serviceResult.get("msgBody");
            if (totalCount == 1) {
                JSONObject item = (JSONObject) msgBody.get("itemList");
                String latestStopId = item.get("LATEST_STOP_ID").toString();
                int arrivalEstimateTime = Integer.parseInt(item.get("ARRIVALESTIMATETIME").toString());

                return Optional.of(new BusArrivalTimeResponse(latestStopId, arrivalEstimateTime));
            }
        }

        return Optional.empty();
    }
}
