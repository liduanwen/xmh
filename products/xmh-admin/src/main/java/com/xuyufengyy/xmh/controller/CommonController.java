package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.Area;
import com.xuyufengyy.xmh.AreaService;
import com.xuyufengyy.xmh.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * controller -
 *
 * @author Xu minghua 2018/05/22
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {

    @Resource
    private AreaService areaService;

    /**
     * 地区选择
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public @ResponseBody
    Map<Long, String> area(Long parentId) {
        List<Area> areaList = new ArrayList<Area>();
        Area area = null;
        Map<Long, String> options = new HashMap<Long, String>();
        if (parentId != null) {
            area = areaService.getOne(parentId);
        }
        if (area != null) {
            areaList = area.getChildren();
        } else {
            areaList = areaService.findRoots();// 一级分类
        }
        for (Area areaEntity : areaList) {
            options.put(areaEntity.getId(), areaEntity.getName());
        }
        return options;
    }

    /**
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    void upload(File file) throws IOException {
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
//            URL url = new URL(fileUrl);

            String fileName = DateUtils.toString(new Date(), "yyyyMMddHHmmss") + (int) ((Math.random() * 9 + 1) * 100000) + ".mp3";
            File file2 = new File("/Users/xuyufengyy/Desktop/" + fileName);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }

            fos = new FileOutputStream(file2);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

//        FileInputStream fis = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        FileOutputStream fos  = null;
//        PrintWriter pw = null;
//        try {
//            File file = new File(pathName);//文件路径(包括文件名称)
//            //将文件读入输入流
//            fis = new FileInputStream(file);
//            isr = new InputStreamReader(fis);
//            br = new BufferedReader(isr);
//            StringBuffer buffer = new StringBuffer();
//
//            //文件原有内容
//            for(int i=0;(temp =br.readLine())!=null;i++){
//                buffer.append(temp);
//                // 行与行之间的分隔符 相当于“\n”
//                buffer = buffer.append(System.getProperty("line.separator"));
//            }
//            buffer.append(filein);
//
//            fos = new FileOutputStream(file);
//            pw = new PrintWriter(fos);
//            pw.write(buffer.toString().toCharArray());
//            pw.flush();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }finally {
//            //不要忘记关闭
//            if (pw != null) {
//                pw.close();
//            }
//            if (fos != null) {
//                fos.close();
//            }
//            if (br != null) {
//                br.close();
//            }
//            if (isr != null) {
//                isr.close();
//            }
//            if (fis != null) {
//                fis.close();
//            }
//        }
    }
}



