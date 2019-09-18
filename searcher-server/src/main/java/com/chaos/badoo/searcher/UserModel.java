package com.chaos.badoo.searcher;

import com.chaos.badoo.searcher.badoo.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Getter
@Setter
@ToString
public class UserModel {

    public static final String INTERESTS = "interests";
    public static final String JOB = "profileFields.Work";
    public static final String NAME = "name";

    private String id;
    @JsonProperty(NAME)
    private String name;
    private Integer age;
    private LinkedHashSet<String> largePhotoUrls;
    private Boolean newbie;
    private Boolean hot;
    private Boolean allowChat;
    private Boolean allowQuickChat;
    private Integer onlineStatus;
    private Integer popularityLevel;
    private Integer popularityPnbPlace;
    private Integer popularityVisitorsToday;
    private Integer popularityVisitorsMonth;

    @JsonProperty(INTERESTS)
    private List<String> interests;
    private List<SocialNetwork> socialNetworks;
    private List<String> displayedAboutMe;
    private Map<String, String> profileFields;

    /**
     * Additional data for full text search. Mostly for keyword values
     */
    private String fullTextSearch;

    @Getter
    @Setter
    @ToString
    public static class SocialNetwork {
        private String networkType;
        private String networkUrl;
    }
}
