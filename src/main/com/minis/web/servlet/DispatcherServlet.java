package com.minis.web.servlet;


import com.minis.beans.BeansException;
import com.minis.util.MappingValue;
import com.minis.web.context.WebApplicationContext;
import com.minis.web.context.support.AnnotationConfigWebApplicationContext;
import com.minis.web.method.HandlerMethod;
import com.minis.web.method.annotation.RequestMappingHandlerMapping;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
    public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
    public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;
    private String sContextConfigLocation;
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;

    public DispatcherServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(this.sContextConfigLocation, this.parentApplicationContext);
        Refresh();
    }

    //对所有的mappingValues中注册的类进行实例化，默认构造函数
    protected void Refresh() {
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
        initViewResolvers(this.webApplicationContext);
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        try {
            this.handlerMapping = (HandlerMapping) wac.getBean(HANDLER_MAPPING_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        try {
            this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    protected void service(HttpServletRequest request, HttpServletResponse
            response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerMethod handlerMethod = null;
        ModelAndView mv = null;

        handlerMethod = this.handlerMapping.getHandler(processedRequest);
        if (handlerMethod == null) {
            return;
        }

        HandlerAdapter ha = this.handlerAdapter;

        mv = ha.handle(processedRequest, response, handlerMethod);

        render(processedRequest, response, mv);
    }

    protected void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
        if (mv == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        String sTarget = mv.getViewName();
        Map<String, Object> modelMap = mv.getModel();
        View view = resolveViewName(sTarget, modelMap, request);
        view.render(modelMap, request, response);
    }

    protected View resolveViewName(String viewName, Map<String, Object> model,
                                   HttpServletRequest request) throws Exception {
        if (this.viewResolver != null) {
            View view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

}
