package com.alexandreloiola.MenuRbac.repository;

import com.alexandreloiola.MenuRbac.model.MenuItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemModel, UUID> {
    Optional<MenuItemModel> findByTitleAndIsActiveTrue(String title);

    Set<MenuItemModel> findByIsActiveTrue();

    @Query(value = "SELECT mi.title AS menu_item_title, r.description AS role_description " +
            "FROM TB_MENU_ITEM mi " +
            "JOIN TB_ROLE_MENU_ITEM rmi ON mi.id = rmi.menu_item_id " +
            "JOIN TB_ROLE r ON r.id = rmi.role_id " +
            "WHERE mi.title = :menuItemTitle", nativeQuery = true)
    Set<Object[]> findMenuItemWithRoles(@Param("menuItemTitle") String menuItemTitle);

    @Modifying
    @Query(value = "DELETE FROM TB_ROLE_MENU_ITEM WHERE MENU_ITEM_ID = :menuItemId", nativeQuery = true)
    void deleteMenuItemRoles(@Param("menuItemId") UUID menuItemId);
}
