package backend.goldenlink.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class EntityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String userid;
    private String userpw;
    private String name;
    private String phone;
    private String email;
    private String role = "user";
    @Column(nullable = true)
    private String address;
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)

    public EntityUser() {
    }

    public EntityUser(Long id, String userid, String userpw, String name, String phone, String email, String address,
        String role) {
        this.id = id;
        this.userid = userid;
        this.userpw = userpw;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (role == null || role.isEmpty()) {
            this.role = "user";
        } else {
            this.role = role;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpw() {
        return userpw;
    }

    public void setUserpw(String userpw) {
        this.userpw = userpw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "EntityUser [id=" + id + ", userid=" + userid + ", userpw=" + userpw + ", name=" + name + ", phone="
                + phone + ", email=" + email + ", address=" + address + ", role=" + role + "]";
    }
}
