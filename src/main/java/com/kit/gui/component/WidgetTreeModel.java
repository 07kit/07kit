package com.kit.gui.component;

import com.kit.api.MethodContext;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetGroup;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Widget;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author const_
 * @author A_C
 */
public class WidgetTreeModel implements TreeModel {

    private final Object root = new Object();
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private List<WidgetGroupTreeWrapper> groupTreeWrapperList = new ArrayList<>();
    private MethodContext context;

    public WidgetTreeModel(MethodContext context) {
        this.context = context;
    }

    public void refresh() {
        groupTreeWrapperList.clear();
        for (WidgetGroup group : context.widgets.getGroups()) {
            groupTreeWrapperList.add(new WidgetGroupTreeWrapper(group));
        }
        fireEvents(root);
    }

    public void search(String searchString) {
        groupTreeWrapperList.clear();
        for (WidgetGroup group : context.widgets.getGroups()) {
            iterator:
            for (Widget widget : group.getAll()) {
                if (widget != null && widget.getText() != null &&
                        widget.getText().toLowerCase().contains(searchString.toLowerCase())) {
                    groupTreeWrapperList.add(new WidgetGroupTreeWrapper(group));
                    for (Widget child : widget.getChildren()) {
                        if (child.getText().toLowerCase().contains(searchString.toLowerCase())) {
                            groupTreeWrapperList.add(new WidgetGroupTreeWrapper(group));
                            break iterator;
                        }
                    }
                }
            }
        }
        fireEvents(root);
    }

    private void fireEvents(Object root) {
        TreeModelEvent event = new TreeModelEvent(this, new Object[]{root});
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeStructureChanged(event);
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent.equals(root)) {
            return groupTreeWrapperList.get(index);
        }
        if (parent instanceof WidgetGroupTreeWrapper) {
            return new WidgetTreeWrapper(((WidgetGroupTreeWrapper) parent).getWidgetGroup().getAll()[index]);
        }
        if (parent instanceof WidgetTreeWrapper) {
            return new WidgetTreeWrapper(((WidgetTreeWrapper) parent).getWidget().getChild(index));
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent.equals(root)) {
            return groupTreeWrapperList.size();
        }
        if (parent instanceof WidgetGroupTreeWrapper) {
            return ((WidgetGroupTreeWrapper) parent).getWidgetGroup().getAll().length;
        }
        if (parent instanceof WidgetTreeWrapper) {
            return ((WidgetTreeWrapper) parent).getWidget().getChildren().length;
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof WidgetTreeWrapper && ((WidgetTreeWrapper) node).getWidget().getChildren().length == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }  // Tree should not be editable.

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == root) {
            return groupTreeWrapperList.indexOf(child);
        } else if (parent instanceof WidgetGroupTreeWrapper) {
            return Arrays.asList(((WidgetGroupTreeWrapper) parent).getWidgetGroup().getAll()).indexOf(((WidgetTreeWrapper) child).getWidget());
        } else if (parent instanceof WidgetTreeModel) {
            return Arrays.asList(((WidgetTreeWrapper) parent).getWidget().getChildren()).indexOf(((WidgetTreeWrapper) child).getWidget());
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        treeModelListeners.add(listener);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(treeModelListeners);
    }

    public class WidgetGroupTreeWrapper {

        private WidgetGroup widgetGroup;

        public WidgetGroupTreeWrapper(WidgetGroup widgetGroup) {
            this.widgetGroup = widgetGroup;
        }

        public WidgetGroup getWidgetGroup() {
            return widgetGroup;
        }

        @Override
        public String toString() {
            return String.format("Widget Group: %s", getWidgetGroup().getGroup());
        }
    }

    public class WidgetTreeWrapper {

        public Widget widget;

        public WidgetTreeWrapper(Widget widget) {
            this.widget = widget;
        }

        public Widget getWidget() {
            return widget;
        }

        @Override
        public String toString() {
            return String.format("Widget: %s", getWidget().getId());
        }
    }

}
