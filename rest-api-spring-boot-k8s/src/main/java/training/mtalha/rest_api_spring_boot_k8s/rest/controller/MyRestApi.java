package training.mtalha.rest_api_spring_boot_k8s.rest.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mohamed, TALHA
 * @created 07-12-2024
 *
 */
@RestController
@RequestMapping("/home")
public class MyRestApi {

    @Getter
    @Setter
    public class ResponseInfo {
        private Integer id;
        private String name;
        private String place;
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseInfo> getInfo() {
        ResponseInfo response = new ResponseInfo();
        response.setId(1);
        response.setName("ENSA");
        response.setPlace("Marrakech, MAROC");
        return new ResponseEntity<ResponseInfo>(response, HttpStatus.OK);
    }

    @Getter
    @Setter
    public class ResponseEnv {
        private String dbHost;
        private String dbName;
        private Integer dbPort;
    }

    @GetMapping("/env")
    public ResponseEntity<ResponseEnv> getEnv() {
        ResponseEnv response = new ResponseEnv();
        response.setDbHost(System.getenv("DB_HOST"));
        response.setDbName(System.getenv("DB_NAME"));
        response.setDbPort(Integer.valueOf(System.getenv("DB_PORT")));
        return new ResponseEntity<ResponseEnv>(response, HttpStatus.OK);
    }

    @Getter
    @Setter
    public class ResponseSecret {
        private String dbUser;
        private String dbPassword;
    }

    @GetMapping("/secrets")
    public ResponseEntity<ResponseSecret> getSecrets() {
        ResponseSecret response = new ResponseSecret();
        response.setDbUser(System.getenv("DB_USER"));
        response.setDbPassword(System.getenv("DB_PASSWORD"));
        return new ResponseEntity<ResponseSecret>(response, HttpStatus.OK);
    }

}
