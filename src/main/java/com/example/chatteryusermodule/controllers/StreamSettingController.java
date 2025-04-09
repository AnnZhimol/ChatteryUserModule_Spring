package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.dto.StreamSettingDto;
import com.example.chatteryusermodule.models.StreamSetting;
import com.example.chatteryusermodule.services.impl.StreamSettingServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/setting")
public class StreamSettingController {
    private final StreamSettingServiceImpl streamSettingServiceImpl;

    public StreamSettingController(StreamSettingServiceImpl streamSettingServiceImpl) {
        this.streamSettingServiceImpl = streamSettingServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addStreamSetting(@RequestBody StreamSetting streamSetting) {
        try {
            streamSettingServiceImpl.add(streamSetting);
            return new ResponseEntity<>("StreamSetting successfully add.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editStreamSetting(@RequestBody StreamSettingDto streamSettingDto) {
        try {
            streamSettingServiceImpl.edit(streamSettingDto);
            return new ResponseEntity<>("StreamSetting successfully edit.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getStreamSettingByTranslation")
    public ResponseEntity<StreamSettingDto> getStreamSettingByTranslation(@RequestParam String translationId) {
        StreamSettingDto streamSetting = streamSettingServiceImpl.getByTranslationId(translationId);
        return streamSetting == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(streamSetting, HttpStatus.OK);
    }

    @GetMapping("/getStreamSetting")
    public ResponseEntity<StreamSettingDto> getStreamSetting(@RequestParam String streamSettingId) {
        StreamSettingDto streamSetting = streamSettingServiceImpl.getById(streamSettingId);
        return streamSetting == null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(streamSetting, HttpStatus.OK);
    }
}
