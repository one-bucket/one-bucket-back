package com.onebucket.domain.boardManage.entity.post;

import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : Board
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * Post에 대한 엔티티. Discriminator type 을 사용하여, 해당 테이블을 상속받는 테이블에 대하여 실제 데이터베이스에서
 * 동일한 테이블에 저장된다.
 * board에 대한 인덱스를 생성하였다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * private Long id;
 * private Board board;
 * private Member author;
 * private String title;
 * private String text;
 * private List<Comment> comments;
 * private List<String> imageUrls;
 * private Long views;
 * private Long likes;
 *
 * private LocalDateTime createdDate;
 * private LocalDateTime modifiedDate;
 * private boolean isModified;
 * } </pre>
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
@Table(
        name = "post",
        indexes = {
                @Index(name = "idx_board_id", columnList = "board_id") // board_id 컬럼에 대한 인덱스 추가
        }
)

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "board_id", insertable = false, updatable = false)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = true)
    private Member author;


    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    private Long views = 0L;

    @Builder.Default
    private Long likes = 0L;

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void addImage(String url) {
        imageUrls.add(url);
    }
    public void deleteImage(String url) {
        imageUrls.remove(url);
    }


    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private boolean isModified;

}
