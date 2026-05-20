package com.keletu.ancienttweaks.cap;

public class HurtCounter implements IHurtCounter {
    private double hurtCounter;
    private int hurtSinceLastDamage;

    @Override
    public double getHurtCounter() {
        return this.hurtCounter;
    }

    @Override
    public void setHurtCounter(double hurtCounter) {
        this.hurtCounter = hurtCounter;
    }

    @Override
    public int getHurtSinceLastDamage() {
        return this.hurtSinceLastDamage;
    }

    @Override
    public void setHurtSinceLastDamage(int damageCounter) {
        this.hurtSinceLastDamage = damageCounter;
    }

}