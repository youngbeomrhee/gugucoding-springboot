package com.example.mreview.repository;

import com.example.mreview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
//    @Query("select m, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
//            "left outer join Review  r on r.movie = m group by m")
//    Page<Object[]> getListPage(Pageable pageable);

//    @Query("select m, max(mi), avg(coalesce(r.grade,0)),  count(distinct r) from Movie m " +
    @Query("select m, mi, avg(coalesce(r.grade,0)),  count(distinct r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review  r on r.movie = m group by m, mi ")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select m, i, count(r) from Movie m left join MovieImage i on i.movie = m " +
            "and i.inum = (select max(i2.inum) from MovieImage i2 where i2.movie = m ) " +
            "left outer join Review r on r.movie = m group by m")
    Page<Object[]> getListPageWithLastImage(Pageable pageable);

    @Query("select m, mi ,avg(coalesce(r.grade,0)),  count(r)" +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review  r on r.movie = m "+
            " where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno);
}
