package com.alexandreloiola.MenuRbac.form;

import com.alexandreloiola.MenuRbac.dto.RoleDto;
import com.alexandreloiola.MenuRbac.model.MenuItemModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemForm {

    @NotNull(message = "The title field cannot be empty")
    @NotBlank(message = "The title field cannot be blank.")
    @Size(max = 100, message = "The title must be less than 100 characters.")
    private String title;

    @NotNull(message = "The Uri field cannot be empty")
    @NotBlank(message = "The Uri field cannot be blank.")
    @Size(max = 200, message = "The Uri must be less than 200 characters.")
    private String uri;

    @NotNull(message = "The IconUri field cannot be empty")
    @NotBlank(message = "The IconUri field cannot be blank.")
    @Size(max = 200, message = "The iconUri must be less than 200 characters.")
    private String iconUri;

    private String nextMenuItemTitle;

    @Valid
    private Set<RoleForm> roles = new HashSet<>();

    private String parentMenuItemTitle;

}
