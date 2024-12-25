package in.ac.iitj.instiapp.payload;

import org.springframework.http.converter.json.MappingJacksonValue;

public class Views {
    public static class Public{}
    public static class Detailed extends Public{}
    public static class Private extends Detailed{}
}
