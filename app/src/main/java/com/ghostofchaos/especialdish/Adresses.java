package com.ghostofchaos.especialdish;

/**
 * Created by Ghost on 26.05.2016.
 */
public class Adresses {

    public static String HOST = "http://osoboebludo.com/";
    public static String HOST_API = HOST + "api/?notabs&json";
    public static String GET_RESTAURANTS =  HOST_API + "&content_id=16";
    public static String GET_DISHES =       HOST_API + "&content_id=17";
    public static String GET_NEWS =         HOST_API + "&content_id=1";
    public static String GET_PROJECTS =     HOST_API + "&content_id=6";
    public static String GET_REVIEWS =      HOST_API + "&content_id=5";
    public static String GET_BLOGS =        HOST_API + "&content_id=14";
    public static String GET_CALENDAR =     HOST_API + "&content_id=15";

    public static String GET_CONTENT = "http://osoboebludo.com/uploads/content/";

    public static String PAGE = "&page=";
    public static String PER_PAGE = "&per_page=";
    public static String KEYWORDS = "&keywords=";
    public static String DATE_ADDED = "&date_added=";
    public static String DATE_MODIFIED = "&date_modified=";
}
