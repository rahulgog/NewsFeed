package com.cts.exception;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cts.entity.ResponseMessage;


public class ExeceptionHandle {
	   private static final Logger LOGGER = LoggerFactory.getLogger(ExeceptionHandle.class);
	   String defaultMessage="";
	   @ExceptionHandler(Exception.class)
	   @ResponseStatus(code=HttpStatus.BAD_REQUEST)
	   public ResponseEntity<ResponseMessage> handle(Exception e){
		   LOGGER.error(e.getMessage());
		 
		   if(e instanceof MethodArgumentNotValidException){
	
			   MethodArgumentNotValidException ex =(MethodArgumentNotValidException) e;
			   BindingResult bindingResult = ex.getBindingResult();
			   LOGGER.error(""+ex.getBindingResult());
			   List<FieldError> fieldError = bindingResult.getFieldErrors();
			   fieldError.stream().forEach(err->{
				   String test= err.getField()+" "+err.getDefaultMessage()+",\n";
				   defaultMessage+=test;
				   
			   });
			   return new ResponseEntity<ResponseMessage>(new ResponseMessage(
					   defaultMessage.substring(0,defaultMessage.length()-1)),HttpStatus.BAD_REQUEST);
			   
		   }
		   if(e instanceof BadCredentialsException){
			 String  defaultMessage1="error:";
			   BadCredentialsException ex =(BadCredentialsException) e;
			   
			   return new ResponseEntity<ResponseMessage>(new ResponseMessage(
					   defaultMessage1.substring(0,defaultMessage1.length()-1)),HttpStatus.BAD_REQUEST);
			   
		   }
		   if(e instanceof ConstraintViolationException){
			   ConstraintViolationException ex =(ConstraintViolationException) e;
			   Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
			   String errorMessage="Validation Failed:";
			   for(ConstraintViolation<?> constratintViolation:set){
				   errorMessage+=constratintViolation.getMessageTemplate()+",";
			   }
			   return new ResponseEntity<ResponseMessage>(new ResponseMessage(
					   errorMessage.substring(0,errorMessage.length()-1)),HttpStatus.BAD_REQUEST);
			   
		   }
		   if(e instanceof InternalAuthenticationServiceException){
			   InternalAuthenticationServiceException ex= (InternalAuthenticationServiceException) e;
			  String defaultMessage2=ex.getMessage();
			   return new ResponseEntity<ResponseMessage>(new ResponseMessage(
					   defaultMessage2),HttpStatus.BAD_REQUEST);
		   }
		   
		   
		  
		return new ResponseEntity<ResponseMessage>(new ResponseMessage(
				e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		   
	   }
	   
	   
	   
	}