package com.example.news.back.repository;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.entity.DocsUser;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    아래와 같은 선언만으로 UsersRepository 인터페이스 구현 클래스로 생성된 객체가 bean으로 관리됨.
    상속받는 순간 inser,update 메소드 사용 가능
 */
public interface DocsUserRepository extends JpaRepository<DocsUser,Long> {

    public DocsUser getDocsUserByName(String name);

    String name(String name);


    /*
        정렬된 결과를 select 하는 메소드를 custom으로 추가 가능
        단, 정해진 형식이 존재!
        - findAllOrderBy칼럼명Desc()
        - findAllOrderBy칼럼명Asc()
        * 칼럼명은 카멜 케이스로 작성
     */
    
    // 밑에 같은 코드로 명령어 내닐 수 있음(간편)
    //public List<Users> findAll();

    /*
        - from 다음과 entity 별칭은 필수
        - 모든 열 선택 = m
     */

    /*
         Java Persistence Query Language (JPQL)
           - JPQL 은 SQL과 유사하지만 엔티티와 속성에 기반하여 작성되며, 데이터베이스 종속적이지 않음
	       - JPQL 만의 문법이 존재한다
	       - ENTITY 의 name 은 @Entity 어노테이션이 붙어있는 클래스의 이름 혹은 name 속성의 value
	       - select 된 row 의 정보를 ENTITY 혹은 Dto 에 담을 수 있다.

	        @Query(value = "SELECT m FROM Users u ORDER BY u.userId DESC")
	        public List<Member> getList(); // 메소드명은 마음대로 규칙없이
     */




}
