package com.example.spring.spring.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ScriptUtil {
        public static void init(HttpServletResponse response){
            response.setContentType("text/html; charset=euc-kr");
            response.setCharacterEncoding("euc-kr");
        }

        public static void alert(HttpServletResponse response, String alertText) throws IOException {
            init(response);
            PrintWriter out = response.getWriter();
            out.println("<script>alert('" + alertText + "');</script>");
            out.flush();
        }

        public static void alert_location(HttpServletResponse response, String alertText, String location) throws IOException{
            init(response);
            PrintWriter out = response.getWriter();
            out.println("<script>alert('" + alertText + "'); location.href='"+location+"'; </script>");
            out.flush();
        }
        public static void alert_back(HttpServletResponse response, String alertText) throws IOException{
            init(response);
            PrintWriter out = response.getWriter();
            out.println("<script>alert('" + alertText + "');  history.go(-1);</script>");
            out.flush();
        }

}
