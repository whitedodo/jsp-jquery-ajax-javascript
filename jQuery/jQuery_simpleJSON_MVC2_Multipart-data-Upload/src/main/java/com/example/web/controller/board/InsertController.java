package com.example.web.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.web.controller.Controller;
import com.example.web.util.HttpUtil;

public class InsertController implements Controller {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		HttpUtil.forward(req, res, "/WEB-INF/views/board/result.jsp");

	}

}
