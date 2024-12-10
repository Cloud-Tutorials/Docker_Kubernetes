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
 * This Rest API just returns an object which has id, name and place.
 *
 */
@RestController
@RequestMapping("/home")
public class MyRestApi {

    @GetMapping("/info")
    public ResponseEntity<ResponseData> getInfo() {
        ResponseData responseData = new ResponseData();
        responseData.setId(1);
        responseData.setName("ENSA");
        responseData.setPlace("Marrakech");
        return new ResponseEntity<ResponseData>(responseData, HttpStatus.OK);
    }

    @Getter
    @Setter
    public class ResponseData {
        private String name;
        private Integer id;
        private String place;
    }
}
