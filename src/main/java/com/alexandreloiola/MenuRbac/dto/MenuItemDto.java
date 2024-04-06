package com.alexandreloiola.MenuRbac.dto;

import com.alexandreloiola.MenuRbac.model.MenuItemModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto {
    private String title;

    private String Uri;

    private String iconUri;

    private String nextMenuItemTitle;

    private String parentMenuItemTitle;

    private Set<RoleDto> roles = new HashSet<>();

}
