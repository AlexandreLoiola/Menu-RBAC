package com.alexandreloiola.MenuRbac.service.exception.Role;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

public class RoleNotFoundException  extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public RoleNotFoundException(String msg) { super(msg); }

    public RoleNotFoundException(String msg, Throwable cause) {
        super(msg);
        this.initCause(cause);
    }
}
