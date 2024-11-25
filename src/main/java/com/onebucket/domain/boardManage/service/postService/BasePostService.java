package com.onebucket.domain.boardManage.service.postService;

import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : BasePostService
 * <br>date           : 2024-09-21
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link AbstractPostService} 에 대한 인터페이스이다. 공통된 메서드들에 대한 주석 및 정의를 포함한다.
 * </pre>
 */
public interface BasePostService {

    /**
     * 새로운 post를 생성하는 메서드이다. 다만, 해당 객체가
     * {@link com.onebucket.domain.boardManage.entity.post.Post Post} 혹은
     * {@link com.onebucket.domain.boardManage.entity.post.GroupTradePost MarketPost} 이고 따라서 생성을 위한
     * 매개 변수가 다르다. 이는 {@link PostDto.Create} 와 이를 상속받는
     * {@link com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto.Create MarketPostDto.Create}
     * 으로 구성될 수 있으며, 제네릭을 사용하여 정의하였다.
     * <br>
     * 해당 메서드 내부의 {@code convertCreatePostDtoToPost} 는 {@code protected abstract} 메서드로 정의하였으며
     * 이는 해당 추상 클래스를 상속받는 구현 클래스에서 각 엔티티의 칼럼에 맞게 dto와 엔티티를 매핑시킨다.
     * 이후 제네릭으로 선언된 {@code Repository}의 save 메서드를 이용해 값을 저장한다.
     *
     * @param dto - PostDto.Create 혹은 이를 상속받는 MarketPostDto.Create
     * @return 생성된 게시글에 대한 id
     */
    <D extends PostDto.Create> Long createPost(D dto);

    /**
     * 게시팜 id를 기반으로 해당하는 모든 post에 대한 정보를 반환하는 로직. 보통의 경우, 해당 게시판의
     * 리스트를 생성하고 반환하는데 사용한다. 컨트롤러 레이어로부터 {@code Pageable} 객체를 받아 이를 기반으로
     * 데이터베이스에서 검색하여 {@code Poge} 로 래핑하여 건내준다.
     * <br>
     * 엔티티 자체를 넘겨주기 보단, 엔티티를 {@link PostDto.Thumbnail}로 변환하는데 이를 위해
     * {@code convertPostToTHumbnailDto} 를 사용한다. 이는 추상 메서드이며 구현 클래스에서 이를 구현할
     * 의무를 가진다. 실제 {@code marketPost}의 경우 이를 상속받는
     * {@link com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto.Thumbnail MarketPostDto.Thumnail}
     * 에 대한 매핑을 수행하며, 타입 캐스팅을 통해 컨트롤러에서 변환하여 사용한다.
     * @param dto boardId와 Pageable 이 저장되어 있음
     * @return Post를 dto로 변환한 PstDto.Thumbnail에 대한 Page
     */
    Page<PostDto.InternalThumbnail> getPostsByBoard(PostKeyDto.BoardPage dto);

    /**
     * post에 대한 keyword로 검색한다. dto에는 option 필드가 존재하는데 1은 제목, 2는 본문, 3은 제목 + 본문을
     * 의미한다. 이후 데이터베이스에서 jqpl을 통해 커스터마이징한 repostiory 쿼리문을 통해 검색을 실시한다.
     * @param dto keyword와 option이 저장되어 있다.
     * @return 검색 결과에 대한 PostDto.Thumbnail의 Page로 구성되어 있다.
     */
    Page<PostDto.InternalThumbnail> getSearchResult(PostKeyDto.SearchPage dto);

    /**
     * 사용자가 작성한 게시글 들을 반환한다. 해당 메서드는 추상 클래스에 선언되어 있고 따라서 이에 대한 구현 클래스에서
     * 해당 메서드가 선언되는데, repository는 엔티티 별로 존재하고, 각 구현 클래스마다 별도로 주입되었으므로
     * marketPost와 post가 분리되어 반환된다.
     * @param dto authorId와 pageable 이 저장되어 있다.
     * @return Post.Thumbnail에 대한 page
     */
    Page<PostDto.InternalThumbnail> getPostByAuthorId(PostKeyDto.AuthorPage dto);

