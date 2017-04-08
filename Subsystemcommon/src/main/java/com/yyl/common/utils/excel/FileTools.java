/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yyl.common.utils.excel;

import org.apache.commons.lang.StringUtils;

import com.yyl.common.entity.BaseException;

/**
 *
 * @author yangyl
 * @date 2016-9-12
 */
public class FileTools {
    /**
     * 获取文件后缀
     * @return 
     */
    public static String getSuffix (String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        } else if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return "";
    }
    
    public static class FileException extends BaseException {

        public FileException(String ex, int code) {
            super(ex, code);
        }

        public FileException(Throwable ex, int code) {
            super(ex, code);
        }

        public final static int PARAMETER_INVALID_ERROR = 71000;

        public final static int FILE_TYPE_MISMATCHED_ERROR = 72000;

        public final static int FILE_READ_ERROR = 72001;

        public final static int FILE_WRITE_ERROR = 72002;

        public final static int FILE_INVALID_ERROR = 72003;

    }
}
