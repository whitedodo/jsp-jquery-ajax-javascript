package com.example.web.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.web.controller.Controller;
import com.example.web.util.HttpUtil;

public class MultiUploadController implements Controller {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		HttpUtil.uploadFile(req, res);
		
		/*
		res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        
        @SuppressWarnings("unchecked")
		List<Object> fileInfoList = (List<Object>) req.getAttribute("fileInfoList");
        @SuppressWarnings("unchecked")
		List<String> reqInfoList = (List<String>) req.getAttribute("reqInfoList");
        
        // Model 구성해서 만들어도 무방
        HttpUtil.getFileinfoParser(fileInfoList, null);
        HttpUtil.getReqinfoParser(reqInfoList, null);
		
		*/

		res.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		out.print("success");
		
		out.flush();
		out.close();
		
	}

}
