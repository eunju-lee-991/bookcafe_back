package cafe_in.cafe_in.repository.member

import lombok.Builder

class MemberSql {
    public static final String INSERT_MEMBER = """
        INSERT INTO MEMBER(ID, NICKNAME, EMAIL) 
        VALUES (:id, :nickname, :email)
    """

    public static final String SELECT_MEMBER_BY_ID = """
        SELECT ID, NICKNAME, EMAIL FROM MEMBER 
        WHERE ID = :id
    """

}
