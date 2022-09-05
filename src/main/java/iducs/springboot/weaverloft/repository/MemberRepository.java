package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>,
        QuerydslPredicateExecutor<MemberEntity> {
    @Query("select m from MemberEntity m where m.email = :email and m.pw = :pw")

    Object getMemberByEmail(@Param("email") String email, @Param("pw") String pw);
}