    /**
     * postId에 대하여 해당 게시글에 대한 정보를{@link PostDto.Info} 로 반환한다. 해당 사용자가 해당 게시글에
     * 접근할 수 있는 권한이 있는지에 대한 여부는 컨트롤러 레이어에서 판단해야 하며 해당 메서드는 오직 id에 대한 내용을
     * 반환하는 로직만 담아야 한다.
     * <br>
     * 댓글의 경우, 댓글 전체 내용이 아닌, {@code convertToGetCommentDto} 인 private 메서드를 이용하여
     * 필요한 정보만 dto로 매핑한다. reply에 대해(대댓글)서도 재귀적으로 호출하여 해결하도록 한다.
     * @param dto postId만 저장되어 있다.
     * @return PostDto.Info 로 post에 대한 정보를 담고 있다
     */
    <D extends PostDto.Info> D getPost(PostKeyDto.PostKey dto);

    /**
     * 모든 정보를 덮어씌워서 update 한다.
     * @param dto post에 대한 정보가 담겨 있음
     * @return 해당 post의 id
     */
    <D extends PostDto.Update> Long updatePost(D dto);
    /**
     * 어떤 사용자가 게시글을 확인할 때, 이에 대한 view를 증가시키는 메서드. 그러나 post에 대한 모든 get 요청마다
     * 단순히 조회수를 1 올릴 경우 한 유저가 조회수를 계속 올릴 수 있는 문제가 발생할 수 있기에,
     * 쿨타임을 설정하였다. 직접 mysql 에 테이블을 두고 저장하기 보다 redis를 사용하여 사용자의 조회 기록을 관리하고
     * 최대 크기를 설정하여 메모리의 오버헤드를 줄인다.
     * <br>
     * redis의 sortedSet을 사용하여 조회 기록을 관리하였다. 사용자의 id와 조회한 post의 id, 조회할 당시의 시간 기록이다.
     * 시간 기록의 경우, {@code currentTimeMillis} 에 대하여 밀리초를 떼어내고 이를 6자리로 만들어 저장한다.
     * 6자리 넘는 초단위의 시간 차이가 나는 두 데이터가 이상하게 작동할 수 있지만 최대 expire hour를 설정함으로서 방지하였다.
     * 시간을 기준으로 조회 기록을 저장하였고, 메모리의 오버헤드를 줄이기 위해 각 유저 별 저장할 수 있는 최대 조회수({@code MAX_SIZE})
     * 가 넘으면, 가자 오래된 조회기록을 삭제하고 최신의 기록을 낸다. 만약 {@code EXPIRE_HOUR}을 넘긴 데이터가 존재한다면, 이역시 사라진다.
     * @param dto userId와 postId가 저장되어 있다.
     */
    void increaseViewCount(PostKeyDto.UserPost dto);

    /**
     * 각 게시글의 좋아요를 추가하는 메서드이다. 개별적인 {@link com.onebucket.domain.boardManage.entity.LikesMap LikesMap}
     * 을 이용하여 사용자과 게시글 간의 매핑 관게를 정의했으며 해당 테이블에 데이터를 저장/삭제를 통해 데이터를 관리하도록 한다.
     * 또한 이 값을 redis에서 실시간으로 관리하는데 이는 post에 대한 조회가 발생할 시, 매번 해당 테이블에서 count 쿼리를
     * 없애기 위해서이다.
     * <br>
     * redis에 저장된 값은 postId와 likes 인데, 이는 특정 시간대마다 초기화가 되며, 이때
     * {@link com.onebucket.domain.boardManage.entity.LikesMap LikesMap}에서 해당 post에 대한 좋아요 수를 집계한다. 이때 집계할
     * post는 redis에 저장된 key 들에 의해 정의되며 해당 key가 redis에 정의되어 있으면 배치 시스템이 정보를 갱신하여 post에 저장한다.
     * <br>
     * redis에 저장된 값은 이후 {@code Post} 나 이에 대한 {@code Thumbnail}이 조회되면, 현재 {@code Post}에 저장된
     * {@code likes} 값과 redis에 저장된 값을 합쳐 산출하도록 한다.
     * @param dto userId와 postId 가 저장되어 있다.
     */
    void increaseLikesCount(PostKeyDto.UserPost dto);

