package com.onebucket.domain.memberManage.dao;

import com.onebucket.domain.memberManage.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <br>package name   : com.onebucket.member.repository
 * <br>file name      : memberRepository
 * <br>date           : 2024-06-22
 * <pre>
 * <span style="color: white;">[description]</span>
 * MemberRepository to manage data access of {@link Member} entity. Provide basic CRUD algorithm,
 * also include {@code findByUsername(username), existsByUsername(username), deleteByUsername(username)}
 * Exception should be change to domain custom exception at service.
 * </pre>
 * @tested true
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);

    @Query("SELECT m.id FROM Member m WHERE m.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);

    @Query("SELECT m.nickname FROM Member m WHERE m.id = :id")
    Optional<String> findNicknameById(@Param("id") Long id);
}
