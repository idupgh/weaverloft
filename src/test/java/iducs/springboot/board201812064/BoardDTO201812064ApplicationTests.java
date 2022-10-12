package iducs.springboot.board201812064;

import iducs.springboot.weaverloft.Board201812064Application;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import iducs.springboot.weaverloft.entity.ReplyEntity;
import iducs.springboot.weaverloft.repository.MemberRepository;
import iducs.springboot.weaverloft.repository.ReplyRepository;
import iducs.springboot.weaverloft.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest(classes = Board201812064Application.class)
class BoardDTO201812064ApplicationTests {

    @Autowired
    MemberRepository memberRepository; // MemoRepository 클래스형 객체를 Spring 생성
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Test
    void testMemberInitialize() {
        // Integer 데이터 흐름 ..? Lambda 식 - 함수형 언어의 특징을 활용
        IntStream.rangeClosed(1, 100).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("idid" + i)
                    .pw("pw" + i)
                    .name("아무개")
                    .email("email" + i + "@naver.com")
                    .phone("01012345678")
                    .address("address" + i)
                    .block("unblock")
                    .delete_yn("n")
                    .role("user")
                    .build();
            String encodedPassword = passwordEncoder.encode(member.getPw());
            member.setPw(encodedPassword);
            LocalDateTime date = LocalDateTime.now();
            member.setPwUpdateDate(date);
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
        String str = "pg9912";
        MemberEntity entity = MemberEntity.builder()
                .id(str)
                .pw("1234")
                .name("관리자")
                .email(str + "@naver.com")
                .phone("01012341234")
                .address("address" + new Random().nextInt(50))
                .block("unblock")
                .delete_yn("n")
                .role("admin")
                .build();
        String encodedPassword = passwordEncoder.encode(entity.getPw());
        entity.setPw(encodedPassword);
        LocalDateTime date = LocalDateTime.now();
        entity.setPwUpdateDate(date);
        memberRepository.save(entity);
    }

    @Test // 임의의 게시글을 대상으로 댓글추가(300개)
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            // 1부터 100까지 임의의 번호
            long bno = (long) (Math.random() * 100) + 1;
            String id = "admin";

            BoardEntity board = BoardEntity.builder().bno(bno).build();
            MemberEntity member = MemberEntity.builder().id(id).build();

            ReplyEntity reply = ReplyEntity.builder()
                    .text("Reply...." + i)
                    .board(board)
                    .member(member)
                    .build();

            replyRepository.save(reply);

        });
    }
}
