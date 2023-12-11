package me.horizonlin.pixelmonfoodliquid.fluid;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.DropItemHelper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.horizonlin.pixelmonfoodliquid.PixelmonFoodLiquid;
import me.horizonlin.pixelmonfoodliquid.block.BlockEntry;
import me.horizonlin.pixelmonfoodliquid.particle.PixelmonParticleTypeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.Console;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FluidEntry {

    public static final ResourceLocation WATER_STILL_FOOD = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_FOOD = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_OVERLAY_FOOD = new ResourceLocation("block/water_still");
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, PixelmonFoodLiquid.MOD_ID);

    public static final RegistryObject<FlowingFluid> FOOD_FLUID
            = FLUIDS.register("pixelmon_food_fluid", () -> new ForgeFlowingFluid.Source(FluidEntry.FOOD_PROPERTIES){
        private int tick = 0;
        @Override
        protected boolean ticksRandomly() {
            return true;
        }

        @Override
        protected void randomTick(World world, BlockPos pos, FluidState state, Random random) {
            if (random.nextInt(100) < 30) {
                List<PixelmonEntity> PixelmonEntities = world.getEntitiesWithinAABB(PixelmonEntity.class, (new AxisAlignedBB(pos)));
                System.out.println(PixelmonEntities);
                if (!PixelmonEntities.isEmpty()) {
                    for (PixelmonEntity entity: PixelmonEntities) {
                        Pokemon pokemon = entity.getPokemon();
                        if (pokemon.getOwnerPlayer() != null) {
                            ServerPlayerEntity player = pokemon.getOwnerPlayer();
                            List<ItemStack> items = DropItemRegistry.getDropsForPokemon(entity);
                            Iterator var3 = items.iterator();

                            while(var3.hasNext()) {
                                ItemStack stack = (ItemStack)var3.next();
                                if (stack.isFood()) {
                                    DropItemHelper.dropItemOnGround(entity.getPositionVec(), player, stack, false, false);
                                }
                            }
                        }
                    }
                };
            }
        }
        @Override
        @OnlyIn(Dist.CLIENT)
        protected void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
            tick++;
            if (tick == 15) {
                tick = 0;
                if (random.nextInt(100) < 60 && worldIn.isAirBlock(pos.up())) {
                    worldIn.addParticle(PixelmonParticleTypeRegistry.FOOD_FLUID_RISING_PARTICLE.get(), pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0f, 0.004f, 0f);
                }
            }
        }
    });

    public static final RegistryObject<FlowingFluid> FOOD_FLOWING
            = FLUIDS.register("pixelmon_food_flowing_fluid", () -> new ForgeFlowingFluid.Flowing(FluidEntry.FOOD_PROPERTIES) {
            private int tick = 0;
        @Override
        @OnlyIn(Dist.CLIENT)
        protected void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
            tick++;
            if (tick == 15) {
                tick = 0;
                if (random.nextInt(100) < 60 && worldIn.isAirBlock(pos.up())) {
                    worldIn.addParticle(PixelmonParticleTypeRegistry.FOOD_FLUID_RISING_PARTICLE.get(), pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0f, 0.004f, 0f);
                }
            }
        }
    });

    public static final ForgeFlowingFluid.Properties FOOD_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> FOOD_FLUID.get(), () -> FOOD_FLOWING.get(), FluidAttributes.builder(WATER_STILL_FOOD, WATER_FLOWING_FOOD)
            .sound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK)
            .overlay(WATER_OVERLAY_FOOD)).slopeFindDistance(2).levelDecreasePerBlock(5).tickRate(45)
            .block(() -> FluidEntry.FOOD_FLUID_BLOCK.get());

    public static final RegistryObject<FlowingFluidBlock> FOOD_FLUID_BLOCK
            = BlockEntry.BLOCKS.register("food_fluid_block", () -> new FlowingFluidBlock(()-> FluidEntry.FOOD_FLUID.get(),
            AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().notSolid().hardnessAndResistance(100.0F).setLightLevel((state) -> {
                return 15;
            }).noDrops()));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
