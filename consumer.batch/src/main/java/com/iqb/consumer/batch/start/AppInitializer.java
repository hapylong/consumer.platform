package com.iqb.consumer.batch.start;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @Copyright 北京爱钱帮财富科技有限公司
 * @author jack
 * @Date 2015年12月21日-下午4:16:40
 */
public class AppInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(AppConfig.class);
        webApplicationContext.afterPropertiesSet();

        // SpringUtil.getInstance().setWebApplicationContext(webApplicationContext);
        // com.iqb.p2p.commpro.message.utils.SpringUtil.getInstance().setWebApplicationContext(webApplicationContext);//message
        // com.iqb.p2p.commpro.qclient.utils.SpringUtil.getInstance().setWebApplicationContext(webApplicationContext);//mqclent
        // com.iqb.p2p.commpro.file.utils.SpringUtil.getInstance().setWebApplicationContext(webApplicationContext);//file

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        // 设置编码utf-8
        /*
         * CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
         * characterEncodingFilter.setEncoding("utf-8");
         * characterEncodingFilter.setForceEncoding(true);
         * servletContext.addFilter("EncodingFilter", characterEncodingFilter);
         */
        // 设置自定义spring工具类
        dynamic.addMapping("/");
    }

}
