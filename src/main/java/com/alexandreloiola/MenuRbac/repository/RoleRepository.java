package com.alexandreloiola.MenuRbac.repository;

import com.alexandreloiola.MenuRbac.model.MenuItemModel;
import com.alexandreloiola.MenuRbac.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByDescriptionAndIsActiveTrue(String description);

    Set<RoleModel> findByIsActiveTrue();
}
