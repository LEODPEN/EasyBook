package com.ecnu.easybookweb.oss;

import com.ecnu.easybookweb.exception.BookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ecnu.easybookweb.enums.ResultEnum.*;

/**
 * @author LEO D PEN
 * @date 2021/1/6
 * @desc
 */
@Component("OssService")
@Slf4j
public class OssServiceImpl implements OssService {

    @Value("${oss.store-path}")
    private String storePath;

    @Value("${oss.url-prefix}")
    private String urlPrefix;

    @Override
    public String storeFile(byte[] bytes) {
        Path filePath = Paths.get(storePath);
        String fileName;
        while (true) {
            try {
                Files.createFile(filePath = filePath.resolve(fileName =
                        String.valueOf(System.currentTimeMillis())));
                break;
            } catch (FileAlreadyExistsException e) {
                // continue
            } catch (Throwable e) {
                log.error("[OssServiceImpl uploadImage] createFile error", e);
                throw new BookException(OSS_CREATE_FILE_ERROR);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "rw")) {
            raf.write(bytes);
        } catch (Throwable e) {
            log.error("[OssServiceImpl uploadImage] writeFile error", e);
            throw new BookException(OSS_WRITE_FILE_ERROR);
        }

        return urlPrefix + fileName;
    }
}
