package me.specter.spring_boot_demo_02.entity.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{
    @Query("""
        SELECT t 
        FROM Token t 
        INNER JOIN AppUser u 
        ON u.id = t.user.id 
        WHERE t.revoked = false 
        AND u.id = :userId
    """)
    List<Token> findAllValidToken(@Param("userId") Integer userId);

    Optional<Token> findByToken(String token);
}
