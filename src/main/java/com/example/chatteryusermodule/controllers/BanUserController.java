package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.dto.BanUserDto;
import com.example.chatteryusermodule.dto.creation.CreateBanUserDto;
import com.example.chatteryusermodule.services.impl.BanUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/banUser")
public class BanUserController {
    private final BanUserServiceImpl banUserServiceImpl;

    public BanUserController(BanUserServiceImpl banUserServiceImpl) {
        this.banUserServiceImpl = banUserServiceImpl;
    }

    @GetMapping("/getBanUsers")
    public ResponseEntity<List<BanUserDto>> getBanUsers(@RequestParam String translationId) {
        List<BanUserDto> banUsers = banUserServiceImpl.getBanUsersByTranslation(translationId);
        return banUsers.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(banUsers, HttpStatus.OK);
    }

    @GetMapping("/getBanUser")
    public ResponseEntity<BanUserDto> getBanUser(@RequestParam String banUserId) {
        BanUserDto banUser = banUserServiceImpl.getById(banUserId);
        return banUser == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(banUser, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBanUser(@RequestBody CreateBanUserDto banUserDto) {
        try {
            banUserServiceImpl.add(banUserDto);
            return new ResponseEntity<>("BanUser successfully add.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBanUser(@RequestBody BanUserDto banUserDto) {
        try{
            banUserServiceImpl.delete(banUserDto);
            return new ResponseEntity<>("BanUser successfully delete.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBanUsers")
    public ResponseEntity<String> deleteBanUsers(@RequestParam String translationId) {
        try{
            banUserServiceImpl.deleteBanUsersByTranslation(translationId);
            return new ResponseEntity<>("BanUsers successfully delete.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
