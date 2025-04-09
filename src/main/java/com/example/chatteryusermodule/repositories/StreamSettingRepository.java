package com.example.chatteryusermodule.repositories;

import com.example.chatteryusermodule.models.StreamSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamSettingRepository extends JpaRepository<StreamSetting, Long> {
    StreamSetting findStreamSettingByTranslation_Id(Long translationId);
}
