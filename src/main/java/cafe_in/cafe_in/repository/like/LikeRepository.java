package cafe_in.cafe_in.repository.like;

import cafe_in.cafe_in.domain.Like;

import java.util.Optional;

public interface LikeRepository {
    public Long createLike(Like like);
    public Optional<Like> findLike(Long reviewId, Long memberId);

    public int deleteLike(Long likeId);

    public int getCountForLikeId(Long likeId);
}
