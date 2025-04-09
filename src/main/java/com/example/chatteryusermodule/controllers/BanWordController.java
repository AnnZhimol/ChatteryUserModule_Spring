package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.dto.BanWordDto;
import com.example.chatteryusermodule.dto.creation.CreateBanWordDto;
import com.example.chatteryusermodule.services.impl.BanWordServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/banWord")
public class BanWordController {
    private final BanWordServiceImpl banWordServiceImpl;

    public BanWordController(BanWordServiceImpl banWordServiceImpl) {
        this.banWordServiceImpl = banWordServiceImpl;
    }

    @GetMapping("/getBanWords")
    public ResponseEntity<List<BanWordDto>> getBanWords(@RequestParam String translationId) {
        List<BanWordDto> banWords = banWordServiceImpl.getBanWordsByTranslation(translationId);
        return banWords.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(banWords, HttpStatus.OK);
    }

    @GetMapping("/getBanWord")
    public ResponseEntity<BanWordDto> getBanWord(@RequestParam String banWordId) {
        BanWordDto banWord = banWordServiceImpl.getById(banWordId);
        return banWord == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(banWord, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBanWord(@RequestBody CreateBanWordDto banWordDto) {
        try {
            banWordServiceImpl.add(banWordDto);
            return new ResponseEntity<>("BanWord successfully add.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBanWord(@RequestBody BanWordDto banWordDto) {
        try{
            banWordServiceImpl.delete(banWordDto);
            return new ResponseEntity<>("BanWord successfully delete.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBanWords")
    public ResponseEntity<String> deleteBanWords(@RequestParam String translationId) {
        try{
            banWordServiceImpl.deleteBanWordsByTranslation(translationId);
            return new ResponseEntity<>("BanWords successfully delete.", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
