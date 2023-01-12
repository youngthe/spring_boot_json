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
    });
    var inputFileList = new Array();     // 이미지 파일을 담아놓을 배열 (업로드 버튼 누를 때 서버에 전송할 데이터)

    // 파일 선택 이벤트
    $('input[name=images]').on('change', function(e) {
        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);

        // 업로드 된 파일 유효성 체크
        if (filesArr.length > 3) {
            alert("이미지는 최대 3개까지 업로드 가능합니다.");
            $('input[name=images]')val();
            return;
        }

        filesArr.forEach(function(f) {
            inputFileList.push(f);    // 이미지 파일을 배열에 담는다.
        });
    });


    // 업로드 수행
    $('#uploadBtn').on('click', function() {
        console.log("inputFileList: " + inputFileList);
        let formData = new FormData($('#uploadForm')[0]);  // 폼 객체

        for (let i = 0; i < inputFileList.length; i++) {
            formData.append("images", inputFileList[i]);  // 배열에서 이미지들을 꺼내 폼 객체에 담는다.
        }

        $.ajax({
            type:'post'
            , enctype:"multipart/form-data"  // 업로드를 위한 필수 파라미터
            , url: '/test'
            , data: formData
            , processData: false   // 업로드를 위한 필수 파라미터
            , contentType: false   // 업로드를 위한 필수 파라미터
            , success: function(data) {
                alert(data);
            }
            , error: function(e) {
                alert("error:" + e);
            }
        });
    });


    </script>




    <style>body {
          padding-top: 70px;
          padding-bottom: 30px;
    }</style>

</head>



<body>
<article>
    <div class="container mt-5" role="main"><h2>게시판</h2>
        <form name="form" id="form" role="form" method="post" action="/community/testwrite/" enctype="multipart/form-data">
            <div class="mb-3"><label for="title">제목</label> <input type="text" class="form-control" name="title"
                                                                   id="title" placeholder="제목을 입력해 주세요"></div>
            <div class="mb-3"><label for="content">내용</label> <textarea class="form-control" rows="5" name="content"
                                                                        id="content"
                                                                        placeholder="내용을 입력해 주세요"></textarea></div>

            <div class="mb-3"><label for="content">내용</label> <input type="file" name="upload"/></div>

        <div>
            <button type="submit" class="btn btn-sm btn-primary">저장</button>
            <button type="button" class="btn btn-sm btn-primary" id="btnList">뒤로가기</button>
        </div>
        </form>
    </div>
</article>
</body>
</html>