package com.template.demo.dto.response;

import java.util.List;

public record UserFollowed(long userId, String userName, List<UserInformation> followed) {}