    /**
     * 좋아요를 철회/삭제하는 메서드이다. 기본적으로 userId와 postId를 받아
     * {@link com.onebucket.domain.boardManage.entity.LikesMap LikesMap}에서 삭제를 요청하며
     * 만약 데이터가 존재하지 않아 삭제가 되지 않는다면 예외를 뱉는다.
     * 데이터 삭제가 성공한 경우 redis에 저장된 값을 1 줄인다. 다만 이는 음수가 될 수 있다.
     * @param dto userId와 postId 가 저장되어 있다.
     * @throws com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException <br>
     *          BoardErrorCode.NOT_EXISTING / db에 해당 데이터가 없어 삭제를 진행할 수 없음.
     */
    void decreaseLikesCount(PostKeyDto.UserPost dto);

    /**
     * 삭제에 대한 권한은 컨트롤러에서 검증하고 , 실질적인 삭제만 담당한다.
     * @param dto - postId만 존재
     */
    void deletePost(PostKeyDto.PostKey dto);

    void deleteImageOnPost(Long postId);

    /**
     * 게시글에 댓글을 추가하는 로직. 혹은 기존에 존재하는 댓글에 대하여 대댓글을 추가하는 로직을 포함한다.
     * 기본적으로 댓글과 대댓글은 허용되지만, 그 이상 즉 대대댓글을 허용하지 않는다.
     * 이는 {@link com.onebucket.domain.boardManage.entity.Comment Comment} 엔티티에 {@code Layer} 칼럼을 통해
     * 확인하는데, 일단 부모 댓글에 대한 필드가 dto에 존재하지 않으면 일반적인 댓글이므로 정상적으로 추가 작업을 수행한다.
     * 기본적으로 {@code Layer} 은 default 값이 0이다.
     * <br>
     * 만약 부모 댓글이 null이 아니고, 존재한다면 해당 부모 댓글을 불러와 부모 댓글의 {@code Layer} 필드를 조사한다. 만약 0이 아니면
     * 부모 댓글은 대댓글이므로 더 이상의 댓글을 추가하지 않는다.
     * 만약 부모 댓글의 Layer가 0이라면 해당 부모 댓글에 대한 대댓글을 생성하고 Layer 값을 1로 설정한다. 댓글은 {@code PostRepository}
     * 에 추가되는데, CASCADE 옵션에 따라 자동적으로 {@link com.onebucket.domain.boardManage.entity.Comment Comment}
     * 테이블에 추가된다. 대댓글의 경우, 바로 {@link com.onebucket.domain.boardManage.entity.Comment Comment} 에 추가된다.
     *
     * <br>
     * {@code @CacheEvict} 라는 어노테이션을 사용하였는데, 이는 댓글 수에 대해 캐싱을 하여 데이터베이스에 대한 cnt문을
     * 최소로 줄이고자 하였다. 즉, 만약 댓글 추가가 해당 게시글에 대해 발생한다면 redis에 저장된 값이 없어진다.
     * @param dto postId와 부모 댓글의 id 및 본문의 내용이 포함
     * @throws com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException <br>
     *          {@code BoardErrorCode.UNKNOWN_COMMENT} / 부모 댓글의 id를 찾아봤지만 존재하지 않을 때 <br>
     *          {@code BoardErrorCode.COMMENT_LAYER_OVERHEAD} / 대대댓글 혹은 그 이상을 요청한 경우
     */
    List<Long> addCommentToPost(CreateCommentDto dto);

