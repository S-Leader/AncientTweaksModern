/*package com.keletu.ancienttweaks.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWaffle extends EntityArrow {
    public static final DataParameter<Integer> DISPOSE_TIME = EntityDataManager.createKey(EntityWaffle.class, DataSerializers.VARINT);

    public Entity shooter;
    int maxDisposeTime = 15;
    private int knockbackStrength;

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (key == DISPOSE_TIME) {
            maxDisposeTime = Math.min(dataManager.get(DISPOSE_TIME) - ticksExisted, 15);
        }
    }

    public EntityWaffle(World w) {
        super(w);
        this.setDamage(9F);
        this.setSize(1.0F, 1.0F);
    }

    public EntityWaffle(World worldIn, EntityLivingBase shooter, double dmg) {
        super(worldIn, shooter);
        this.setDamage(dmg);
        this.setSize(1.0F, 1.0F);
        this.shooter = shooter;

        this.motionX *= -0.01F;
        this.motionY *= -0.1F;
        this.motionZ *= -0.01F;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = posX - toX;
        double d1 = posY - toY;
        double d2 = posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    public void onUpdate() {
        super.onUpdate();
        noClip = true;

        float sqrt = MathHelper.sqrt((float) (this.motionX * this.motionX + this.motionZ * this.motionZ));
        if ((sqrt < 0.1F) && this.ticksExisted > 100) {
            this.setDead();
        }

        if (this.ticksExisted >= 100)
            this.setDead();
    }

    public boolean hasNoGravity() {
        return false;
    }

    @Override
    protected void onHit(RayTraceResult object) {
        if (this.isDead)
            return;

        if (object.typeOfHit == RayTraceResult.Type.BLOCK)
            this.setDead();

        Entity entity = object.entityHit;
        if (entity != null) {
            Entity e = object.entityHit;
            if (e == shooter)
                return;

            int damage = MathHelper.ceil(getDamage());
            if (this.getIsCritical()) {
                damage += this.rand.nextInt(damage / 2 + 2);
            }

            DamageSource damageSource;
            if (this.shootingEntity == null) {
                damageSource = DamageSource.causeArrowDamage(this, this);
            } else {
                damageSource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damageSource, (float) damage)) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                    if (this.knockbackStrength > 0) {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        if (f1 > 0.0F) {
                            entitylivingbase.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579 / (double) f1, 0.1, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579 / (double) f1);
                        }
                    }

                    if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);
                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }
                this.setDead();
            }
        } else {
            this.setDead();
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DISPOSE_TIME, 120);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }

    @Override
    public void setDead() {
        if (this.world.isRemote) {
            spawnBreakParticles();
        } else {
            this.playSound(SoundEvents.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
        }

        super.setDead();
    }

    private void spawnBreakParticles() {
        int particleCount = 30 + this.rand.nextInt(21);

        for (int i = 0; i < particleCount; i++) {
            double offsetX = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;
            double offsetY = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;
            double offsetZ = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;

            double velocityX = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;
            double velocityY = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;
            double velocityZ = ((double) this.rand.nextFloat() - 0.5D) * 0.5D;

            this.world.spawnParticle(
                    EnumParticleTypes.ITEM_CRACK,
                    this.posX + offsetX,
                    this.posY + offsetY,
                    this.posZ + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ,
                    Item.getIdFromItem(ItemRegister.waffleItem)
            );
        }
    }
}*/