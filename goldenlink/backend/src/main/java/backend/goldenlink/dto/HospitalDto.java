package backend.goldenlink.dto;

public class HospitalDto {

    private String hname; // 병원명
    private String haddress; // 주소
    private String htel; // 전화번호
    private Double hlat; // 위도
    private Double hlon; // 경도

    public HospitalDto() {
    }

    public HospitalDto(String hname,
            String haddress,
            String htel,
            Double hlat,
            Double hlon) {
        this.hname = hname;
        this.haddress = haddress;
        this.htel = htel;
        this.hlat = hlat;
        this.hlon = hlon;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHaddress() {
        return haddress;
    }

    public void setHaddress(String haddress) {
        this.haddress = haddress;
    }

    public String getHtel() {
        return htel;
    }

    public void setHtel(String htel) {
        this.htel = htel;
    }

    public Double getHlat() {
        return hlat;
    }

    public void setHlat(Double hlat) {
        this.hlat = hlat;
    }

    public Double getHlon() {
        return hlon;
    }

    public void setHlon(Double hlon) {
        this.hlon = hlon;
    }

    @Override
    public String toString() {
        return "HospitalDto [hname=" + hname + ", haddress=" + haddress + ", htel=" + htel + ", hlat=" + hlat
                + ", hlon=" + hlon + "]";
    }

}
