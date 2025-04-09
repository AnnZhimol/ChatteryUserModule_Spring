package com.example.chatteryusermodule.services.impl;

import com.example.chatteryusermodule.dto.BanUserDto;
import com.example.chatteryusermodule.dto.creation.CreateBanUserDto;

import java.util.List;

public interface BanUserServiceImpl {
    List<BanUserDto> getBanUsersByTranslation(String translationId);
    void delete(BanUserDto entity);
    void add(CreateBanUserDto entity);
    BanUserDto getById(String id);
    void deleteBanUsersByTranslation(String translationId);
}
