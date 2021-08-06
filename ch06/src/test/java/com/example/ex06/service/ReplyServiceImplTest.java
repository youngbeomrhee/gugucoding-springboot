package com.example.ex06.service;

import com.example.ex06.dto.ReplyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceImplTest {

    @Autowired
    private ReplyService service;

    @Test
    void testGetList() {
        Long bno = 100L;    // 데이터베이스에 존재하는 번호
        List<ReplyDTO> replyDTOList = service.getList(bno);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));
    }
}