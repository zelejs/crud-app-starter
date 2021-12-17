package com.jfeat.am.dbss.api.tip;

/**
 * Created by Silent-Y on 2017/11/2.
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super(code, message);
    }


    public static ErrorTip create(int code, String message) {
        return new ErrorTip(code, message);
    }
}
