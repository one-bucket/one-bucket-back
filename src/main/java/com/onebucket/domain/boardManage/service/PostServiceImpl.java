package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;

import com.onebucket.domain.boardManage.dto.internal.comment.GetCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.entity.Board;

import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;

import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;

import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : PostServiceImpl
 * <br>date           : 2024-07-18
 * <pre>
 * <span style="color: white;">[description]</span>
 * post 에 대한 service layer. CRUD 및 조회수 증가를 포함하고 있다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * @see PostRepository
 * @see BoardRepository
 * @see MemberRepository
 * @see SecurityUtils
 * @see CommentRepository
 * @see RedisRepository
 */


@Service
public class PostServiceImpl extends AbstractPostService<Post, PostRepository> implements PostService {


    public PostServiceImpl(PostRepository repository,
                           BoardRepository boardRepository,
                           MemberRepository memberRepository,
                           SecurityUtils securityUtils,
                           CommentRepository commentRepository,
                           RedisRepository redisRepository,
                           LikesMapRepository likesMapRepository) {
        super(repository, boardRepository, memberRepository, securityUtils,
                commentRepository, redisRepository, likesMapRepository);
    }

    @Override
    protected Post convertCreatePostDtoToPost(CreatePostDto dto) {
        Member member = findMember(dto.getUsername());
        Board board = findBoard(dto.getBoardId());

        return Post.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .author(member)
                .board(board)
                .build();
    }

    @Override
    protected PostThumbnailDto convertPostToThumbnailDto(Post post) {

        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if(member != null) {
            nickname = member.getNickname();
        }

        return PostThumbnailDto.builder()
                .views(post.getViews())
                .likes(post.getLikes())
                .title(post.getTitle())
                .text(post.getText())
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .postId(post.getId())
                .build();
    }

    @Override
    protected PostInfoDto convertPostToPostInfoDto(Post post, List<GetCommentDto> comments) {
        String nickname = "(unknown)";
        Member member = post.getAuthor();
        if (member != null) {
            nickname = member.getNickname();
        }
        return PostInfoDto.builder()
                .views(post.getViews())
                .likes(post.getLikes())
                .title(post.getTitle())
                .text(post.getText())
                .boardId(post.getBoardId())
                .authorNickname(nickname)
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .comments(comments)
                .build();
    }
}
