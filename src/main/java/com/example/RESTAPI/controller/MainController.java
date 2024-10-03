package com.example.RESTAPI.controller;

import com.example.RESTAPI.model.Message;

import com.example.RESTAPI.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Valeev A.R.
 */

@RestController
public class MainController {

    @Autowired
    DBController db_connect;

    private void setOffsetTime(int minOffsetTime, int maxOffsetTime) throws InterruptedException {
        Thread.sleep( (int) ((Math.random() * (maxOffsetTime - minOffsetTime)) + minOffsetTime) );
    }

    @GetMapping(path="/")
    @ResponseBody
    public ResponseEntity<?> getUserByLogin(@RequestParam String login) throws InterruptedException {
        setOffsetTime(1000,2000);
        User user = null;
        try {
            user = db_connect.getUser(login);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/")
    @ResponseBody
    public ResponseEntity<?> postUsers(@RequestBody Map<String, String> body) throws InterruptedException {
        setOffsetTime(1000,2000);
        try {
            if (body.size() == 3 &&
                body.get("login") != null && body.get("password") != null && body.get("email") != null &&
                !body.get("login").trim().isEmpty() && !body.get("password").trim().isEmpty() && !body.get("email").trim().isEmpty()) {
                db_connect.createUser(new User(body.get("login"), body.get("password"), body.get("email")));
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                throw new Exception("JSON is incorrect");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}