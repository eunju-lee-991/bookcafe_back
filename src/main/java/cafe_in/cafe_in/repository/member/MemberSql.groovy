package cafe_in.cafe_in.repository.member

import lombok.Builder

class MemberSql {
    public static final String INSERT_MEMBER = """
        INSERT INTO MEMBER(ID, NAME, NICKNAME, EMAIL, JOINDATE) 
        VALUES (:id, :name, :nickname, :email, :joinDate)  
    """
    // 파라미터는 대소문자 구분해야한다!

    public static final String SELECT_MEMBER_ALL = """
        SELECT ID, NAME, NICKNAME, EMAIL, JOINDATE FROM MEMBER
    """

    public static final String SELECT_MEMBER_ALL_ORDER = """
        SELECT ID, NAME, NICKNAME, EMAIL, JOINDATE FROM MEMBER
        ORDER BY :order
    """

    public static final String SELECT_MEMBER_BY_ID = """
        SELECT ID, NAME, NICKNAME, EMAIL, JOINDATE FROM MEMBER 
        WHERE ID = :id
    """

    public static final String SELECT_MEMBER_BY_NAME = """
        SELECT ID, NAME, NICKNAME, EMAIL, JOINDATE FROM MEMBER 
        WHERE NAME = :name
    """

    public static final String DELETE_MEMBER = """
        DELETE FROM MEMBER
        WHERE ID = :id
    """
}
