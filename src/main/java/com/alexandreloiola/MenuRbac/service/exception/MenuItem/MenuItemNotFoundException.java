package com.alexandreloiola.MenuRbac.service.exception.MenuItem;

import jakarta.persistence.EntityNotFoundException;

public class MenuItemNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 1L;

    public MenuItemNotFoundException(String msg) { super(msg); }

    public MenuItemNotFoundException(String msg, Throwable cause) {
        super(msg);
        this.initCause(cause);
    }
}
