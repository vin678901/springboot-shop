package com.example.springbootshop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();//혹시나 파일 이름이 중복될 수도 있으니까 uuid를 만들어 줌
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));//확장자 추출
        //extension으로 이미지 파일인지 체크


        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);//파일 업로드
        fos.close();
        return savedFileName;


    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
