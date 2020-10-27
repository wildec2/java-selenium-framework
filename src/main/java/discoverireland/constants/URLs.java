package discoverireland.constants;

public class URLs {

    private URLs() {}

    public static final String HOMEPAGE = System.getProperty("base_url", "https://www.discoverireland.ie/");
    public static final String DESTINATIONS_PAGE = String.format("%sdestinations", HOMEPAGE);

}
