package com.example.fidledemo.config;


import com.alibaba.fastjson.JSON;
import com.example.fidledemo.BO.Result;
import com.example.fidledemo.BO.ResultCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 * @author 11313
 */
@RestControllerAdvice
public class ExceptionAdvice
{
  /**
   * 请求参数错误
   * @param e
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handle(MethodArgumentNotValidException e)
  {
    e.printStackTrace();
    return JSON.toJSONString(Result.failureResult(ResultCode.PARAM_ERROR));
  }

//  @ExceptionHandler(HttpMessageNotReadableException.class)
//  public String customerException(HttpMessageNotReadableException e)
//  {
//    e.printStackTrace();
//
//    return JSON.toJSONString(Result);
//  }

  /**
   * 其他未知错误
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public String exception(Exception e)
  {
    e.printStackTrace();
    return JSON.toJSONString(Result.failureResult(ResultCode.SERVER_ERROR));
  }
}
