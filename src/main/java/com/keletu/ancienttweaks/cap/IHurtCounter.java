package com.keletu.ancienttweaks.cap;

public interface IHurtCounter {
    double getHurtCounter();

    int getHurtSinceLastDamage();

    void setHurtCounter(double time);

    void setHurtSinceLastDamage(int time);
}