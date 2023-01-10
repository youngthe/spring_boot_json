<%--
  Created by IntelliJ IDEA.
  User: whdud
  Date: 2022-07-10
  Time: 오후 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script>
    function Checkform(){

        if(form.id.value == ""){
            form.id.focus();
            alert("id를 입력하세요 ! ");
            return false;
        }else if(form.pw.value == ""){
            form.pw.focus();
            alert("pw를 입력하세요 ! ");
            return false;
        }else if(form.pw1.value == ""){
            form.pw1.focus();
            alert("pw를 입력하세요 ! ");
            return false;
        }


        if(form.pw.value != form.pw1.value){
            alert("입력한 비밀번호가 동일하지 않습니다.");
            return false;
        }else{
            return true;
        }
    }
</script>
<head>
    <title>계정 만들기</title>

</head>

<body>
<form name="form" action="/register" method = "post" onsubmit=" return Checkform() ">
    <table>
        <tr>
            <td>ID : </td><td><input type="text" name="id" id="id"></td>
        </tr>
        <tr>
            <td>PW : </td><td><input type="password" name="pw" id="pw"></td>
        </tr>
        <tr>
            <td>PW one more : </td><td><input type="password" name="pw1" id="pw1"></td>
        </tr>
        <tr>
            <td>NAME : </td><td><input type="text" name="name" id="name"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="회원가입"></td>
        </tr>
    </table>
</form>
</body>
</html>
