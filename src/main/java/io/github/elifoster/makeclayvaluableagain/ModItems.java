package io.github.elifoster.makeclayvaluableagain;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

import static io.github.elifoster.makeclayvaluableagain.MakeClayValuableAgain.MODID;

class ModItems {
    private static ModItems instance;

    private final Item claySword;
    private final Item clayPick;
    private final Item clayAxe;
    private final Item clayShovel;
    private final Item clayHoe;

    ModItems() {
        if (instance != null) {
            throw new IllegalAccessError("Trying to reinitialize MakeClayValuableAgain's values! Sad!");
        }
        claySword = register(new ItemSword(Item.ToolMaterial.STONE), "clay_sword");
        clayPick = register(new ClayPickaxe(), "clay_pickaxe");
        clayAxe = register(new ClayAxe(), "clay_axe");
        clayShovel = register(new ItemSpade(Item.ToolMaterial.STONE), "clay_shovel");
        clayHoe = register(new ItemHoe(Item.ToolMaterial.STONE), "clay_hoe");
        instance = this;
    }

    private Item register(Item item, String name) {
        item.setUnlocalizedName(MODID + ":" + name);
        item.setRegistryName(new ResourceLocation(MODID, name));
        item.setCreativeTab(CreativeTabs.TOOLS);
        return GameRegistry.register(item);
    }

    void recipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(claySword),
          "c",
          "c",
          "s",
          'c', Items.CLAY_BALL,
          's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(clayPick),
          "ccc",
          " s ",
          " s ",
          'c', Items.CLAY_BALL,
          's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(clayAxe),
          "cc",
          "cs",
          " s",
          'c', Items.CLAY_BALL,
          's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(clayShovel),
          "c",
          "s",
          "s",
          'c', Items.CLAY_BALL,
          's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(clayHoe),
          "cc",
          " s",
          " s",
          'c', Items.CLAY_BALL,
          's', "stickWood"));
    }

    @Nonnull
    static ModItems getInstance() {
        if (instance == null) {
            new ModItems();
        }
        return instance;
    }

    boolean isValidClayHarvester(ItemStack stack) {
        return stack != ItemStack.EMPTY && (stack.getItem() == clayPick || stack.getItem() == clayShovel);
    }

    // The constructors for the ItemPickaxe and ItemAxe classes are protected, so we have to make our own children.

    private class ClayPickaxe extends ItemPickaxe {
        private ClayPickaxe() {
            super(Item.ToolMaterial.STONE);
        }
    }

    private class ClayAxe extends ItemAxe {
        private ClayAxe() {
            super(Item.ToolMaterial.STONE);
        }
    }

    public interface ModelRegistrar {
        default void registerModels() {}
    }

    @SuppressWarnings("unused")
    public static class ServerModelRegistrar implements ModelRegistrar {
        public ServerModelRegistrar() {}
    }

    @SuppressWarnings("unused")
    public static class ClientModelRegistrar implements ModelRegistrar {
        public ClientModelRegistrar() {}
        @Override
        public void registerModels() {
            registerModel(ModItems.getInstance().clayAxe);
            registerModel(ModItems.getInstance().clayHoe);
            registerModel(ModItems.getInstance().clayPick);
            registerModel(ModItems.getInstance().clayShovel);
            registerModel(ModItems.getInstance().claySword);
        }

        private void registerModel(@Nonnull Item item) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}
