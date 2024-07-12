package com.capgemini.warehouse.util;

import com.capgemini.warehouse.exception.FileFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class WarehouseUtility {


    public static void validateFile(MultipartFile multipartFile) {
        if(multipartFile == null || !Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".json"))
            throw new FileFormatException("Only JSON files are allowed for upload.");
    }
}
