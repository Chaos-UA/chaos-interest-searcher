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

    @RequestMapping(method = RequestMethod.GET, path = "/api/users/initiate-sync")
    public void initiateSync() {
        userService.synchronizeWithBadoo();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/users")
    public List<UserModel> searchUsers(@RequestBody SearchUserParams params) {
        return userService.searchUsers(params);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/interests")
    public List<SimpleItem> getInterests() {
        return userService.getInterests();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/jobs")
    public List<SimpleItem> getJobs() {
        return userService.getJobs();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/names")
    public List<SimpleItem> getNames() {
        return userService.getNames();
    }

}
