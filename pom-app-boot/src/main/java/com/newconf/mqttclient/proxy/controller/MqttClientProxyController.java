package com.newconf.mqttclient.proxy.controller;

import com.newconf.mqttclient.MqttConfiguration;
import com.newconf.mqttclient.proxy.request.PublishRequest;

import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mqtt/proxy")
public class MqttClientProxyController {
    private final static String CLIENT_ID = UUID.randomUUID().toString();
    private final static Integer  QOS = 1;
    // private Integer DELAYED = 3000;


    @Resource
    private MqttConfiguration mqttConfiguration;
    //    private String BROKER_URL = "wss://202.63.172.178:8084";
    //    private String  USERNAME = "admin";
    //    private String  PASSWORD = "admin";


    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody PublishRequest publishRequest) {
//        try {
//            MqttConnectManager.init(
//                    MQTT_TOPIC,
//                    CLIENT_ID,
//                    DELAYED,
//                    new MqttConnectionProperties(BROKER_URL, null, USERNAME, PASSWORD, QOS));
//            MqttConnectManager.publish(publishRequest.getTopic(),publishRequest.getMessage());
//            MqttConnectManager.close();
//            return ResponseEntity.ok("Message published to topic: " + publishRequest.getTopic());
//        } catch (MqttException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish message: " + e.getMessage());
//        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish message: ");
    }
}

