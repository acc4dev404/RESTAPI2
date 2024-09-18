package com.example.RESTAPI.controller;

import com.example.RESTAPI.model.Message;

import com.example.RESTAPI.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Valeev A.R.
 */

@RestController
public class MainController {

    private long setOffsetTime(int minOffsetTime, int maxOffsetTime) throws InterruptedException {
        long start = System.currentTimeMillis();
        int randomTime = (int) ((Math.random() * (maxOffsetTime - minOffsetTime)) + minOffsetTime);
        Thread.sleep(randomTime);
        return System.currentTimeMillis() - start;
    }

    @GetMapping(path="/")
    @ResponseBody
    public ResponseEntity<?> getUserByLogin(@RequestParam String login) throws InterruptedException {
        long offsetTime = setOffsetTime(1000,2000);
        try {
            DBController db_connect = new DBController("jdbc:postgresql://192.168.1.45:5432/loadDB", "loaderDB", "ReSo999+");
            User user = db_connect.getUser(login);
            if (user == null) {
                throw new Exception("Login not found");
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.err.format("ERROR: %s\n", e.getMessage());
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/")
    @ResponseBody
    public ResponseEntity<?> postUsers(@RequestBody Map<String, String> body) throws InterruptedException {
        long offsetTime = setOffsetTime(1000,2000);
        try {
            if (
                    body.size() == 3 &&
                            body.get("login") != null && body.get("password") != null && body.get("email") != null &&
                            !body.get("login").trim().isEmpty() && !body.get("password").trim().isEmpty() && !body.get("email").trim().isEmpty()
            )
            {
                DBController db_connect = new DBController("jdbc:postgresql://192.168.1.45:5432/loadDB", "loaderDB", "ReSo999+");
                if (db_connect.createUser(new User(body.get("login"), body.get("password"), body.get("email"))) == 0) {
                    throw new Exception("Login already exists or the user data is incorrect");
                }
                return new ResponseEntity<>(new Message("User created") , HttpStatus.OK);
            }
            else {
                throw new Exception("JSON is incorrect");
            }
        }
        catch (Exception e) {
            System.err.format("ERROR: %s\n", e.getMessage());
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}