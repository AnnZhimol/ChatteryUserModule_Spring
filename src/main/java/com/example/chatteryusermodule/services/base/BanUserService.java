package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.dto.BanUserDto;
import com.example.chatteryusermodule.dto.TranslationDto;
import com.example.chatteryusermodule.dto.creation.CreateBanUserDto;
import com.example.chatteryusermodule.models.BanUser;
import com.example.chatteryusermodule.repositories.BanUserRepository;
import com.example.chatteryusermodule.services.impl.BanUserServiceImpl;
import com.example.chatteryusermodule.services.impl.TranslationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.chatteryusermodule.utils.CipherUtil.decryptId;
import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;

@Service
public class BanUserService implements BanUserServiceImpl {
    private final BanUserRepository banUserRepository;
    private final TranslationServiceImpl translationServiceImpl;
    private final Logger log = LoggerFactory.getLogger(BanUserService.class);

    public BanUserService(BanUserRepository banUserRepository, TranslationServiceImpl translationServiceImpl) {
        this.banUserRepository = banUserRepository;
        this.translationServiceImpl = translationServiceImpl;
    }

    private BanUser getBanUserFromDto(BanUserDto banUserDto) {
        return BanUser.builder()
                .id(decryptId(banUserDto.getId()))
                .username(banUserDto.getUsername())
                .build();
    }

    private BanUserDto getBanUserDtoFromOrigin(BanUser banUser) {
        return BanUserDto.builder()
                .id(encryptId(banUser.getId()))
                .username(banUser.getUsername())
                .build();
    }

    private BanUser getBanUserFromCreateDto(CreateBanUserDto banUserDto) {
        return BanUser.builder()
                .username(banUserDto.getUsername())
                .translation(translationServiceImpl.getTranslationFromDto(
                        translationServiceImpl.getById(
                                banUserDto.getTranslationId()
                        )
                ))
                .build();
    }

    @Override
    public List<BanUserDto> getBanUsersByTranslation(String translationId) {
        TranslationDto translationDto = translationServiceImpl.getById(translationId);

        if (translationDto == null) {
            log.error("Search Translation with id = {} failed", decryptId(translationId));
            throw new NullPointerException();
        }

        return banUserRepository.findBanUsersByTranslation(
                translationServiceImpl.getTranslationFromDto(translationDto)
        ).stream().map(this::getBanUserDtoFromOrigin).toList();
    }

    @Override
    public void delete(BanUserDto entity) {
        if (entity == null) {
            log.error("Error deleting BanUser. BanUser = null");
            throw new NullPointerException();
        }

        banUserRepository.delete(getBanUserFromDto(entity));
        log.info("BanUser with id = {} successfully delete", decryptId(entity.getId()));
    }

    @Override
    public void add(CreateBanUserDto entity) {
        if (entity == null) {
            log.error("Error adding BanUser. BanUser = null");
            throw new NullPointerException();
        }

        BanUser newBannedUser = banUserRepository.save(getBanUserFromCreateDto(entity));
        log.info("BanUser with id = {} successfully added", newBannedUser.getId());
    }

    @Override
    public BanUserDto getById(String id) {
        Long longId = decryptId(id);
        BanUser foundBanUser = banUserRepository.findById(longId).orElse(null);

        if (foundBanUser != null) {
            log.info("BanUser with id = {} successfully find", longId);
            return getBanUserDtoFromOrigin(foundBanUser);
        }

        log.error("Search BanUser with id = {} failed", longId);
        return null;
    }

    @Override
    public void deleteBanUsersByTranslation(String translationId) {
        TranslationDto translationDto = translationServiceImpl.getById(translationId);

        if (translationDto == null) {
            log.error("Search Translation with id = {} failed", decryptId(translationId));
            throw new NullPointerException();
        }

        banUserRepository.deleteBanUsersByTranslation(
                translationServiceImpl.getTranslationFromDto(translationDto)
        );
    }
}
