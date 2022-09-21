
package com.resourcing.controller;

import java.nio.file.AccessDeniedException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import javax.persistence.NonUniqueResultException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jmx.MBeanServerNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionClass extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NonUniqueResultException.class)
	public String handleNonUniqueResultException(NonUniqueResultException nonUniqueResultException) {
		return "error";
	}

	@ExceptionHandler(ClassNotFoundException.class)
	public String handelException(ClassNotFoundException classNotFoundException) {
		return "error";
	}

	@ExceptionHandler(IllegalAccessException.class)
	public String handelIllegalAccessException(IllegalAccessException illegalAccessException) {
		return "error";
	}

	@ExceptionHandler(NoSuchFieldException.class)
	public String handelNoSuchFieldException(NoSuchFieldException noSuchFieldException) {
		return "exceptionhandle";
	}

	@ExceptionHandler(NoSuchMethodException.class)
	public String handelNoSuchMethodException(NoSuchMethodException noSuchFieldException) {
		return "error";
	}

	@ExceptionHandler(InterruptedException.class)
	public String handelInterruptedException(InterruptedException interruptedException) {
		return "error";
	}

	@ExceptionHandler(IndexOutOfBoundsException.class)
	public String handelIndexOutOfBoundsException(IndexOutOfBoundsException indexOutOfBoundsException) {
		return "error";
	}

	@ExceptionHandler(IllegalThreadStateException.class)
	public String handelIllegalThreadStateException(IllegalThreadStateException illegalThreadStateException) {
		return "error";
	}

	@ExceptionHandler(InputMismatchException.class)
	public String handelInputMismatchException(InputMismatchException inputMismatchException) {
		return "error";
	}

	@ExceptionHandler(NumberFormatException.class)
	public String handelNumberFormatException(NumberFormatException numberFormatException) {
		return "error";
	}

	@ExceptionHandler(ArithmeticException.class)
	public String handelArithmeticException(ArithmeticException ArithmeticException) {
		return "error";
	}

	@ExceptionHandler(NoSuchElementException.class)
	public String handelNoSuchElementException(NoSuchElementException NoSuchElementException) {
		return "error";
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String handelAccessDeniedException(AccessDeniedException AccessDeniedException) {
		return "error";
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public String handelAccessDeniedException(EmptyResultDataAccessException emptyResultDataAccessException) {
		return "error";
	}

	@ExceptionHandler(NullPointerException.class)
	public String handelNullPointerException(NullPointerException nullPointerException) {
		return "error";
	}

	@ExceptionHandler(MBeanServerNotFoundException.class)
	public String handelMBeanServerNotFoundException(MBeanServerNotFoundException MBeanServerNotFoundException) {
		return "error";
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView globalExceptionHandler(Exception e) {
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}

	// 22-08-2022 testing

	/*
	 * @ExceptionHandler(DataAccessException.class)
	 * 
	 * @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason =
	 * "The session has expired") public String
	 * handleSessionExpired(DataAccessException ex) {
	 * System.out.println("invoked:::::"); return "homepage"; }
	 */

}
