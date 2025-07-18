package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnnouncementsService {

    public Long save(AnnouncementsDto announcementsDto, String username, List<MultipartFile> files) throws IOException;

    public List<String> getMediaUrlsOfAnnouncement(Long publicId);

    public Long update(AnnouncementsDto announcementsDto, String username, List<MultipartFile> newFiles) throws IOException;

    public List<AnnouncementsDto> getAllAnnouncements();

    public void delete(Long publicId, String username);
    public void saveForEventAnnouncement(AnnouncementsDto announcementsDto, String username, List<String> mediaUrl);
}
