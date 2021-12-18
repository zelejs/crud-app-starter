package com.jfeat.am.dbss.api.tip;

/**
 * Created by Silent-Y on 2017/11/2.
 */
public class Tip {
        protected int code;
        protected String message;

        public Tip() {
        }

        public Tip(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

}
