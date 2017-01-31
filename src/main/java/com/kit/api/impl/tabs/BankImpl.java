package com.kit.api.impl.tabs;


import com.kit.api.Bank;
import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.queries.BankQuery;
import com.kit.api.Bank;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.queries.BankQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import org.apache.log4j.Logger;
import com.kit.api.Bank;
import com.kit.api.MethodContext;
import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.queries.BankQuery;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author const_
 */
public class BankImpl implements Bank {

	private static final int NEED_UP_LESS = 84;
	private static final int NEED_DOWN_GREATER = 260;
	private static final int BANK_ID = 12;
	private static final int BANK_PANE_ID = 12;
	private static final int BANK_SCROLL_ID = 11;
	private static final int WITHDRAW_X_PARENT = 548;
	private static final int WITHDRAW_X_CHILD = 122;
	private static final int TABS_ID = 10;
	private static final int TITLE_ID = 4;
	private static final int SIZE_ID = 5;
	private static final int SEARCH_ID = 23;
	private static final int DEPOSIT_INVENTORY_ID = 25;
	private static final int DEPOSIT_EQUIPMENT_ID = 27;
	private static final int BANK_PANE_AREA_ID = 0;
	private static final int[] BANK_OBJECTS = {782, 2012, 2015, 2213, 2196,
			4483, 2453, 6084, 11758, 12759, 14367, 19230, 18491, 24914, 25808,
			26972, 27663, 29085, 34752, 35647, 36786, 4483, 8981, 14382, 20607, 21301,
			24101 // Falador bank booths
	};
	private static final int SCROLL_UP_ID = 4;
	private static final int SCROLL_DOWN_ID = 5;
	private static final int BANK_ACTION_BAR = 1;
	private final MethodContext ctx;
	private static final Logger logger = Logger.getLogger(BankImpl.class);


