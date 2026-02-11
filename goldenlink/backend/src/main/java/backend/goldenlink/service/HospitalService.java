package backend.goldenlink.service;

import backend.goldenlink.dto.HospitalDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class HospitalService {

    @Value("${openapi.serviceKey}")
    private String serviceKey;

    @Value("${kakao.restKey}")
    private String kakaoKey;

    private final WebClient nemcClient = WebClient.builder()
            .baseUrl("https://apis.data.go.kr/B552657/ErmctInfoInqireService")
            .build();

    private final XmlMapper xmlMapper = new XmlMapper();

    // =========================
    // 1️⃣ 병원 이름 검색
    // =========================
    public List<HospitalDto> searchByName(String keyword) {

        List<HospitalDto> result = new ArrayList<>();

        try {
            String uri = "/getEgytListInfoInqire?serviceKey=" + serviceKey
                    + "&pageNo=1&numOfRows=200";

            String xml = nemcClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map map = xmlMapper.readValue(xml, Map.class);
            Map body = (Map) map.get("body");
            Map items = (Map) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

            for (Map<String, Object> item : itemList) {

                String name = (String) item.get("dutyName");

                if (name != null && name.contains(keyword)) {

                    HospitalDto dto = new HospitalDto(
                            (String) item.get("dutyName"), // hname
                            (String) item.get("dutyAddr"), // haddress
                            pickTel(item), // htel
                            parseDouble(item.get("wgs84Lat")), // hlat
                            parseDouble(item.get("wgs84Lon")) // hlon
                    );

                    result.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =========================
    // 2️⃣ 좌표 주변 검색
    // =========================
    public List<HospitalDto> nearby(double lat, double lon) {
        List<HospitalDto> result = new ArrayList<>();

        try {
            String uri = "/getEgytLcinfoInqire?serviceKey=" + serviceKey
                    + "&WGS84_LAT=" + lat
                    + "&WGS84_LON=" + lon
                    + "&pageNo=1&numOfRows=20";

            String xml = nemcClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 🔍 응답 확인용 (디버깅)
            System.out.println("=== nearby API 응답 ===");
            System.out.println(xml);
            System.out.println("=====================");

            Map map = xmlMapper.readValue(xml, Map.class);

            // 🔍 맵 구조 확인
            System.out.println("Map keys: " + map.keySet());

            Map body = (Map) map.get("body");
            if (body == null) {
                System.out.println("❌ body가 null입니다!");
                return result;
            }

            System.out.println("Body keys: " + body.keySet());

            Map items = (Map) body.get("items");
            if (items == null) {
                System.out.println("❌ items가 null입니다!");
                return result;
            }

            Object itemObj = items.get("item");
            if (itemObj == null) {
                System.out.println("❌ item이 null입니다!");
                return result;
            }

            // item이 List인지 Map인지 확인 List는 순서가 있는 데이터 묶음
            if (itemObj instanceof List) {
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemObj;
                System.out.println("✅ 검색된 병원 수: " + itemList.size());

                for (Map<String, Object> item : itemList) {
                    System.out.println("Item keys: " + item.keySet());

                    HospitalDto dto = new HospitalDto(
                            (String) item.get("dutyName"),
                            (String) item.get("dutyAddr"),
                            (String) item.get("dutyTel1"),
                            parseDouble(item.get("latitude")),
                            parseDouble(item.get("longitude")));

                    System.out.println("생성된 DTO: " + dto);
                    result.add(dto);
                }
            } else if (itemObj instanceof Map) {
                // 결과가 1개일 때는 Map으로 올 수 있음 Map key(이름)와 value(값)로 이루어진 구조
                Map<String, Object> item = (Map<String, Object>) itemObj;
                System.out.println("✅ 검색된 병원 수: 1");
                System.out.println("Item keys: " + item.keySet());

                HospitalDto dto = new HospitalDto(
                        (String) item.get("dutyName"),
                        (String) item.get("dutyAddr"),
                        (String) item.get("dutyTel1"),
                        parseDouble(item.get("latitude")),
                        parseDouble(item.get("longitude")));

                System.out.println("생성된 DTO: " + dto);
                result.add(dto);
            }

        } catch (Exception e) {
            System.out.println("❌ 에러 발생:");
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

    // =========================
    // 3️⃣ 주소 → 좌표 → 주변검색
    // =========================
    public List<HospitalDto> searchByAddress(String address) {
        try {
            WebClient kakaoClient = WebClient.builder()
                    .baseUrl("https://dapi.kakao.com")
                    .defaultHeader("Authorization", "KakaoAK " + kakaoKey)
                    .build();

            Map response = kakaoClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // 🔍 Kakao 응답 확인
            System.out.println("=== Kakao API 응답 ===");
            System.out.println(response);

            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("documents");

            if (docs == null || docs.isEmpty()) {
                System.out.println("❌ Kakao에서 주소를 찾지 못했습니다: " + address);
                return List.of();
            }

            Map<String, Object> first = docs.get(0);
            System.out.println("First document: " + first);

            double lat = Double.parseDouble((String) first.get("y"));
            double lon = Double.parseDouble((String) first.get("x"));

            System.out.println("변환된 좌표 - lat: " + lat + ", lon: " + lon);

            return nearby(lat, lon);

        } catch (Exception e) {
            System.out.println("❌ searchByAddress 에러:");
            e.printStackTrace();
            return List.of();
        }
    }

    private Double parseDouble(Object obj) {
        try {
            if (obj == null)
                return null;
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private String pickTel(Map<String, Object> item) {
        Object tel1 = item.get("dutyTel1");
        if (tel1 != null && !tel1.toString().isBlank()) {
            return tel1.toString();
        }

        Object tel3 = item.get("dutyTel3");
        if (tel3 != null && !tel3.toString().isBlank()) {
            return tel3.toString();
        }

        return null;
    }

}
