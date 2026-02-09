package backend.glodenlink;

import backend.glodenlink.dto.HospitalDto;
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

    private final WebClient nemcClient =
            WebClient.builder()
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
            List<Map<String, Object>> itemList =
                    (List<Map<String, Object>>) items.get("item");

            for (Map<String, Object> item : itemList) {

                String name = (String) item.get("dutyName");

                if (name != null && name.contains(keyword)) {

                    HospitalDto dto = new HospitalDto(
                        (String) item.get("dutyName"),        // hname
                        (String) item.get("dutyAddr"),        // haddress
                        pickTel(item),                        // htel
                        parseDouble(item.get("wgs84Lat")),    // hlat
                        parseDouble(item.get("wgs84Lon"))     // hlon
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

            Map map = xmlMapper.readValue(xml, Map.class);
            Map body = (Map) map.get("body");
            Map items = (Map) body.get("items");
            List<Map<String, Object>> itemList =
                    (List<Map<String, Object>>) items.get("item");

            for (Map<String, Object> item : itemList) {

                HospitalDto dto = new HospitalDto(
        (String) item.get("dutyName"),          // hname
        (String) item.get("dutyAddr"),          // haddress
        (String) item.get("dutyTel1"),          // htel
        parseDouble(item.get("latitude")),      // hlat
        parseDouble(item.get("longitude"))      // hlon
);


                result.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // =========================
    // 3️⃣ 주소 → 좌표 → 주변검색
    // =========================
    public String searchByAddressTest(String address) {

        try {

            WebClient kakaoClient = WebClient.builder()
                    .baseUrl("https://dapi.kakao.com")
                    .defaultHeader("Authorization", "KakaoAK " + kakaoKey)
                    .build();
//https://dapi.kakao.com/v2/local/search/address.json?query=서울 강남구 언주로 211&key=7dd93083965269c6d73dc08000bda4a2
            String response = kakaoClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

                    System.out.println(response);

                    return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "ok";
        }
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
//https://dapi.kakao.com/v2/local/search/address.json?query=서울 강남구 언주로 211&key=7dd93083965269c6d73dc08000bda4a2
            Map response = kakaoClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> docs =
                    (List<Map<String, Object>>) response.get("documents");

            if (docs.isEmpty()) return List.of();

            Map<String, Object> first = docs.get(0);

            double lat = Double.parseDouble((String) first.get("y"));
            double lon = Double.parseDouble((String) first.get("x"));

            return nearby(lat, lon);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private Double parseDouble(Object obj) {
        try {
            if (obj == null) return null;
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
