/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yyl.common.entity;

/**
 *
 * @author yangyl
 */
public class BaseException extends Exception {

    private int code;

    private BaseException() {
    }

    public BaseException(String ex) {
    	super(ex);
    }

    private BaseException(Throwable ex) {
        throw new RuntimeException("Unsupported");
    }

    public BaseException(String ex, int code) {
        super(ex);
        this.code = code;
    }

    public BaseException(Throwable ex, int code) {
        super(ex);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
