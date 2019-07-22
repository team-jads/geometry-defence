package com.jads.geometrydefense;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context mContext;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();

        loadGameData();
    }

    public void loadGameData() {
        GameConfig gameConfig = GameConfig.getInstance();



        gameConfig.gameData = new StaticGameData()
                .addCreepType(new CreepType(R.drawable.ic_box1, 2, 1, 1.5f))
                .addCreepType(new CreepType(R.drawable.ic_box2, 5, 5, 1.5f))
                .addCreepType(new CreepType(R.drawable.ic_box3, 13, 10, 1.5f))
                .addCreepType(new CreepType(R.drawable.ic_box4, 34, 20, 1.5f))
                .addCreepType(new CreepType(R.drawable.ic_box5, 89, 60, 1.5f))
                .addCreepType(new CreepType(R.drawable.ic_circ1, 3, 1, 1))
                .addCreepType(new CreepType(R.drawable.ic_circ2, 8, 5, 1))
                .addCreepType(new CreepType(R.drawable.ic_circ3, 21, 10, 1))
                .addCreepType(new CreepType(R.drawable.ic_circ4, 55, 20, 1))
                .addCreepType(new CreepType(R.drawable.ic_circ5, 144, 60, 1))
        ;
    }
}