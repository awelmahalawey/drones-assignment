package com.musala.soft.drones.api.testRouter;

import com.musala.soft.drones.api.MockApi;
import com.musala.soft.drones.model.BaseInfoResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("TestingControllerV1")
@RequestMapping("/api/v1")
public class TestingController implements MockApi {

    @Override
    public ResponseEntity<BaseInfoResource> mockTestApi(){
        BaseInfoResource baseInfoResource = new BaseInfoResource();
        baseInfoResource.setMessage("Done");
        return ResponseEntity.ok(baseInfoResource);
    }
}

