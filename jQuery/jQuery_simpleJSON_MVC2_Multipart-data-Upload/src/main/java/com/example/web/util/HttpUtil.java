package com.example.web.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class HttpUtil extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String charset = null;										// 문자열

    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  	// 3MB		// 
    private static final long MAX_FILE_SIZE      = 1024 * 1024 * 40; 	// 40MB
    private static final long MAX_REQUEST_SIZE   = 1024 * 1024 * 50; 	// 50MB
 
	private static String UPLOAD_FOLDER = "upload";
	private static String UPLOAD_TMP_FOLDER = File.separator + "WEB-INF" + File.separator + "temp";
	private static List<String> reqInfoList = null;								// req 정보(MultiRequest) - 문맥 방식
	private static List<Object> fileInfoList = null; 							// 다중 파일 지원 - 문맥 방식
	private static Map<String, Object> reqInfoMap = null;						// req 정보(MultiRequest) - Map 자료구조
	private static Map<Integer, Map<String, Object>> fileInfoMap = null; 		// 다중 파일 지원 - Map 자료구조
	private static boolean FILE_TOKEN_CHECK = false;							// req 토큰 체크하기 
	private static String FILE_AUTH_TOKEN_KEY = null;							// req 인증키 추가
	private static String FILE_RESTRICT_EXT = ".jpg.jpeg.bmp.png.gif.txt";		// 파일 확장자 제한
	private static int reqNum = 0;							// reqNum
	private static int fileNum = 0;							// fileNum

	public static void forward(HttpServletRequest req, HttpServletResponse res,
			String path) throws ServletException, IOException {

		try {
			RequestDispatcher dispatcher = req.getRequestDispatcher(path);
			dispatcher.forward(req, res);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void uploadFile(HttpServletRequest req, HttpServletResponse res) throws
		ServletException, IOException {

		reqInfoList = new ArrayList<String>();
		fileInfoList = new ArrayList<Object>();

		fileInfoMap = new HashMap<Integer, Map<String, Object>>(); 
		reqInfoMap = new HashMap<>();
		
		// 파일 인증키 인증 완료 후 업로드
		List<FileItem> tmpItems = new ArrayList<FileItem>();
		FILE_AUTH_TOKEN_KEY = "1";
		
		reqNum = 1;
		fileNum = 1;
	    
	    PrintWriter out = res.getWriter();
	    //out.println("<HTML><HEAD><TITLE>Multipart Test</TITLE></HEAD><BODY>");
		
		try {
	        
	        //디스크상의 프로젝트 실제 경로얻기
	        //String contextRootPath = "c:" + File.separator + "upload";
			String dirName = UPLOAD_FOLDER ; 
			// String dirName = "upload"; 
			String contextRootPath = req.getSession().getServletContext().getRealPath("/") + dirName;
						
	        System.out.println("실제경로:" + contextRootPath);
	        
	        //1. 메모리나 파일로 업로드 파일 보관하는 FileItem의 Factory 설정
	        DiskFileItemFactory diskFactory = new DiskFileItemFactory(); 	 // 디스크 파일 아이템 공장
	        diskFactory.setSizeThreshold(MEMORY_THRESHOLD); 				 // 업로드시 사용할 임시 메모리
	        // diskFactory.setSizeThreshold(4096); 						 	 // 업로드시 사용할 임시 메모리
	        diskFactory.setRepository(new File(contextRootPath + UPLOAD_TMP_FOLDER)); 		// 임시저장폴더
	        // diskFactory.setRepository(new File(contextRootPath + UPLOAD_TMP_FOLDER));	// 임시저장폴더
	        
	        //2. 업로드 요청을 처리하는 ServletFileUpload생성
	        ServletFileUpload upload = new ServletFileUpload(diskFactory);
	
	        // upload.setSizeMax(3 * 1024 * 1024); //3MB : 전체 최대 업로드 파일 크기
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	        
	        // sets maximum size of upload file(파일 단위 업로드 크기)
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	        
	        //3. 업로드 요청파싱해서 FileItem 목록구함​​
	        List<FileItem> items = upload.parseRequest(req); 
	
	        Iterator<FileItem> iter = items.iterator(); //반복자(Iterator)로 받기​            
	        while(iter.hasNext()) { //반목문으로 처리​    
	            FileItem item = (FileItem) iter.next(); //아이템 얻기
	             //4. FileItem이 폼 입력 항목인지 여부에 따라 알맞은 처리
	            
	            if(item.isFormField()){ 
	            	//파일이 아닌경우
	                processFormField(out, item);
	                
	            } else {
	            	//파일인 경우
	            	// System.out.println("오류:" + item.getName());
	            	
	            	// 버그 개선 item 이름값 비어있을 때
	            	if ( item.getName() != "") {
	            		tmpItems.add(item);
	            		// processUploadFile(out, item, contextRootPath);
	            	}
	            	// System.out.println("오류2:");
	            }
	        }
	        
	        // 파일 토큰 확인될 경우
	        if ( FILE_TOKEN_CHECK ) {
	        	
	        	// 파일 업로드 처리
	        	for (FileItem readyItem: tmpItems)
	        	{
	        		processUploadFile(out, readyItem, contextRootPath);
	        	}
	        	
	        }
	        
	        
	        
	    } catch(Exception e) {
	        e.printStackTrace(out);
	    }
		
		//out.println( "usrID(Map): " + reqMap.get("usrID") );
		//out.println( "usrPasswd(Map):" + reqMap.get("usrPasswd") );
	    
	    // out.println("</BODY></HTML>");
		
		// req.setAttribute("usrID", reqMap.get("usrID"));
		// req.setAttribute("login", 1);//Object Type으로 넘어감
	    req.setAttribute("reqInfoMap", reqInfoMap);
	    req.setAttribute("fileInfoMap", fileInfoMap);
	    req.setAttribute("reqInfoList", reqInfoList);
	    req.setAttribute("fileInfoList", fileInfoList);
	    
		// System.out.println("오류3:" + reqMap.get("usrID"));
	    
	}
	
	//업로드한 정보가 파일인경우 처리
	private static void processUploadFile(PrintWriter out, FileItem item, String contextRootPath)
			throws Exception {
		
		Map<String, Object> fileMapNode = new HashMap<String, Object>();
		List<String> fileListNode = new ArrayList<String>();
		
		boolean resultUpload = false;
	
		String dirName = UPLOAD_FOLDER ; 
		String name = item.getFieldName(); 					// 파일의 필드 이름 얻기
		String fileName = item.getName();					 	// 파일명 얻기
		
		// 임시 - 실제 원본 이름 추출
		File originalFile = new File(fileName);
		String originalFileName = originalFile.getName();
		
		System.out.println("임시:" + originalFileName );
		
		String contentType = item.getContentType();		// 컨텐츠 타입 얻기
		long fileSize = item.getSize(); 								// 파일의 크기 얻기
		
		// 업로드 파일명을 현재시간으로 변경후 저장
		String fileExt = fileName.substring(fileName.lastIndexOf("."));
		String uploadedFileName = System.currentTimeMillis() + ""; 
		System.out.println(fileExt);
		System.out.println(uploadedFileName);
		
		// 저장할 절대 경로로 파일 객체 생성
		String realUploadFile = File.separator + dirName + File.separator + uploadedFileName;
		System.out.println("실제 저장직전폴더:" + contextRootPath + realUploadFile);
		File uploadedFile = new File(contextRootPath + realUploadFile);
		
		if ( FILE_RESTRICT_EXT.contains(fileExt.toLowerCase()) ) {
			item.write(uploadedFile); 		// 파일 저장
			resultUpload = true;			// 파일 업로드 완료
		}
		
		//========== 뷰단에 출력 =========//
		//out.println("<P>");
		//out.println("파라미터 이름:" + name + "<BR>");
		//out.println("파일 이름:" + fileName + "<BR>");
		//out.println("콘텐츠 타입:" + contentType + "<BR>");
		//out.println("파일 사이즈:" + fileSize + "<BR>");
		
		//확장자가 이미지인겨우 이미지 출력
		/*
		if(".jpg.jpeg.bmp.png.gif".contains(fileExt.toLowerCase())) {
				out.println("<IMG SRC='upload/" 
					+ uploadedFileName 
					+ "' width='300'><BR>");
		}
		*/
		
		// out.println("</P>");
		// out.println("<HR>");
		// out.println("실제저장경로 : "+uploadedFile.getPath()+"<BR>");
		// out.println("<HR>");
		
		// 파일 전송이 완료되었을 때
		if ( resultUpload == true ) {
		
			// 파일 정보(Map 자료구조 방식)
			fileMapNode.put("name", name);
			fileMapNode.put("fileName", originalFileName);
			fileMapNode.put("contentType", contentType);
			fileMapNode.put("fileSize", fileSize);
			fileMapNode.put("fileExt", fileExt);
			fileMapNode.put("uploadedFileName", uploadedFileName);
			fileMapNode.put("realName", uploadedFile.getName());
			fileMapNode.put("realPath", uploadedFile.getPath());
			
			// 파일 정보(문자열 구분 방식)
			fileListNode.add( "id:" + fileNum + ",name:name,value:" + name + "/" );
			fileListNode.add( "id:" + fileNum + ",name:filename,value:" + originalFileName + "/" );
			fileListNode.add( "id:" + fileNum + ",name:contentType,value:" + contentType + "/" );
			fileListNode.add( "id:" + fileNum + ",name:fileSize,value:" + fileSize + "/" );
			fileListNode.add( "id:" + fileNum + ",name:fileExt,value:" + fileExt + "/" );
			fileListNode.add( "id:" + fileNum + ",name:uploadedFileName,value:" + uploadedFileName + "/" );
			fileListNode.add( "id:" + fileNum + ",name:realName,value:" + uploadedFile.getName() + "/" );
			fileListNode.add( "id:" + fileNum + ",name:realPath,value:" + uploadedFile.getPath() + "/" );
			
			fileInfoList.add(fileListNode);
			
			fileNum++;
		}
	}
	
	private static void processFormField(PrintWriter out, FileItem item) 
		throws Exception{
		
		String name = item.getFieldName(); //필드명 얻기
		Object value = item.getString("UTF-8"); //UTF-8형식으로 필드에 대한 값읽기
		
		//out.println(name + ":" + value + "<BR>"); //출력
		
		// 파일 토큰 인증 여부
		if ( name.contentEquals("token")
				&& value.equals(FILE_AUTH_TOKEN_KEY) ) {
			System.out.println("참 - 인증");
			FILE_TOKEN_CHECK = true;
		}
		
		// Map 자료구조 저장
		reqInfoMap.put(name, value);
		
		// 문자열 구문 저장 - 문자열 검색원리 적용	(검색 기법 적용)
		reqInfoList.add( "id:" + reqNum + ",name:" + name + ",value:" + value + "/" );
		reqNum++;
		
	}
	
	// FileInfoList 내용 - 문맥 문석기(임시)
	public static void getFileinfoParser(List<Object> fileParser, String tarName) {
		
		// 분석 시작
		if ( fileParser != null ) {
			
			for ( Object fileNode : fileParser ) {
				
				@SuppressWarnings("unchecked")
				List<String> sep = (List<String>) fileNode;
				
				// 문자열 분석 반환 받기 (id = 0, name = 1, value = 2)
				Object result = getFileAttrAnal(sep, tarName);
				
			}
			
			
		}
		
	}

	// FileInfoList 내용 - 문자열 구분
	private static Object getFileAttrAnal(List<String> fileNode, String tarName) {
		
		int pId = -1;
		String pName = null;
		Object pValue = null;
		
		String trashTxt = null;
		
		if ( fileNode != null) {
			
			for ( String usrTxt : fileNode  ) {
				
				trashTxt = usrTxt.substring(usrTxt.indexOf("id:") + 3, usrTxt.indexOf(",name") );
				pId = Integer.valueOf(trashTxt);
				// System.out.print("ID:" + pId);
				

				trashTxt = usrTxt.substring(usrTxt.indexOf("name:") + 5, usrTxt.indexOf(",value") );
				pName = trashTxt;
				// System.out.print(",name:" + pName);
				
				trashTxt = usrTxt.substring(usrTxt.indexOf("value:") + 6, usrTxt.indexOf("/") );
				pValue = trashTxt;
				// System.out.println(",value:" + pValue);
				
			}
			
		}
		
		return null;
		
	}

	// ReqInfoList 내용 - 문맥 문석기(임시)
	public static Object getReqinfoParser(List<String> reqNode, String tarName) {
		
		int pId;
		String pName ;
		Object pValue ;
		
		String trashTxt;
		
		if ( reqNode != null) {
			
			for ( String usrTxt : reqNode  ) {
				
				trashTxt = usrTxt.substring(usrTxt.indexOf("id:") + 3, usrTxt.indexOf(",name") );
				pId = Integer.valueOf(trashTxt);
				System.out.print("ID:" + pId);

				trashTxt = usrTxt.substring(usrTxt.indexOf("name:") + 5, usrTxt.indexOf(",value") );
				pName = trashTxt;
				System.out.print(",name:" + pName);
				
				trashTxt = usrTxt.substring(usrTxt.indexOf("value:") + 6, usrTxt.indexOf("/") );
				pValue = trashTxt;
				System.out.println(",value:" + pValue);
				
			}
			
		}
		
		return null;
		
	}	
	
}