package com.example.chatteryusermodule.services.impl;

import com.example.chatteryusermodule.dto.StreamSettingDto;
import com.example.chatteryusermodule.models.StreamSetting;

public interface StreamSettingServiceImpl {
    void add(StreamSetting entity);
    StreamSettingDto getById(String id);
    StreamSettingDto getByTranslationId(String translationId);
    void edit(StreamSettingDto entity);
    void save(StreamSetting entity);
}
