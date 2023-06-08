package dev.lauchy.dragonsimulatorextras.pagination.menus;

import dev.lauchy.dragonsimulatorextras.Main;
import dev.lauchy.dragonsimulatorextras.Utils;
import dev.lauchy.dragonsimulatorextras.pagination.PaginatedMenu;
import dev.lauchy.dragonsimulatorextras.pagination.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerMenu extends PaginatedMenu {
    public PlayerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Utils.c("&7All Online Players");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        switch (e.getCurrentItem().getType()){
            case SKULL:

                switch (e.getCurrentItem().getType()){
                    case ANVIL:
                        PlayerMenuUtility playerMenuUtility = Main.getPlayerMenuUtility((Player) e.getWhoClicked());
                        playerMenuUtility.setSelected(Bukkit.getPlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(1))));

                        new PlayerSubMenu(playerMenuUtility).open();

                        break;

                }

        }


    }

    @Override
    public void setMenuItems() {

        ArrayList<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        if (players != null && !players.isEmpty()){
            for(int i = 0; i < super.maxItemsPerPage; i++){
                index = super.maxItemsPerPage * page + i;
                if (index >= players.size()) break;
                if (players.get(index) != null) {

                    ItemStack playerItem = new ItemStack(Material.SKULL, 1);
                    ItemMeta playerMeta = playerItem.getItemMeta();
                    playerMeta.setDisplayName(Utils.c("&e" + players.get(index).getDisplayName()));
                    UUID uuid = players.get(index).getUniqueId();
                    List<String> lore = Collections.singletonList(Utils.c("&8uuid.toString()"));
                    playerMeta.setLore(lore);

                    playerItem.setItemMeta(playerMeta);
                    inventory.addItem(playerItem);


                }
            }
        }

    }
}
