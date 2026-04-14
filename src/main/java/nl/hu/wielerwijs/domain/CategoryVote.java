package nl.hu.wielerwijs.domain;

import nl.hu.security.domain.User;

public record CategoryVote(String category, User user, Renner renner) {}