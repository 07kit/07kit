package com.kit.api.exception;

/**
 */
public class BankNotOpenException extends IllegalArgumentException {

    public BankNotOpenException() {
        super("Can't withdraw an item if the bank is not open. Call bank.open() first.");
    }

}
