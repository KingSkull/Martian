package com.mars.core.load;

import com.mars.common.annotation.api.MarsApi;
import com.mars.common.annotation.api.MarsInterceptor;
import com.mars.common.annotation.bean.MarsOnLoad;
import com.mars.common.annotation.bean.MarsBean;
import com.mars.common.annotation.jdbc.MarsDao;
import com.mars.common.constant.MarsConstant;
import com.mars.common.constant.MarsSpace;
import com.mars.core.model.MarsBeanClassModel;

import java.util.List;
import java.util.Set;

/**
 * 加载本地资源
 */
public class LoadBeans {

    private static MarsSpace constants = MarsSpace.getEasySpace();

    /**
     * 加载所有的bean，包括MarsApi 的class对象
     *
     * @throws Exception 异常
     */
    public static void loadBeans() throws Exception {
        try {

            Set<String> scanClassList = LoadHelper.getSacnClassList();

            for (String str : scanClassList) {
                Class<?> cls = Class.forName(str);
                MarsApi marsApi = cls.getAnnotation(MarsApi.class);
                MarsBean marsBean = cls.getAnnotation(MarsBean.class);
                MarsInterceptor marsInterceptor = cls.getAnnotation(MarsInterceptor.class);
                MarsDao marsDao = cls.getAnnotation(MarsDao.class);
                MarsOnLoad marsOnLoad = cls.getAnnotation(MarsOnLoad.class);

                int count = 0;

                if(marsApi != null) {
                    LoadBeans.loadMarsApi(cls, marsApi);
                    count++;
                }
                if(marsBean != null) {
                    LoadBeans.loadMarsBean(cls, marsBean);
                    count++;
                }
                if(marsInterceptor != null){
                    LoadBeans.loadInterceptor(cls, marsInterceptor);
                    count++;
                }
                if(marsDao != null){
                    LoadBeans.loadDao(cls, marsDao);
                    count++;
                }
                if(marsOnLoad != null){
                    LoadBeans.loadMarsAfter(cls);
                    count++;
                }

                if(count > 1){
                    throw new Exception("类:["+cls.getName()+"]上不允许有多个Mars注解");
                }
            }
        } catch (Exception e) {
            throw new Exception("从扫描出来的类里面筛选有注解的类,出现异常",e);
        }
    }

    /**
     * 将所有MarsApi存到全局存储空间
     * @param cls 类型
     * @param marsApi 注解
     */
    public static void loadMarsApi(Class<?> cls, MarsApi marsApi) {

        List<MarsBeanClassModel> marsApis = LoadHelper.getMarsApiList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(marsApi);

        marsApis.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.CONTROLLERS, marsApis);
    }

    /**
     * 将所有marsBean存到全局存储空间
     * @param cls 类型
     * @param marsBean 注解
     */
    public static void loadMarsBean(Class<?> cls, MarsBean marsBean) {

        List<MarsBeanClassModel> marsBeans = LoadHelper.getBeanList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(marsBean);

        marsBeans.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.MARS_BEANS, marsBeans);
    }

    /**
     * 将所有拦截器存到全局存储空间
     * @param cls 类型
     * @param interceptor 注解
     */
    public static void loadInterceptor(Class<?> cls, MarsInterceptor interceptor){

        List<MarsBeanClassModel> interceptors = LoadHelper.getInterceptorList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(interceptor);

        interceptors.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.INTERCEPTORS, interceptors);
    }

    /**
     * 加载dao
     * @param cls 类型
     * @param marsDao 注解
     */
    public static void loadDao(Class<?> cls, MarsDao marsDao){

        List<MarsBeanClassModel> marsDaos = LoadHelper.getDaoList();

        MarsBeanClassModel marsBeanClassModel = new MarsBeanClassModel();
        marsBeanClassModel.setClassName(cls);
        marsBeanClassModel.setAnnotation(marsDao);

        marsDaos.add(marsBeanClassModel);
        constants.setAttr(MarsConstant.MARS_DAOS, marsDaos);
    }

    /**
     * 加载marsAfter
     * @param cls 类型
     */
    public static void loadMarsAfter(Class<?> cls){
        List<Class> marsAfters = LoadHelper.getMarsAfterList();
        marsAfters.add(cls);
        constants.setAttr(MarsConstant.MARS_AFTERS, marsAfters);
    }
}
