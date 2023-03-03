package cafe_in.cafe_in.repository.review

class ReviewSql {
    public static final String INSERT_REVIEW = """

    """

    public static final String SELECT_REVIEW = """
        SELECT REVIEWID, MEMBERID, TITLE, CONTENTS, ISBN, BOOKTITLE, CREATEDDATE, UPDATEDDATE FROM REVIEW
        WHERE 1=1
    """
}
