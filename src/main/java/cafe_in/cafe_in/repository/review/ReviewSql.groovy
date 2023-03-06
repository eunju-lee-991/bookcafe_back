package cafe_in.cafe_in.repository.review

class ReviewSql {
    public static final String INSERT_REVIEW = """
        INSERT INTO REVIEW(MEMBERID, TITLE, CONTENTS, ISBN, BOOKTITLE, CREATEDDATE, UPDATEDDATE) VALUES (
        :memberId, :title, :contents, :isbn, :bookTitle, :createdDate, :updatedDate)
    """

    public static final String SELECT_REVIEW = """
        SELECT REVIEWID, TITLE, BOOKTITLE, CREATEDDATE, UPDATEDDATE, MEMBERID, CONTENTS, ISBN FROM REVIEW
        WHERE 1=1
    """

    public static final String SELECT_REVIEW_JOIN_MEMBER = """
        SELECT A.NICKNAME,
               B.REVIEWID, B.TITLE, B.BOOKTITLE, B.CREATEDDATE, B.UPDATEDDATE, B.MEMBERID, B.CONTENTS, B.ISBN    
        FROM MEMBER A, REVIEW B
        WHERE A.ID = B.MEMBERID
        AND B.REVIEWID = :reviewId
    """

    public static final String SELECT_REVIEW_TOTAL_COUNT = """
        SELECT COUNT(REVIEWID) FROM REVIEW
        WHERE 1=1
    """

    public static final String WHERE_REVIEWID = " AND REVIEWID = :reviewId"

    public static final String WHERE_MEMBERID = " AND MEMBERID = :memberId"

    public static final String WHERE_TITLE= " AND TITLE LIKE :title";

    public static final String WHERE_CONTENTS= " AND CONTENTS LIKE :contents"

    public static final String WHERE_BOOKTITLE= " AND BOOKTITLE LIKE :bookTitle"

    public static final String ORDER_BY = " ORDER BY :order LIMIT :limit OFFSET :offset"

    public static final String UPDATE_REVIEW = """
        UPDATE REVIEW
        SET TITLE = :title, CONTENTS = :contents, UPDATEDDATE = :updatedDate
        WHERE REVIEWID = :reviewId
    """
    public static final String DELETE_REVIEW = """
        DELETE FROM REVIEW
        WHERE REVIEWID = :reviewId
    """
}
