package com.example.chatteryusermodule.services.base;

import com.example.chatteryusermodule.models.User;
import com.example.chatteryusermodule.models.UserVerification;
import com.example.chatteryusermodule.models.enums.StateVerification;
import com.example.chatteryusermodule.repositories.UserVerificationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserVerificationService {
    private final UserVerificationRepository userVerificationRepository;
    private final Logger log = LoggerFactory.getLogger(UserVerificationService.class);
    private final Random random;

    @Autowired
    public UserVerificationService(UserVerificationRepository userVerificationRepository) {
        this.userVerificationRepository = userVerificationRepository;
        this.random = new Random();
    }

    private String generateUniqueHash() {
        int length = 6;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        String hash = sb.toString();
        log.info("Random 6-digit code was created: " + hash);
        return hash;
    }

    @Transactional
    public void createUserVerificationForUser(User user) {
        if (user == null) {
            log.error("Can't create UserVerification. User = null.");
            throw new NullPointerException();
        }

        LocalDateTime moment = LocalDateTime.now();

        UserVerification userVerification = UserVerification.builder()
                .state(StateVerification.ACTIVE)
                .user(user)
                .createDate(moment)
                .hash(generateUniqueHash())
                .deadline(moment.plusMinutes(3))
                .build();

        user.setUserVerification(userVerification);
        userVerificationRepository.save(userVerification);

        log.info("UserVerification with hash = {} was created", userVerification.getHash());
    }

    public void expiredUserVerificationForUser(Long userVerificationHash) {
        UserVerification userVerification = userVerificationRepository.findById(userVerificationHash).orElse(null);

        if (userVerification == null) {
            log.error("Can't deactivate userVerification with hash = {}. UserVerification = null.", userVerificationHash);
            throw new NullPointerException();
        }

        User user = userVerification.getUser();

        userVerification.setState(StateVerification.EXPIRED);
        user.setUserVerification(null);

        userVerificationRepository.save(userVerification);
        log.info("UserVerification with hash = {} was expired.", userVerificationHash);
    }
}
