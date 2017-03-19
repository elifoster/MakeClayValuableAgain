package io.github.elifoster.makeclayvaluableagain;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class ClayProgressionImprovements {
    @SubscribeEvent
    public void onSkeletonDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Entity sourceOfDamage = event.getSource().getSourceOfDamage();
        if (entity instanceof EntitySkeleton && sourceOfDamage instanceof EntityPlayer &&
          entity.isServerWorld()) {
            spawnWitherSkeleton(entity.getEntityWorld(), entity.getPosition());
            destroyHeldTool((EntityPlayer) sourceOfDamage);
        }
    }

    private void destroyHeldTool(EntityPlayer player) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != ItemStack.EMPTY) {
            held.damageItem(held.getMaxDamage(), player);
        }
    }

    private void spawnWitherSkeleton(World world, BlockPos toSpawnIn) {
        Entity wither = new EntityWitherSkeleton(world);
        wither.setPosition(toSpawnIn.getX(), toSpawnIn.getY() + 0.5, toSpawnIn.getZ());
        world.spawnEntity(wither);
    }

    @SubscribeEvent
    public void dropClay(LivingDropsEvent event) {
        EntityLivingBase deadEntity = event.getEntityLiving();
        World world = deadEntity.getEntityWorld();
        BlockPos position = deadEntity.getPosition();
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();
        if (deadEntity instanceof EntityWitherSkeleton) {
            if (world.rand.nextInt(10) == 5) {
                event.getDrops().add(new EntityItem(world, x, y, z, new ItemStack(Items.CLAY_BALL)));
            }
        } else if (deadEntity instanceof EntityDragon || deadEntity instanceof EntityWither) {
            event.getDrops().add(new EntityItem(world, x, y, z, new ItemStack(Items.CLAY_BALL)));
        }
    }

    @SubscribeEvent
    public void suppressClayDrops(BlockEvent.HarvestDropsEvent event) {
        List<ItemStack> drops = event.getDrops();
        if (drops.stream().noneMatch(this::isClay)) {
            return;
        }
        EntityPlayer harvester = event.getHarvester();
        if (harvester != null && ModItems.getInstance().isValidClayHarvester(harvester.getHeldItemMainhand())) {
            return;
        }
        drops.clear();
    }

    private boolean isClay(ItemStack stack) {
        Item clayBlockItem = Item.getItemFromBlock(Blocks.CLAY);
        return stack != ItemStack.EMPTY && (stack.getItem() == Items.CLAY_BALL || stack.getItem() == clayBlockItem);
    }
}
