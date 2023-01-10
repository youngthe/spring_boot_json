<%--
  Created by IntelliJ IDEA.
  User: whdud
  Date: 2022-07-12
  Time: 오후 1:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>커뮤니티 페이지</title>
    <link href="${path}/resources/css/community_main.css" rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">

    </script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
</head>
<body>
<input type="button" class="btn btn-dark float-right m-3" value="로그아웃" onclick="location.href='/logout'">
<section class="notice">

    <div class="page-title">

        <div class="container">
            <h3>공지사항</h3>

        </div>

    </div>

    <!-- board seach area -->
    <div id="board-search">
        <div class="container">
            <div class="search-window">
                <form action="/community" method="get">
                    <div class="search-wrap">
                        <label for="search" class="blind">공지사항 내용 검색</label>
                        <input type="search" id="search"  name="search" placeholder="검색어를 입력해주세요." value="">
                        <button type="submit" class="btn btn-dark">검색</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- board list area -->
    <div id="board-list">
        <div class="container">
            <table class="board-table">
                <thead>
                <tr>
                    <th scope="col" class="th-num">번호</th>
                    <th scope="col" class="th-title">제목</th>
                    <th scope="col" class="th-date">작성자</th>
                    <th scope="col" class="th-date">등록일</th>
                    <th scope="col" class="th-date">조회수</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="list" items="${community_list}" varStatus="status">
                <tr>

                    <td>${list.id}</td>
                    <th>
                        <a href="/community/detail/${list.id}">${list.title}</a>
                    </th>
                    <td>${list.user.name}</td>

                    <td>${list.date}</td>
                    <td>${list.hits}</td>
                </tr>
                </c:forEach>
<%--                <tr>--%>
<%--                    <td></td>--%>
<%--                    <th></th>--%>

<%--                </tr>--%>
                </tbody>
            </table>

    <div class="writer_btn">
        <input type="button" class="btn btn-dark" onclick="location.href='/community/write'"  value="글 작성">
    </div>
        </div>
    </div>

</section>
</body>
</html>
