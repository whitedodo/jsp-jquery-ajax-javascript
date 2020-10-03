package com.example.web.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.web.controller.Controller;
import com.example.web.model.CompUsers;

public class ListJSONController implements Controller {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// 1. 공통 선언
		res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        
        // 1-1. ArrayList만 사용할 경우의 반응(방법 1)
        /*
        //  컬렉션 타입
        ArrayList<CompUsers> list = new ArrayList<CompUsers>();
        list.add(new CompUsers("user","mole", 1));
        list.add(new CompUsers("sumin", "pass", 0));
        list.add(new CompUsers("smkim", "php", 1));
      
        PrintWriter out = res.getWriter();

        // 
        JSONObject jsonList = new JSONObject();
        JSONArray itemList = new JSONArray();
        
        jsonList.put("list", list);
        
        out.println(jsonList);
        */
        
        // 1-2. ArrayList와 itemList(JSONArray) 적용
        JSONObject jsonList = new JSONObject();
        JSONArray itemList = new JSONArray();
        
        CompUsers reqUser = new CompUsers();
        
        if ( req.getParameter("username") != null) {
        	reqUser.setUsername(req.getParameter("username"));
        	System.out.println("참" + reqUser.getUsername());
        }
        
        if ( req.getParameter("password") != null)
        	reqUser.setPassword(req.getParameter("password"));
        
        if ( req.getParameter("enanbled") != null)
        	reqUser.setEnabled(Integer.valueOf( req.getParameter("enabled")) );
        
        PrintWriter out = res.getWriter();

        ArrayList<CompUsers> list = new ArrayList<CompUsers>();
        list.add(new CompUsers("user","mole", 1));
        list.add(new CompUsers("sumin", "pass", 0));
        list.add(new CompUsers("smkim", "php", 1));
        
        for (CompUsers user:list) {
        	
        	JSONObject node = new JSONObject();
        	node.put("enabled", user.getEnabled());
        	node.put("password", user.getPassword());
        	node.put("username", user.getUsername());

        	itemList.put(node);
        
        }
        
        jsonList.put("list", itemList);
        jsonList.put("command", "바둑이");
        
        // DTO(또는 Model) 단독으로 입력불가
        // jsonList.put("paramUser", reqUser);
        ArrayList<CompUsers> list2 = new ArrayList<CompUsers>();
        list2.add(reqUser);
        //jsonList.put("paramUser", reqUser);
        jsonList.put("paramUser", list2);
        
        out.println(jsonList);
        
        out.flush();
        out.close();

	}

}
