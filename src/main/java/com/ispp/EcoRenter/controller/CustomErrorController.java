//package com.ispp.EcoRenter.controller;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class CustomErrorController implements ErrorController  {
// 
//	@RequestMapping("/error")
//	public String handleError(HttpServletRequest request) {
//    	Object status;
//    	String errorView;
//    			
//    	status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//    	errorView = "miscellaneous/error";
//        
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//         
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                errorView = "miscellaneous/error-404";
//            }
//        }
//        
//        return errorView;
//	}
// 
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//}