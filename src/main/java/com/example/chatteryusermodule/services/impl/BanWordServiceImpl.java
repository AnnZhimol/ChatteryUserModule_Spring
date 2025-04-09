package com.example.chatteryusermodule.services.impl;

import com.example.chatteryusermodule.dto.BanWordDto;
import com.example.chatteryusermodule.dto.creation.CreateBanWordDto;

import java.util.List;

public interface BanWordServiceImpl {
    List<BanWordDto> getBanWordsByTranslation(String translationId);
    void delete(BanWordDto entity);
    void add(CreateBanWordDto entity);
    BanWordDto getById(String id);
    void deleteBanWordsByTranslation(String translationId);
}
