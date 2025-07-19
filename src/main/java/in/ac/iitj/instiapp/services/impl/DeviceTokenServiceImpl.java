package in.ac.iitj.instiapp.services.impl;


import in.ac.iitj.instiapp.Repository.DeviceTokenRepository;
import in.ac.iitj.instiapp.database.entities.DeviceToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DeviceTokenServiceImpl {

    private final DeviceTokenRepository deviceTokenRepository;

    @Autowired
    public DeviceTokenServiceImpl(DeviceTokenRepository deviceTokenRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public DeviceToken save(DeviceToken deviceToken) {
        return deviceTokenRepository.save(deviceToken);
    }

    public List<DeviceToken> findAll(){
        return deviceTokenRepository.findAll();
    }

    public String findUserByToken(String DeviceToken){
        return deviceTokenRepository.findUserIdByToken(DeviceToken);
    }

    public DeviceToken getAllDeviceTokens(String username){
        return deviceTokenRepository.getAllDeviceTokens(username);
    }
}

