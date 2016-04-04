package com.example.zhouyou.namefight;

/**
 * Created by zhouyou on 16/3/30.
 */

public class RoleValue {
    private int hitPoint,attack;

    //初始化血量
    public void init_hitPoint(String name){
        this.hitPoint=100;
    }

    //初始化攻击力
    public void init_attack(String name){
        this.attack=10;
    }

    public void sethitPoint(int hitPoint){
        this.hitPoint=hitPoint;
    }

    public int gethitPoint(){
        return this.hitPoint;
    }

    public void setattack(int attack){
        this.attack=attack;
    }

    public int getattack(){
        return this.attack;
    }

}
