package com.alexandreloiola.MenuRbac.service;

import com.alexandreloiola.MenuRbac.dto.RoleDto;
import com.alexandreloiola.MenuRbac.form.RoleForm;
import com.alexandreloiola.MenuRbac.model.RoleModel;
import com.alexandreloiola.MenuRbac.repository.RoleRepository;
import com.alexandreloiola.MenuRbac.service.exception.Role.RoleInsertException;
import com.alexandreloiola.MenuRbac.service.exception.Role.RoleNotFoundException;
import com.alexandreloiola.MenuRbac.service.exception.Role.RoleUpdateException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private ModelMapper modelMapper;

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDto getRoleDtoByDescription(String description) {
        RoleModel roleModel = findRoleModelByDescription(description);
        return modelMapper.map(roleModel, RoleDto.class);
    }

    public RoleModel findRoleModelByDescription(String description) {
        return roleRepository.findByDescriptionAndIsActiveTrue(description)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format("The role ‘%s’ was not found", description)
                ));
    }

    public Set<RoleDto> getAllRoleDto() {
        Set<RoleModel> roleModelSet = roleRepository.findByIsActiveTrue();
        if (roleModelSet.isEmpty()) {
            throw new RoleNotFoundException("No active role was found");
        }
        return roleModelSet.stream()
                .map(roleModel -> modelMapper.map(roleModel, RoleDto.class))
                .collect(Collectors.toSet());
    }

    @Transactional
    public RoleDto insertRole(RoleForm roleForm) {
        if (roleRepository.findByDescriptionAndIsActiveTrue(roleForm.getDescription()).isPresent()) {
            throw new RoleInsertException(
                    String.format("The role ‘%s’ is already registered", roleForm.getDescription())
            );
        }
        try {
            RoleModel roleModel = modelMapper.map(roleForm, RoleModel.class);
            Date date = new Date();
            roleModel.setCreatedAt(date);
            roleModel.setUpdatedAt(date);
            roleModel.setIsActive(true);
            roleModel.setVersion(1);
            roleRepository.save(roleModel);
            return modelMapper.map(roleModel, RoleDto.class);
        } catch (RoleInsertException err) {
            throw new RoleInsertException(String.format("Failed to register the role ‘%s’. Check if the data is correct", roleForm.getDescription()));
        }
    }

    @Transactional
    public RoleDto updateRole(String description, RoleForm roleForm) {
        try {
            RoleModel roleModel = findRoleModelByDescription(description);
            roleModel.setDescription(roleForm.getDescription());
            roleModel.setUpdatedAt(new Date());
            roleRepository.save(roleModel);
            return modelMapper.map(roleModel, RoleDto.class);
        } catch (RoleUpdateException err) {
            throw new RoleUpdateException(String.format("Failed to update the role ‘%s’. Check if the data is correct", description));
        }
    }

    @Transactional
    public void deleteRole(String description) {
        try {
            RoleModel roleModel = findRoleModelByDescription(description);
            roleModel.setIsActive(false);
            roleModel.setUpdatedAt(new Date());
            roleRepository.save(roleModel);
        } catch (RoleUpdateException err) {
            throw new RoleUpdateException(String.format("Failed to update the role ‘%s’. Check if the data is correct", description));
        }
    }
}
