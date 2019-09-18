package com.chaos.badoo.searcher.badoo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserDto {

    private String user_id;
    private String name;
    private Integer age;
    private Boolean is_newbie;
    private Boolean is_hot;
    private Boolean allow_chat;
    private Boolean allow_quick_chat;
    private Integer online_status;
    private Integer popularity_level;
    private Integer popularity_pnb_place;
    private Integer popularity_visitors_today;
    private Integer popularity_visitors_month;
    private ProfilePhoto profile_photo;
    private List<Interest> interests;
    private List<SocialNetwork> social_networks;
    private List<ProfileField> profile_fields;
    private List<String> displayed_about_me;
    private List<Album> albums;

    @Getter
    @Setter
    @ToString
    public static class Album {
        private String name;
        private List<Photo> photos;

        @Getter
        @Setter
        @ToString
        public static class Photo {
            private String id;
            private String preview_url;
            private String large_url;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Interest {
        private String interest_id;
        private String name;
        private Integer group_id;
        private Boolean is_matched;
    }

    @Getter
    @Setter
    @ToString
    public static class ProfileField {
        /**
         * Field type (location, aboutme_text, languages etc)
         */
        private String id;
        private Integer type;
        private String name;
        private String display_value;
    }

    @Getter
    @Setter
    @ToString
    public static class ProfilePhoto {
        private String id;
        private String preview_url;
        private String large_url;
    }

    @Getter
    @Setter
    @ToString
    public static class SocialNetwork {
        private String url;
        private ExternalProvider external_provider;

        @Getter
        @Setter
        @ToString
        public static class ExternalProvider {
            private String id;
            private String display_name;
            private Integer type;
        }
    }
}
