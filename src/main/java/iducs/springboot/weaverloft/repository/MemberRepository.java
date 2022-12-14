package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>,
        QuerydslPredicateExecutor<MemberEntity> {
    @Query("select m from MemberEntity m where m.id = :id")
    Object getMemberById(@Param("id") String id);

    @Query("select m from MemberEntity m where m.delete_yn = :delete_yn")
    Object deleteById(@Param("delete_yn") String delete_yn);

    boolean existsByName(String name);

    boolean existsById(String id);

    MemberEntity findById(String id);

    MemberEntity getById(String id);

}
