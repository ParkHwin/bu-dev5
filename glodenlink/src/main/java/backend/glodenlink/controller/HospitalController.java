package backend.glodenlink.controller;

import backend.glodenlink.HospitalService;
import backend.glodenlink.dto.HospitalDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HospitalController {

    private final HospitalService service;

    public HospitalController(HospitalService service) {
        this.service = service;
    }

    // 병원 이름 검색
    @GetMapping("/search")
    public List<HospitalDto> search(@RequestParam String name) {
        return service.searchByName(name);
    }

    // 좌표 기반 주변 병원
    @GetMapping("/nearby")
    public List<HospitalDto> nearby(@RequestParam double lat,
                                    @RequestParam double lon) {
        return service.nearby(lat, lon);
    }

    // 주소 기반 주변 병원
    @GetMapping("/address")
    public List<HospitalDto> address(@RequestParam String address) {
        //System.out.println(service.searchByAddressTest(address)); 

        for(HospitalDto dto : service.searchByAddress(address)) {
            System.out.println(dto.toString());
        }

        return service.searchByAddress(address);
    }

    // @GetMapping("/addressTest")
    // public String addressTest(@RequestParam String address) {
    //     return service.searchByAddressTest(address);
    // }
}
