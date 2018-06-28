package com.spring.fileup;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class Upload {
	
	public boolean fileUpload(MultipartHttpServletRequest mRequest){ //MultipartHttpServletRequest는 업로드에 의해 서버에 온 값
		boolean isUpload = false;
		
		String uploadPath = "C:\\javaStudy\\uploadpractice\\"; //서버에 저장될 위치(이 값은 정확히 서버에 저장될 위치로 사용됨)
		
		Iterator<String> iterator = mRequest.getFileNames(); //Iterator<String> 반복자
		
		while(iterator.hasNext()){ //파일 이름이 없을 때까지 계속
			String uploadFileName = iterator.next();
			
			MultipartFile mFile = mRequest.getFile(uploadFileName); //mfile은 임시 파일, WAS(톰캣) 안에 임시 경로에 저장되어 있는 파일
			String originFileName = mFile.getOriginalFilename(); //올리는 파일 이름
			String saveFileName = originFileName; //서버에 저장 될 파일명(실제 저장되는 파일명)
			
			if(saveFileName != null && !saveFileName.equals("")){
				if(new File(uploadPath + saveFileName).exists()){ //경로+파일명 존재한다면(중복된다면)
					//저장되는 파일명
					saveFileName = System.currentTimeMillis() + "_" + saveFileName; //시스템의 현재 시간의 천분의 1초까지 나타난 시간을 파일명에 넣어줌.
				}//if()
				
				try {
					//실제 하드디스크에 저장하는 과정
					mFile.transferTo(new File(uploadPath+saveFileName)); //여기에서 실제 서버에 저장하게 됨.
					isUpload=true;
				} catch (IllegalStateException e) {
					e.printStackTrace();
					isUpload=false;
				} catch (IOException e2){
					e2.printStackTrace();
					isUpload = false;
				}				
			}//if()
		}//while()
		
		
		return isUpload; //정상적으로 업로드하면 1(true), 실패시 0(false) 리턴함.
	}//fileUpload
}
