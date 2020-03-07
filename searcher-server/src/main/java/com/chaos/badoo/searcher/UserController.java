package com.chaos.badoo.searcher;

import com.chaos.badoo.searcher.dto.SimpleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/api/users/initiate-sync")
    public void initiateSync() {
        userService.synchronizeWithBadoo();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/users")
    public List<UserModel> searchUsers(@RequestBody SearchUserParams params) {
        return userService.searchUsers(params);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/interests")
    public List<SimpleItem> getInterests(@RequestBody SearchUserParams params) {
        return userService.getInterests(params);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/jobs")
    public List<SimpleItem> getJobs(@RequestBody SearchUserParams params) {
        return userService.getJobs(params);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/names")
    public List<SimpleItem> getNames(@RequestBody SearchUserParams params) {
        return userService.getNames(params);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/ages")
    public List<SimpleItem> getAges(@RequestBody SearchUserParams params) {
        return userService.getAges(params);
    }

}
