package aws.lambda.entities;

import aws.lambda.util.BaseFull;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "ADUser")
@Table( name ="ad_user", schema = "admin")
@Cacheable
public class ADUser extends BaseFull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 64, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "cognito_id", nullable = false)
    private String cognitoId;
    @Basic
    @Column(name = "username", length = 50)
    private String username;
    @Basic
    @Column(name = "date_in")
    private java.sql.Date dateIn;
    @Basic
    @Column(name = "user_in")
    private java.sql.Date userIn;
    @Basic
    @Column(name = "email", length = 54)
    private String email;
    @Basic
    @Column(name = "token")
    private String token;
    @Basic
    @Column(name = "session_ip", length = 54)
    private String sessionIp;
    @Basic
    @Column(name = "session_start")
    private java.sql.Date sessionStart;
    @Basic
    @Column(name = "session_end")
    private Date sessionEnd;
    @Basic
    private String name;
    @Basic
    private String ci;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCognitoId() {
        return cognitoId;
    }

    public void setCognitoId(String cognitoId) {
        this.cognitoId = cognitoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getUserIn() {
        return userIn;
    }

    public void setUserIn(Date userIn) {
        this.userIn = userIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionIp() {
        return sessionIp;
    }

    public void setSessionIp(String sessionIp) {
        this.sessionIp = sessionIp;
    }

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(Date sessionStart) {
        this.sessionStart = sessionStart;
    }

    public Date getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(Date sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
}
