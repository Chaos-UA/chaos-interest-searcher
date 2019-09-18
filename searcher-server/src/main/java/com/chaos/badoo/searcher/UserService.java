package com.chaos.badoo.searcher;

import com.chaos.badoo.searcher.badoo.BadooApi;
import com.chaos.badoo.searcher.badoo.UserDto;
import com.chaos.badoo.searcher.dto.SimpleItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private BadooApi badooApi;

    @Autowired
    private UserDao userDao;

    public synchronized void synchronizeWithBadoo() {
        LOGGER.info("Synchronization with badoo has been initiated");
        badooApi.getAllUsersFromNearByPages(100).forEachRemaining(usersPage -> {
            userDao.save(usersPage.getItems().stream().map(this::toUserModel).collect(Collectors.toList()));
        });
        LOGGER.info("Synchronization with badoo has been finished");
    }

    public List<UserModel> searchUsers(SearchUserParams params) {
        List<UserModel> users = userDao.searchUsers(params);
        for (UserModel user : users) {
            user.setFullTextSearch(null);
        }
        return users;
    }

    public List<SimpleItem> getInterests() {
        return userDao.getInterests();
    }

    public List<SimpleItem> getJobs() {
        return userDao.getJobs();
    }

    public List<SimpleItem> getNames() {
        return userDao.getNames();
    }

    private String toUrlWithoutQueryParams(String url) {
        try {
            var uriBuilder = new URIBuilder(url);
            uriBuilder.removeQuery();
            return uriBuilder.build().toString();
        } catch (Exception e) {
            LOGGER.error("Failed to parse URL: {}", url, e);
            return url;
        }
    }

    private UserModel toUserModel(UserDto dto) {
        UserModel model = new UserModel();
        model.setId(dto.getUser_id());
        model.setName(dto.getName());
        model.setAge(dto.getAge());
        model.setLargePhotoUrls(new LinkedHashSet<>());
        if (dto.getProfile_photo() != null && dto.getProfile_photo().getLarge_url() != null) {
            model.getLargePhotoUrls().add(toUrlWithoutQueryParams(dto.getProfile_photo().getLarge_url()));
        }

        if (dto.getAlbums() != null) {
            for (UserDto.Album album : dto.getAlbums()) {
                if (album.getPhotos() != null) {
                    for (UserDto.Album.Photo photo : album.getPhotos()) {
                        if (photo.getLarge_url() != null) {
                            model.getLargePhotoUrls().add(toUrlWithoutQueryParams(photo.getLarge_url()));
                        }
                    }
                }
            }
        }

        model.setNewbie(dto.getIs_newbie());
        model.setHot(dto.getIs_hot());
        model.setAllowChat(dto.getAllow_chat());
        model.setAllowQuickChat(dto.getAllow_quick_chat());
        model.setOnlineStatus(dto.getOnline_status());
        model.setPopularityLevel(dto.getPopularity_level());
        model.setPopularityPnbPlace(dto.getPopularity_pnb_place());
        model.setPopularityVisitorsToday(dto.getPopularity_visitors_today());
        model.setPopularityVisitorsMonth(dto.getPopularity_visitors_month());
        model.setDisplayedAboutMe(dto.getDisplayed_about_me());
        model.setInterests(CollectionUtils.isEmpty(dto.getInterests()) ? null : dto.getInterests().stream().map(UserDto.Interest::getName).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(dto.getSocial_networks())) {
            model.setSocialNetworks(dto.getSocial_networks().stream().filter(v -> v.getExternal_provider() != null).map(v -> {
                UserModel.SocialNetwork socialNetwork = new UserModel.SocialNetwork();
                socialNetwork.setNetworkType(v.getExternal_provider().getDisplay_name());
                socialNetwork.setNetworkUrl(v.getUrl());
                return socialNetwork;
            }).collect(Collectors.toList()));
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (CollectionUtils.isNotEmpty(dto.getProfile_fields())) {
            Map<String, String> profileFields = dto.getProfile_fields().stream()
                    .filter(v -> v.getName() != null)
                    .collect(Collectors.toMap(UserDto.ProfileField::getName, UserDto.ProfileField::getDisplay_value));
            model.setProfileFields(profileFields);

            profileFields.values().forEach(v -> {
                append(stringBuilder, v);
            });
        }

        if (model.getInterests() != null) {
            model.getInterests().forEach(v -> {
                append(stringBuilder, v);
            });
        }

        append(stringBuilder, model.getName());

        if (model.getSocialNetworks() != null) {
            model.getSocialNetworks().forEach(v -> {
                append(stringBuilder, v.getNetworkType());
            });
        }

        model.setFullTextSearch(stringBuilder.toString());

        return model;
    }

    private void append(StringBuilder stringBuilder, String value) {
        stringBuilder.append("\n");
        stringBuilder.append(value);
    }
}
