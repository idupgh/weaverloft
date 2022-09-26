package iducs.springboot.board201812064;

import iducs.springboot.weaverloft.Board201812064Application;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import iducs.springboot.weaverloft.entity.ReplyEntity;
import iducs.springboot.weaverloft.repository.MemberRepository;
import iducs.springboot.weaverloft.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest(classes = Board201812064Application.class)
class BoardDTO201812064ApplicationTests {

    @Autowired
    MemberRepository memberRepository; // MemoRepository 클래스형 객체를 Spring 생성
    @Autowired
    ReplyRepository replyRepository;

    @Test
    void testMemberInitialize() {
        // Integer 데이터 흐름 ..? Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 100).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("id" + i)
                    .pw("pw" + i)
                    .name("name" + i)
                    .email("email" + i + "@naver.com")
                    .phone("phone" + i)
                    .address("address" + i)
                    .block("unblock")
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    void testMemberPlus() {
        // Integer 데이터 흐름 ..? Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 100).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                            .delete_yn("n")
                                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    void testAdmin() {
        // Integer 데이터 흐름, Lambda 식 - 함수형 언어의 특징을 활용
        String str = "admin";
        MemberEntity entity = MemberEntity.builder()
                .id(str)
                .pw(str)
                .name("name" + str )
                .email(str + "@induk.ac.kr")
                .phone("phone" + new Random().nextInt(50))
                .address("address" + new Random().nextInt(50))
                .block("unblock")
                .build();
        memberRepository.save(entity);
    }

    @Test // 임의의 게시글을 대상으로 댓글추가(300개)
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            // 1부터 100까지 임의의 번호
            long bno = (long) (Math.random() * 100) + 1;

            BoardEntity board = BoardEntity.builder().bno(bno).build();

            ReplyEntity reply = ReplyEntity.builder()
                    .text("Reply...." + i)
                    .board(board)
                    .replier("guest")
                    .build();

            replyRepository.save(reply);

        });
    }
}
