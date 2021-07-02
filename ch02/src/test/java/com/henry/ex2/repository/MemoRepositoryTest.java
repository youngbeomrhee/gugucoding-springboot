package com.henry.ex2.repository;

import com.henry.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemoRepositoryTest {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("==============================");
        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        Long mno = 101L;

        Memo memo = memoRepository.getById(mno);

        System.out.println("==============================");

        System.out.println(memo);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(101L).memoText("Update Text2").build();
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 101L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("----------------------------------");
        System.out.println("Total pages: " + result.getTotalPages());
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page?: " + result.hasNext());
        System.out.println("first page?: " + result.isFirst());
        System.out.println("----------------------------------");
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });

        System.out.println("---------------------------------");
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);
        Pageable pageable2 = PageRequest.of(0, 10, sortAll);
        Page<Memo> result2 = memoRepository.findAll(pageable2);
        result2.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // 쿼리 메서드 (Query Methods)
    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodsWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> memos = memoRepository.findByMnoBetween(10L, 50L, pageable);

        memos.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(20L);
    }

    @Test
    public void testJPQLSelect() {
        List<Memo> memos = memoRepository.getListDesc();
        for (Memo memo : memos) {
            System.out.println(memo);
        }
    }

    @Test
    public void testJQPLParameterBinding() {
        Integer updatedCount = memoRepository.updateMemoText(102L, "updated by JPQL");
        assertEquals(updatedCount, 1);
        assertNotEquals(updatedCount, 0);
    }

    @Test
    public void updateMemoText2() {
        Memo memo = Memo.builder().mno(102L).memoText("updated by JPQL2").build();
        Integer updatedCount = memoRepository.updateMemoText2(memo);
        assertEquals(updatedCount, 1);
        assertNotEquals(updatedCount, 0);
    }

    @Test
    void getListWithQuery() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno"));
        Page<Memo> memos = memoRepository.getListWithQuery(100L, pageable);
        for (Memo memo : memos) {
            System.out.println(memo);
        }
    }

    @Test
    void getListWithQueryObject() throws IllegalAccessException, NoSuchFieldException {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno"));
        Page<Object[]> memos = memoRepository.getListWithQueryObject(100L, pageable);

        for (Object memo : memos) {
            Object[] memoObj = ((Object[])memo);
            for (Object o : memoObj) {
                System.out.println(o.toString());
            }
        }
    }

    @Test
    void getNativeResult() {
        List<Memo> memos = memoRepository.getNativeResult();
        for (Memo memo : memos) {
            System.out.println(memo);
        }
    }
}