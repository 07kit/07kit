package com.kit.plugins.grandexchange;

import com.kit.api.event.ActionEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.Session;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.api.plugin.Option;
import com.kit.core.control.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.kit.api.plugin.Option.Type.TEXT;
import static com.kit.api.plugin.Option.Type.TOGGLE;

/**
 */
public class GrandExchangePlugin extends Plugin {
    public static final int PRICE_CHECK_OPCODE = 16001;
    private final GrandExchangeSidebarWidget widget = new GrandExchangeSidebarWidget();
    private final GrandExchangeSearchSidebarWidget searchWidget = new GrandExchangeSearchSidebarWidget();
    private final List<IGrandExchangeOffer> seenOffers = new ArrayList<>();

    @Option(type = Option.Type.TOGGLE, label = "Show notifications when an offer updates.", value = "true")
    private boolean showNotification;

    public GrandExchangePlugin(PluginManager manager) {
        super(manager);
        CompletedOfferRecorder completedOfferRecorder = new CompletedOfferRecorder();
        Session.get().getEventBus().register(completedOfferRecorder);
        new Thread(completedOfferRecorder).start();
    }

    @Override
    public String getName() {
        return "Grand Exchange";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(widget);
        ui.registerSidebarWidget(searchWidget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
        ui.deregisterSidebarWidget(searchWidget);
    }

    @Schedule(1000)
    public void pollExchange() {
        if (!isLoggedIn()) {
            logger.debug("Not logged in.");
        }

        List<IGrandExchangeOffer> validOffers = Arrays.asList(client().getGrandExchangeOffers()).stream()
                .filter(x -> x != null && x.getItemId() > 0)
                .collect(Collectors.toList());

        // Remove the old offers
        List<IGrandExchangeOffer> disappearedOffers = seenOffers.stream()
                .filter(x -> !validOffers.contains(x))
                .collect(Collectors.toList());
        disappearedOffers.forEach(x -> {
            widget.removeGrandExchangeOffer(x);
            seenOffers.remove(x);
        });

        // Add the new offers
        List<IGrandExchangeOffer> appearedOffers = validOffers.stream()
                .filter(x -> !seenOffers.contains(x))
                .collect(Collectors.toList());
        appearedOffers.forEach(x -> {
            widget.addGrandExchangeOffer(x);
            seenOffers.add(x);
        });
    }

    @EventHandler
    public void onMenuClick(ActionEvent event) {

    }
}
