package com.bskoczylas.modelinglegalreasoning.domain.models.DTO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProjectRepository {
    public void save(ProjectData projectData, String path) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(projectData);
            out.flush();
            byte[] encryptedData = AESEncryption.encrypt(bos.toByteArray());
            Files.write(Paths.get(path), encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProjectData load(String path) {
        try {
            byte[] encryptedData = Files.readAllBytes(Paths.get(path));
            byte[] decryptedData = AESEncryption.decrypt(encryptedData);
            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
            ObjectInputStream in = new ObjectInputStream(bis);
            return (ProjectData) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
