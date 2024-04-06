package com.alexandreloiola.MenuRbac.service;

import com.alexandreloiola.MenuRbac.dto.MenuItemDto;
import com.alexandreloiola.MenuRbac.form.MenuItemForm;
import com.alexandreloiola.MenuRbac.form.RoleForm;
import com.alexandreloiola.MenuRbac.model.MenuItemModel;
import com.alexandreloiola.MenuRbac.model.RoleModel;
import com.alexandreloiola.MenuRbac.repository.MenuItemRepository;
import com.alexandreloiola.MenuRbac.service.exception.MenuItem.MenuItemInsertException;
import com.alexandreloiola.MenuRbac.service.exception.MenuItem.MenuItemNotFoundException;
import com.alexandreloiola.MenuRbac.service.exception.MenuItem.MenuItemUpdateException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    @Autowired
    private ModelMapper modelMapper;

    private final MenuItemRepository menuItemRepository;

    private final RoleService roleService;

    public MenuItemService(MenuItemRepository menuItemRepository, RoleService roleService) {
        this.menuItemRepository = menuItemRepository;
        this.roleService = roleService;
    }

    public MenuItemModel findMenuItemModelByTitle(String title) {
       MenuItemModel menuItemModel = menuItemRepository.findByTitleAndIsActiveTrue(title)
                .orElseThrow(() -> new MenuItemNotFoundException(
                        String.format("The Menu Item ‘%s’ was not found", title)
                ));
        Set<Object[]> results = menuItemRepository.findMenuItemWithRoles(title);
        for (Object[] result : results) {
            String roleDescription = (String) result[1];
            RoleModel roleModel = roleService.findRoleModelByDescription(roleDescription);
            menuItemModel.getRoles().add(roleModel);
        }
        return menuItemModel;
    }

    public MenuItemDto getMenuItemByTitle(String title) {
        MenuItemModel menuItemModel = findMenuItemModelByTitle(title);
        return modelMapper.map(menuItemModel, MenuItemDto.class);
    }

    public Set<MenuItemDto> getAllMenuItem() {
        Set<MenuItemModel> menuItemModels = menuItemRepository.findByIsActiveTrue();
        if (menuItemModels.isEmpty()) {
            throw new MenuItemNotFoundException("No active menu item was found");
        }
        return menuItemModels.stream()
                .map(menuItemModel -> modelMapper.map(menuItemModel, MenuItemDto.class))
                .collect(Collectors.toSet());
    }

    public Set<MenuItemDto> getMenuItemsByRole(String roleDescription) {
        RoleModel role = roleService.findRoleModelByDescription(roleDescription);
        return role.getMenuItems().stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDto.class))
                .collect(Collectors.toSet());
    }

    @Transactional
    public MenuItemDto insertMenuItem(MenuItemForm menuItemForm) {
        MenuItemModel menuItemModel = modelMapper.map(menuItemForm, MenuItemModel.class);
        if (menuItemRepository.findByTitleAndIsActiveTrue(menuItemForm.getTitle()).isPresent()) {
            throw new MenuItemInsertException(
                    String.format("The menu item ‘%s’ is already registered", menuItemForm.getTitle())
            );
        }
        MenuItemModel parentMenuItem = null;
        if (menuItemForm.getParentMenuItemTitle() != null) {
            parentMenuItem = findMenuItemModelByTitle(menuItemForm.getParentMenuItemTitle());
        }
        Set<RoleModel> roleModels = new HashSet<>();
        for (RoleForm roleForm : menuItemForm.getRoles()) {
            var roleModel = roleService.findRoleModelByDescription(roleForm.getDescription());
            roleModels.add(roleModel);
        }
        try {
            menuItemModel.setRoles(roleModels);
            Date date = new Date();
            menuItemModel.setCreatedAt(date);
            menuItemModel.setUpdatedAt(date);
            menuItemModel.setIsActive(true);
            menuItemModel.setVersion(1);
            menuItemModel.setParentMenuItemModelId(parentMenuItem);
            menuItemRepository.save(menuItemModel);
            return modelMapper.map(menuItemModel, MenuItemDto.class);
        } catch (MenuItemInsertException err) {
            throw new MenuItemInsertException(String.format("Failed to register the menu item ‘%s’. Check if the data is correct", menuItemForm.getTitle()));
        }
    }

    @Transactional
    public MenuItemDto updateMenuItem(String title, MenuItemForm menuItemForm) {
        MenuItemModel menuItemModel = findMenuItemModelByTitle(title);
        MenuItemModel parentMenuItem = null;
        if (menuItemForm.getParentMenuItemTitle() != null) {
            parentMenuItem = findMenuItemModelByTitle(menuItemForm.getParentMenuItemTitle());
        }
        MenuItemModel nextMenuItem = null;
        if (menuItemForm.getNextMenuItemTitle() != null) {
            nextMenuItem = findMenuItemModelByTitle(menuItemForm.getNextMenuItemTitle());
        }
        menuItemModel.getRoles().clear();
        menuItemRepository.deleteMenuItemRoles(menuItemModel.getId());
        Set<RoleModel> roleModels = new HashSet<>();
        for (RoleForm roleForm : menuItemForm.getRoles()) {
            var roleModel = roleService.findRoleModelByDescription(roleForm.getDescription());
            roleModels.add(roleModel);
        }
        try {
            menuItemModel.setTitle(menuItemForm.getTitle());
            menuItemModel.setUri(menuItemForm.getUri());
            menuItemModel.setIconUri(menuItemForm.getIconUri());
            menuItemModel.setUpdatedAt(new Date());
            menuItemModel.setParentMenuItemModelId(parentMenuItem);
            menuItemModel.setNextMenuItem(nextMenuItem);
            menuItemModel.setRoles(roleModels);
            menuItemRepository.save(menuItemModel);
            return modelMapper.map(menuItemModel, MenuItemDto.class);
        } catch (MenuItemUpdateException err) {
            throw new MenuItemUpdateException(String.format("Failed to update the menu item ‘%s’. Check if the data is correct", title));
        }
    }

    @Transactional
    public void deleteMenuItem(String title) {
        try {
            MenuItemModel menuItemModel = findMenuItemModelByTitle(title);
            menuItemModel.setIsActive(false);
            menuItemModel.setUpdatedAt(new Date());
            menuItemRepository.save(menuItemModel);
        } catch (MenuItemUpdateException err) {
            throw new MenuItemUpdateException(String.format("Failed to update the menu item ‘%s’. Check if the data is correct", title));
        }
    }
}

