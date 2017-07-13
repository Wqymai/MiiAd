package com.mg.interf;

import java.io.Serializable;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface MiiNativeListener extends Serializable,MiiAbsADListener {
     void onADLoaded(MiiNativeADDataRef dataRef);
}
