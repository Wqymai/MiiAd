package com.mg.others.task;


import com.mg.others.model.DeviceInfo;


public interface IDeviceInfoListener {
    /*
    * 初始化用户信息完成
    * */
    void deviceInfoLoaded(DeviceInfo deviceInfo);
}
