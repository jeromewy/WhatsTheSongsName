package de.jerome.whatsthesongsname.spigot.manager;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import de.jerome.whatsthesongsname.spigot.WITSNMain;
import de.jerome.whatsthesongsname.spigot.object.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static final ConfigManager configManager = WITSNMain.getInstance().getConfigManager();

    // Must not be final, otherwise the title cannot be changed during a reload.
    private Inventory choseInventory;

    public InventoryManager() {
        reload();
    }

    public void reload() {
        choseInventory = Bukkit.createInventory(null, 9, configManager.getMessage(Messages.INVENTORY_TITLE));
        addItems();
    }

    private void addItems() {
        choseInventory.setItem(0, getItem(Material.GRAY_STAINED_GLASS_PANE));
        choseInventory.setItem(1, getItem(Material.MUSIC_DISC_CHIRP));
        choseInventory.setItem(2, getItem(Material.GRAY_STAINED_GLASS_PANE));
        choseInventory.setItem(3, getItem(Material.MUSIC_DISC_CHIRP));
        choseInventory.setItem(4, getItem(Material.GRAY_STAINED_GLASS_PANE));
        choseInventory.setItem(5, getItem(Material.MUSIC_DISC_CHIRP));
        choseInventory.setItem(6, getItem(Material.GRAY_STAINED_GLASS_PANE));
        choseInventory.setItem(7, getItem(Material.MUSIC_DISC_CHIRP));
        choseInventory.setItem(8, getItem(Material.GRAY_STAINED_GLASS_PANE));
    }

    public void updateChoseItems(Song chose1, Song chose2, Song chose3, Song chose4) {
        updateChoseItem(1, chose1);
        updateChoseItem(3, chose2);
        updateChoseItem(5, chose3);
        updateChoseItem(7, chose4);
    }

    private void updateChoseItem(int slot, Song song) {
        ItemStack itemStack = choseInventory.getItem(slot);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.getByChar(configManager.getMessage(Messages.INVENTORY_SONG_ITEMS_DISPLAYNAME_COLOR)) + song.getTitle());

        List<String> lore = new ArrayList<>();
        for (String loreEntry : WITSNMain.getInstance().getConfigManager().getInventorySongItemsLore()) {
            lore.add(loreEntry.replaceAll("\\{songTitle}", song.getTitle())
                    .replaceAll("\\{songAuthor}", song.getAuthor()));
        }
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
    }

    private ItemStack getItem(Material material) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.setLore(new ArrayList<>());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public @NotNull Inventory getChoseInventory() {
        return choseInventory;
    }
}
