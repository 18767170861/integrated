package org.wmc.integrated.bizlog.service.impl;

import org.wmc.integrated.bizlog.service.UserQueryService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Override
    public List<User> getUserList(List<String> userIds) {
        return null;
    }
}