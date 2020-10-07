package com.kal.connect.customLibs.HTTP.Multipart;

import java.io.File;

/**
 * Created by lakeba_prabhu on 29/03/17.
 */

public class MultipartModel {

    String key;
    Boolean isFile;
    String value;
    File file;

    /**
     * With Value
     *
     * @param key
     * @param value
     */
    public MultipartModel(String key, String value) {
        this.key = key;
        this.value = value;
        this.isFile = false;
    }

    /**
     * With Value
     *
     * @param key
     * @param file
     */
    public MultipartModel(String key, File file) {
        this.key = key;
        this.file = file;
        this.isFile = true;
    }

}
