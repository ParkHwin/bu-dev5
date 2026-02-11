package backend.goldenlink.controller;

import backend.goldenlink.service.HospitalService;
import backend.goldenlink.dto.HospitalDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HospitalController {

    private final HospitalService service;

    public HospitalController(HospitalService service) {
        this.service = service;
    }

    // =========================
    // 0️⃣ 초기 로드: 전국 모든 병원
    // =========================
    /**
     * 초기 지도 로드 시 전국의 모든 응급의료센터를 반환
     * 
     * 사용 예:
     * GET http://localhost:8080/api/hospitals
     */
    @GetMapping("/hospitals")
    public List<HospitalDto> getAllHospitals() {
        return service.getAllHospitals();
    }

    // =========================
    // 1️⃣ 병원 이름 검색
    // =========================
    /**
     * 병원명으로 검색 (검색어 선택)
     * 
     * 사용 예:
     * GET http://localhost:8080/api/search                  → 전체 165개
     * GET http://localhost:8080/api/search?name=서울대      → 서울대 포함 병원
     * GET http://localhost:8080/api/search?name=삼성        → 삼성 포함 병원
     * GET http://localhost:8080/api/search?name=           → 전체 165개 (빈 문자열)
     */
    @GetMapping("/search")
    public List<HospitalDto> search(@RequestParam(required = false) String name) {
        return service.searchByName(name);
    }

    // =========================
    // 2️⃣ 좌표 기반 주변 병원
    // =========================
    /**
     * 좌표 기반으로 주변 병원 검색 (API가 반경 처리)
     * 
     * 사용 예:
     * GET http://localhost:8080/api/nearby?lat=37.5&lon=127.0
     * GET http://localhost:8080/api/nearby?lat=37.4882&lon=127.0756  (강남역 근처)
     * GET http://localhost:8080/api/nearby?lat=37.5665&lon=126.9780  (서울역 근처)
     */
    @GetMapping("/nearby")
    public List<HospitalDto> nearby(@RequestParam double lat,
                                    @RequestParam double lon) {
        return service.nearby(lat, lon);
    }

    // =========================
    // 3️⃣ 주소 기반 주변 병원
    // =========================
    /**
     * 주소를 좌표로 변환 후 주변 병원 검색
     * 
     * 사용 예:
     * GET http://localhost:8080/api/address?address=강남구
     * GET http://localhost:8080/api/address?address=서울시%20송파구
     * GET http://localhost:8080/api/address?address=서울시%20강남구%20역삼동
     * 
     * 💡 주소에 공백이 있으면 URL 인코딩 필요:
     *    - 공백 = %20
     *    - 예: "서울시 강남구" → "서울시%20강남구"
     *    - JavaScript: encodeURIComponent('서울시 강남구')
     */
    @GetMapping("/address")
    public List<HospitalDto> address(@RequestParam String address) {
        return service.searchByAddress(address);
    }
}