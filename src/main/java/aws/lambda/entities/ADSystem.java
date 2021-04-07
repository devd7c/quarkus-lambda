package aws.lambda.entities;

import aws.lambda.util.BaseFull;

import javax.persistence.*;

@Entity(name = "ADSystem")
@Table( name ="ad_system", schema = "admin")
@Cacheable
public class ADSystem extends BaseFull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_id", length = 64, nullable = false)
    private Integer id;

    @Basic
    @Column(length = 150)
    private String name;
    @Basic
    @Column(length = 500)
    private String description;
    @Basic
    @Column(length = 150)
    private String address;
    @Basic
    @Column(length = 500)
    private String message;
    @Basic
    @Column(length = 54)
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
