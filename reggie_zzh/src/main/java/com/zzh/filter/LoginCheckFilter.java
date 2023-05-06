package com.zzh.filter;


import com.alibaba.fastjson.JSON;
import com.zzh.common.BaseContext;
import com.zzh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 *
 * @ServletComponentScan    在启动类上添加该注解，以用来扫描webfilter这样的注解包
 * filterName = "loginCheckFilter"   命名
 * urlPatterns = "/*"   拦截所有路径
 *filterChain.doFilter(request,response);   放行
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * 1、获取本次请求的url
         * 2、判断本次请求是否需要处理
         * 3、如果不需要处理，则直接放行
         * 4、判断登录状态，如果已登录状态，则直接放行
         * 5、如果未登录则返回未登录结果
         */
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的url
        String requestURI = request.getRequestURI();

        //定义不用处理的请求路径
       // log.info("拦截到请求ID：{}",request.getRequestURI());
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check){
           // log.info("拦截到不需要处理的请求：{}",request.getRequestURI());
            filterChain.doFilter(request,response);
            return;
        }

        //4、判断登录状态，如果已登录状态，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            //log.info("用户登录；{}",request.getSession().getAttribute("employee"));
            //long id = Thread.currentThread().getId();
            //log.info("已登录的线程的id{}",id);

            //利用线程存id
            Long lid = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(lid);
            filterChain.doFilter(request,response);
            return;
        }

        //log.info("未登录");
        //5、如果未登录则返回未登录结果,通过输出流的方式向客户端页面相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }


    /**
     * 路径匹配，检查本次请求是否放行，
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
