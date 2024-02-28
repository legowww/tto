package com.tto.subwayapi.domain.subway;

import com.tto.subwayapi.config.ODsayProperties;
import com.tto.ttodomain.transportation.subway.SubwayArrivalTimeService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ODsaySubwayArrivalService implements SubwayArrivalTimeService {

    private final ODsayProperties oDsayProperties;
    private final OdsayApi odsayApi;

    public List<LocalDateTime> execute(String stationId, String wayCode) {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> result = new ArrayList<>();

        try {
            Optional<SubwayTimeDto> opt = getSubwayArrivalStationTime(stationId, wayCode);

            if (opt.isEmpty()) {
                throw new IllegalStateException("지하철이 운행할수 없는 시간입니다.");
            }

            SubwayTimeDto subwayTimeDto = opt.get();
            String idx = subwayTimeDto.getIdx();
            String[][] list = subwayTimeDto.getList();

            if (idx.compareTo("24") == 0) {
                for (int i = 0; i < list[0].length; i++) {
                    if (result.size() == 4)
                        break;
                    LocalDateTime cmp = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, Integer.parseInt(list[0][i])));
                    if (now.compareTo(cmp) < 0)
                        result.add(cmp);
                }
            } else if (idx.compareTo("23") == 0) {
                for (int i = 0; i < list[1].length; i++) {
                    if (result.size() == 4)
                        break;
                    LocalDateTime cmp = LocalDateTime.of(LocalDate.now(), LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[1][i])));
                    if (now.compareTo(cmp) < 0)
                        result.add(cmp);
                }

                for (int i = 0; i < list[2].length; i++) {
                    if (result.size() == 4)
                        break;
                    LocalDateTime cmp = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, Integer.parseInt(list[2][i])));
                    if (now.compareTo(cmp) < 0)
                        result.add(cmp);
                }
            } else {
                for (int i = 0; i < list[1].length; i++) {
                    if (result.size() == 4)
                        break;
                    LocalDateTime cmp = LocalDateTime.of(LocalDate.now(), LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[1][i])));
                    if (now.compareTo(cmp) < 0)
                        result.add(cmp);
                }

                for (int i = 0; i < list[2].length; i++) {
                    if (result.size() == 4)
                        break;
                    LocalDateTime cmp = LocalDateTime.of(LocalDate.now(), LocalTime.of(Integer.parseInt(idx) + 1, Integer.parseInt(list[2][i])));
                    if (now.compareTo(cmp) < 0)
                        result.add(cmp);
                }
            }

            return result;
        } catch (RuntimeException e) {
            throw new IllegalStateException("[SubwayArrivalService.getTimeResponse]error");
        }
    }

    private Optional<SubwayTimeDto> getSubwayArrivalStationTime(String stationId, String wayCode) {
        try {
            LocalDateTime now = LocalDateTime.now();

            String response = odsayApi.callSubwayTimeTableApi(
                    oDsayProperties.getSubway().getKey(),
                    "0",
                    stationId,
                    wayCode
            );

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            JSONObject result = (JSONObject) json.get("result");

            JSONObject time;
            String idx;
            String list;
            JSONObject time2;
            String list2;

            String[][] resultList = new String[3][10];

            if (String.valueOf(now.getDayOfWeek()).equals("SATURDAY")) {
                JSONObject satList = (JSONObject) result.get("SatList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) satList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }
                else {
                    JSONObject down = (JSONObject) satList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }


            }
            else if (String.valueOf(now.getDayOfWeek()).equals("SUNDAY")) {
                JSONObject sunList = (JSONObject) result.get("SunList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) sunList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }
                else {
                    JSONObject down = (JSONObject) sunList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }
            }
            else {
                JSONObject ordList = (JSONObject) result.get("OrdList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) ordList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }
                else {
                    JSONObject down = (JSONObject) ordList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[0] = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList[1] = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            resultList[2] = splitList(list2);
                        }
                        return Optional.ofNullable(new SubwayTimeDto(idx, resultList));
                    }
                }
            }

            return Optional.empty();
        } catch (ParseException e) {
            throw new IllegalStateException("[SubwayArrivalService.getSubwayArrivalStationTime]ParseException error");
        }
    }

    private static String[] splitList(String list) {
        String replaceList = list.replaceAll("[^0-9]", "");
        String[] result = new String[replaceList.length() / 2];

        for (int i = 0; i < replaceList.length() / 2; i++) {
            String a = String.valueOf(replaceList.charAt(i * 2));
            String b = String.valueOf(replaceList.charAt(i * 2 + 1));
            result[i] = a + b;
        }

        return result;
    }



    private String temp() {
        return "{\"result\":{\"type\":21,\"laneName\":\"인천 1호선\",\"laneCity\":\"인천\",\"OrdList\":{\"down\":{\"time\":[{\"Idx\":5,\"list\":\"43(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":6,\"list\":\"06(송도달빛축제공원) 13(송도달빛축제공원) 20(송도달빛축제공원) 27(송도달빛축제공원) 34(송도달빛축제공원) 41(송도달빛축제공원) 48(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":7,\"list\":\"00(송도달빛축제공원) 05(송도달빛축제공원) 10(송도달빛축제공원) 15(송도달빛축제공원) 20(송도달빛축제공원) 24(송도달빛축제공원) 29(송도달빛축제공원) 33(송도달빛축제공원) 38(송도달빛축제공원) 42(송도달빛축제공원) 47(송도달빛축제공원) 51(송도달빛축제공원) 56(송도달빛축제공원)\"},{\"Idx\":8,\"list\":\"00(송도달빛축제공원) 05(송도달빛축제공원) 09(송도달빛축제공원) 14(송도달빛축제공원) 18(송도달빛축제공원) 23(송도달빛축제공원) 27(송도달빛축제공원) 32(송도달빛축제공원) 36(송도달빛축제공원) 41(송도달빛축제공원) 45(송도달빛축제공원) 50(송도달빛축제공원) 54(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":9,\"list\":\"03(송도달빛축제공원) 08(송도달빛축제공원) 14(송도달빛축제공원) 20(송도달빛축제공원) 26(송도달빛축제공원) 32(송도달빛축제공원) 38(송도달빛축제공원) 44(송도달빛축제공원) 50(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":10,\"list\":\"06(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 32(송도달빛축제공원) 40(송도달빛축제공원) 49(송도달빛축제공원) 57(송도달빛축제공원)\"},{\"Idx\":11,\"list\":\"06(송도달빛축제공원) 14(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 40(송도달빛축제공원) 48(송도달빛축제공원) 57(송도달빛축제공원)\"},{\"Idx\":12,\"list\":\"05(송도달빛축제공원) 14(송도달빛축제공원) 22(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 48(송도달빛축제공원) 56(송도달빛축제공원)\"},{\"Idx\":13,\"list\":\"05(송도달빛축제공원) 13(송도달빛축제공원) 22(송도달빛축제공원) 30(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 56(송도달빛축제공원)\"},{\"Idx\":14,\"list\":\"04(송도달빛축제공원) 13(송도달빛축제공원) 21(송도달빛축제공원) 30(송도달빛축제공원) 38(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":15,\"list\":\"04(송도달빛축제공원) 12(송도달빛축제공원) 21(송도달빛축제공원) 29(송도달빛축제공원) 38(송도달빛축제공원) 46(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":16,\"list\":\"03(송도달빛축제공원) 12(송도달빛축제공원) 20(송도달빛축제공원) 29(송도달빛축제공원) 37(송도달빛축제공원) 46(송도달빛축제공원) 54(송도달빛축제공원)\"},{\"Idx\":17,\"list\":\"03(송도달빛축제공원) 10(송도달빛축제공원) 16(송도달빛축제공원) 22(송도달빛축제공원) 28(송도달빛축제공원) 34(송도달빛축제공원) 40(송도달빛축제공원) 46(송도달빛축제공원) 52(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":18,\"list\":\"04(송도달빛축제공원) 10(송도달빛축제공원) 15(송도달빛축제공원) 21(송도달빛축제공원) 26(송도달빛축제공원) 32(송도달빛축제공원) 37(송도달빛축제공원) 43(송도달빛축제공원) 49(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":19,\"list\":\"01(송도달빛축제공원) 07(송도달빛축제공원) 13(송도달빛축제공원) 19(송도달빛축제공원) 25(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 56(송도달빛축제공원)\"},{\"Idx\":20,\"list\":\"04(송도달빛축제공원) 13(송도달빛축제공원) 21(송도달빛축제공원) 30(송도달빛축제공원) 38(송도달빛축제공원) 48(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":21,\"list\":\"08(송도달빛축제공원) 18(송도달빛축제공원) 28(송도달빛축제공원) 38(송도달빛축제공원) 48(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":22,\"list\":\"08(송도달빛축제공원) 18(송도달빛축제공원) 28(송도달빛축제공원) 38(송도달빛축제공원) 48(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":23,\"list\":\"08(송도달빛축제공원) 18(송도달빛축제공원) 28(송도달빛축제공원) 38(송도달빛축제공원) 48(송도달빛축제공원)\"},{\"Idx\":24,\"list\":\"00(송도달빛축제공원) 12(송도달빛축제공원) 24(국제업무지구) 36(송도달빛축제공원) 48(국제업무지구) 59(동막)\"}]}},\"SatList\":{\"down\":{\"time\":[{\"Idx\":5,\"list\":\"43(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":6,\"list\":\"06(송도달빛축제공원) 15(송도달빛축제공원) 26(송도달빛축제공원) 37(송도달빛축제공원) 47(송도달빛축제공원) 57(송도달빛축제공원)\"},{\"Idx\":7,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":8,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":9,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":10,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":11,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":12,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":13,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":14,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":15,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":16,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":17,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":18,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":19,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 40(송도달빛축제공원) 49(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":20,\"list\":\"07(송도달빛축제공원) 16(송도달빛축제공원) 25(송도달빛축제공원) 34(송도달빛축제공원) 43(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":21,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":22,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":23,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":24,\"list\":\"03(송도달빛축제공원) 14(송도달빛축제공원) 25(국제업무지구) 36(송도달빛축제공원) 48(국제업무지구) 59(동막)\"}]}},\"SunList\":{\"down\":{\"time\":[{\"Idx\":5,\"list\":\"43(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":6,\"list\":\"06(송도달빛축제공원) 15(송도달빛축제공원) 26(송도달빛축제공원) 37(송도달빛축제공원) 47(송도달빛축제공원) 57(송도달빛축제공원)\"},{\"Idx\":7,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":8,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":9,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":10,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":11,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":12,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":13,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":14,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":15,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":16,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":17,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 39(송도달빛축제공원) 47(송도달빛축제공원) 55(송도달빛축제공원)\"},{\"Idx\":18,\"list\":\"03(송도달빛축제공원) 11(송도달빛축제공원) 19(송도달빛축제공원) 27(송도달빛축제공원) 35(송도달빛축제공원) 43(송도달빛축제공원) 51(송도달빛축제공원) 59(송도달빛축제공원)\"},{\"Idx\":19,\"list\":\"07(송도달빛축제공원) 15(송도달빛축제공원) 23(송도달빛축제공원) 31(송도달빛축제공원) 40(송도달빛축제공원) 49(송도달빛축제공원) 58(송도달빛축제공원)\"},{\"Idx\":20,\"list\":\"07(송도달빛축제공원) 16(송도달빛축제공원) 25(송도달빛축제공원) 34(송도달빛축제공원) 43(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":21,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":22,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":23,\"list\":\"02(송도달빛축제공원) 12(송도달빛축제공원) 22(송도달빛축제공원) 32(송도달빛축제공원) 42(송도달빛축제공원) 52(송도달빛축제공원)\"},{\"Idx\":24,\"list\":\"03(송도달빛축제공원) 14(송도달빛축제공원) 25(국제업무지구) 36(송도달빛축제공원) 48(국제업무지구) 59(동막)\"}]}},\"upWay\":\"계양\",\"downWay\":\"송도달빛축제공원\",\"stationID\":20131,\"stationName\":\"동춘\"}}\n";
    }
}
