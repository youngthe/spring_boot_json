<%--
  Created by IntelliJ IDEA.
  User: whdud
  Date: 2022-07-12
  Time: 오후 2:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>게시글 상세 보기</title>
    <link href="${path}/resources/css/community_sub.css" rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.6.0.slim.js" integrity="sha256-HwWONEZrpuoh951cQD1ov2HUK5zA5DwJ1DNUXaM6FsY=" crossorigin="anonymous"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa"
            crossorigin="anonymous"></script>
</head>

<section class="notice">
<div class="container">
    <body>
        <table class="board-table">
            <tr>
                <th>글 번호</th><td>${community.id}</td>
                <th>조회수</th><td>${community.hits}</td>
            </tr>
            <tr>
                <th>작성자</th><td>${community.user.name}</td>
                <th>작성시간</th><td>${community.date}</td>
            </tr>
            <tr>
                <th>제목</th><td>${community.title}</td><th>파일</th><td><a location.href="/community/">${community.file_name}</a></td>
            </tr>
            <tr>
                <th>좋아요 여부(0 or 1)</th><td></td> <th><input type="button" class="btn btn-dark m-1" id="" value="좋아요" onclick="location.href='/community/heart/${community.id}'"></th><td></td>
            </tr>
            <tr>
                <td colspan="4">${community.content}</td>
            </tr>
        </table>
        <div class="bottom">
            <c:if test="${user eq community.user.account}">
                <input type="button" class="btn btn-dark m-1" id="modify_button" value="수정하기" onclick="location.href='/community/modify/${community.id}'">
                <input type="button" class="btn btn-dark m-1" id="delete_button" value="삭제하기">
            </c:if>
                <input type="button" class="btn btn-dark m-1" id="back_button" value="돌아가기" onclick="location.href='/community/'">
        </div>
        <div class="input_comment">
            <form action="/community/comments/${community.id}" method="post">
                <input type="text" name="comments">
                <input type="submit" value="작성">
            </form>
        </div>

        <div class="view_comments">
            <table class="table_comments">
                <c:forEach var="comment" items="${comments}" varStatus="status">


                    <c:choose>
                        <c:when test="${a eq comment.parent.id}">
                            <tr>
                                <td id="id">   ${comment.parent.id}</td>
                            </tr>

                            <tr>
                                <td id="date">     ${comment.date}</td>
                            </tr>
                            <tr>
                                <th id="board-comment">${comment.comment}</th>
                            </tr>
                            <tr>
                            <td>
                                <input type="button" value="x" onclick="location.href='/community/comment/delete/${comment.id}'"></td>
                            </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            </table>
                            <table class="table_comments">

                            <tr>
                                <td id="id2">${comment.parent.id}</td>
                            </tr>

                            <tr>
                                <td id="date2">${comment.date}</td>
                            </tr>
                            <tr>
                                <th id="board-comment2"> ${comment.comment}</th>
                            </tr>
                            <tr>
                                <td>
                                    <form action="/community/recomments/${comment.id}" method="post">
                                        <input type="text" name="comments">
                                        <input type="submit" value="댓글">
                                    </form>
                                    <input type="button" value="x" onclick="location.href='/community/comment/delete/${comment.id}'">
                                </td>
                            </tr>
                        </c:otherwise>

                    </c:choose>

                    <c:set var="a" value="${comment.parent.id}"></c:set>

                </c:forEach>
            </table>

        </div>
    </body>
</div>
</section>

<%--Modal--%>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">안내문</h5>
            </div>
            <div class="modal-body">
                정말로 삭제하시겠습니까?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeModalBtn">아니요</button>
                <button type="button" class="btn btn-primary" onclick="location.href='/community/delete/${community.id}'">예</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#delete_button').on('click', function(){
        $('#exampleModal').modal('show');
        console.log("click open");
    });

    $('#closeModalBtn').on('click', function(){
        $('#exampleModal').modal('hide');
        console.log("click close");
    });
</script>
</html>
