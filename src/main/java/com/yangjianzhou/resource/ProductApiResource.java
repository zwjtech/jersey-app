package com.yangjianzhou.resource;

import com.google.common.collect.Lists;
import com.sun.jersey.api.core.InjectParam;
import com.yangjianzhou.bean.BatchType;
import com.yangjianzhou.bean.ProductPaginationBean;
import com.yangjianzhou.bean.ResultGson;
import com.yangjianzhou.dto.ProductDTO;
import com.yangjianzhou.service.ProductService;
import com.yangjianzhou.service.TradeRecordService;
import com.yangjianzhou.util.ReadFileThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by yangjianzhou on 16-4-17.
 */

@Path("test")
@Component
public class ProductApiResource {
    @InjectParam
    @Resource
    private ProductService productService;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @GET
    @Path("insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String testInsert() {
        productService.saveProduct();
        return "PENDING";
    }

    @GET
    @Path("ibatis-batch-insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String ibatisBatchInsert() {
        List<String> fileNames = Lists.newArrayList("dailyIncomeFile.txt_0");
        String pathPrefix = "/home/yangjianzhou/Test/";

        for (String fileName : fileNames) {
            taskExecutor.execute(new ReadFileThread(pathPrefix + fileName, tradeRecordService, BatchType.IBATIS));
        }
        return "true";
    }

    @GET
    @Path("spring-batch-insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String springBatchInsert() {
        List<String> fileNames = Lists.newArrayList("dailyIncomeFile.txt_1");
        String pathPrefix = "/home/yangjianzhou/Test/";

        for (String fileName : fileNames) {
            taskExecutor.execute(new ReadFileThread(pathPrefix + fileName, tradeRecordService, BatchType.SPRING));
        }
        return "true";
    }

    @GET
    @Path("jdbc-batch-insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String jdbcBatchInsert() {
        List<String> fileNames = Lists.newArrayList("dailyIncomeFile.txt_2");
        String pathPrefix = "/home/yangjianzhou/Test/";

        for (String fileName : fileNames) {
            taskExecutor.execute(new ReadFileThread(pathPrefix + fileName, tradeRecordService, BatchType.JDBC));
        }
        return "true";
    }

    @GET
    @Path("get-all")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultGson<List<ProductDTO>> getAllProduct() {
        ResultGson<List<ProductDTO>> resultGson = productService.getAllProduct();
        return resultGson;
    }

    @GET
    @Path("update-product-name")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProductName(@QueryParam("id") int id, @QueryParam("version") int version, @QueryParam("name") String name) {
        productService.updateProductName(id, version, name);
        return "PENDING";
    }

    @GET
    @Path("pagination-query")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultGson<ProductPaginationBean> paginationQuery(@QueryParam("pageSize") int pageSize, @QueryParam("pageNo") int pageNo) {
        ResultGson<ProductPaginationBean> paginationBeanResultGson = productService.mergePaginationQuery(pageNo, pageSize);
        return paginationBeanResultGson;
    }

    @GET
    @Path("batch-insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String paginationQuery() {
        tradeRecordService.parseFile();
        return "success";
    }

    @GET
    @Path("generate-file")
    @Produces(MediaType.APPLICATION_JSON)
    public String generateFile() {
        tradeRecordService.generateFile();
        return "success";
    }

}
