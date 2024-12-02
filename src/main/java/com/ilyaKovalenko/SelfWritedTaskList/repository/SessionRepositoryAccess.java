package com.ilyaKovalenko.SelfWritedTaskList.repository;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepositoryAccess extends JpaRepository<Token, Long> {

    @Modifying
    @Query(value = "DELETE FROM token_sessions_access WHERE token = :token", nativeQuery = true)
    void logout(@Param("token") String token);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM token_sessions_access WHERE token = :token)", nativeQuery = true)
    boolean confirm(@Param("token") String token);

}
