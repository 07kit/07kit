package com.kit.gui.controller;

import com.kit.Application;
import com.kit.game.engine.IClient;
import com.kit.gui.Controller;
import com.kit.gui.view.SettingsDebugView;
import com.kit.Application;
import com.kit.game.engine.IClient;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.view.SettingsDebugView;
import com.kit.game.engine.IClient;
import com.kit.gui.Controller;
import com.kit.gui.view.SettingsDebugView;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Date: 05/11/13
 * Time: 09:31
 *
 * @author Matt Collinge
 */
public class SettingsDebugController extends Controller<SettingsDebugView> {

    private SettingsDebugView view;
    private IClient client;

    public SettingsDebugController() {
        ControllerManager.add(SettingsDebugController.class, this);
    }

    public void show(IClient client) {
        this.client = client;
        getComponent().setModal(false);
        refreshList();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setIconImage(Application.ICON_IMAGE);
        getComponent().setVisible(true);
    }

    public void hide() {
        client = null;
        getComponent().dispose();
    }

    @Override
    public SettingsDebugView getComponent() {
        if (view == null) {
            view = new SettingsDebugView(this);
        }
        return view;
    }

    private void updateTable(int index) {
        // TODO: Check if we're using the right settings array
        if (index >= 0 && client.getGameSettings().length > index) {
            int settingValue = client.getGameSettings()[index];
            Object[][] data = new Object[][]{
                    {"Decimal", settingValue},
                    {"Hex", Integer.toHexString(settingValue)},
                    {"Binary", Integer.toBinaryString(settingValue)}
            };
            getComponent().getSettingsInfoTable().setModel(new DefaultTableModel(data, new String[]{"Data", "Value"}));
        }
    }

    private void refreshList() {
        int selectionIndex = getComponent().getSettingsList().getSelectedIndex(); // preserve selection
        int[] settingsData = client.getGameSettings();
        //List<String> dataList = new ArrayList<>();
        String[] data = new String[settingsData.length];
        for (int i = 0; i < settingsData.length; i++) {
            /*if (settingsData[i] > 0) {
                dataList.add(String.format("Setting %s: %s", i, settingsData[i]));
            }*/
            data[i] = String.format("Setting %s: %s", i, settingsData[i]);
        }
        getComponent().getSettingsList().setListData(data);
        //getComponent().getSettingsList().setListData(dataList.toArray(new String[dataList.size()]));
        getComponent().getSettingsList().setSelectedIndex(selectionIndex);
    }

    public void onListSelectionChanged() {
        updateTable(getComponent().getSettingsList().getSelectedIndex());
    }

    public void addSettingChange(int index, int oldValue) {
        refreshList();
        if (getComponent().getSettingsList().getSelectedIndex() == index) {
            updateTable(index);
        }
        getComponent().getSettingsLogArea().append(String.format("Setting %s: %s -> %s\n", index, oldValue, client.getGameSettings()[index]));
        getComponent().getSettingsLogArea().setCaretPosition(getComponent().getSettingsLogArea().getText().length());
    }

}
