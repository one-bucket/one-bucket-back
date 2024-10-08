package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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
     * {@link com.onebucket.domain.boardManage.entity.post.MarketPost MarketPost} 이고 따라서 생성을 위한
     * 매개 변수가 다르다. 이는 {@link PostDto.Create} 와 이를 상속받는
     * {@link com.onebucket.domain.boardManage.dto.parents.MarketPostDto.Create MarketPostDto.Create}
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
     * 기존에 존재하는 post를 삭제한다. {@link ValueDto.FindPost}는 {@code userId}와 {@code postId}
     * 가 존재하는데, postId를 통해 특정 post를 불러와 매개변수로 받은 userId와 비교한다.
     * <br>
     * 이런 권한 문제를 컨트롤러 레이어에서 처리하지 않은 까닭은, 보편적인 데이터에 대한 read 권한이 아닌
     * 어떤 특정 게시글에 대해 단 하나의 유저만이 이러한 삭제 요청을 수행할 수 있기 때문이다.
     * @param dto - userId와 postId가 포함된 dto
     * @throws com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException
     *          자신의 게시글이 아닌 것에 대한 삭제 요청 시
     *
     * @throws com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException
     *          사용자가 null인 경우 - 사용자가 탈퇴 후 옳바르지 않은 사용자가 삭제 요청 시
     */
    void deletePost(ValueDto.FindPost dto);

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
    void addCommentToPost(CreateCommentDto dto);

    /**
     *
     * @param dto
     */
    void deleteCommentFromPost(ValueDto.FindComment dto);
    Page<PostDto.Thumbnail> getPostsByBoard(ValueDto.PageablePost dto);
    PostDto.Info getPost(ValueDto.GetPost dto);
    Page<PostDto.Thumbnail> getPostByAuthorId(ValueDto.AuthorPageablePost dto);
    void increaseViewCount(ValueDto.FindPost dto);
    void increaseLikesCount(ValueDto.FindPost dto);
    void decreaseLikesCount(ValueDto.FindPost dto);
    Long getCommentCount(Long postId);
    Long getLikesInRedis(Long postId);
    boolean isUserLikesPost(ValueDto.FindPost dto);
    void saveImage(MultipartFile multipartFile, SaveImageDto dto);
    Page<PostDto.Thumbnail> getSearchResult(ValueDto.SearchPageablePost dto);
}
