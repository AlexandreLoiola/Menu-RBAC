package com.alexandreloiola.MenuRbac.controller;

import com.alexandreloiola.MenuRbac.dto.MenuItemDto;
import com.alexandreloiola.MenuRbac.form.MenuItemForm;
import com.alexandreloiola.MenuRbac.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/menuItems")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<Set<MenuItemDto>> getAllMenuItem() {
        Set<MenuItemDto> menuItemDto = menuItemService.getAllMenuItem();
        return ResponseEntity.ok().body(menuItemDto);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Set<MenuItemDto>> getMenuItemsByRole(@PathVariable String role) {
        Set<MenuItemDto> menuItemDto = menuItemService.getMenuItemsByRole(role);
        return ResponseEntity.ok().body(menuItemDto);
    }

    @GetMapping("/{title}")
    public ResponseEntity<MenuItemDto> getMenuItemByTitle(
            @PathVariable("title") String title
    ) {
        MenuItemDto menuItemDto = menuItemService.getMenuItemByTitle(title);
        return ResponseEntity.ok().body(menuItemDto);
    }

    @PostMapping
    public ResponseEntity<MenuItemDto> insertMenuItem(
            @Valid @RequestBody MenuItemForm menuItemForm
    ) {
        MenuItemDto menuItemDto = menuItemService.insertMenuItem(menuItemForm);
        return ResponseEntity.ok().body(menuItemDto);
    }

    @PutMapping("/{title}")
    public ResponseEntity<MenuItemDto> updateMenuItem(
            @PathVariable("title") String title,
            @Valid @RequestBody MenuItemForm menuItemForm
    ) {
        MenuItemDto menuItemDto = menuItemService.updateMenuItem(title, menuItemForm);
        return ResponseEntity.ok().body(menuItemDto);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<MenuItemDto> deleteMenuItem(
            @PathVariable("title") String title
    ) {
        menuItemService.deleteMenuItem(title);
        return ResponseEntity.noContent().build();
    }
}
