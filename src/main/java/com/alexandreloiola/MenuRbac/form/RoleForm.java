package com.alexandreloiola.MenuRbac.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleForm {
    @NotNull(message = "The Role field cannot be empty")
    @NotBlank(message = "The Role field cannot be blank.")
    @Size(min = 3, max = 100, message = "The Role description must be between 3 and 100 characters.")
    private String description;
}
