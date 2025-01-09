package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.GrievanceRepository;
import in.ac.iitj.instiapp.services.GrievanceService;
import org.springframework.stereotype.Service;

@Service
public class GrievanceServiceImpl implements GrievanceService {

    private final GrievanceRepository grievanceRepository;

    public GrievanceServiceImpl(GrievanceRepository grievanceRepository) {
        this.grievanceRepository = grievanceRepository;
    }

    public void existGrievance(String publicId){
        grievanceRepository.existGrievance(publicId);
    }





}
