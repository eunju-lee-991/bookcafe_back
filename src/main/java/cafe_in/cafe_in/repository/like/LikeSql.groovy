package cafe_in.cafe_in.repository.like

class LikeSql {
    public static final String INSERT_LIKE = """
        INSERT INTO LIKE_REVIEW(REVIEWID, MEMBERID)
        VALUES (:reviewId, :memberId)
    """

    public static final String SELECT_LIKE = """
        SELECT LIKEID, REVIEWID, MEMBERID, CLICKDATE
        FROM LIKE_REVIEW
        WHERE REVIEWID = :reviewId
        AND MEMBERID = :memberId
    """

    public static final String DELETE_LIKE = """
        DELETE FROM LIKE_REVIEW
        WHERE LIKEID = :likeId
    """
    public static final String SELECT_COUNT_LIKEID = """
        SELECT COUNT(LIKEID)
        FROM LIKE_REVIEW
        WHERE LIKEID = :likeId
    """
}