	public BankImpl(MethodContext ctx) {
		this.ctx = ctx;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Widget getWidget() {
		return ctx.widgets.find(BANK_ID, BANK_PANE_ID);
	}

	private Map<Tab, Integer> getItemsForTabs() {
		Map<Tab, Integer> map = new HashMap<>();
		Widget widget = getTabWidget();
		int tab = 1;
		if (widget != null) {
			for (Widget widgetChild : widget.getChildren()) {
				if (widgetChild.getItemId() != -1) {
					map.put(Tab.forId(tab), widgetChild.getItemId());
					tab++;
				}
			}
		}
		return map;
	}

	private Widget getBankScrollWidget() {
		return ctx.widgets.find(BANK_ID, BANK_SCROLL_ID);
	}

	private Widget getTabWidget() {
		return ctx.widgets.find(BANK_ID, TABS_ID);
	}

	public int getSize() {
		if (isOpen()) {
			Widget widget = ctx.widgets.find(BANK_ID, SIZE_ID);
			if (widget != null && widget.getText() != null && widget.getText().trim().length() > 0) {
				return Integer.parseInt(ctx.widgets.find(BANK_ID, SIZE_ID).getText());
			}
		}
		return 475;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		return getWidget() != null;
	}

	private boolean isVisible(WidgetItem item) {
		return item.getWidget().getY() > NEED_UP_LESS &&
				item.getWidget().getY() < NEED_DOWN_GREATER;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<WidgetItem> getAll() {
		java.util.List<WidgetItem> widgets = newArrayList();
		Widget bank = getWidget();
		int i = 0;
		int max = getSize();
		for (Widget child : bank.getChildren()) {
			if (i > max) {
				break;
			}
			if (child.getItemId() > 0) {
				widgets.add(new WidgetItem(ctx, child.getItemId(),
						child.getItemStackSize(), child, child.getId(), WidgetItem.Type.BANK));
			}
			i++;
		}
		return widgets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(int... ids) {
		return find().id(ids).asList().size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(int... ids) {
		for (int id : ids) {
			if (!find(id).exists()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankQuery find(int... ids) {
		return find().id(ids);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankQuery find(String... names) {
		return find().named(names);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(boolean stacks, int... ids) {
		int count = 0;
		for (WidgetItem item : find().id(ids).asList()) {
			count += stacks ? item.getStackSize() : 1;
		}
		return count;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankQuery find() {
		return new BankQuery(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(String... names) {
		return find().named(names).asList().size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(String... names) {
		for (String name : names) {
			if (!find(name).exists()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(boolean stacks, String... names) {
		int count = 0;
		for (WidgetItem item : find().named(names).asList()) {
			count += stacks ? item.getStackSize() : 1;
		}
		return count;
	}

	public boolean canOpen(Tab tab) {
		return !isOpen(tab) && getTabCount() - 1 >= tab.getWidgetId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabCount() {
		if (!isOpen()) {
			return -1;
		}
		Widget tabs = getTabWidget();
		int count = 0;
		if (tabs != null) {
			for (Widget child : tabs.getChildren()) {
				if (child.getItemId() != -1) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public List<WidgetItem> getItemsForTab(Tab tab) {
		List<WidgetItem> list = new ArrayList<>();
		if (!isOpen() || tab == null) {
			return list;
		}
		int tabCount = getTabCount();
		if (tabCount < tab.getWidgetId()) {
			return list;
		}
		List<WidgetItem> all = getAll();

		if (tab == Tab.ALL) {
			return all;
		}

		int currTab = -1;

		Set<Integer> ids = new HashSet<>();
		Collection<Integer> startIds = getItemsForTabs().values();
		Collections.reverse(all);

		for (WidgetItem item : all) {
			if (startIds.contains(item.getId()) || ids.contains(item.getId())) {
				if (currTab == -1) {
					currTab = tabCount;
				} else {
					currTab--;
				}
			}
			ids.add(item.getId());
			if (currTab == tab.getWidgetId()) {
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tab getCurrent() {
		if (!isOpen()) {
			return null;
		}
		Widget widget = ctx.widgets.find(BANK_ID, TITLE_ID);
		if (widget != null) {
			String text = widget.getText();
			if (!text.contains("Tab")) {
				return Tab.ALL;
			}
			return Tab.forId(Integer.parseInt(text.split(" ")[1]));
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen(Tab tab) {
		return getCurrent() == tab;
	}

	@Override
	public Widget getTitleWidget() {
		return ctx.widgets.find(BANK_ID, TITLE_ID);
	}

	private int getOpenTextureID(Widget[] widgets) {
		int[] textureIDs = new int[widgets.length];
		Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
		for (int i = 0; i < widgets.length; i++) {
			textureIDs[i] = widgets[i].getSpriteId();
			if (frequency.containsKey(textureIDs[i])) {
				int freq = frequency.get(textureIDs[i]);
				frequency.remove(textureIDs[i]);
				frequency.put(textureIDs[i], freq + 1);
			} else {
				frequency.put(textureIDs[i], 1);
			}
		}
		return extractSingleton(frequency);
	}

	private <T> T extractSingleton(Map<T, Integer> items) {
		for (Map.Entry<T, Integer> entries : items.entrySet()) {
			if (entries.getValue() == 1) {
				return entries.getKey();
			}
		}
		return null;
	}

	private final StatePredicate walking = new StatePredicate() {
		@Override
		public boolean apply() {
			return !ctx.players.getLocal().isMoving();
		}
	};

	private final StatePredicate closed = new StatePredicate() {
		@Override
		public boolean apply() {
			return !isOpen();
		}
	};

	public enum TransferPolicy {

		PER_ONE, PER_FIVE, PER_10, PER_X

	}

	public enum Tab {
		ALL(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
		SIX(6), SEVEN(7), EIGHT(8), NINE(9);

		private int widgetId;

		Tab(int widgetId) {
			this.widgetId = widgetId;
		}

		public int getWidgetId() {
			return widgetId;
		}

		public static Tab forId(int id) {
			switch (id) {
				case 0:
					return ALL;
				case 1:
					return ONE;
				case 2:
					return TWO;
				case 3:
					return FOUR;
				case 4:
					return FOUR;
				case 5:
					return FIVE;
				case 6:
					return SIX;
				case 7:
					return SEVEN;
				case 8:
					return EIGHT;
				case 9:
					return NINE;
			}
			return null;
		}
	}
}
