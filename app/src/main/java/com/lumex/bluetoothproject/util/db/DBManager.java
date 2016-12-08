package com.lumex.bluetoothproject.util.db;

import android.content.Context;

import com.lumex.bluetoothproject.common.Constants;

/**
 * Created by 阿泰Charles on 2016/12/08.
 */

public class DBManager {
    private static DBManager instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    /**
     * [获取DBManager实例，单例模式实现]
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 构造方法
     * @param context
     */
    private DBManager(Context context) {
        if(daoSession == null){
            if(daoMaster == null){
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, Constants.DB_NAME, null);
                daoMaster = new DaoMaster(helper.getWritableDatabase());
            }
            daoSession = daoMaster.newSession();
        }
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}

