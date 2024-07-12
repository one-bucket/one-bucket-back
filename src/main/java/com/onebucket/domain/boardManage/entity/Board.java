package com.onebucket.domain.boardManage.entity;

import com.onebucket.domain.universityManage.domain.University;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : Board
 * <br>date           : 2024-07-12
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
 * 2024-07-12        jack8              init create
 * </pre>
 */

@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "board_type_id")
    private BoardType boardType;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
