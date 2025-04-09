package com.template.demo.dto.response;

import java.util.List;

public record UserFollowers(long userId, String userName, List<UserInformation> followers) {}
