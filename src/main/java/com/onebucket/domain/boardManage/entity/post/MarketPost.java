package com.onebucket.domain.boardManage.entity.post;


import com.onebucket.domain.tradeManage.entity.GroupTrade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity
 * <br>file name      : MarcketPost
 * <br>date           : 2024-07-12
 * <pre>
 * <span style="color: white;">[description]</span>
 * 거래 게시판에 포함되는 정보에 대한 엔티티. 테이블 자체는 실제로 Post 테이블에 포함되어 있다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *     private String item;
 *     private int joins;
 *     private int wanted;
 *     private boolean isFin;
 *     private String location;
 *
 *
 *     private Long price;
 *     private Long count;
 * } </pre>
 */

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class MarketPost extends Post {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private GroupTrade groupTrade;
}
