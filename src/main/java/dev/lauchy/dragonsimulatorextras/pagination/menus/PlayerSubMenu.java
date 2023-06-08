package dev.lauchy.dragonsimulatorextras.pagination.menus;

import dev.lauchy.dragonsimulatorextras.Main;
import dev.lauchy.dragonsimulatorextras.Utils;
import dev.lauchy.dragonsimulatorextras.pagination.PaginatedMenu;
import dev.lauchy.dragonsimulatorextras.pagination.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class PlayerSubMenu extends PaginatedMenu {
    public PlayerSubMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Utils.c("Player Name Here");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        switch (e.getCurrentItem().getType()){
            case ANVIL:

                Player target = super.playerMenuUtility.getSelected();

            case BARRIER:
                new PlayerMenu(playerMenuUtility).open();
                break

        }

    }

    @Override
    public void setMenuItems() {

    }


}
