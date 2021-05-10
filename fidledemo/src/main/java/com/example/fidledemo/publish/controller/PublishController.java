package com.example.fidledemo.publish.controller;

import com.alibaba.fastjson.JSON;
import com.example.fidledemo.BO.*;
import com.example.fidledemo.DO.TagOfActivityDO;
import com.example.fidledemo.DO.TagOfGoodsDO;
import com.example.fidledemo.DO.TagOfTaskDO;
import com.example.fidledemo.DO.UserDO;
import com.example.fidledemo.dao.TagOfActivityDAO;
import com.example.fidledemo.dao.TagOfGoodsDAO;
import com.example.fidledemo.dao.TagOfTaskDAO;
import com.example.fidledemo.publish.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WWJ
 */
@RestController
@RequestMapping("/publish")
@PropertySource("classpath:application.yml")
public class PublishController {
    @Autowired
    PublishService publishService;
    @Autowired
    TagOfTaskDAO tagOfTaskDAO;
    @Autowired
    TagOfActivityDAO tagOfActivityDAO;
    @Autowired
    TagOfGoodsDAO tagOfGoodsDAO;
    @Value("${imageHost.url}")
    String url;
    @Value("${imageHost.path}")
    String path;

    /**
     * 任务委托发布接口
     *
     * @param request
     * @param session
     * @return
     * @throws ParseException
     */
    @PostMapping("/task")
    @UserLoginToken
    public String insertTask(HttpServletRequest request, HttpSession session) throws ParseException {
        TaskInfoBO taskInfoBO = new TaskInfoBO();

        //获得发布者id
        UserDO userDO = (UserDO) session.getAttribute("user");
        taskInfoBO.setPubId(userDO.getId());

        taskInfoBO.setTitle(request.getParameter("title"));
        taskInfoBO.setReward(new BigDecimal(request.getParameter("reward")));

        //String转Datetime
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        taskInfoBO.setStartTime(formatter.parse(request.getParameter("start_time")));
        taskInfoBO.setEndTime(formatter.parse(request.getParameter("end_time")));

        taskInfoBO.setDescription(request.getParameter("description"));

        //String转Long放入CategoryBO
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCategoryId(Long.parseLong(request.getParameter("category")));
        taskInfoBO.setCategory(categoryBO);

        //将String[]转为List<TagBO>
        String[] tags = request.getParameterValues("tags");
        List<TagBO> tagBOS = new ArrayList<>();
        for (String tag : tags) {
            TagBO tagBO = new TagBO();
            //标签不存在
            if (tagOfTaskDAO.checkTaskTag(tag) == null) {
                TagOfTaskDO tagOfTaskDO = new TagOfTaskDO();
                tagOfTaskDO.setContent(tag);
                tagOfTaskDAO.insertTagOfTask(tagOfTaskDO);
                tagBO.setId(tagOfTaskDO.getId());
                tagBO.setContent(tag);
                tagBO.setType(2);
            } else {
                tagBO.setId(tagOfTaskDAO.checkTaskTag(tag));
                tagBO.setContent(tag);
                tagBO.setType(2);
            }
            tagBOS.add(tagBO);
        }
        taskInfoBO.setState(2);
        taskInfoBO.setTagList(tagBOS);
        publishService.insertTask(taskInfoBO);
        return JSON.toJSONString(Result.successResult());
    }

