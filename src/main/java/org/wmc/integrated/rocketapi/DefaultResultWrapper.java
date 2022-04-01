package org.wmc.integrated.rocketapi;

import org.wmc.integrated.mvc.Result;
import org.wmc.integrated.mvc.ResultCode;
import com.github.alenfive.rocketapi.entity.ResultWrapper;
import com.github.alenfive.rocketapi.extend.IResultWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认结果包装类
 */
@Component
public class DefaultResultWrapper implements IResultWrapper {

    @Override
    public Object wrapper(Object data, HttpServletRequest request, HttpServletResponse response) {
        // return new ResultWrapper("0",request.getRequestURI(),"succeed",data);
        return Result.success(data);
    }

    @Override
    public Object throwable(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        // return new ResultWrapper("500",request.getRequestURI(),throwable.getMessage(),null);
        return Result.failure(ResultCode.FAIL);
    }


}