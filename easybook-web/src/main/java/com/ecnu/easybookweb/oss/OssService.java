package com.ecnu.easybookweb.oss;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc 图片上传
 */
public interface OssService {

    String storeFile(byte[] bytes);

}