    /**
     * 二手物品发布接口
     *
     * @param request
     * @param session
     * @return
     */
    @PostMapping("/goods")
    @UserLoginToken
    public String insertGoods(HttpServletRequest request, HttpSession session) {
        GoodsInfoBO goodsInfoBO = new GoodsInfoBO();

        //获得发布者id
        UserDO userDO = (UserDO) session.getAttribute("user");
        goodsInfoBO.setPubId(userDO.getId());

        goodsInfoBO.setTitle(request.getParameter("title"));
        goodsInfoBO.setPrice(new BigDecimal(request.getParameter("price")));
        goodsInfoBO.setOriginalPrice(new BigDecimal(request.getParameter("original_price")));
        goodsInfoBO.setDescription(request.getParameter("description"));
        goodsInfoBO.setCondition(Integer.parseInt(request.getParameter("condition")));

        //String转Long放入CategoryBO
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCategoryId(Long.parseLong(request.getParameter("category")));
        goodsInfoBO.setCategory(categoryBO);

        //将String[]转为List<TagBO>
        String[] tags = request.getParameterValues("tags");
        List<TagBO> tagBOS = new ArrayList<>();
        for (String tag : tags) {
            TagBO tagBO = new TagBO();
            //标签不存在
            if (tagOfGoodsDAO.checkGoodsTag(tag) == null) {
                TagOfGoodsDO tagOfGoodsDO = new TagOfGoodsDO();
                tagOfGoodsDO.setContent(tag);
                tagOfGoodsDAO.insertTagOfGoods(tagOfGoodsDO);
                tagBO.setId(tagOfGoodsDO.getId());
                tagBO.setContent(tag);
                tagBO.setType(1);
            } else {
                tagBO.setId(tagOfGoodsDAO.checkGoodsTag(tag));
                tagBO.setContent(tag);
                tagBO.setType(1);
            }
            tagBOS.add(tagBO);
        }
        goodsInfoBO.setTagList(tagBOS);

        //String[]转List<ImageBO>
        String[] image_links = request.getParameterValues("image_links");
        List<ImageBO> imageBOS = new ArrayList<>();
        for (String image_link : image_links) {
            ImageBO imageBO = new ImageBO();
            imageBO.setType(1);
            imageBO.setImageLink(image_link);
            imageBOS.add(imageBO);
        }
        goodsInfoBO.setImageList(imageBOS);
        goodsInfoBO.setSold(1);

        publishService.insertGoods(goodsInfoBO);
        return JSON.toJSONString(Result.successResult());
    }

    /**
     * 活动信息发布接口
     *
     * @param request
     * @param session
     * @return
     * @throws ParseException
     */
    @PostMapping("/activity")
    @UserLoginToken
    public String insertActivity(HttpServletRequest request, HttpSession session) throws ParseException {
        ActivityInfoBO activityInfoBO = new ActivityInfoBO();

        //获得发布者id
        UserDO userDO = (UserDO) session.getAttribute("user");
        activityInfoBO.setPubId(userDO.getId());

        activityInfoBO.setTitle(request.getParameter("title"));
        activityInfoBO.setAddress(request.getParameter("address"));

        //String转Datetime
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        activityInfoBO.setStartTime(formatter.parse(request.getParameter("start_time")));
        activityInfoBO.setEndTime(formatter.parse(request.getParameter("end_time")));

        activityInfoBO.setDescription(request.getParameter("description"));

        //String[]转List<ImageBO>
        String[] image_links = request.getParameterValues("image_links");
        List<ImageBO> imageBOS = new ArrayList<>();
        for (String image_link : image_links) {
            ImageBO imageBO = new ImageBO();
            imageBO.setType(3);
            imageBO.setImageLink(image_link);
            imageBOS.add(imageBO);
        }
        activityInfoBO.setImageList(imageBOS);

        //String转Long放入CategoryBO
        CategoryBO categoryBO = new CategoryBO();
        categoryBO.setCategoryId(Long.parseLong(request.getParameter("category")));
        activityInfoBO.setCategory(categoryBO);

        //将String[]转为List<TagBO>
        String[] tags = request.getParameterValues("tags");
        List<TagBO> tagBOS = new ArrayList<>();
        for (String tag : tags) {
            TagBO tagBO = new TagBO();
            //标签不存在
            if (tagOfActivityDAO.checkActivityTag(tag) == null) {
                TagOfActivityDO tagOfActivityDO = new TagOfActivityDO();
                tagOfActivityDO.setContent(tag);
                this.tagOfActivityDAO.insertTagOfActivity(tagOfActivityDO);
                tagBO.setId(tagOfActivityDO.getId());
                tagBO.setContent(tag);
                tagBO.setType(3);
            } else {
                tagBO.setId(tagOfActivityDAO.checkActivityTag(tag));
                tagBO.setContent(tag);
                tagBO.setType(3);
            }
            tagBOS.add(tagBO);
        }
        activityInfoBO.setTagList(tagBOS);

        publishService.insertActivity(activityInfoBO);
        return JSON.toJSONString(Result.successResult());
    }

