package com.kit.gui.controller;


import com.kit.Application;
import com.kit.api.impl.game.WidgetsImpl;
import com.kit.gui.Controller;
import com.kit.gui.component.WidgetTreeModel;
import com.kit.gui.view.WidgetDebugView;
import com.kit.Application;
import com.kit.api.MethodContext;
import com.kit.api.debug.impl.WidgetDebug;
import com.kit.api.impl.game.WidgetsImpl;
import com.kit.api.wrappers.Widget;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.WidgetTreeModel;
import com.kit.gui.view.WidgetDebugView;
import com.kit.api.MethodContext;
import com.kit.api.debug.impl.WidgetDebug;
import com.kit.api.impl.game.WidgetsImpl;
import com.kit.api.wrappers.Widget;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.component.WidgetTreeModel;
import com.kit.gui.view.WidgetDebugView;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * User: Cov
 * Date: 02/11/13
 * Time: 10:38
 */
public class WidgetDebugController extends Controller<WidgetDebugView> {

    private WidgetDebugView view;
    private WidgetDebug debug;

    public WidgetDebugController() {
        ControllerManager.add(WidgetDebugController.class, this);
    }

    @Override
    public WidgetDebugView getComponent() {
        if (view == null) {
            view = new WidgetDebugView(this);
        }
        return view;
    }

    public void show(WidgetDebug debug) {
        this.debug = debug;
        getComponent().setModal(false);
        MethodContext methodContext = new MethodContext();
        methodContext.widgets = new WidgetsImpl(methodContext);
        WidgetTreeModel model = new WidgetTreeModel(methodContext);
        getComponent().getWidgetTree().setModel(model);
        onRefresh();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setIconImage(Application.ICON_IMAGE);
        getComponent().setVisible(true);
    }

    public void hide() {
        debug.setSelectedWidget(null);
        debug.setEnabled(false);
        getComponent().dispose();
    }

    public void onRefresh() {
        ((WidgetTreeModel) getComponent().getWidgetTree().getModel()).refresh();
    }

    public void onSearch() {
        ((WidgetTreeModel) getComponent().getWidgetTree().getModel()).search(view.getSearchBox().getText());
    }

    public void onWidgetSelected() {
        Object selectedComponent = getComponent().getWidgetTree().getLastSelectedPathComponent();
        if (selectedComponent instanceof WidgetTreeModel.WidgetTreeWrapper) {
            Widget selectedWidget = ((WidgetTreeModel.WidgetTreeWrapper) selectedComponent).getWidget();
            debug.setSelectedWidget(selectedWidget);

            StringBuilder actionStringBuilder = new StringBuilder();
            if (selectedWidget.getActions() != null) {
                for (String action : selectedWidget.getActions()) {
                    actionStringBuilder.append((action != null ? action : "null") + ",\n");
                }
            }

            StringBuilder invBuilder = new StringBuilder();
            if (selectedWidget.getInventory() != null) {
                for (int id : selectedWidget.getInventory()) {
                    invBuilder.append(id).append(",");
                }
            }

            StringBuilder invStackBuilder = new StringBuilder();
            if (selectedWidget.getInventoryStackSizes() != null) {
                for (int id : selectedWidget.getInventoryStackSizes()) {
                    invBuilder.append(id).append(",");
                }
            }
            // TODO - Improve this to stop errors
            Object[][] data = new Object[][]{
                    {"X", selectedWidget.getX()},
                    {"Y", selectedWidget.getY()},
                    {"Click Point", selectedWidget.getClickPoint().toString()},
                    {"Base Point", selectedWidget.getBasePoint().toString()},
                    {"Width", selectedWidget.getWidth()},
                    {"Height", selectedWidget.getHeight()},
                    {"Text", selectedWidget.getText() != null ? selectedWidget.getText() : "null"},
                    {"Actions", actionStringBuilder.toString()},
                    {"Item Id", selectedWidget.getItemId()},
                    {"Item Stack", selectedWidget.getItemStackSize()},
                    {"Texture Id", selectedWidget.getSpriteId()},
                    {"Model Id", selectedWidget.getModelId()},
                    {"Type", selectedWidget.getType()},
                    {"Valid", selectedWidget.isValid()},
                    {"Inventory", invBuilder.toString()},
                    {"Stack size(s)", invStackBuilder.toString()}
            };

            DefaultTableModel newInfoModel = new DefaultTableModel(data, new String[]{"Data", "Value"});
            getComponent().getInfoTable().setModel(newInfoModel);
        }
    }

}
