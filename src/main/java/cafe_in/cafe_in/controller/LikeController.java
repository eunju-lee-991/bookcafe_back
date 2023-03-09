package cafe_in.cafe_in.controller;

import cafe_in.cafe_in.domain.Like;
import cafe_in.cafe_in.dto.like.PostLikeForm;
import cafe_in.cafe_in.dto.like.LikeResponse;
import cafe_in.cafe_in.dto.like.PostLikeResponse;
import cafe_in.cafe_in.exception.BindingFieldFailException;
import cafe_in.cafe_in.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ResponseEntity createLike(@RequestBody @Valid PostLikeForm postLikeForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BindingFieldFailException(bindingResult.getFieldErrors().stream().findFirst().get());
        }
        Long createdLikeid = null;
        Like like = new Like();
        like.setReviewId(postLikeForm.getReviewId());
        like.setMemberId(postLikeForm.getMemberId());

        createdLikeid = likeService.createLike(like);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostLikeResponse(createdLikeid));
    }

    @GetMapping("/likes")
    public LikeResponse findLike(@RequestParam Long reviewId, @RequestParam Long memberId) {
        LikeResponse response = new LikeResponse();

        if (likeService.findLike(reviewId, memberId).isPresent()) {
            Like like = likeService.findLike(reviewId, memberId).get();
            response.setLikeId(like.getLikeId());
            response.setClickDate(like.getClickDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            response.setClicked(true);
        } else {
            response.setLikeId(null);
            response.setClickDate(null);
            response.setClicked(false);
        }

        return response;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/likes/{likeId}")
    public void deleteLike(@PathVariable Long likeId) {
        likeService.deleteLike(likeId);
    }
}
