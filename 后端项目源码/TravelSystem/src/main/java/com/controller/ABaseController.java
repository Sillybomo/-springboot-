package com.controller;

import com.entity.vo.ResultVO;
import com.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 18:34:18
 * @Description: Controller基类（包含公共方法）
 * @Version: 1.0
 */
public abstract class ABaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // 日期格式自动绑定（支持yyyy-MM-dd和yyyy-MM-dd HH:mm:ss）
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(parseDate(text));
            }

            private Date parseDate(String text) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
                } catch (Exception e) {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").parse(text);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // 成功响应快捷方法
    protected ResultVO ok(Object data) {
        return ResultVO.ok(data);
    }

    // 成功响应快捷方法
    protected ResultVO ok() {
        return ResultVO.ok();
    }


    // 失败响应快捷方法
    protected ResultVO fail(int code, String message) {
        return ResultVO.err(code, message);
    }


}
