package com.db.persistence.repository;

import com.db.persistence.entity.DmnTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DmnTemplateRepository extends JpaRepository<DmnTemplate, Long> {
  Optional<DmnTemplate> findByType(String type);
}
