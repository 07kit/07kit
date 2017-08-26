package com.kit.game;

import com.kit.Application;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.Player;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.game.engine.scene.tile.*;
import com.kit.game.exception.ClassPreloadException;
import com.kit.game.transform.Extender;
import com.kit.game.transform.impl.*;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.Hooks;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.collect.Maps.newHashMap;

/**
 * An alternative class loader that allows
 * for runtime class modification.
 */
public final class AppletClassLoader extends ClassLoader {
    private final List<Extender> extenders = new ArrayList<>();
    private final Map<String, byte[]> classes = new HashMap<>();
    private final JarFile jarFile;

    /**
     * Constructor
     *
     * @param jarFile Jar file to load classes from.
     */
    public AppletClassLoader(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    @SuppressWarnings("unchecked")
    private Class<?> defineSelf(String name, byte[] data) throws ClassFormatError {
        if (data != null && data.length > 0) {
            return defineClass(name, data, 0, data.length);
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> result = findLoadedClass(name);
        if (result != null) {
            return result;
        }
        result = defineSelf(name, classes.get(name));
        if (result != null) {
            return result;
        }
        result = AppletClassLoader.class.getClassLoader().loadClass(name);
        return result;
    }


    /**
     * Pre-loads all classes so they can be modified AOT.
     *
     * @throws ClassPreloadException when a class fails the
     *                               preloading process.
     */
    public void preload() throws ClassPreloadException {
        int revision = 120;
        while (revision < 1000) {
            try {
                final Socket socket = new Socket("oldschool1.runescape.com", 43594);

                final byte[] bytes = new byte[]{15, 0, 0, (byte) (revision >> 8), (byte) revision};
                socket.getOutputStream().write(bytes, 0, bytes.length);
                if (socket.getInputStream().read() != 6) {
                    break;
                }
                revision++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final Hooks hooks = Hooks.getHooks();
        if (hooks.revision != revision) {
            NotificationsUtil.showNotification("Error", "07Kit is currently outdated :(, check twitter for updates!");
            Application.outdated = true;
            Application.APPLET_VIEW.refresh();
            return;
        }
        Map<String, ClassDefinition> definitions = newHashMap();
        try {
            for (ClassDefinition item : hooks.classDefinitions) {
                definitions.put(item.originalName, item);
            }
            initExtenders(definitions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Problem with hooks!?", e);
        }

        Enumeration<JarEntry> entries = jarFile.entries();

        List<ClassNode> cn = new ArrayList<>();
        while (entries.hasMoreElements()) {
            JarEntry current = entries.nextElement();
            if (!current.getName().endsWith(".class")) {
                continue;
            }
            try {
                ClassNode node = new ClassNode();
                ClassReader reader = new ClassReader(jarFile.getInputStream(current));
                reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                applyExtenders(definitions, node);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                cn.add(node);
                classes.put(node.name, writer.toByteArray());
            } catch (IOException | ClassFormatError e) {
                throw new ClassPreloadException(current.getName(), e);
            }
        }
    }


    /**
     * Attempts to apply every extender to the class
     *
     * @param clazz class
     */
    private void applyExtenders(Map<String, ClassDefinition> definitions, ClassNode clazz) {
        for (Extender extender : extenders) {
            if (extender.canRun(clazz)) {
                extender.apply(definitions, clazz);
            }
        }
    }

    /**
     * Gets the jar file being loaded by the classloader.
     *
     * @return jar file
     */
    public JarFile getJarFile() {
        return jarFile;
    }

    private void initExtenders(Map<String, ClassDefinition> definitions) {
        try {
            extenders.add(new CanvasExtender());
            extenders.add(new EngineExtender());
            extenders.add(new KeyboardExtender());
            extenders.add(new ModelExtender());
            extenders.add(new GCRemoverExtender());
            extenders.add(new EventExtender(definitions));
            extenders.add(new DoActionExtender(definitions));
            extenders.add(new MessageExtender(definitions));
            extenders.add(new SwapInventoryItemsExtender(definitions));
            extenders.add(new ClientLoopExtender(definitions));
            extenders.add(new SpawnGroundItemExtender(definitions));
            extenders.add(new PlayerMenuActionsExtender(definitions));
            extenders.add(new NpcMenuActionsExtender(definitions));
            extenders.add(new MemoryLimitExtender());
//            extenders.add(new ObjectMenuActionsExtender(definitions));
            extenders.add(new GrandExchangeOfferUpdatedExtender(definitions));
            extenders.add(new PlayerRegionChangeExtender(definitions));
            extenders.add(new WrapperExtender(Npc.class, INpc.class, null));
            extenders.add(new WrapperExtender(Player.class, IPlayer.class, null));
            extenders.add(new WrapperExtender(GameObject.class, IInteractableObject.class, IGameObject.class));
            extenders.add(new WrapperExtender(GameObject.class, IFloorObject.class, IGameObject.class));
            extenders.add(new WrapperExtender(GameObject.class, IBoundaryObject.class, IGameObject.class));
            extenders.add(new WrapperExtender(GameObject.class, IWallObject.class, IGameObject.class));
            extenders.add(new GameObjectTypeExtender());
//            extenders.add(new SpawnInteractableObjectExtender(definitions));
            extenders.add(new EventBusExtender());
            extenders.add(new UIDExtender());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
