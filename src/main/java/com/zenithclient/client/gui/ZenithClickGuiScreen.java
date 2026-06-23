package com.zenithclient.client.gui;

import com.zenithclient.client.module.ModuleCategory;
import com.zenithclient.client.module.ModuleRegistry;
import com.zenithclient.client.module.ZenithModule;
import com.zenithclient.client.safety.SafetyLock;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ZenithClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 154;
    private static final int PANEL_HEADER_HEIGHT = 24;
    private static final int ROW_HEIGHT = 24;
    private static final int PANEL_GAP = 12;
    private static final int BACKDROP = 0xE60B0F14;
    private static final int PANEL = 0xE6171B22;
    private static final int PANEL_LOCKED = 0xE621242C;
    private static final int ROW = 0xFF202630;
    private static final int ROW_HOVER = 0xFF2A3340;
    private static final int ROW_ON = 0xFF12362F;
    private static final int TEXT = 0xFFE5E7EB;
    private static final int MUTED = 0xFF9CA3AF;
    private static final int DISABLED = 0xFF6B7280;
    private static final int ACCENT = 0xFF6EE7B7;
    private static final int WARNING = 0xFFF87171;

    private final Map<ModuleCategory, PanelBounds> panels = new EnumMap<>(ModuleCategory.class);
    private final List<RowBounds> rows = new ArrayList<>();

    public ZenithClickGuiScreen() {
        super(Component.literal("Zenith Client"));
    }

    @Override
    protected void init() {
        panels.clear();
        rows.clear();

        int totalWidth = ModuleCategory.values().length * PANEL_WIDTH + (ModuleCategory.values().length - 1) * PANEL_GAP;
        int startX = Math.max(12, (width - totalWidth) / 2);
        int startY = Math.max(26, height / 5);

        int index = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            List<ZenithModule> modules = ModuleRegistry.byCategory(category);
            int x = startX + index * (PANEL_WIDTH + PANEL_GAP);
            int panelHeight = PANEL_HEADER_HEIGHT + modules.size() * ROW_HEIGHT + 10;
            panels.put(category, new PanelBounds(x, startY, PANEL_WIDTH, panelHeight));

            int rowY = startY + PANEL_HEADER_HEIGHT + 5;
            for (ZenithModule module : modules) {
                rows.add(new RowBounds(module, x + 7, rowY, PANEL_WIDTH - 14, ROW_HEIGHT - 5));
                rowY += ROW_HEIGHT;
            }

            index++;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.fill(0, 0, width, height, BACKDROP);
        graphics.drawCenteredString(font, title, width / 2, 12, TEXT);

        for (ModuleCategory category : ModuleCategory.values()) {
            renderPanel(graphics, category, mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        for (RowBounds row : rows) {
            if (row.contains(mouseX, mouseY)) {
                if (row.module.safetyLocked()) {
                    return true;
                }

                row.module.toggle();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderPanel(GuiGraphics graphics, ModuleCategory category, int mouseX, int mouseY) {
        PanelBounds panel = panels.get(category);
        if (panel == null) {
            return;
        }

        boolean locked = category == ModuleCategory.CHEATS && SafetyLock.isSafetyLockEngaged();
        graphics.fill(panel.x, panel.y, panel.x + panel.width, panel.y + panel.height, locked ? PANEL_LOCKED : PANEL);
        graphics.fill(panel.x, panel.y, panel.x + panel.width, panel.y + 2, locked ? WARNING : ACCENT);
        graphics.drawString(font, category.displayName(), panel.x + 9, panel.y + 8, locked ? DISABLED : TEXT, false);

        if (locked) {
            graphics.drawString(font, "Safety Lock", panel.x + panel.width - 72, panel.y + 8, WARNING, false);
        }

        for (RowBounds row : rows) {
            if (row.module.category() == category) {
                renderRow(graphics, row, mouseX, mouseY);
            }
        }
    }

    private void renderRow(GuiGraphics graphics, RowBounds row, int mouseX, int mouseY) {
        boolean disabled = row.module.safetyLocked();
        boolean hovered = row.contains(mouseX, mouseY) && !disabled;
        int background = row.module.enabled() && !disabled ? ROW_ON : hovered ? ROW_HOVER : ROW;
        int textColor = disabled ? DISABLED : TEXT;
        int stateColor = disabled ? DISABLED : row.module.enabled() ? ACCENT : MUTED;

        graphics.fill(row.x, row.y, row.x + row.width, row.y + row.height, background);
        graphics.drawString(font, row.module.name(), row.x + 7, row.y + 6, textColor, false);
        graphics.drawString(font, disabled ? "Locked" : row.module.enabled() ? "On" : "Off", row.x + row.width - 39, row.y + 6, stateColor, false);
    }

    private record PanelBounds(int x, int y, int width, int height) {
    }

    private record RowBounds(ZenithModule module, int x, int y, int width, int height) {
        private boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
}