    /**
     * 上传二手物品图片
     *
     * @param image
     * @return
     */
    @PostMapping("/uploadGoodsImage")
    @UserLoginToken
    public String uploadGoodsImage(@RequestParam("image") MultipartFile image) {
        String image_link = "";

        //判断文件是否为空
        if (image.isEmpty()) {
            return JSON.toJSONString(Result.failureResult(ResultCode.FILE_EMPTY));
        }

        //获取文件名
        String fileName = image.getOriginalFilename();
        //加个时间戳，尽量避免图片名称重复
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;

        //创建文件路径
        String imagePath = path + fileName;

        File dest = new File(imagePath);
        if (dest.exists()) {
            return JSON.toJSONString(Result.failureResult(ResultCode.FILE_EXISTED));
        }

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            image.transferTo(dest);
            image_link = url + fileName;
        } catch (IOException e) {
            return JSON.toJSONString(Result.failureResult(ResultCode.UPLOAD_FAILURE));
        }
        ImageBO imageBO = new ImageBO(null, image_link, 1);
        imageBO.setId(publishService.insertImage(imageBO));
        return JSON.toJSONString(Result.successResult(imageBO));
    }

    /**
     * 上传活动信息图片
     *
     * @param image
     * @return
     */
    @PostMapping("/uploadActivityImage")
    @UserLoginToken
    public String uploadActivityImage(@RequestParam("image") MultipartFile image) {
        String image_link = "";

        //判断文件是否为空
        if (image.isEmpty()) {
            return JSON.toJSONString(Result.failureResult(ResultCode.FILE_EMPTY));
        }

        //获取文件名
        String fileName = image.getOriginalFilename();
        //加个时间戳，尽量避免图片名称重复
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;

        //创建文件路径
        String imagePath = path + fileName;

        File dest = new File(imagePath);
        if (dest.exists()) {
            return JSON.toJSONString(Result.failureResult(ResultCode.FILE_EXISTED));
        }

        //判断图片大小是否大于50MB
        if(image.getSize()>(long)52428800){
            return JSON.toJSONString(Result.failureResult(ResultCode.IMAGE_SIZE_ERROR));
        }

        //判断图片格式是否正确
        String imageName = image.getOriginalFilename();// 文件原名称
        String type = imageName.indexOf(".") != -1 ? imageName.substring(imageName.lastIndexOf(".") + 1, imageName.length()) : null;
        if(!(type.equals("jpg")||type.equals("png")||type.equals("jpeg"))){
            return JSON.toJSONString(Result.failureResult(ResultCode.FORMAT_ERROR));
        }

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            image.transferTo(dest);
            image_link = url + fileName;
        } catch (IOException e) {
            return JSON.toJSONString(Result.failureResult(ResultCode.UPLOAD_FAILURE));
        }
        ImageBO imageBO = new ImageBO(null, image_link, 3);
        imageBO.setId(publishService.insertImage(imageBO));
        return JSON.toJSONString(Result.successResult(imageBO));
    }

    /**
     * 删除二手物品图片
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteGoodsImage/{id}")
    @UserLoginToken
    public String deleteGoodsImage(@PathVariable("id") Long id) {
        ImageBO imageBO = new ImageBO();
        imageBO.setType(1);
        imageBO.setId(id);
        publishService.deleteImage(imageBO);
        return JSON.toJSONString(Result.successResult());
    }

    /**
     * 删除活动信息图片
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteActivityImage/{id}")
    @UserLoginToken
    public String deleteActivityImage(@PathVariable("id") Long id) {
        ImageBO imageBO = new ImageBO();
        imageBO.setType(3);
        imageBO.setId(id);
        publishService.deleteImage(imageBO);
        return JSON.toJSONString(Result.successResult());
    }

}
