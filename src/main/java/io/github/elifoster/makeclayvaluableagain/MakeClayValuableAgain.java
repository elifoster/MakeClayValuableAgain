package io.github.elifoster.makeclayvaluableagain;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Iterator;

import static io.github.elifoster.makeclayvaluableagain.MakeClayValuableAgain.MODID;

@Mod(modid = MODID, name = "Make Clay Valuable Again", version = "1.0.0")
public class MakeClayValuableAgain {
    static final String MODID = "makeclayvaluableagain";

    @SidedProxy(
      clientSide = "io.github.elifoster.makeclayvaluableagain.ModItems$ClientModelRegistrar",
      serverSide = "io.github.elifoster.makeclayvaluableagain.ModItems$ServerModelRegistrar",
      modId = MODID
    )
    private static ModItems.ModelRegistrar itemRegistrar;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new ModItems();
        itemRegistrar.registerModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        removeRecipe(Items.STONE_AXE);
        removeRecipe(Items.STONE_HOE);
        removeRecipe(Items.STONE_PICKAXE);
        removeRecipe(Items.STONE_SWORD);
        removeRecipe(Items.STONE_SHOVEL);

        ModItems.getInstance().recipes();
        MinecraftForge.EVENT_BUS.register(new ClayProgressionImprovements());
    }

    /**
     * Removes all recipes that produce a given item.
     * @param itemToRemove The item whose recipes are to be removed.
     */
    private static void removeRecipe(Item itemToRemove) {
        Iterator<IRecipe> iter = CraftingManager.getInstance().getRecipeList().iterator();
        while (iter.hasNext()) {
            IRecipe recipe = iter.next();
            ItemStack out = recipe.getRecipeOutput();
            if (out != ItemStack.EMPTY && out.getItem() == itemToRemove) {
                FMLLog.info("Removing recipe for " + out);
                iter.remove();
            }
        }
    }
}
