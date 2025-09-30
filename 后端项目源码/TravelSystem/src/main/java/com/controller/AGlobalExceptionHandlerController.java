package com.controller;

import com.entity.vo.ResultVO;
import com.enums.StatusCode;
import com.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;

/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 18:49:09
 * @Description: 异常处理
 * @Version: 1.0
 */
@RestControllerAdvice
public class AGlobalExceptionHandlerController extends ABaseController{
    protected final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    public ResultVO handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址{},错误信息:", request.getRequestURI(),e);
        ResultVO resultVO = new ResultVO();
        //404
        if(e instanceof NoHandlerFoundException){
            resultVO.setCode(StatusCode.NOT_FOUND.getCode());
            resultVO.setMessage("请求地址不存在");
            resultVO.setSuccess(false);
        }else if(e instanceof BusinessException){
            //业务错误
            resultVO.setCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
            resultVO.setMessage("业务错误");
            resultVO.setSuccess(false);
        }else if(e instanceof IllegalArgumentException){
            //参数错误
            resultVO.setCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
            resultVO.setMessage("参数错误");
            resultVO.setSuccess(false);
        }else if(e instanceof BindException){
            resultVO.setCode(StatusCode.BAD_REQUEST.getCode());
            resultVO.setMessage(e.getMessage());
            resultVO.setSuccess(false);
        } else if (e instanceof DuplicateKeyException) {
            resultVO.setCode(StatusCode.BAD_REQUEST.getCode());
            resultVO.setMessage("数据库中已存在该数据");
            resultVO.setSuccess(false);

        } else{
            resultVO.setCode(StatusCode.INTERNAL_SERVER_ERROR.getCode());
            resultVO.setMessage("系统错误");
            resultVO.setSuccess(false);
        }

        return resultVO;
    }

}
