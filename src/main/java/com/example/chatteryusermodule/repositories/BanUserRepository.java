package com.example.chatteryusermodule.repositories;

import com.example.chatteryusermodule.models.BanUser;
import com.example.chatteryusermodule.models.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BanUserRepository extends JpaRepository<BanUser, Long> {
    List<BanUser> findBanUsersByTranslation(Translation translation);
    @Transactional
    void deleteBanUsersByTranslation(Translation translation);
}
