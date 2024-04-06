package com.alexandreloiola.MenuRbac.service.exception.MenuItem;

import org.springframework.dao.DataIntegrityViolationException;

public class MenuItemUpdateException extends DataIntegrityViolationException {
    private static final long serialVersionUID = 1L;

    public MenuItemUpdateException(String msg) {
        super(msg);
    }

    public MenuItemUpdateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
