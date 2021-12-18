package com.jfeat.am.dbss.api.tip;

/**
 * Created by Silent-Y on 2017/11/2.
 */
public class SuccessTip extends Tip {
    private Object data;

    public Object getData() {
        return this.data;
    }

    public SuccessTip setData(Object data) {
        this.data = data;
        return this;
    }

    public SuccessTip(Object data) {
        this();
        this.data = data;
    }

    public SuccessTip() {
        super.code = 200;
        super.message = "操作成功";
    }

    public static SuccessTip create() {
        return new SuccessTip();
    }

    public static SuccessTip create(Object data) {
        return new SuccessTip(data);
    }
}
