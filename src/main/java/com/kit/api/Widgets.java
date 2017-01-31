package com.kit.api;

import com.kit.api.collection.queries.WidgetQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetGroup;
import com.kit.api.collection.queries.WidgetQuery;
import com.kit.api.wrappers.Widget;

import java.util.List;

/**
 * Methods for finding and interacting with widgets.
 *
 */
public interface Widgets {

    /**
     * Gets a widget group
     *
     * @param parent - the id of the group
     * @return widget group
     */
    WidgetGroup getGroup(int parent);

    /**
     * Gets an array of all the NON-NULL widget groups in the client.
     *
     * @return widget groups
     */
    List<WidgetGroup> getGroups();

//    /**
//     * Gets an array of all the NON-NULL widgets in the client
//     *
//     * @return widgets
//     */
    //Disabled cause of memory issues
//    List<Widget> getAll();

//    /**
//     * Creates a widget query to use widgets
//     *
//     * @return query
//     */
    //Disabled cause of memory issues
//    WidgetQuery find();

    /**
     * Creates a widget query based on the two most basic
     * attribute of a widget, the parent id and the child id.
     *
     * @param parent parent id
     * @param child  child id
     * @return query
     */
    Widget find(int parent, int child);

    /**
     * Checks if a widget with the continue text is valid
     *
     * @return <t>true if it finds a widget with the continue text</t> otherwise false
     */
    boolean canContinue();

}
