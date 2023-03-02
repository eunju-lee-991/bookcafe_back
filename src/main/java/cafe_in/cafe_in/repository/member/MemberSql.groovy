package cafe_in.cafe_in.repository.member

import lombok.Builder

class MemberSql {
    public static final String INSERT_MEMBER = """
        INSERT INTO MEMBER(ID, NICKNAME, EMAIL, JOINDATE) 
        VALUES (:id, :nickname, :email, :joinDate)  
    """
    // 파라미터는 대소문자 구분해야한다!

    public static final String SELECT_MEMBER = """
        SELECT ID,  NICKNAME, EMAIL, JOINDATE FROM MEMBER
        WHERE 1=1
    """

    public static final String WHERE_ID= " AND ID = :id"

    public static final String WHERE_NICKNAME= " AND NICKNAME LIKE :nickname";

    public static final String WHERE_EMAIL= " AND EMAIL LIKE :email"

    public static final String ORDER_BY = " ORDER BY :order"

    public static final String DELETE_MEMBER = """
        DELETE FROM MEMBER
        WHERE ID = :id
    """
}
