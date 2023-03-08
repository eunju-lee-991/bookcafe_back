package cafe_in.cafe_in.service;

import cafe_in.cafe_in.domain.Like;
import cafe_in.cafe_in.exception.ReviewNotFoundException;
import cafe_in.cafe_in.repository.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Long createLike(Like like) {
        Long createdId = null;

            createdId = likeRepository.createLike(like);

        return createdId;
    }

    public Optional<Like> findLike(Long reviewId, Long memberId) {

        return likeRepository.findLike(reviewId, memberId);
    }

    public int deleteLike(Long likeId) {
        checkExistence(likeId);

        int result = likeRepository.deleteLike(likeId);

        if (result > 0) {
            return result;
        } else {
            throw new RuntimeException("LIKE 삭제에 실패하였습니다.");
        }
    }

    public void checkExistence(Long likeId) {
        int result = likeRepository.getCountForLikeId(likeId);
        if (result < 1) {
            throw new NoSuchElementException("존재하지 않는 글번호입니다.");
        }
    }
}
