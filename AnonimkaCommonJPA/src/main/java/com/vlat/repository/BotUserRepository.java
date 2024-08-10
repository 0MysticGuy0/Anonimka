package com.vlat.repository;

import com.vlat.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, String> {

    @Query(value = "SELECT * FROM bot_users WHERE state = \'IN_SEARCH\'", nativeQuery = true)
    List<BotUser> findAllUsersInSearch();
}