    /**
     * 댓글을 지우는 로직. dto로부터 {@code commentId}를 받아 엔티티를 검색한 다음, 해당 comment에 저장된
     * {@code postId}가 dto로 받은 postId에 대해 일치하는지를 확인한 뒤, 테이블에서 제거한다.
     * 이후 {@link com.onebucket.domain.boardManage.entity.post.Post Post} 의 {@code deleteComment}
     * 메서드를 통하여 엔티티에 매핑된 외래키를 제거한다.
     * @param dto postId,commentId가 저장되어 있음
     * @throws com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException <br>
     *          BoardErrorCode.UNKOWN_COMMENT / commentId를 기반으로 검색했으나 해당하는 데이터 없음 <br>
     *          BoardErrorCode.UNKNOWN_POST / dto.postId와 comment의 postId가 일치하지 않음 / postId에 해당하는 레코드 없음
     */
    void deleteCommentFromPost(PostKeyDto.CommentKey dto);


    /**
     * 현재 게시글의 댓글 개수를 반환하는 메서드. 레포지토리에서 postId에 대하여 count문을 이용해 값을 센다.
     * {@link com.onebucket.domain.boardManage.entity.Comment Comment} 테이블에는 {@code postId}
     * 에 대한 {@code index} 가 존재한다.
     * <br>
     * {@code commentCountCache} 라는 key에 대해 캐싱을 사용하였으며 댓글에 대한 생성, 삭제 작업이 있을 때 마다
     * 캐시를 redis에서 제거한다.
     * @param postId 해당 post에 대한 검색
     * @return Long 타입의 댓글 개수를 반환
     */
    Long getCommentCount(Long postId);

    /**
     * Redis에 있는 좋아요 개수에 대한 정보를 가져오는 메서드이다. 일반적으로 좋아요 정보는
     * {@link com.onebucket.domain.boardManage.entity.LikesMap LikesMap} 에 저장되어 있으며
     * 이에 대한 count 문을 최대한 줄이기 위해 좋아요 정보는 1차적으로 redis에 저장되도록 하였다. 이후 일정 시간마다
     * redis를 초기화하고, LikesMap에 대하여 각 {@link com.onebucket.domain.boardManage.entity.post.Post Post}
     * 의 {@code Like} 칼럼을 갱신하도록 한다.
     * @param postId 이를 기반으로 redis의 key를 탐색한다.
     * @return redis에 저장된 좋아요 수를 반환한다.
     */
    Long getLikesInRedis(Long postId);

    /**
     * 해당 게시글에 대하여 유저가 이미 좋아요를 눌렀는지를 확인하고 반환하는 메서드이다.
     * {@link com.onebucket.domain.boardManage.entity.LikesMap LikesMap}에는 {@code userId} 와
     * {@code postId} 에 대한 복합 인덱스가 존재하고 이를 기반으로 검색하여 존재 여부를 확인한다.
     * @param dto postId와 userId가 저장되어 있다.
     * @return 해당 사용자가 해당 post에 좋아요를 눌렀는지(테이블에 값이 저장되어 있는지)에 대해 boolean 반환
     */
    boolean isUserLikesPost(PostKeyDto.UserPost dto);


    /**
     * 이미지를 받아 저장한다. 다만, 해당 메서드는 하나의 이미지에 대하여 처리하므로 컨트롤러 레이어에서 여러 장의 이미지를
     * 처리하기 위해선 해당 메서드를 여러번 호출해야 한다.
     * 이미지의 이름은 클라이언트에서 1차적으로 가공된 이름을 사용하며, 그 경로는 /path/{postId}/image/{imageName}
     * 이다. 다만 파일의 확장자 역시 매개변수로 넘어오며 컨트롤러에서는 이름에서 확장자를 분리하여 개별적으로 넘겨주어야 한다.
     *
     * <br>
     * {@link com.onebucket.global.minio.MinioRepository MinioRepository}에 데이터를 저장하며, 저장된 경로는
     * {@link com.onebucket.domain.boardManage.entity.post.Post Post}의 {@code imageUrls} 필드에
     * 저장된다.
     * @param multipartFile 이미지 파일
     * @param dto 이미지 이름 및 파일 확장자
     * @throws com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException <br>
     *          BoardErrorCode.I_AM_AN_APPLE_PIE
     */
    void saveImage(MultipartFile multipartFile, SaveImageDto dto);

}
