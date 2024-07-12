package com.onebucket.domain.boardManage.entity.post;

import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : Board
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-08        jack8              init create
 * </pre>
 */
@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne
    @JoinColumn(name = "author_id")
    private Member author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;



    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;



}
