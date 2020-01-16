package com.kaka.webgm.game.dao;

import java.io.Serializable;

public class CallGameInterfaceResult<B> implements Serializable {
    public final Integer code;
    public final B data;

    public CallGameInterfaceResult(Integer code, B data) {
        this.code = code;
        this.data = data;
    }

    public String toString() {
        return "[code" + ":" + this.code + ",data:" + data.toString() + "]";
    }
}
