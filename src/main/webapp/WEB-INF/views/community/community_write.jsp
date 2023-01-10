<%--
  Created by IntelliJ IDEA.
  User: whdud
  Date: 2022-07-12
  Time: 오후 1:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">

    </script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <title>board</title>
    <script>    $(document).on('click', '#btnSave', function (e) {
        e.preventDefault();
        $("#form").submit();
    });
    $(document).on('click', '#btnList', function (e) {
        e.preventDefault();
        location.href = "${pageContext.request.contextPath}/board/getBoardList";
    });</script>
    <style>body {
          padding-top: 70px;
          padding-bottom: 30px;
    }</style>
</head>
<body>
<article>
    <div class="container mt-5" role="main"><h2>게시판</h2>
        <form name="form" id="form" role="form" method="post"
              action="/community/write">
            <div class="mb-3"><label for="title">제목</label> <input type="text" class="form-control" name="title"
                                                                   id="title" placeholder="제목을 입력해 주세요"></div>
            <div class="mb-3"><label for="content">내용</label> <textarea class="form-control" rows="5" name="content"
                                                                        id="content"
                                                                        placeholder="내용을 입력해 주세요"></textarea></div>
            <div class="mb-3"><label for="tag">TAG</label> <input type="text" class="form-control" name="tag" id="tag"
                                                                  placeholder="태그를 입력해 주세요"></div>
        <div>
            <button type="submit" class="btn btn-sm btn-primary">저장</button>
            <button type="button" class="btn btn-sm btn-primary" id="btnList">뒤로가기</button>
        </div>
        </form>
    </div>
</article>
</body>
</html>