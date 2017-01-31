package com.kit.api.impl.tabs;

import com.kit.api.DepositBox;
import com.kit.api.collection.queries.BankQuery;
import com.kit.api.DepositBox;
import com.kit.api.collection.queries.BankQuery;
import com.kit.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author : const_
 */

//TODO: Complete this
public class DepositBoxImpl implements DepositBox {
    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public List<WidgetItem> getAll() {
        return null;
    }

    @Override
    public boolean contains(int... ids) {
        return false;
    }

    @Override
    public boolean containsAll(int... ids) {
        return false;
    }

    @Override
    public BankQuery find(int... ids) {
        return null;
    }

    @Override
    public BankQuery find(String... names) {
        return null;
    }

    @Override
    public int count(boolean stacks, int... ids) {
        return 0;
    }

    @Override
    public BankQuery find() {
        return null;
    }

    @Override
    public boolean contains(String... names) {
        return false;
    }

    @Override
    public boolean containsAll(String... names) {
        return false;
    }

    @Override
    public int count(boolean stacks, String... names) {
        return 0;
    }

}
