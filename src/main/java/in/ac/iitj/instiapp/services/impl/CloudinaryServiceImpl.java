package in.ac.iitj.instiapp.services.impl;


import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import net.in.spacekart.backend.services.CloudinaryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {


    @Resource
    Cloudinary cloudinary;



    @Override
    public Map<String , String> deleteFile(String public_id) {
        try {
            System.out.println(public_id);
            HashMap<Object, Object> options = new HashMap<>();
            options.put("invalidate", true);

            Map result = cloudinary.uploader().destroy(public_id,options);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting file "+ e.getMessage());

        }
    }


    public Map<Object, Object> uploadFile(byte[] file, String folderName) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);

            System.out.println(folderName);


            Map uploadedFile = cloudinary.uploader().upload(file, options);
            System.out.println(uploadedFile.toString());
            return Map.of("public_id", uploadedFile.get("public_id"), "url", uploadedFile.get("url"),"asset_id",uploadedFile.get("asset_id").toString());



        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Could not upload file "+ e.getMessage());
        }
    }

    @Async
    @Override
    public void deleteFileAsync(String public_id) {

        try {
            System.out.println(public_id);
            HashMap<Object, Object> options = new HashMap<>();
            options.put("invalidate", true);

            cloudinary.uploader().destroy(public_id,options);

        } catch (IOException e) {
            throw new RuntimeException("Error while deleting file "+ e.getMessage());

        }
    }
}