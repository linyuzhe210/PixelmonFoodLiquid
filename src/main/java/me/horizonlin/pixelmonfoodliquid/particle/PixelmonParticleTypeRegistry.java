package me.horizonlin.pixelmonfoodliquid.particle;

import me.horizonlin.pixelmonfoodliquid.PixelmonFoodLiquid;
import me.horizonlin.pixelmonfoodliquid.client.particle.FoodRisingParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PixelmonParticleTypeRegistry {

    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PixelmonFoodLiquid.MOD_ID);
    public static final RegistryObject<BasicParticleType> FOOD_FLUID_RISING_PARTICLE = REGISTER.register("food_fluid_rising_particle", () -> new BasicParticleType(false));

    @OnlyIn(Dist.CLIENT)
    public static void registerFactories() {
        ParticleManager particles = Minecraft.getInstance().particles;
        particles.registerFactory(FOOD_FLUID_RISING_PARTICLE.get(), FoodRisingParticle.Factory::new);
    }
}
