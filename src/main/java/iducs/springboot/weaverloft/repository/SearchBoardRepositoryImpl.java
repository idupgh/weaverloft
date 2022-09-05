package iducs.springboot.weaverloft.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.QBoardEntity;
import iducs.springboot.weaverloft.entity.QMemberEntity;
import iducs.springboot.weaverloft.entity.QReplyEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("SearchBoardRepositoryImpl")
@Log4j2
public class SearchBoardRepositoryImpl
        extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl() {
        super(BoardEntity.class);
    }

    @Override
    public BoardEntity searchBoard() {
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("--------- searchPage -------------");
        QBoardEntity boardEntity = QBoardEntity.boardEntity;
        QReplyEntity replyEntity = QReplyEntity.replyEntity;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        JPQLQuery<BoardEntity> jpqlQuery = from(boardEntity);
        jpqlQuery.leftJoin(memberEntity).on(boardEntity.writer.eq(memberEntity));
        jpqlQuery.leftJoin(replyEntity).on(replyEntity.board.eq(boardEntity));
        // select b, w, count(r) from BoardEntity b left join b.writer w left join ReplyEntity r on r.board = b;
        JPQLQuery<Tuple> tuple = jpqlQuery.select(boardEntity, memberEntity, replyEntity.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression= boardEntity.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for(String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(boardEntity.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(memberEntity.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(boardEntity.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();
        // tuple.orderBy(board.bno.desc());
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(BoardEntity.class, "boardEntity");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(boardEntity);

        // page 처리

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,
                count);
    }
}
