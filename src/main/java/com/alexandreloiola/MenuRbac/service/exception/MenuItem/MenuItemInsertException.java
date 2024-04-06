package com.alexandreloiola.MenuRbac.service.exception.MenuItem;

import org.springframework.dao.DataIntegrityViolationException;

public class MenuItemInsertException extends DataIntegrityViolationException {
    private static final long serialVersionUID = 1L;

    public MenuItemInsertException(String msg) { super(msg); }

    public MenuItemInsertException(String msg, Throwable cause) {super(msg, cause);}
}
