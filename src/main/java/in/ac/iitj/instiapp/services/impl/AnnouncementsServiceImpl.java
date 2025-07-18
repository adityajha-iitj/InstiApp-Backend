package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.AnnouncementsRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.mappers.AnnouncementsMapper;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import in.ac.iitj.instiapp.services.AnnouncementsService;
import in.ac.iitj.instiapp.services.BucketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class AnnouncementsServiceImpl implements AnnouncementsService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AnnouncementsRepository announcementsRepository;
    private final AnnouncementsMapper announcementsMapper;
    private final OrganisationRepositoryImpl organisationRepositoryImpl;
    private final BucketService bucketService;

    public AnnouncementsServiceImpl(AnnouncementsRepository announcementsRepository,
                                    AnnouncementsMapper announcementsMapper,
                                    OrganisationRepositoryImpl organisationRepository,
                                    BucketService bucketService) {
        this.announcementsRepository = announcementsRepository;
        this.announcementsMapper = announcementsMapper;
        this.organisationRepositoryImpl = organisationRepository;
        this.bucketService = bucketService;
    }

    public Long save(AnnouncementsDto announcementsDto, String username, List<MultipartFile> files) throws IOException {

        Announcements announcements = announcementsMapper.toEntity(announcementsDto);
        announcements.setOrganisation(organisationRepositoryImpl.getOrganisationByUsername(username));


        String s3Url = "";
        Set<Media> mediaSet = new HashSet<>();
        for (MultipartFile file : files) {
            String extension =
                    file.getOriginalFilename()
                            .substring(file.getOriginalFilename().lastIndexOf('.'));
            String objectKey = "announcements/" + username + "/"
                    + UUID.randomUUID() + extension;

            // 2.b. Save to a temp file
            File temp = File.createTempFile("announcements-", extension);
            file.transferTo(temp);

            // 2.c. Upload & get URL
            bucketService.uploadFile(bucketName, objectKey, temp.getAbsolutePath());
            s3Url = bucketService.getFileUrl(bucketName, objectKey);

            Media media = new Media();
            media.setPublicUrl(s3Url);

            mediaSet.add(media);

            // 2.d. Clean up the temp file
            temp.delete();
        }

        announcements.setMedia(mediaSet);
        Announcements a = announcementsRepository.save(announcements);
        return a.getPublicId();
    }

    public List<String> getMediaUrlsOfAnnouncement(Long publicId){
        return announcementsRepository.getAnnouncementMediaUrl(publicId);
    }

    public Long update(AnnouncementsDto announcementsDto, String username, List<MultipartFile> newFiles) throws IOException {

        Organisation org = organisationRepositoryImpl.getOrganisationByUsername(username);
        Announcements existing = announcementsRepository.findByAnnouncementByPublicId(announcementsDto.getPublicId());
        if (!existing.getOrganisation().getId().equals(org.getId())) {
            throw new AccessDeniedException("You do not have permission to update this announcement.");
        }

        Announcements announcements = announcementsMapper.toEntity(announcementsDto);


        // 2. Update scalar fields
        existing.setOrganisation(org);
        existing.setTitle(announcementsDto.getTitle());
        existing.setDescription(announcementsDto.getDescription());
        existing.setDateOfAnnouncement(announcementsDto.getDateOfAnnouncement());
        existing.setTimeOfAccouncement(announcementsDto.getTimeOfAccouncement());

        // 3. Rebuild media set from DTO’s existing URLs
        Set<Media> mediaSet = new HashSet<>();
        if (announcementsDto.getMediaPublicUrls() != null) {
            for (String url : announcementsDto.getMediaPublicUrls()) {
                Media m = new Media();
                m.setPublicUrl(url);
                mediaSet.add(m);
            }
        }

        // 4. Upload any new files and add their URLs
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                String ext       = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf('.'));
                String objectKey = "announcements/" + username + "/" + UUID.randomUUID() + ext;

                File tmp = File.createTempFile("announcements-", ext);
                file.transferTo(tmp);
                bucketService.uploadFile(bucketName, objectKey, tmp.getAbsolutePath());
                String s3Url = bucketService.getFileUrl(bucketName, objectKey);
                tmp.delete();

                Media m = new Media();
                m.setPublicUrl(s3Url);
                mediaSet.add(m);
            }
        }

        // 5. Replace the entity’s media set & save
        existing.getMedia().clear();          // remove all old entries (orphans get deleted)
        existing.getMedia().addAll(mediaSet); // add back the merged set
        announcementsRepository.save(existing);

        return existing.getPublicId();
    }

    public List<AnnouncementsDto> getAllAnnouncements(){
        List<Announcements> eventsList = announcementsRepository.findAll();
        List<AnnouncementsDto> DtoList = new ArrayList<>();
        for (Announcements announcements : eventsList) {
            AnnouncementsDto announcementsDto = announcementsMapper.toDto(announcements);
            announcementsDto.setMediaPublicUrls(announcementsRepository.getAnnouncementMediaUrl(announcements.getPublicId()));
            DtoList.add(announcementsDto);
        }
        return DtoList;
    }

    @Transactional
    public void delete(Long publicId, String username){

        Organisation org = organisationRepositoryImpl.getOrganisationByUsername(username);
        Announcements announcements = announcementsRepository.findByAnnouncementByPublicId(publicId);
        if (!announcements.getOrganisation().getId().equals(org.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this event.");
        }

        Announcements ann =  announcementsRepository.findByAnnouncementByPublicId(publicId);
        announcementsRepository.delete(ann);
    }

    public void saveForEventAnnouncement(AnnouncementsDto announcementsDto, String username, List<String> mediaUrl) {

        Announcements announcements = announcementsMapper.toEntity(announcementsDto);
        announcements.setOrganisation(organisationRepositoryImpl.getOrganisationByUsername(username));


        String s3Url = "";
        Set<Media> mediaSet = new HashSet<>();
        for (String url : mediaUrl) {
            Media m = new Media();
            m.setPublicUrl(url);
            mediaSet.add(m);
        }

        announcements.setMedia(mediaSet);
        Announcements a = announcementsRepository.save(announcements);
    }
}
