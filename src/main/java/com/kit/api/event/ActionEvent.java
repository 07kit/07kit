package com.kit.api.event;

import com.kit.plugins.NpcMarkerOverlayPlugin;
import com.kit.plugins.NpcMarkerOverlayPlugin;
import com.kit.plugins.hiscore.HiscorePlugin;

import static com.kit.plugins.hiscore.HiscorePlugin.HISCORES_OPCODE;

public class ActionEvent {

	public enum Opcode {
		CLICK_TILE_VIEWPORT(23),
		NORMAL_CLICK(1006),
		WIELD_ITEM(34),
		USE_ITEM_FIRST(38),
		USE_ITEM_SECOND(31),
		CLICK_WIDGET(57),
		INTERACT_NPC(9),
		ATTACK_NPC(10),
		CLICK_CONTINUE(30),
		EXAMINE_OBJECT(1002),
		INTERACT_OBJECT(3),
		HISCORES_PLAYER(HiscorePlugin.HISCORES_OPCODE),
		MARK_NPC_OPCODE(NpcMarkerOverlayPlugin.MARK_NPC_OPCODE),
		UNMARK_NPC_OPCODE(NpcMarkerOverlayPlugin.UNMARK_NPC_OPCODE);

		private int opcode;

		Opcode(int opcode) {
			this.opcode = opcode;
		}

		public int getOpcode() {
			return opcode;
		}

		public static Opcode forOpcode(int opcode) {
			for (Opcode opcode_ : values()) {
				if (opcode == opcode_.opcode) {
					return opcode_;
				}
			}
			return null;
		}
	}
	private int var0;
	private int var1;
	private int rawOpcode;
	private Opcode opcode;
	private int id;
	private String interaction;
	private String entityName;

	public ActionEvent(int var0, int var1, int opcode, int id, String interaction, String entityName) {
		this.var0 = var0;
		this.var1 = var1;
		this.rawOpcode = opcode;
		this.opcode = Opcode.forOpcode(opcode);
		this.id = id;
		this.interaction = interaction;
		this.entityName = entityName;
	}

	public int getVar0() {
		return var0;
	}

	public int getVar1() {
		return var1;
	}

	public Opcode getOpcode() {
		return opcode;
	}

	public int getId() {
		return id;
	}

	public String getInteraction() {
		return interaction;
	}

	public String getEntityName() {
		return entityName;
	}

	public int getRawOpcode() {
		return rawOpcode;
	}

	@Override
	public String toString() {
		return "ActionEvent{" +
				"var0=" + var0 +
				", var1=" + var1 +
				", rawOpcode=" + rawOpcode +
				", opcode=" + opcode +
				", id=" + id +
				", interaction='" + interaction + '\'' +
				", entityName='" + entityName + '\'' +
				'}';
	}
}
